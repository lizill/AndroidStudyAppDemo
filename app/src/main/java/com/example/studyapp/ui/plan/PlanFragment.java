package com.example.studyapp.ui.plan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.FirstActivity;
import com.example.studyapp.HomeActivity;
import com.example.studyapp.JSONTask;
import com.example.studyapp.R;
import com.example.studyapp.recycle.PlanAdapter;
import com.example.studyapp.recycle.PlanBtnData;
import com.example.studyapp.recycle.PlanData;
import com.example.studyapp.ui.group.GroupPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlanFragment extends Fragment {

    static ArrayList<PlanData> recycleArrayList;
    static PlanAdapter planAdapter;
    private static Toast toast;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PlanViewModel planViewModel;

    private String userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID,null);
    private String userPassword = FirstActivity.userInfo.getString(FirstActivity.USER_PASSWORD,null);
    static ProgressBar progressBar;

    static ViewGroup container;
    static LayoutInflater inflater;
    static PlanFragment planFragment;
    static int[][] time = new int[24][6];
    static int[][] viewID = new int[24][6];
    static TextView[][] tvs = new TextView[24][6];
    /*
    view 생성 선언 등등...
    TextView textView = root.findViewById(R.id.text_home);
    */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID,null);
//        userPassword = FirstActivity.userInfo.getString(FirstActivity.USER_PASSWORD,null);


        this.container = container;
        this.inflater = inflater;
        this.planFragment = this;


        planViewModel =
                new ViewModelProvider(this).get(PlanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plan, container, false);
        /*
        view 생성 선언 등등...
        TextView textView = root.findViewById(R.id.text_home);
        */
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) root.findViewById(R.id.rv_plan);
        linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recycleArrayList = new ArrayList<>();
        planAdapter = new PlanAdapter(recycleArrayList);

        /*
        arrayListBtn
        planBtnAdapter
        */

        recyclerView.setAdapter(planAdapter);

        planViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                progressBar.setVisibility(View.VISIBLE);

                for(int i = 0;i<tvs.length;i++){
                    for(int j = 0;j<tvs[i].length;j++){
                        String id = "hr";
                        id += (i<10)?"0"+i+j:""+i+j;
                        tvs[i][j] = (TextView)root.findViewById(root.getResources().getIdentifier(id,"id", PlanFragment.planFragment.getActivity().getPackageName()));
//                        tvs[i][j].setBackgroundColor(Color.parseColor("#3080ff"));
                    }
                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("user_id", userID);
                    jsonObject.accumulate("user_password", userPassword);

                    PlanTask planTask = new PlanTask(jsonObject, "plan", "POST");
                    planTask.execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
                onChnaged= 뷰를 눌러서 실행했을때 실행시킬 이벤트 삽입
                 */
//                try{
//                    while(check) {
//                        System.out.println(check);
//                        Thread.sleep(1000);
//                    }
//                }catch(InterruptedException e){
//
//                }



//                AlertDialog.Builder alter = new AlertDialog.Builder(root.getContext());

                AlertDialog.Builder alter = new AlertDialog.Builder(root.getContext());
                Button btn_add = (Button) root.findViewById(R.id.btn_add_plan);
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(recycleArrayList.size()>14){
                            showToast(root.getContext(),"플랜이 너무 많습니다.");
                        }else{
                            Intent intent = new Intent(getActivity(), PlanSetPage.class);

                            startActivity(intent);
                        }
                    }
                });
                Button btn_min = (Button) root.findViewById(R.id.btn_min_plan);
                btn_min.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(recycleArrayList.size()>0){


                            alter.setTitle("계획을 지우시겠습니까?").setMessage("공부 한 내역은 사라지지 않습니다.");

                            alter.setNegativeButton("네", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.accumulate("user_id", userID);
                                        jsonObject.accumulate("user_password", userPassword);
                                        jsonObject.accumulate("position", recycleArrayList.size());
                                        PlanTask planTask = new PlanTask(jsonObject, "planDelete", "POST");
                                        planTask.execute();

                                        planTask = new PlanTask(jsonObject, "plan", "POST");
                                        planTask.execute();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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

                            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    Button positiveB = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                    Button negativeB = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                                    positiveB.setTextColor(R.color.maincolor);
                                    negativeB.setTextColor(R.color.maincolor);
                                }
                            });


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

    public static void fillTable(){
        View root = inflater.inflate(R.layout.fragment_plan, PlanFragment.container, false);
        for(int i = 0;i<time.length;i++){
            for(int j=0;j<time[i].length;j++){
//                String id = "hr";
//                id += (i<10)?"0"+i+j:""+i+j;
//                int resID = root.getResources().getIdentifier(id,"id", PlanFragment.planFragment.getActivity().getPackageName());
//                viewID[i][j] = resID;
//                TextView tv = (TextView)root.findViewById(resID);
                TextView tv = tvs[i][j];
                String color = Integer.toHexString(255*time[i][j]/10);
                color =(color.length()==1)?"0"+color:color;
                tv.setBackgroundColor(Color.parseColor("#"+color+"3080ff"));

            }
        }

    }

    public static void listRemove(){
        while(!recycleArrayList.isEmpty()){
            recycleArrayList.remove(0);
        }
        for(int i = 0;i<time.length;i++){
            for(int j = 0;j<time[i].length;j++){
                time[i][j]=0;
            }
        }
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