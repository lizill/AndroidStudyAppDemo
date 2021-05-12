package com.example.studyapp.ui.plan;

import android.os.AsyncTask;

import com.example.studyapp.FirstActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONTask extends AsyncTask <String, String, String>{
    private JSONObject jsonObject;
    String userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID,null);
    String userPassword =FirstActivity.userInfo.getString(FirstActivity.USER_PASSWORD,null);
    public JSONTask(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    protected String doInBackground(String... urls)  {
        try {
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://132.226.20.103:3000/plan");
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                System.out.println(userID);
                System.out.println(userPassword);
                jsonObject.accumulate("user_id", userID);
                jsonObject.accumulate("user_password", userPassword);

                con.setDoOutput(true);
                con.setDoInput(true);

                con.connect();

                OutputStream outputStream = con.getOutputStream();
                System.out.println(jsonObject.toString());
                // 서버로 데이터를 보내기 위한 버퍼
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();

                // 서버로부터 데이터를 받기 위한 버퍼
                InputStream inputStream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString(); // 결과값을 리턴해줌
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null) con.disconnect();
                if(reader != null) reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println(result);
    }
}
