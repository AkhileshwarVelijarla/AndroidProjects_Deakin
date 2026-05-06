package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.lostandfound.databinding.ActivityItemListBinding;

public class ItemListActivity extends AppCompatActivity {
    private ActivityItemListBinding binding;
    private ItemListViewModel viewModel;
    private AdvertAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_list);
        viewModel = new ViewModelProvider(this).get(ItemListViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        setupSpinner();
        setupRecyclerView();

        viewModel.items.observe(this, adverts -> {
            adapter.setAdverts(adverts);
            binding.emptyTextView.setVisibility(adverts == null || adverts.isEmpty()
                    ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadItems();
    }

    private void setupSpinner() {
        String[] categories = {"All", "Electronics", "Pets", "Wallets", "Keys", "Bags", "Documents", "Others"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.filterSpinner.setAdapter(spinnerAdapter);
        binding.applyFilterButton.setOnClickListener(v ->
                viewModel.setCategory(binding.filterSpinner.getSelectedItem().toString()));
    }

    private void setupRecyclerView() {
        adapter = new AdvertAdapter(advert -> {
            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra(ItemDetailActivity.EXTRA_ADVERT_ID, advert.getId());
            startActivity(intent);
        });
        binding.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.itemsRecyclerView.setAdapter(adapter);
    }
}
