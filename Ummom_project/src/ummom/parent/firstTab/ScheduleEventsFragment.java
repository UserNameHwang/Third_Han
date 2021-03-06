package ummom.parent.firstTab;

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

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class ScheduleEventsFragment extends Fragment {
	
	private LinearLayout rel;
	private View view;
	
	private View child;
	
	private TextView title;
	private TextView des;
	
	private LayoutInflater inflater;
	private ViewGroup container;

	private String Sch_id;
	
	public String getSch_id() {
		return Sch_id;
	}

	public void setSch_id(String id) {
		this.Sch_id = id;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		this.container = container;
		
		view = inflater.inflate(R.layout.schedule_events2, container,false);
		
		
		rel = (LinearLayout) view.findViewById(R.id.linear_events_list);
		
		initEventsFramget(CalendarFragment.touchDay);
		
		return view;
	}
	
	public void initEventsFramget(String date){
		CalendarThread calendarthread = new CalendarThread(date, "test");				
		calendarthread.start();					
		try {						
			calendarthread.join();						
			viewClear();						
			if (calendarthread.getCount() > 0) {
				JSONArray array = calendarthread.getJarray();										
				for(int i=0; i < calendarthread.getCount() ; i++){											
					JSONObject tmp = array.getJSONObject(i);											
					int type = Integer.parseInt(tmp.getString("schedule_type"));											
					if(type == 2){						
						setOnView(tmp.getString("schedule_title"), 
								tmp.getString("schedule_des"), tmp.getString("schedule_id"));
					}
					
				}
			}else {
				
			}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void viewClear(){
		rel.removeAllViews();
	}

	public void setOnView(String data_title, String data_des, String schedule_id) {	
		String list_title = data_title;
		String list_des = data_des;
		final String id = schedule_id;
		child = inflater.inflate(R.layout.homework_listitem2, container ,false);
		
		title = (TextView) child.findViewById(R.id.textView_Schedulelist_title);
		des = (TextView) child.findViewById(R.id.textView_Schedulelist_des);
		
		title.setText(list_title);
		des.setText(list_des);
		
		rel.addView(child);
		
		view.invalidate();
	}
}

