package com.example.studyapp.ui.plan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.FirstActivity;
import com.example.studyapp.R;
import com.example.studyapp.recycle.PlanAdapter;
import com.example.studyapp.recycle.PlanData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlanFragment extends Fragment {


    // 로그인하면 FirstActivity에 User의 ID와 PASSWORD가 저장되는데
    // 이를 여기에서 서버의 DB와 연결하기 위해 가져옴
    private String userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID,null);
    private String userPassword = FirstActivity.userInfo.getString(FirstActivity.USER_PASSWORD,null);

    //  ArrayList에 저장 한 뒤 Adapter와 RecyclerView로 ArrayList를 이용해 화면에 동적으로 띄운다.
    static ArrayList<PlanData> recycleArrayList;
    static PlanAdapter planAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    /*
     * Navigation View를 이용하면 화면을 동적으로 바꿀 수 있는데
     * 거기에 사용되는 Fragment를 Java Fragment를 상속받은 클래스와 그냥 연결해준다고 끝나는게 아니다
     * 그렇게 때문에 planViewModel을 사용하여 navigation view에서 해당 화면으로 이동시켜주는 이벤트를 발동했을 때
     * 사용되는 메소드를 이용해 여러 작업을 한다.
     */
    private PlanViewModel planViewModel;

    /*
     * planFragment에 사용할 table에 들어간 view와 각 테이블에 지정된 시간만큼 색을 칠하기 위해
     * 2차원 배열 두개를 생성한다.
     */
    private static TextView[][] tvs = new TextView[24][6];
    static int[][] time = new int[24][6];

    /*
     * DB와 연결 할 떄 보여줄 progressBar 화면 가운데에서 돌아간다.
     */
    static ProgressBar progressBar;
    


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        /*
         * planViewModel에 사용할 Fragment의 class를 정하고
         * 사용되는 view를 가져온다.
         * root에는 가져온 view와 Fragment xml을 이어줌
         */
        planViewModel =
                new ViewModelProvider(this).get(PlanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plan, container, false);

        /*
         * view 생성 선언 등등...
         * 로딩시 보여줄 progressbar
         * 동적으로 사용될 recyclerView와 거기에 사용되는 layout, arraylist, adapter
         */
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) root.findViewById(R.id.rv_plan);
        linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recycleArrayList = new ArrayList<>();
        planAdapter = new PlanAdapter(recycleArrayList);
        recyclerView.setAdapter(planAdapter);

        /*
         * 이 화면으로 왔을 때 실행 될 메소드
         *
         * PlanTask를 이용해 서버로 이 사용자가 누구인지 알려 줄 id와 password를 같이 발송
         */
        planViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                progressBar.setVisibility(View.VISIBLE);

                for(int i = 0;i<tvs.length;i++){
                    for(int j = 0;j<tvs[i].length;j++){
                        String id = "hr";
                        id += (i<10)?"0"+i+j:""+i+j;
                        tvs[i][j] = (TextView)root.findViewById(root.getResources().getIdentifier(id,"id", getActivity().getPackageName()));

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
                 * Fragment xml파일에 있는 사용자 추가하는 버튼, 사용자가 추가버튼을 누를 때 이벤트를 등록
                 * 버튼을 누르면 추가생성하는 페이지로 이동하게 되고 플랜 갯수를 확인하여 플랜 갯수가
                 * 일정이상 되었을 때는 더 이상 생성이 되지 않으며 Toast가 뜨게 됨
                 */
                Button btn_add = (Button) root.findViewById(R.id.btn_add_plan);
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(recycleArrayList.size()>14){
                            PlanTask.showToast(root.getContext(),"플랜이 너무 많습니다.");
                        }else{
                            Intent intent = new Intent(getActivity(), PlanSetPage.class);
                            intent.putExtra("userID",userID);
                            intent.putExtra("userPassword",userPassword);
                            startActivity(intent);
                        }
                    }
                });


                /*
                 * alert을 사용하여 맨 마지막 계획을 지울지말지 정함.
                 * jsonObject에 데이터를 넣고 PlanTask를 이용하여 서버와 연결해 데이터를 송수신함
                 */
                AlertDialog.Builder alert = new AlertDialog.Builder(root.getContext());
                Button btn_min = (Button) root.findViewById(R.id.btn_min_plan);
                btn_min.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(recycleArrayList.size()>0){
                            alert.setTitle("계획을 지우시겠습니까?").setMessage("공부 한 내역은 사라지지 않습니다.");

                            alert.setNegativeButton("네", new DialogInterface.OnClickListener(){
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
                                    PlanTask.showToast(root.getContext(),"지웠습니다");
                                }
                            });

                            alert.setPositiveButton("아니오", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    PlanTask.showToast(root.getContext(),"취소했습니다");
                                }
                            });

                            AlertDialog alertDialog = alert.create();

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
                            PlanTask.showToast(root.getContext(),"플랜이 하나도 없습니다");
                        }
                    }
                });
            }
        });
        return root;
    }
    
    /*
     * 해당 table에 저장 해 둔 table을 저장된 time 2차원배열에 공부 시간에 따라 표가 색칠됨
     * 표가 색칠 된 뒤 Recycler view를 이용해 동적으로 추가함
     */

    /**
     *
     */
    public static void fillTable(){
        for(int i = 0;i<time.length;i++){
            for(int j=0;j<time[i].length;j++){
                TextView tv = tvs[i][j];
                String color = Integer.toHexString(255*time[i][j]/10);
                color =(color.length()==1)?"0"+color:color;
                tv.setBackgroundColor(Color.parseColor("#"+color+"3080ff"));

            }
        }
        planAdapter.notifyDataSetChanged();
//        progressBar.setVisibility(View.GONE);
    }

    /*
     * recycler view에 사용되는 arraylist를 맨마지막만 하나씩 줄이고 늘여도 상관없지만
     * 만일에 중간 내용이 변할 우려를 생각해 전부 제거하고 추가하는 방식을 사용.
     * 시간 배열같은 경우엔 초기화가 필요하기 때문에 무조건 이 메소드를 돌려야 함
     */

    /**
     *
     */
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
}