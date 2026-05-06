package com.example.lostandfound;

import android.net.Uri;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.util.Date;

public class BindingAdapters {
    @BindingAdapter("imageUri")
    public static void setImageUri(ImageView imageView, String imageUri) {
        if (imageUri == null || imageUri.trim().isEmpty()) {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        } else {
            imageView.setImageURI(Uri.parse(imageUri));
        }
    }

    @BindingAdapter("postedTime")
    public static void setPostedTime(TextView textView, long timestamp) {
        if (timestamp == 0) {
            textView.setText("");
        } else {
            String text = DateFormat.format("dd/MM/yyyy hh:mm a", new Date(timestamp)).toString();
            textView.setText("Posted at: " + text);
        }
    }
}
