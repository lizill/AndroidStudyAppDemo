package com.example.studyapp.ui.plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlanViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    public PlanViewModel() {
        // PlanFragment에서 불러온 Fragment가 아무것도 없을 경우 기본적으로 띄울 Text
        mText = new MutableLiveData<>();
        mText.setValue("This is plan fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}