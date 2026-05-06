package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.lostandfound.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.createAdvertButton.setOnClickListener(v ->
                startActivity(new Intent(this, CreateAdvertActivity.class)));
        binding.showItemsButton.setOnClickListener(v ->
                startActivity(new Intent(this, ItemListActivity.class)));
    }
}
