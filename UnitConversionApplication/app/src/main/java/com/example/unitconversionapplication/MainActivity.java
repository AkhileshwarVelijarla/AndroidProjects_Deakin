package com.example.unitconversionapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Spinner catSpinner, fromSpinner, toSpinner;
    EditText editInput;
    Button btnConvert;
    TextView txtResult;

    // Unit lists
    String[] currency = {"USD", "AUD", "EUR", "JPY", "GBP"};
    String[] fuel = {"mpg", "km/L"};
    String[] volume = {"Gallon (US)", "Liters"};
    String[] distance = {"Nautical Mile", "Kilometers"};
    String[] temp = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI
        catSpinner = findViewById(R.id.catSpinner);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        editInput = findViewById(R.id.editInput);
        btnConvert = findViewById(R.id.btnConvert);
        txtResult = findViewById(R.id.txtResult);

        // Category change logic
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cat = catSpinner.getSelectedItem().toString();
                updateSpinners(cat);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Convert button click
        btnConvert.setOnClickListener(v -> {
            String input = editInput.getText().toString();
            if (input.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_value_toast), Toast.LENGTH_SHORT).show();
                return;
            }
            
            double val;
            try {
                val = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                Toast.makeText(this, getString(R.string.enter_value_toast), Toast.LENGTH_SHORT).show();
                return;
            }

            String cat = catSpinner.getSelectedItem().toString();
            String from = fromSpinner.getSelectedItem().toString();
            String to = toSpinner.getSelectedItem().toString();
            double res = 0;

            // Simple conversion logic
            if (cat.equals("Currency")) res = convertCurrency(val, from, to);
            else if (cat.equals("Fuel Efficiency")) res = convertFuel(val, from, to);
            else if (cat.equals("Liquid Volume")) res = convertVol(val, from, to);
            else if (cat.equals("Distance")) res = convertDist(val, from, to);
            else if (cat.equals("Temperature")) res = convertTemp(val, from, to);

            txtResult.setText(getString(R.string.result_format, res, to));
        });
    }

    void updateSpinners(String cat) {
        String[] list = {};
        if (cat.equals("Currency")) list = currency;
        else if (cat.equals("Fuel Efficiency")) list = fuel;
        else if (cat.equals("Liquid Volume")) list = volume;
        else if (cat.equals("Distance")) list = distance;
        else if (cat.equals("Temperature")) list = temp;

        // Use standard dark-compatible layout
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        fromSpinner.setAdapter(ad);
        toSpinner.setAdapter(ad);
    }

    double convertCurrency(double v, String f, String t) {
        double usd = v;
        // Convert to USD first
        if (f.equals("AUD")) usd = v / 1.55;
        else if (f.equals("EUR")) usd = v / 0.92;
        else if (f.equals("JPY")) usd = v / 148.50;
        else if (f.equals("GBP")) usd = v / 0.78;

        // Convert from USD to target
        if (t.equals("AUD")) return usd * 1.55;
        if (t.equals("EUR")) return usd * 0.92;
        if (t.equals("JPY")) return usd * 148.50;
        if (t.equals("GBP")) return usd * 0.78;
        return usd;
    }

    double convertFuel(double v, String f, String t) {
        if (f.equals(t)) return v;
        return f.equals("mpg") ? v * 0.425 : v / 0.425;
    }

    double convertVol(double v, String f, String t) {
        if (f.equals(t)) return v;
        return f.equals("Gallon (US)") ? v * 3.785 : v / 3.785;
    }

    double convertDist(double v, String f, String t) {
        if (f.equals(t)) return v;
        return f.equals("Nautical Mile") ? v * 1.852 : v / 1.852;
    }

    double convertTemp(double v, String f, String t) {
        if (f.equals(t)) return v;
        double c = v;
        if (f.equals("Fahrenheit")) c = (v - 32) / 1.8;
        else if (f.equals("Kelvin")) c = v - 273.15;

        if (t.equals("Fahrenheit")) return (c * 1.8) + 32;
        if (t.equals("Kelvin")) return c + 273.15;
        return c;
    }
}