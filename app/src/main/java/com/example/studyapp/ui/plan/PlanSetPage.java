package com.example.studyapp.ui.plan;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.studyapp.R;
import com.example.studyapp.ui.group.Group;
import com.example.studyapp.ui.group.GroupListAdapter;
import com.example.studyapp.ui.group.GroupPage;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlanSetPage extends AppCompatActivity {
    private ListView groupListView;
    private GroupListAdapter adapter;
    private List<Group> groupList;
    private String userID;
    private TimePickerDialog.OnTimeSetListener callbackMethod;
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
        // Handle presses on the action bar items

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);  // Hostfragment
//        NavController navController = navHostFragment.getNavController();
//        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
//        NavGraph graph = inflater.inflate(R.navigation.mobile_navigation);
//
//
//        graph.setStartDestination(R.id.navigation_plan);
//
//        navController.setGraph(graph);
//
//
//
//        NavigationView navigationView = findViewById(R.id.navigation_plan);
//        NavigationUI.setupWithNavController(navigationView, navHostFragment.getNavController());
        String response = "{\"response\":[{\"START\":\"14:22:13\",\"END\":\"16:20:18\",\"focusOn\":\"01:29:55\",\"TERM\":\"01:58:05\"}]}";
        try{
            JSONObject jsonObject = new JSONObject(response);
        }catch(JSONException e){

        }
        ArrayList<String> list = new ArrayList<>();
        try {
            writeFile("PlanTime.txt", "Going To Distance For Better Than Yesterday\n", list);
            writeFile("PlanTime.txt", response, list);
            writeFile("PlanTime.txt", response, list);
            list = readFile("PlanTime.txt", list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(list);
        /*
        String pathname = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/PlanTime";
        File file = new File(pathname);
        if(!file.exists())
            file.mkdir();
        try{
            file = new File(pathname+"/Time.txt");
            FileWriter fw = new FileWriter(pathname+"/Time.txt",true);
            BufferedWriter bw = new BufferedWriter(fw);
            if(!file.exists()) {
                file.createNewFile();
                bw.write("Going To Distance For Better Than Yesterday");
            }
            bw.write(response);
            bw.flush();
            bw.close();

//            System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));
//            System.out.println(pathname);
//            System.out.println(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()));
            InputStream is = openFileInput("mytest.txt");

            if(is!=null){
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String str;
                StringBuffer sb = new StringBuffer();
                while((str=br.readLine())!=null) {
                    System.out.println(str);
                    sb.append(str);
                }
                is.close();
                String sc = sb.toString();
            }
        }catch(IOException e){

        }finally{

        }*/

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

        onBackPressed();
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


    private <T> void writeFile(String fileName, String msg, ArrayList<T> list) {
//        try {
//            OutputStreamWriter oStreamWriter = new OutputStreamWriter(openFileOutput(fileName,
//                    Context.MODE_PRIVATE));
//            list = readFile("PlanTime.txt", list);
//            for(int i = 0;i<list.size();i++){
//                oStreamWriter.write((String)list.get(i));
//            }
//            oStreamWriter.write(msg);
//            oStreamWriter.write("yaho");
//            oStreamWriter.close();
//        } catch(FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        FileOutputStream fos = null;
        DataOutputStream dos = null;
        try{
            fos = openFileOutput(fileName,MODE_PRIVATE);
            dos = new DataOutputStream(fos);
            dos.writeUTF(msg);
            dos.flush();
            dos.close();
        }catch(IOException e){

        }
    }

    private <T> ArrayList<T> readFile(String fileName, ArrayList<T> list) throws IOException {
//        try {
//            InputStream iStream = openFileInput(fileName);
//            if(iStream != null) {
//                InputStreamReader iStreamReader = new InputStreamReader(iStream);
//                BufferedReader bufferedReader = new BufferedReader(iStreamReader);
//                String str = "";
//                StringBuffer sBuffer = new StringBuffer();
//                while((str = bufferedReader.readLine()) != null) {
//                    sBuffer.append(str);
//                    list.add((T)sBuffer.toString());
//                }
//                iStream.close();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        FileInputStream fis = openFileInput(fileName);
        DataInputStream dis = new DataInputStream(fis);

        String data2 = dis.readUTF();
        dis.close();
        System.out.println(data2);
        list.add((T)data2);
        return list;
    }

}
