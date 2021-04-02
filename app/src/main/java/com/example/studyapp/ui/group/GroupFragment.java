package com.example.studyapp.ui.group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyapp.FirstActivity;
import com.example.studyapp.GroupActivity;
import com.example.studyapp.HomeActivity;
import com.example.studyapp.R;

public class GroupFragment extends Fragment {

    private GroupViewModel groupViewModel;
    private Button logoutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        groupViewModel =
                new ViewModelProvider(this).get(GroupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_group, container, false);
        final TextView textView = root.findViewById(R.id.text_group);
        groupViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                logoutButton = (Button) root.findViewById(R.id.logoutButton);
                logoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Logout
                        SharedPreferences.Editor editor = FirstActivity.userInfo.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(root.getContext(), FirstActivity.class);
                        root.getContext().startActivity(intent);
//                        finish();
                        Toast.makeText(root.getContext(), "Logout!", Toast.LENGTH_LONG).show();
                    }
                });

                textView.setText(s);
            }
        });
        return root;
    }
}