package ummom.parent.common;

import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import ummom.parent.managePage.TeacherInfo_Thread;
import ummom.parent.view.SlidingTabLayout;
import ummom.parent.costPage.CircleGraphSetting;
import ummom.parent.costPage.LineGraphSetting;

import ummom.login.LoginModel;
import ummom.login.R;
import com.handstudio.android.hzgrapherlib.graphview.CircleGraphView;
import com.handstudio.android.hzgrapherlib.graphview.LineGraphView;
import com.handstudio.android.hzgrapherlib.vo.circlegraph.CircleGraphVO;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraphVO;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import android.widget.TextView;

public class SlidingTabsBasicFragment extends Fragment {

	private SlidingTabLayout mSlidingTabLayout;

	private ViewPager mViewPager;
	private View v, mapView, costView, crudView;

	CalendarView calendar;
	
	private int check=0;

	private String loginID;
	private String teacherID = "";
	
	public SlidingTabsBasicFragment(LoginModel model) {
		// TODO Auto-generated constructor stub
		this.loginID = model.id;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_parent, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager_parent);

		mViewPager.setAdapter(new SamplePagerAdapter());

		mSlidingTabLayout = (SlidingTabLayout) view
				.findViewById(R.id.sliding_tabs_parent);
		mSlidingTabLayout.setViewPager(mViewPager);

		ActionBar bar = getActivity().getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6495ed")));
		bar.setDisplayUseLogoEnabled(false);
	}

	// PageAdapter
	class SamplePagerAdapter extends PagerAdapter {

		// Tab의 갯수를 지정한다.
		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return o == view;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		// TabTitle 관리.
		@Override
		public CharSequence getPageTitle(int position) {

			return "";
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			if (position == 0) {
				
				if(check == 0 ){
					mapView = getActivity().getLayoutInflater().inflate(
							R.layout.parent_firsttab, container, false);
					container.addView(mapView);
					
					check++;					
					return mapView;
				}else{
					container.addView(mapView);					
					return mapView;
				}
			}

			else if (position == 1) {

				costView = getActivity().getLayoutInflater().inflate(
						R.layout.cost_item, container, false);
				container.addView(costView);
				
				LinearLayout lineGraph, circleGraph;

				lineGraph = (LinearLayout)costView.findViewById(R.id.GraphView1);
				circleGraph = (LinearLayout)costView.findViewById(R.id.GraphView2);
				
				LineGraphSetting LineSetting = new LineGraphSetting();
				LineGraphVO LineVo = LineSetting.makeLineGraphAllSetting();

				lineGraph.addView(new LineGraphView(getActivity(), LineVo));
	
				CircleGraphSetting CircleSetting = new CircleGraphSetting();
				CircleGraphVO CircleVo = CircleSetting.makeCircleGraphAllSetting();
				
				circleGraph.addView(new CircleGraphView(getActivity(),CircleVo));

				return costView;
			}

			else if (position == 2) {

				crudView = getActivity().getLayoutInflater().inflate(
						R.layout.crud_item, container, false);
				container.addView(crudView);

				TeacherID tIDthread = new TeacherID(loginID);

				tIDthread.start();
				try {
					tIDthread.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				teacherID = tIDthread.gettID();
				
				if( !teacherID.equals("") )
				{	
					TeacherInfo_Thread teacher_info = new TeacherInfo_Thread(teacherID);
					
					teacher_info.start();
					
					try {
						teacher_info.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Bundle bundle = teacher_info.getBundle();
					
					@SuppressWarnings("unchecked")
					HashMap<String, String> map = (HashMap<String, String>) bundle.getSerializable("HashMap");
					
					ViewFlipper title, content;
					
					TextView name = (TextView)crudView.findViewById(R.id.teacher_namecon);
					TextView grade = (TextView)crudView.findViewById(R.id.teacher_classcon);
					TextView contract = (TextView)crudView.findViewById(R.id.teacher_contactcon);
					ImageView image = (ImageView)crudView.findViewById(R.id.teacher_image);
				//	TextView comment = (TextView)findViewById(R.id.teacher_today);
					
					name.setText(map.get("name"));
					grade.setText(map.get("grade"));
					contract.setText(map.get("phone"));
					
				//	Log.d("comments", ""+map.get("comments"));
					
					Bitmap profile = bundle.getParcelable("Bitmap");
					image.setImageBitmap(profile);
					
				//	comment.setText(map.get("comments"));
					title = (ViewFlipper)crudView.findViewById(R.id.titleFlipper);
					content = (ViewFlipper)crudView.findViewById(R.id.contentFlipper);
					
					if (title.getDisplayedChild() == 0) {
						title.showNext();
						content.showNext();
					}
				}
				return crudView;

			}
			else if (position == 3)
			{
				v = getActivity().getLayoutInflater().inflate(
						R.layout.info_page, container, false);
				container.addView(v);

				return v;
			}
			else {
				v = getActivity().getLayoutInflater().inflate(
						R.layout.pager_item, container, false);
				container.addView(v);
				TextView title = (TextView) v.findViewById(R.id.item_title);
				
				title.setText(String.valueOf(position + 1));
				return v;
			}
			// return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);

		}

		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			super.finishUpdate(container);
			ActionBar bar = getActivity().getActionBar();
			bar.setDisplayUseLogoEnabled(false);

			switch(mSlidingTabLayout.getCursoredpage()){
			case 0 :
				bar.setTitle("일정 확인");
				break;
			case 1 :
				bar.setTitle("지출 관리");
				break;
			case 2 :
				bar.setTitle("선생님 정보");
				break;
			case 3 :
				bar.setTitle("생활 정보");
				break;
			default : 
				bar.setTitle("error");
				break;
			}
		}
	}
	
	public class TeacherID extends Thread
	{
		private String t_id, p_id;
		
		public TeacherID(String p_id) {
			// TODO Auto-generated constructor stub
			this.p_id = p_id;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();

			String getURL = "http://14.63.212.236/index.php/teacher/getTeacherId/?id="
					+ p_id;
			
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 3000);
			HttpGet get = new HttpGet(getURL);

			HttpResponse responseGet;
			
			try {
				responseGet = client.execute(get);
				
				HttpEntity resEn = responseGet.getEntity();
				
				JSONObject object = new JSONObject(EntityUtils.toString(resEn));
				String des = object.getString("description");
				
				Log.d("msg", object+" @@");
				
				if( des.equals("no_data") )
					t_id = "";
				else
					t_id = object.getString("teacher_id");

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		public String gettID()
		{
			return t_id;
		}
	}
}
