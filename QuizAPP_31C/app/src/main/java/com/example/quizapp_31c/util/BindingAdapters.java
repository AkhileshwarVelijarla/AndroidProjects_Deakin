package com.example.quizapp_31c.util;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.example.quizapp_31c.R;
import com.example.quizapp_31c.model.OptionState;

public final class BindingAdapters {
    private BindingAdapters() {
    }

    @BindingAdapter("visibleGone")
    public static void setVisibleGone(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("feedbackColor")
    public static void setFeedbackColor(TextView view, int color) {
        view.setTextColor(ContextCompat.getColor(view.getContext(), color));
    }

    @BindingAdapter("optionState")
    public static void setOptionState(Button button, OptionState state) {
        int backgroundRes = R.color.option_default;
        int textColorRes = R.color.black;

        if (state == OptionState.SELECTED) {
            backgroundRes = R.color.option_selected;
        } else if (state == OptionState.CORRECT) {
            backgroundRes = R.color.feedback_correct;
            textColorRes = R.color.white;
        } else if (state == OptionState.INCORRECT) {
            backgroundRes = R.color.feedback_incorrect;
            textColorRes = R.color.white;
        }

        button.setBackgroundTintList(ColorStateList.valueOf(button.getContext().getColor(backgroundRes)));
        button.setTextColor(button.getContext().getColor(textColorRes));
    }
}
