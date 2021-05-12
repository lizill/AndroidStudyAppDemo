package com.example.studyapp.ui.plan;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.FirstActivity;
import com.example.studyapp.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.sql.DriverManager.println;

public class PlanSetPage extends AppCompatActivity {
    private String userID;
    private final int PERMISSIONS_REQUEST_RESULT = 1;

    public static Button st_btn;
    public static Button en_btn;
    public static TextView en_txt;

    static int st_hour;
    static int st_min;
    static int en_hour;
    static int en_min;
    static boolean touch=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_set_page);
        userID= FirstActivity.USER_ID;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //권한을 거절하면 재 요청을 하는 함수
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_RESULT);
            }
        }//권한 요청
        
        EditText editText = findViewById(R.id.plan_subject);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_ENTER) return true;
                return false;
            }
        });

        Calendar mCal = Calendar.getInstance();
        st_hour = mCal.get(Calendar.HOUR_OF_DAY);
        st_min = mCal.get(Calendar.MINUTE);

        st_btn = findViewById(R.id.start_time_btn);
        en_btn = findViewById(R.id.end_time_btn);
        en_txt = findViewById(R.id.end_time);
        st_btn.setText(PlanTimePicker.hourCal(st_hour, st_min,true));
        en_hour = st_hour;
        en_min = st_min+30;
        en_btn.setText(PlanTimePicker.hourCal(en_hour, en_min,false));

        PlanTimePicker planTimePicker = new PlanTimePicker();
        st_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                touch=true;
                planTimePicker.show(getSupportFragmentManager(),
                        "help");

            }
        });
        en_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                touch=false;
                planTimePicker.show(getSupportFragmentManager(),
                        "help");
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        ArrayList<String> list = new ArrayList<>();
//        new PlanTask("https://dong0110.com/plan").execute();
//        new PlanTask("https://dong0110.com/planInsert").execute();
//        parseJson("https://dong0110.com/plan",st_hour+":"+st_min,en_hour+":"+en_min);
//        parseJson("https://dong0110.com/planInsert",st_hour+":"+st_min,en_hour+":"+en_min);

        try {
            JSONObject jsonObject = new JSONObject();
            new JSONTask(jsonObject).execute();
//            jsonObject.accumulate("user_id", userID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch(item.getItemId()){
            case R.id.plan_action_btn:
                System.out.println(st_hour+":"+st_min);
                System.out.println(en_hour+":"+en_min);
                String startTime = "{"+st_hour+":"+st_min+"}";
                String endTime = "{"+st_hour+":"+st_min+"}";
                break;
            default:
                break;
        }



//        onBackPressed();

//        Intent intent = new Intent(this, GroupPage.class);
//        startActivity(intent);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (PERMISSIONS_REQUEST_RESULT == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "권한 요청이 됐습니다.", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "권한 요청을 해주세요.", Toast.LENGTH_SHORT).show();
                finish();
            }
            return;
        }
    }


    /*private <T> void writeFile(String fileName, String msg, ArrayList<T> list) {
        File file = new File("/data/data/com.example.studyapp/files/PlanTime.txt");
        boolean find = file.exists();


//        FileOutputStream fos = null;
//        DataOutputStream dos = null;
//        try{
//            fos = openFileOutput(fileName,MODE_APPEND);
//            dos = new DataOutputStream(fos);
//            if(!find)
//                dos.writeUTF("Going To Distance For Better Than Yesterday\n");
//            dos.writeUTF(msg);
//            dos.flush();
//            dos.close();
//        }catch(IOException e){
//
//        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            if(!find){
                file.createNewFile();
                bw.write("Going To Distance For Better Than Yesterday\n");
                bw.write(msg);
            }else{
                bw.write(","+msg);
            }

            bw.flush();
            bw.close();
        }catch(IOException e){

        }
    }*/

    /*private <T> ArrayList<T> readFile(String fileName, ArrayList<T> list) throws IOException {

//        FileInputStream fis = openFileInput(fileName);
//        DataInputStream dis = new DataInputStream(fis);
//
//        String str = dis.readUTF();
//        dis.close();
//        while(str!=null){
//            list.add((T)str);
//            str = dis.readUTF();
//        }

        File file = new File("/data/data/com.example.studyapp/files/PlanTime.txt");
        FileReader fr = null ;
        BufferedReader br = null;
        String str = "";
        try {
            // open file.
            fr = new FileReader(file) ;
            br = new BufferedReader(fr);
            while((str=br.readLine())!=null){
                list.add((T)str);
            }

            fr.close() ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }

        return list;
    }*/

}
