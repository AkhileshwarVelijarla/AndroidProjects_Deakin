package com.example.lostandfound;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.lostandfound.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final float DEFAULT_RADIUS_KM = 5f;

    private ActivityMapsBinding binding;
    private MapsViewModel viewModel;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private final ExecutorService geocodingExecutor = Executors.newSingleThreadExecutor();
    private final AtomicInteger renderGeneration = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        viewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupLocationPermission();
        binding.radiusEditText.setText(String.valueOf((int) DEFAULT_RADIUS_KM));
        binding.showNearbyButton.setOnClickListener(v -> showNearbyItems());
        binding.showAllButton.setOnClickListener(v -> addMarkers(viewModel.adverts.getValue(), null, 0));

        viewModel.adverts.observe(this, adverts -> {
            if (googleMap != null) {
                addMarkers(adverts, null, 0);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        enableMyLocationIfAllowed();
        viewModel.loadAdverts();
    }

    private void setupLocationPermission() {
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean fine = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
                    boolean coarse = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION));
                    if (fine || coarse) {
                        enableMyLocationIfAllowed();
                        requestNearbyItems();
                    } else {
                        Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showNearbyItems() {
        if (hasLocationPermission()) {
            requestNearbyItems();
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

    private void enableMyLocationIfAllowed() {
        if (googleMap == null || !hasLocationPermission()) {
            return;
        }
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException ignored) {
        }
    }

    private void requestNearbyItems() {
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
                        addMarkers(viewModel.adverts.getValue(), location, getRadiusKm());
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Location error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } catch (SecurityException e) {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private float getRadiusKm() {
        try {
            float value = Float.parseFloat(binding.radiusEditText.getText().toString());
            return value > 0 ? value : DEFAULT_RADIUS_KM;
        } catch (NumberFormatException e) {
            return DEFAULT_RADIUS_KM;
        }
    }

    private void addMarkers(List<Advert> adverts, Location currentLocation, float radiusKm) {
        if (googleMap == null) {
            return;
        }
        int generation = renderGeneration.incrementAndGet();
        geocodingExecutor.execute(() -> {
            List<MarkerData> markers = buildMarkers(adverts, currentLocation, radiusKm);
            runOnUiThread(() -> {
                if (generation != renderGeneration.get()) {
                    return;
                }
                showMarkers(markers, currentLocation, radiusKm);
            });
        });
    }

    private List<MarkerData> buildMarkers(List<Advert> adverts, Location currentLocation, float radiusKm) {
        List<MarkerData> markers = new ArrayList<>();
        if (adverts == null) {
            return markers;
        }

        for (Advert advert : adverts) {
            LatLng itemPosition = getAdvertPosition(advert);
            if (itemPosition == null) {
                continue;
            }
            if (currentLocation != null && !isInsideRadius(itemPosition, currentLocation, radiusKm)) {
                continue;
            }
            markers.add(new MarkerData(
                    itemPosition,
                    advert.getPostType() + ": " + advert.getName(),
                    makeSnippet(advert)));
        }
        return markers;
    }

    private LatLng getAdvertPosition(Advert advert) {
        if (advert.hasCoordinates()) {
            return new LatLng(advert.getLatitude(), advert.getLongitude());
        }

        if (!Geocoder.isPresent()
                || advert.getLocation() == null
                || advert.getLocation().trim().isEmpty()) {
            return null;
        }

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(advert.getLocation(), 1);
            if (addresses == null || addresses.isEmpty()) {
                return null;
            }
            Address address = addresses.get(0);
            if (!address.hasLatitude() || !address.hasLongitude()) {
                return null;
            }
            return new LatLng(address.getLatitude(), address.getLongitude());
        } catch (Exception e) {
            return null;
        }
    }

    private void showMarkers(List<MarkerData> markers, Location currentLocation, float radiusKm) {
        googleMap.clear();
        LatLng firstPosition = null;

        if (currentLocation != null) {
            LatLng currentPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .title("Current location"));
            firstPosition = currentPosition;
        }

        for (MarkerData marker : markers) {
            googleMap.addMarker(new MarkerOptions()
                    .position(marker.position)
                    .title(marker.title)
                    .snippet(marker.snippet));
            if (firstPosition == null) {
                firstPosition = marker.position;
            }
        }

        if (firstPosition != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstPosition, currentLocation == null ? 11f : 13f));
        }

        if (markers.isEmpty()) {
            String message = currentLocation == null
                    ? "No adverts with map coordinates yet"
                    : String.format(Locale.getDefault(), "No adverts within %.1f km", radiusKm);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInsideRadius(LatLng itemPosition, Location currentLocation, float radiusKm) {
        float[] results = new float[1];
        Location.distanceBetween(
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                itemPosition.latitude,
                itemPosition.longitude,
                results);
        return results[0] <= radiusKm * 1000f;
    }

    private String makeSnippet(Advert advert) {
        String description = advert.getDescription();
        if (description != null && description.length() > 60) {
            description = description.substring(0, 60) + "...";
        }
        return description + " - " + advert.getLocation();
    }

    @Override
    protected void onDestroy() {
        geocodingExecutor.shutdown();
        super.onDestroy();
    }

    private static class MarkerData {
        final LatLng position;
        final String title;
        final String snippet;

        MarkerData(LatLng position, String title, String snippet) {
            this.position = position;
            this.title = title;
            this.snippet = snippet;
        }
    }
}
