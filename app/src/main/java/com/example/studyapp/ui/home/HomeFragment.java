package com.example.studyapp.ui.home;

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

    private ArrayList<HomeData> arrayList;
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*
        view 생성 선언 등등...
        TextView textView = root.findViewById(R.id.text_home);
        */

        recyclerView = (RecyclerView) root.findViewById(R.id.rv_home);
        linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        homeAdapter = new HomeAdapter(arrayList);
        recyclerView.setAdapter(homeAdapter);



        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                /*
                onChnaged= 뷰를 눌러서 실행했을때 실행시킬 이벤트 삽입
                 */
                Button btn_add = (Button) root.findViewById(R.id.btn_add_home);
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeData homeData = new HomeData(R.mipmap.ic_launcher, "test", "view");
                        arrayList.add(homeData);
                        homeAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        return root;
    }
}