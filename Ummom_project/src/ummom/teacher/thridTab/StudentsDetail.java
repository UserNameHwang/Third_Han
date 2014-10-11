package ummom.teacher.thridTab;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ummom.login.R;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

public class StudentsDetail extends FragmentActivity {
	private String regId;
	
	TextView textV_name_p;
	TextView textV_phone_p;
	TextView textV_birth_p;
	
	TextView textV_name_c;
	TextView textV_phone_c;
	TextView textV_birth_c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.students_detail);

		ActionBar actionbar = getActionBar();
		actionbar.hide();
		Intent intent = getIntent();
		regId = intent.getStringExtra("regId");
		
		textV_name_p = (TextView)findViewById(R.id.textView_detail_Parentname);
		textV_phone_p = (TextView)findViewById(R.id.textV_students_detail_Parentphone);
		textV_birth_p = (TextView)findViewById(R.id.textV_students_detail_Parentbirth);
		
		textV_name_c = (TextView)findViewById(R.id.textView_detail_Childname);
		textV_phone_c = (TextView)findViewById(R.id.textV_students_detail_Childphone);
		textV_birth_c = (TextView)findViewById(R.id.textV_students_detail_Childbirth);
		
		
		DetailTread detailThread = new DetailTread();		
		detailThread.start();
		
		try {
			detailThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class DetailTread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			String id = regId;

			HttpClient client = new DefaultHttpClient();
			String path = "http://14.63.212.236/index.php/teacher/getListDetail";

			HttpPost post = new HttpPost(path);
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 30000);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("reg_id", id));

			UrlEncodedFormEntity ent;
			
			try {
				ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse httpResponse = client.execute(post);
				HttpEntity resEn = httpResponse.getEntity();
				String parse = EntityUtils.toString(resEn);
				JSONObject object = new JSONObject(parse);

				JSONObject obj_p = object.getJSONObject("parent");
				JSONObject obj_c = object.getJSONObject("children");
				
				String name_p = "이름 : " + obj_p.get("mb_name").toString();
				String phone_p = "번호 : " +obj_p.get("mb_phone").toString();
				String birth_p = "생일 : " + obj_p.get("mb_birth").toString();
				
				String name_c = "이름 : " + obj_c.get("mb_name").toString();
				String phone_c = "번호 : " +obj_c.get("mb_phone").toString();
				String birth_c = "생일 : " +obj_c.get("mb_birth").toString();
				String profile_c = obj_c.get("profile_url").toString();				
				
				textV_name_p.setText(name_p);
				textV_phone_p.setText(phone_p);
				textV_birth_p.setText(birth_p);
				
				textV_name_c.setText(name_c);
				textV_phone_c.setText(phone_c);
				textV_birth_c.setText(birth_c);

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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.page_donmove, R.anim.page_disappear);
		finish();

	}
}
