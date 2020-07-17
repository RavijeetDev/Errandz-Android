package com.wmdd.errandz.hirerPostJob;


import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.Response;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;

public class HirerPostJobViewModel extends ViewModel {

    private ErrandzApi errandzApi;

    private MutableLiveData<String> jobDate;
    private MutableLiveData<Response> postJobResponse;
    private long timestamp;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        jobDate = new MutableLiveData<>();
        postJobResponse = new MutableLiveData<>();
        postJobResponse = new MutableLiveData<>();
    }

    public void openDatePicker(FragmentManager fragmentManager) {
        MaterialDatePicker.Builder<Long> datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());
        try {
            datePickerBuilder.setCalendarConstraints(constraintsBuilder.build());
            MaterialDatePicker<?> picker = datePickerBuilder.build();
            picker.show(fragmentManager, picker.toString());
            addPickerListeners(picker);
        } catch (IllegalArgumentException e) {
            Log.e("message", e.getMessage());
        }
    }

    private void addPickerListeners(MaterialDatePicker<?> materialDatePicker) {
        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> {
                    jobDate.setValue(materialDatePicker.getHeaderText());
                    Object timestampObject = materialDatePicker.getSelection();
                    timestamp = (long) timestampObject;
                });
    }

   public MutableLiveData<String> getJobDateString() {
        return jobDate;
   }

    public void makePostJobCall(String jobName, int jobCategory, String wage, String description) {

        int userID = Prefs.getInstance().getUserID();

        errandzApi.postJobRequest(userID, jobName, wage, timestamp, description, jobCategory)
                .enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    postJobResponse.setValue(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }


    public MutableLiveData<Response> getPostJobResponse() {
        return postJobResponse;
    }
}
