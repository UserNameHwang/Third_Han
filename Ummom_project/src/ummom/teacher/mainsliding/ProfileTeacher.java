package ummom.teacher.mainsliding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import ummom.login.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

public class ProfileTeacher extends FragmentActivity{
	
	TextView Tname;
	TextView Tphone;
	TextView Tbirth;
	TextView Tschool;
	TextView Tgrade;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_teacher);
		getActionBar().hide();
		
		Tname = (TextView) findViewById(R.id.textV_profile_teacherName);
		Tphone = (TextView) findViewById(R.id.textV_profile_teacherPhone);
		Tbirth = (TextView) findViewById(R.id.textV_profile_teacherBirth);
		Tschool = (TextView) findViewById(R.id.textV_profile_teacherSchool);
		Tgrade = (TextView) findViewById(R.id.textV_profile_teacherGrade);
		
		TeacherThread tThread = new TeacherThread();
		tThread.start();
		
		try {
			tThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.page_donmove, R.anim.page_disappear);
		finish();
	}
	
	class TeacherThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			String id = "test";

			HttpClient client = new DefaultHttpClient();
			String path = "http://14.63.212.236/index.php/teacher/getTeacherProfile";

			HttpPost post = new HttpPost(path);
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 30000);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", id));

			UrlEncodedFormEntity ent;
			
			try {
				ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse httpResponse = client.execute(post);
				HttpEntity resEn = httpResponse.getEntity();
				String parse = EntityUtils.toString(resEn);
				JSONObject object = new JSONObject(parse);

				JSONObject obj_p = object.getJSONObject("profile");
				
				Log.d("", "profile : " + obj_p.toString() );
				
				String name = "이름 : " + obj_p.get("mb_name").toString();
				String phone = "번호 : " +obj_p.get("mb_phone").toString();
				String birth = "생일 : " + obj_p.get("mb_birth").toString();
				String url = "";
				String school = "학교 : " + obj_p.get("t_school").toString();;
				String grade = "학년 : " + obj_p.get("t_grade").toString();;
				
				Tname.setText(name);
				Tphone.setText(phone);
				Tbirth.setText(birth);
				Tschool.setText(school);
				Tgrade.setText(grade);
				
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
