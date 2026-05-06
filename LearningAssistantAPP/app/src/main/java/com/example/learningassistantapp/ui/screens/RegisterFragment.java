package com.example.learningassistantapp.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learningassistantapp.R;
import com.example.learningassistantapp.ui.AppNavigator;
import com.example.learningassistantapp.viewmodel.RegisterViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment {
    private RegisterViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        TextInputEditText usernameInput = view.findViewById(R.id.registerUsernameInput);
        TextInputEditText emailInput = view.findViewById(R.id.registerEmailInput);
        TextInputEditText confirmEmailInput = view.findViewById(R.id.registerConfirmEmailInput);
        TextInputEditText passwordInput = view.findViewById(R.id.registerPasswordInput);
        TextInputEditText confirmPasswordInput = view.findViewById(R.id.registerConfirmPasswordInput);
        TextInputEditText phoneInput = view.findViewById(R.id.registerPhoneInput);
        TextView errorText = view.findViewById(R.id.registerErrorText);
        MaterialButton createAccountButton = view.findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(v -> {
            String error = viewModel.validateAndRegister(
                    valueOf(usernameInput),
                    valueOf(emailInput),
                    valueOf(confirmEmailInput),
                    valueOf(passwordInput),
                    valueOf(confirmPasswordInput),
                    valueOf(phoneInput)
            );
            if (error == null) {
                errorText.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Account created", Toast.LENGTH_SHORT).show();
                ((AppNavigator) requireActivity()).openInterests();
            } else {
                errorText.setVisibility(View.VISIBLE);
                errorText.setText(error);
            }
        });
    }

    private String valueOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
