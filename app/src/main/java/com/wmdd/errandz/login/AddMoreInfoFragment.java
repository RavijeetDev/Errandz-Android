package com.wmdd.errandz.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.wmdd.errandz.R;
import com.wmdd.errandz.databinding.FragmentAddMoreBinding;
import com.wmdd.errandz.util.Utility;

public class AddMoreInfoFragment extends Fragment implements View.OnClickListener {

    private static final int HIRER_USER_TYPE = 1;
    private static final int TASKER_USER_TYPE = 2;

    private FragmentAddMoreBinding binding;
    private Snackbar snackbar;

    private int userType = 0;
    private long dateOFBirthTimestamp = 0;

    private CallbackListener callbackListener;

    public interface CallbackListener {
        void onAddingMoreInfo(long dateOfBirthTimestamp, int userType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_more,
                container,
                false);

        binding.hiringRadioButton.setOnClickListener(this);
        binding.jobSeekerRadioButton.setOnClickListener(this);
        binding.getStartedButton.setOnClickListener(this);
        binding.birthDateTextInputLayout.setOnClickListener(this);
        binding.birthDateEditText.setOnClickListener(this);

        addTextChangeListener(binding.birthDateTextInputLayout);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() != null) {
            callbackListener = (CallbackListener) getActivity();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.get_started_button:
                sendInfo();
                break;
            case R.id.hiring_radio_button:
                userType = HIRER_USER_TYPE;
                break;
            case R.id.job_seeker_radio_button:
                userType = TASKER_USER_TYPE;
                break;
            case R.id.birth_date_edit_text:
            case R.id.birth_date_text_input_layout:
                MaterialDatePicker.Builder<Long> datePickerBuilder = MaterialDatePicker.Builder.datePicker();
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                try {
                    datePickerBuilder.setCalendarConstraints(constraintsBuilder.build());
                    MaterialDatePicker<?> picker = datePickerBuilder.build();

                    picker.show(getChildFragmentManager(), picker.toString());
                    addPickerListeners(picker);
                } catch (IllegalArgumentException e) {
                    Log.e("message", e.getMessage());
                }
                break;
        }

    }

    private void sendInfo() {
        if(dateOFBirthTimestamp == 0) {
            binding.birthDateTextInputLayout.setError(getString(R.string.birth_date_empty));
        } else if(Utility.getAge(dateOFBirthTimestamp) < 14){
            binding.birthDateTextInputLayout.setError(getString(R.string.invalid_age));
        } else if(userType == 0){

            showSnackbar("Please select your profile type!");

        } else {

            callbackListener.onAddingMoreInfo(dateOFBirthTimestamp, userType);
            getActivity().onBackPressed();
        }
    }

    private void addPickerListeners(MaterialDatePicker<?> materialDatePicker) {
        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> {
                    binding.birthDateEditText.setText(materialDatePicker.getHeaderText());
                    Object timestampObject = materialDatePicker.getSelection();
                    dateOFBirthTimestamp = (long) timestampObject;
                });
    }

    private void showSnackbar(String message) {

        if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
        snackbar = Snackbar.make(binding.birthDateTextInputLayout, message,
                Snackbar.LENGTH_INDEFINITE);
        Utility.initializeSnackBar(snackbar, getContext());
        snackbar.show();
    }

    private void addTextChangeListener(final TextInputLayout textInputLayout) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (textInputLayout.getError() != null) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

}
