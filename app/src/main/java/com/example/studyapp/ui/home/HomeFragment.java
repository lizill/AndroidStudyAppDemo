package com.example.studyapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.R;
import com.example.studyapp.recycle.HomeAdapter;
import com.example.studyapp.recycle.HomeData;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private Button sub1;
    private TextView tv_data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*
        view 생성 선언 등등...
        TextView textView = root.findViewById(R.id.text_home);
        */

        sub1 = (Button) root.findViewById(R.id.sub1);
        tv_data = (TextView) root.findViewById(R.id.tv_data);

        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //누른 과목 정보 보내주기
                Intent intent = new Intent(getActivity(), StopwatchActivity.class);
                intent.putExtra("subject", sub1.getText());
                startActivity(intent);

            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                /*
                onChnaged= 뷰를 눌러서 실행했을때 실행시킬 이벤트 삽입
                 */

            }
        });
        return root;
    }
}