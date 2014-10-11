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

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StudentsListFragment extends Fragment{
	
	ListView listView;
	StudentsListAdapter adapter;
	private FragmentActivity frag;
	View view ;
	
	Button btn;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(frag == null){
			frag = (FragmentActivity) getActivity();
		}
		
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.teacher_thirdtab_studentslist, container,false);
		listView = (ListView) view.findViewById(R.id.listView);		
		adapter = new StudentsListAdapter(view.getContext());
		
		/*btn = (Button) view.findViewById(R.id.testBtn);
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addAdapter();				
			}
		});	*/
		
		ThreadStudentsList threadlist = new ThreadStudentsList();
		
		threadlist.start();
		
		try {
			threadlist.join();
			listView.setAdapter(adapter);			
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {					
					// TODO Auto-generated method stub
					StudentsView sView = (StudentsView) v;
					TextView regId = (TextView)sView.findViewById(R.id.reg_id);
					Log.d("", "RegId : " +  regId.getText().toString());
					
					Intent intent = new Intent(frag, StudentsDetail.class);
					intent.putExtra("regId", regId.getText().toString());
					startActivity(intent);					
					frag.overridePendingTransition(R.anim.page_appear, R.anim.page_donmove);
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return view;
	}
	
	public void addAdapter(){		
		int img5 = R.drawable.sample_view4;
		/*adapter.addItem(new StudentsItem(img5, "김동은2", 
				"91.02.27", "010-3306-5990"));*/
		
		listView.setAdapter(adapter);
		view.invalidate();		
	}
	
	class ThreadStudentsList extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			HttpClient client = new DefaultHttpClient();
			String path = "http://14.63.212.236/index.php/teacher/getStudentsList";
			
			HttpPost post = new HttpPost(path);
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 30000);

			String id = "test";
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mb_id", id));					

			UrlEncodedFormEntity ent;
			try {
				ent = new UrlEncodedFormEntity(params,	HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse httpResponse = client.execute(post);
				HttpEntity resEn = httpResponse.getEntity();
				String parse = EntityUtils.toString(resEn);
				
				JSONObject object = new JSONObject(parse);
				
				JSONArray array = object.getJSONArray("studentsList");
				
				int img5 = R.drawable.sample_view4;
				for(int i=0; i < array.length() ; i++){
					JSONObject tmp =  array.getJSONObject(i);
					String birth = tmp.get("mb_birth").toString();					
					String mb_birth[] = birth.substring(2, birth.length()).split("-");
					
					adapter.addItem(new StudentsItem(tmp.get("reg_id").toString() ,img5, tmp.get("mb_name").toString()
							, mb_birth[0]+"."+mb_birth[1]+"."+mb_birth[2]
							, tmp.get("mb_phone").toString() ));
				}
				
				
				
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
