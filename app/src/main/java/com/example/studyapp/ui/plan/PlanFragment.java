package com.example.studyapp.ui.plan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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

import com.example.studyapp.HomeActivity;
import com.example.studyapp.R;
import com.example.studyapp.recycle.PlanAdapter;
import com.example.studyapp.recycle.PlanBtnData;
import com.example.studyapp.recycle.PlanData;
import com.example.studyapp.ui.group.GroupPage;

import java.util.ArrayList;

public class PlanFragment extends Fragment {

    private ArrayList<PlanData> arrayList;
    private ArrayList<PlanBtnData> arrayListBtn;
    private PlanAdapter planAdapter;
    private static Toast toast;
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


        /*
        arrayListBtn
        planBtnAdapter

        */

        recyclerView.setAdapter(planAdapter);

        planViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                /*
                onChnaged= 뷰를 눌러서 실행했을때 실행시킬 이벤트 삽입
                 */
                AlertDialog.Builder alter = new AlertDialog.Builder(root.getContext());
                Button btn_add = (Button) root.findViewById(R.id.btn_add_plan);
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PlanSetPage.class);
                        startActivity(intent);

//                        if(arrayList.size()<=20){
//                            PlanData planData=new PlanData(R.drawable.p0, "test", "view","#003080ff");
//                            switch(arrayList.size()){
//                                case 0:
//                                    planData = new PlanData(R.drawable.p0, "test", "view","#003080ff");
//                                    break;
//                                case 1:
//                                    planData = new PlanData(R.drawable.p1, "test", "view","#113080ff");
//                                    break;
//                                case 2:
//                                    planData = new PlanData(R.drawable.p2, "test", "view","#223080ff");
//                                    break;
//                                case 3:
//                                    planData = new PlanData(R.drawable.p0, "test", "view","#333080ff");
//                                    break;
//                                case 4:
//                                    planData = new PlanData(R.drawable.p1, "test", "view","#443080ff");
//                                    break;
//                                case 5:
//                                    planData = new PlanData(R.drawable.p2, "test", "view","#553080ff");
//                                    break;
//                                case 6:
//                                    planData = new PlanData(R.drawable.p0, "test", "view","#663080ff");
//                                    break;
//                                case 7:
//                                    planData = new PlanData(R.drawable.p1, "test", "view","#773080ff");
//                                    break;
//                                case 8:
//                                    planData = new PlanData(R.drawable.p2, "test", "view","#883080ff");
//                                    break;
//                                case 9:
//                                    planData = new PlanData(R.drawable.p0, "test", "view","#993080ff");
//                                    break;
//                                case 10:
//                                    planData = new PlanData(R.drawable.p1, "test", "view","#aa3080ff");
//                                    break;
//                                case 11:
//                                    planData = new PlanData(R.drawable.p2, "test", "view","#bb3080ff");
//                                    break;
//                                case 12:
//                                    planData = new PlanData(R.drawable.p0, "test", "view","#cc3080ff");
//                                    break;
//                                case 13:
//                                    planData = new PlanData(R.drawable.p1, "test", "view","#dd3080ff");
//                                    break;
//                                case 14:
//                                    planData = new PlanData(R.drawable.p2, "test", "view","#ee3080ff");
//                                    break;
//                                default:
//                                    planData = new PlanData(R.drawable.p1, "test", "view","#ff3080ff");
//                                    break;
//                            }
////                            PlanData planData = new PlanData(R.drawable.rp, "test", "view","ffffff");
//                            arrayList.add(planData);
//                            planAdapter.notifyDataSetChanged();
//                        }else{
//                            showToast(root.getContext(),"플랜을 너무 많이 생성하셨습니다");
//                        }

                    }
                });
                Button btn_min = (Button) root.findViewById(R.id.btn_min_plan);
                btn_min.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {


                        if(arrayList.size()>0){
                            alter.setTitle("계획을 지우시겠습니까?").setMessage("공부 한 내역은 사라지지 않습니다.");
                            alter.setNegativeButton("네", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    arrayList.remove(arrayList.size()-1);
                                    planAdapter.notifyDataSetChanged();
//                                    Toast.makeText(root.getContext(),"지웠습니다",Toast.LENGTH_SHORT).show();
                                    showToast(root.getContext(),"지웠습니다");
                                }
                            });
                            alter.setPositiveButton("아니오", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
//                                    Toast.makeText(root.getContext(),"취소했습니다.",Toast.LENGTH_SHORT).show();
                                    showToast(root.getContext(),"취소했습니다");
                                }
                            });

                            AlertDialog alertDialog = alter.create();
                            alertDialog.show();

                        }else{
//                            Toast.makeText(root.getContext(), "플랜이 하나도 없습니다.", Toast.LENGTH_SHORT).show();
                            showToast(root.getContext(),"플랜이 하나도 없습니다");
                        }
                    }
                });
            }
        });
        return root;
    }


    public static void showToast(Context context, String message) {

        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}