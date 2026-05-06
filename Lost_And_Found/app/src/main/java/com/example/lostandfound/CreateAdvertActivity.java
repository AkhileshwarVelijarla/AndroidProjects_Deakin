package com.example.lostandfound;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.lostandfound.databinding.ActivityCreateAdvertBinding;

import java.util.Calendar;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {
    private ActivityCreateAdvertBinding binding;
    private CreateAdvertViewModel viewModel;
    private ActivityResultLauncher<String[]> imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_advert);
        viewModel = new ViewModelProvider(this).get(CreateAdvertViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        setupSpinner();
        setupImagePicker();

        binding.dateEditText.setOnClickListener(v -> showDatePicker());
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
}
