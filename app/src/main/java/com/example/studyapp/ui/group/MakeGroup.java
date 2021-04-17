package com.example.studyapp.ui.group;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studyapp.R;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.userInfo;

public class MakeGroup extends AppCompatActivity {
    private String userID;
    private EditText categorySelect;
    final String[] categoryArr = {"초등학교", "중학교", "고등학교", "대학교"};
    private EditText goalTimeSelect;
    final String[] goalTimeArr = new String[12];
    private int goalTime = 0;
    private EditText personCountSelect;
    final String[] personCountArr = new String[49];
    private int personCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);

        userID = userInfo.getString(USER_ID,null);

        categorySelect = findViewById(R.id.category);
        categorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakeGroup.this);
                builder.setTitle("카테고리 선택");
                builder.setItems(categoryArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        categorySelect.setText(categoryArr[which]);
                        Toast.makeText(getApplicationContext(), categoryArr[which], Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });

        goalTimeSelect = findViewById(R.id.goalTime);
        for(int i = 0; i < 12; i++){
            goalTimeArr[i] = "하루 " + (i+1) + "시간";
        }
        goalTimeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakeGroup.this);
                builder.setTitle("목표시간 선택");
                builder.setItems(goalTimeArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goalTimeSelect.setText(goalTimeArr[which]);
                        goalTime = which + 1;
                        Toast.makeText(getApplicationContext(), goalTimeArr[which], Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });

        personCountSelect = findViewById(R.id.personCount);
        for(int i = 0; i < 49; i++){
            personCountArr[i] =(i+2) + "명";
        }
        personCountSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakeGroup.this);
                builder.setTitle("모집인원 선택");
                builder.setItems(personCountArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        personCountSelect.setText(personCountArr[which]);
                        personCount = which + 2;
                        Toast.makeText(getApplicationContext(), personCountArr[which], Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });
    }
}