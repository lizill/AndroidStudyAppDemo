package com.example.studyapp.ui.plan;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.studyapp.JSONTask;
import com.example.studyapp.R;
import com.example.studyapp.recycle.PlanData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;



public class PlanTask extends JSONTask {

    /*
     * 작업시 여러 결과 사항을 띄울 Toast
     * 화면 하단에 띄운다.
     */
    private static Toast toast;


    public PlanTask(JSONObject jsonObject, String urlPath, String method) {
        super(jsonObject, urlPath, method);
    }
    /*
     * 서버와 연결하고 난 뒤 결과를 받을 때 사용하는 메소드
     * 결과에 필요한 시작과 끝 시간을 가져와 메소드로 시간을 사람들이 읽기 쉽게 조절하여 PlanData에 저장하고
     * 그 PLanData를 ArrayList에 넣어 recycler view로 동적으로 보여준다.
     */
    @Override
    protected void onPostExecute(String result) {
        PlanFragment.listRemove();
        try {
            JSONObject jsonObject = new JSONObject(result);
            String resultNum = jsonObject.get("result").toString();
            if(resultNum.equals("1")) {
                JSONArray array = new JSONArray(jsonObject.get("data").toString());
                for(int i = 0;i<array.length();i++){
                    JSONObject jsO = array.getJSONObject(i);
                    int startHour =Integer.parseInt(jsO.getString("START").substring(0,2));
                    int startMin = Integer.parseInt(jsO.getString("START").substring(3,5));
                    int endHour =Integer.parseInt(jsO.getString("END").substring(0,2));
                    int endMin =Integer.parseInt(jsO.getString("END").substring(3,5));

                    String str = timeStr(startHour, startMin,endHour,endMin);
                    timeSet(startHour,startMin,endHour,endMin);

                    PlanData planData = new PlanData(
                            R.drawable.p0,
                            jsO.getString("SUBJECT"),
                            str,
                            "#"+Integer.toHexString(i)+""+Integer.toHexString(i)+"3080ff");
                    PlanFragment.recycleArrayList.add(planData);
//                        ArrayList<PlanData> arrayList;
                }
//                PlanFragment.planAdapter.notifyDataSetChanged();
                PlanFragment.fillTable();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PlanFragment.progressBar.setVisibility(View.GONE);
    }


    private void timeSet(int startHour, int startMin, int endHour, int endMin){
        int totalTime = (endHour*60+endMin)-(startHour*60+startMin);
        if(totalTime>0){
            timeSetCalculator(startHour,startMin,endHour,totalTime);
        }else{
            int startTotal = 24*60-(startHour*60+startMin);
            int endTotal = endHour*60+endMin;
            timeSetCalculator(0,0,endHour,endTotal);
            timeSetCalculator(startHour,startMin,23,startTotal);
        }
    }
    private void timeSetCalculator(int startHour, int startMin, int endHour, int totalTime){
        for(;startHour<=endHour;startHour++){
            for(;startMin/10<PlanFragment.time[startHour].length;){
                if(totalTime<=0)return;
                int plnum = (totalTime<10-startMin%10)?totalTime:10-startMin%10;
                totalTime-=plnum;
                PlanFragment.time[startHour][startMin/10]+=plnum;
                startMin+=plnum;
            }
            startMin =0;
        }

    }



    private String timeStr(int startTime, int startMin, int endTime, int endMin){
        String str = "";
        String startTimeStr = "";
        String startMinStr = ""+startMin;
        String endTimeStr = "";
        String endMinStr = ""+endMin;
        if(startMin<10) startMinStr = "0"+ startMin;
        if(endMin<10) endMinStr = "0"+ endMin;
        if(startTime>=12) {
            str += "오후";
        }else{
            str += "오전";
        }
        if(startTime>12){
            startTime-=12;
        }else if(startTime == 0){
            startTime = 12;
        }
        if(startTime<10)startTimeStr+="0"+startTime;
        else startTimeStr = ""+startTime;
        str += " "+ startTimeStr+":"+startMinStr+" ~ ";

        if(endTime>=12) {
            str += "오후";
        }else{
            str += "오전";
        }
        if(endTime>12){
            endTime-=12;
        }else if(endTime == 0){
            endTime = 12;
        }
        if(endTime<10)endTimeStr+="0"+endTime;
        else endTimeStr = ""+endTime;
        str += " "+ endTimeStr+":"+endMinStr;
        return str;
    }

    public static int timeCal(String timeStr){
        int st_bTime;
        if(timeStr.substring(0,2).equals("오전")&&(timeStr.substring(3,5).equals("12"))){
            // 0
            st_bTime=Integer.parseInt(timeStr.substring(6,8));
        }else if(timeStr.substring(0,2).equals("오후")&&!(timeStr.substring(3,5).equals("12"))){
            // 13~23
            st_bTime=(Integer.parseInt(timeStr.substring(3,5))+12)*60+Integer.parseInt(timeStr.substring(6,8));
        }else{
            // 1~12
            try{
                st_bTime=Integer.parseInt(timeStr.substring(3,5))*60+Integer.parseInt(timeStr.substring(6,8));
            }catch(NumberFormatException e){
                st_bTime=Integer.parseInt(timeStr.substring(3,4))*60+Integer.parseInt(timeStr.substring(5,7));
            }

        }
        return st_bTime;
    }
    public static void showToast(Context context, String message) {

        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}