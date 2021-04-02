package com.example.studyapp.ui.group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyapp.FirstActivity;
import com.example.studyapp.GroupActivity;
import com.example.studyapp.R;

public class GroupViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GroupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is group fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}