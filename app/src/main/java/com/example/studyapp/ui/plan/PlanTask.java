package com.example.studyapp.ui.plan;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studyapp.HomeActivity;
import com.example.studyapp.JSONTask;
import com.example.studyapp.R;
import com.example.studyapp.recycle.PlanData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class PlanTask extends JSONTask {




    public PlanTask(JSONObject jsonObject, String urlPath, String method) {
        super(jsonObject, urlPath, method);
    }

    @Override
    protected void onPostExecute(String result) {
        PlanFragment.listRemove();
        try {
            JSONObject jsonObject = new JSONObject(result);
            String resultNum = jsonObject.get("result").toString();
            if(resultNum.equals("1")) {
                JSONArray array = new JSONArray(jsonObject.get("data").toString());
                // Save user info
                // 결과값이 result로 나옴
//                    JSONArray array = jsonObject.get("data");
//                    System.out.println(array);
                for(int i = 0;i<array.length();i++){
//                        System.out.println("#"+Integer.toHexString(i)+"3080ff");
//                        System.out.println("분리"+i);
                    JSONObject jsO = array.getJSONObject(i);
                    int startHour =Integer.parseInt(jsO.getString("START").substring(0,2));
                    int startMin = Integer.parseInt(jsO.getString("START").substring(3,5));
                    int endHour =Integer.parseInt(jsO.getString("END").substring(0,2));
                    int endMin =Integer.parseInt(jsO.getString("END").substring(3,5));

                    String str = timeStr(startHour, startMin,endHour,endMin);
                    timeSet(startHour,startMin,endHour,endMin);

                    PlanData planData = null;

//                        planData = new PlanData(
//                                R.drawable.p0,
//                                jsO.getString("SUBJECT"),
//                                jsO.getString("START")+"부터 "+jsO.getString("END")+"까지",
//                                "#"+Integer.toHexString(i)+""+Integer.toHexString(i)+"3080ff");

                    planData = new PlanData(
                            R.drawable.p0,
                            jsO.getString("SUBJECT"),
                            str,
                            "#"+Integer.toHexString(i)+""+Integer.toHexString(i)+"3080ff");
                    PlanFragment.recycleArrayList.add(planData);
//                        ArrayList<PlanData> arrayList;
                }
                PlanFragment.fillTable();
                PlanFragment.planAdapter.notifyDataSetChanged();
            }else {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//            for(int i =0;i<arrayList.size();i++){
//                System.out.println(arrayList.get(i).getTv_name());
//            }
//            System.out.println(arrayList.size());



    }


    private void timeSet(int startHour, int startMin, int endHour, int endMin){
//        time[][]
//        int startTime = startHour*60+startMin;
//        int endTime = endHour*60+endMin;
        int totalTime = (endHour*60+endMin)-(startHour*60+startMin);
        if(totalTime>0){

            timeSetCalculator(startHour,startMin,endHour,totalTime);
        }else{
            int startTotal = 23*60+59-(startHour*60+startMin);
            int endTotal = endHour*60+endMin;
            timeSetCalculator(0,0,endHour,endTotal);
            timeSetCalculator(startHour,startMin,23,startTotal);
        }
    }
    private void timeSetCalculator(int startHour, int startMin, int endHour, int totalTime){
        for(;startHour<=endHour;startHour++){
            for(;startMin/10<PlanFragment.time[startHour].length;){
                if(totalTime==0)return;
                if(totalTime<10){
                    totalTime-=totalTime;
                    PlanFragment.time[startHour][startMin/10]+=totalTime;
                    startMin+=totalTime;
                }else{
                    totalTime-=10-startMin%10;
                    PlanFragment.time[startHour][startMin/10]+=10-startMin%10;
                    startMin+=10-startMin%10;
                }
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
            st_bTime=(Integer.parseInt(timeStr.substring(3,5))-12)*60+Integer.parseInt(timeStr.substring(6,8));
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
}