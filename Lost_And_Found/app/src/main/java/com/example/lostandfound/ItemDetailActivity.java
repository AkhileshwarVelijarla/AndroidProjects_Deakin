package com.example.lostandfound;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.lostandfound.databinding.ActivityItemDetailBinding;

public class ItemDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ADVERT_ID = "advert_id";

    private ActivityItemDetailBinding binding;
    private ItemDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_detail);
        viewModel = new ViewModelProvider(this).get(ItemDetailViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        int advertId = getIntent().getIntExtra(EXTRA_ADVERT_ID, -1);
        if (advertId == -1) {
            finish();
            return;
        }

        viewModel.loadItem(advertId);
        binding.removeButton.setOnClickListener(v -> confirmRemove());
    }

    private void confirmRemove() {
        new AlertDialog.Builder(this)
                .setTitle("Remove advert")
                .setMessage("Remove this advert because the item is resolved?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    if (viewModel.deleteItem()) {
                        Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Could not remove advert", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
