package ummom.teacher.etc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ummom.login.LoginModel;

import android.util.Log;


public class HttpGetRequest {
    public String getHTML(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while((line = rd.readLine())!=null){
                result += line;
            }
            rd.close();
            
        } catch (Exception e) {
        	Log.d("@@@","@@@@@@ >> badbadbad"+LoginModel.password);
            e.printStackTrace();
        }
        return result;
    }
}