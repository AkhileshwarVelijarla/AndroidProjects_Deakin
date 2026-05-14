package com.example.lostandfound;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.lostandfound.databinding.ActivityCreateAdvertBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.PlaceAutocomplete;
import com.google.android.libraries.places.widget.PlaceAutocompleteActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateAdvertActivity extends AppCompatActivity {
    private ActivityCreateAdvertBinding binding;
    private CreateAdvertViewModel viewModel;
    private ActivityResultLauncher<String[]> imagePicker;
    private ActivityResultLauncher<Intent> placePicker;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private FusedLocationProviderClient fusedLocationClient;
    private final ExecutorService geocodingExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_advert);
        viewModel = new ViewModelProvider(this).get(CreateAdvertViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        setupSpinner();
        setupImagePicker();
        setupPlaces();
        setupLocationPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding.dateEditText.setOnClickListener(v -> showDatePicker());
        binding.searchLocationButton.setOnClickListener(v -> openPlaceAutocomplete());
        binding.currentLocationButton.setOnClickListener(v -> getCurrentLocation());
        binding.selectImageButton.setOnClickListener(v -> imagePicker.launch(new String[]{"image/*"}));
        binding.saveButton.setOnClickListener(v -> saveAdvert());
    }

    private void setupSpinner() {
        String[] categories = {"Electronics", "Pets", "Wallets", "Keys", "Bags", "Documents", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(adapter);
    }

    private void setupImagePicker() {
        imagePicker = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) {
                getContentResolver().takePersistableUriPermission(uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                viewModel.imageUri.setValue(uri.toString());
            }
        });
    }

    private void setupPlaces() {
        if (!Places.isInitialized() && hasConfiguredGoogleApiKey()) {
            Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), getString(R.string.google_maps_api_key));
        }
        placePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handlePlaceResult);
    }

    private void setupLocationPermission() {
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean fine = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
                    boolean coarse = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION));
                    if (fine || coarse) {
                        requestCurrentLocation();
                    } else {
                        Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openPlaceAutocomplete() {
        if (!hasConfiguredGoogleApiKey()) {
            Toast.makeText(this, "Google search needs an API key. You can type the address manually.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), getString(R.string.google_maps_api_key));
        }
        AutocompleteSessionToken sessionToken = AutocompleteSessionToken.newInstance();
        String initialQuery = viewModel.location.getValue();
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .setAutocompleteSessionToken(sessionToken)
                .setInitialQuery(initialQuery == null ? "" : initialQuery)
                .build(this);
        placePicker.launch(intent);
    }

    private void handlePlaceResult(ActivityResult result) {
        if (result.getResultCode() == PlaceAutocompleteActivity.RESULT_OK && result.getData() != null) {
            AutocompletePrediction prediction = PlaceAutocomplete.getPredictionFromIntent(result.getData());
            AutocompleteSessionToken sessionToken = PlaceAutocomplete.getSessionTokenFromIntent(result.getData());
            if (prediction == null) {
                Toast.makeText(this, "No place selected", Toast.LENGTH_SHORT).show();
                return;
            }
            String locationText = prediction.getFullText(null).toString();
            viewModel.location.setValue(locationText);
            fetchSelectedPlace(prediction, sessionToken, locationText);
        } else if (result.getResultCode() == PlaceAutocompleteActivity.RESULT_ERROR
                && result.getData() != null) {
            Status status = PlaceAutocomplete.getResultStatusFromIntent(result.getData());
            Toast.makeText(this, "Places error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void fetchSelectedPlace(AutocompletePrediction prediction,
                                    AutocompleteSessionToken sessionToken,
                                    String fallbackLocationText) {
        List<Place.Field> fields = Arrays.asList(
                Place.Field.DISPLAY_NAME,
                Place.Field.FORMATTED_ADDRESS,
                Place.Field.LOCATION);
        FetchPlaceRequest request = FetchPlaceRequest.builder(prediction.getPlaceId(), fields)
                .setSessionToken(sessionToken)
                .build();
        PlacesClient placesClient = Places.createClient(this);
        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    Place place = response.getPlace();
                    if (place.getLocation() == null) {
                        Toast.makeText(this, "Selected place has no coordinates", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String address = place.getFormattedAddress();
                    String placeName = place.getDisplayName();
                    String locationText = address != null && !address.trim().isEmpty()
                            ? address
                            : placeName;
                    if (locationText == null || locationText.trim().isEmpty()) {
                        locationText = fallbackLocationText;
                    }
                    viewModel.setSelectedLocation(locationText,
                            place.getLocation().latitude,
                            place.getLocation().longitude);
                })
                .addOnFailureListener(e -> {
                    viewModel.location.setValue(fallbackLocationText);
                    Toast.makeText(this, "Place details error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            showTimePicker(year, month, dayOfMonth);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showTimePicker(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String dateTime = String.format(Locale.getDefault(),
                    "%02d/%02d/%04d %02d:%02d",
                    dayOfMonth, month + 1, year, hourOfDay, minute);
            viewModel.dateText.setValue(dateTime);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void saveAdvert() {
        if (!hasSelectedCoordinates()
                && viewModel.location.getValue() != null
                && !viewModel.location.getValue().trim().isEmpty()
                && Geocoder.isPresent()) {
            Toast.makeText(this, "Finding location coordinates...", Toast.LENGTH_SHORT).show();
            String locationText = viewModel.location.getValue().trim();
            geocodingExecutor.execute(() -> {
                double[] coordinates = getCoordinatesFromAddress(locationText);
                if (coordinates != null) {
                    runOnUiThread(() -> {
                        viewModel.setSelectedLocation(locationText, coordinates[0], coordinates[1]);
                        saveAdvertNow();
                    });
                } else {
                    runOnUiThread(this::saveAdvertNow);
                }
            });
            return;
        }
        saveAdvertNow();
    }

    private void saveAdvertNow() {
        String postType = null;
        if (binding.lostRadioButton.isChecked()) {
            postType = "Lost";
        } else if (binding.foundRadioButton.isChecked()) {
            postType = "Found";
        }
        String category = binding.categorySpinner.getSelectedItem().toString();
        boolean saved = viewModel.saveAdvert(postType, category);
        if (saved) {
            Toast.makeText(this, "Advert saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ItemListActivity.class));
            finish();
        } else {
            Toast.makeText(this, viewModel.errorMessage.getValue(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasSelectedCoordinates() {
        return viewModel.selectedLatitude != 0 || viewModel.selectedLongitude != 0;
    }

    private void getCurrentLocation() {
        if (hasLocationPermission()) {
            requestCurrentLocation();
        } else {
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCurrentLocation() {
        if (!hasLocationPermission()) {
            return;
        }
        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location == null) {
                            Toast.makeText(this, "Could not get current location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        setLocationWithAddress(location.getLatitude(), location.getLongitude());
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Location error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } catch (SecurityException e) {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void setLocationWithAddress(double latitude, double longitude) {
        viewModel.setSelectedLocation(formatCoordinates(latitude, longitude), latitude, longitude);

        if (!Geocoder.isPresent()) {
            return;
        }

        geocodingExecutor.execute(() -> {
            String addressText = getAddressFromCoordinates(latitude, longitude);
            if (addressText == null) {
                return;
            }
            runOnUiThread(() -> viewModel.setSelectedLocation(addressText, latitude, longitude));
        });
    }

    private String getAddressFromCoordinates(double latitude, double longitude) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses == null || addresses.isEmpty()) {
                return null;
            }
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);
            return addressLine == null || addressLine.trim().isEmpty()
                    ? null
                    : addressLine;
        } catch (Exception e) {
            return null;
        }
    }

    private double[] getCoordinatesFromAddress(String addressText) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(addressText, 1);
            if (addresses == null || addresses.isEmpty()) {
                return null;
            }
            Address address = addresses.get(0);
            if (!address.hasLatitude() || !address.hasLongitude()) {
                return null;
            }
            return new double[]{address.getLatitude(), address.getLongitude()};
        } catch (Exception e) {
            return null;
        }
    }

    private String formatCoordinates(double latitude, double longitude) {
        return String.format(Locale.getDefault(),
                "Current location (%.5f, %.5f)",
                latitude,
                longitude);
    }

    private boolean hasConfiguredGoogleApiKey() {
        String apiKey = getString(R.string.google_maps_api_key);
        return apiKey != null
                && !apiKey.trim().isEmpty()
                && !"YOUR_GOOGLE_MAPS_API_KEY_HERE".equals(apiKey);
    }

    @Override
    protected void onDestroy() {
        geocodingExecutor.shutdown();
        super.onDestroy();
    }
}
