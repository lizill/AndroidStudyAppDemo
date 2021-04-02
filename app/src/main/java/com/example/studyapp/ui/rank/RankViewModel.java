package com.example.studyapp.ui.rank;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RankViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RankViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is rank fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}