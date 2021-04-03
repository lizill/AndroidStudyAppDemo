package com.example.studyapp.ui.plan;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.R;
import com.example.studyapp.recycle.PlanAdapter;
import com.example.studyapp.recycle.PlanData;

import java.util.ArrayList;

public class PlanFragment extends Fragment {

    private ArrayList<PlanData> arrayList;
    private PlanAdapter planAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private PlanViewModel planViewModel;
    /*
    view 생성 선언 등등...
    TextView textView = root.findViewById(R.id.text_home);
    */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        planViewModel =
                new ViewModelProvider(this).get(PlanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plan, container, false);
        /*
        view 생성 선언 등등...
        TextView textView = root.findViewById(R.id.text_home);
        */

        recyclerView = (RecyclerView) root.findViewById(R.id.rv_plan);
        linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        planAdapter = new PlanAdapter(arrayList);
        recyclerView.setAdapter(planAdapter);

        planViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                /*
                onChnaged= 뷰를 눌러서 실행했을때 실행시킬 이벤트 삽입
                 */

                Button btn_add = (Button) root.findViewById(R.id.btn_add_plan);
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(arrayList.size()<=20){
                            PlanData planData = new PlanData(R.drawable.rp, "test", "view");
                            arrayList.add(planData);
                            planAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(root.getContext(), "플랜을 너무 많이 생성하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Button btn_min = (Button) root.findViewById(R.id.btn_min_plan);
                btn_min.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(arrayList.size()>0){
                            arrayList.remove(arrayList.size()-1);
                            planAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(root.getContext(), "플랜이 하나도 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return root;
    }
}