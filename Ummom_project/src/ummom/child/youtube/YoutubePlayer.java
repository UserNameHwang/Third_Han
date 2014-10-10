package ummom.child.youtube;

import java.util.ArrayList;

import ummom.child.APIHandler;
import ummom.child.Developerkey;
import ummom.login.R;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;

/**
 * @class youtubeplayer
 * @desc youtube ��� class, fragment�� �ϴ°� �ƴϴ�!
 * 		 ������ ȣ���Ҷ� ���̾ƿ��� ���ڸ� �־��ָ� �װɷ� ��������
 * @author Lemoness
 *
 */
public class YoutubePlayer extends YouTubeFailureRecoveryActivity{
	
	private FragmentActivity mFA_Act;
	private EditText mET_search;
	private Button mBtn_search;
	private ListView mLV_listview;
	private ListAdapter mAdapter;
	//!!! data�� ����� ������� dataset�� �̷������ �Ѵ�!
	private YoutubeDataset mDataset;
	
	public YoutubePlayer(FragmentActivity FA_activity, 
						final EditText ET_search, 
						final Button Btn_search, 
						final ListView LV_listview){
		
		mFA_Act = FA_activity;
		mET_search = ET_search;
		mBtn_search = Btn_search;
		mLV_listview = LV_listview;
		onConstruct();
	}
	public YoutubePlayer(){

		mFA_Act = null;
		mET_search = null;
		mBtn_search = null;
		mLV_listview = null;
	}
	
	public void onConstruct(){

    	//����Ʈ�� Ŭ��������
		mLV_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ListAdapter adapter = (ListAdapter)arg0.getAdapter();
				Intent intent = new Intent(mFA_Act, YoutubePlayer.class);
				intent.putExtra("videoid", adapter.getVideoid(arg2));
				arg1.getContext().startActivity(intent);
			}
		});
		
		// ��ũ�� �ٴڿ� ����� �� üũ �� ����Ʈ �߰�
		mLV_listview.setOnScrollListener(new ListView.OnScrollListener() {
			
			boolean flag = false;
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && flag){
					Toast.makeText(mFA_Act, "bottom", Toast.LENGTH_SHORT).show();
					flag = false;
					new searchTask().execute(mDataset.getQuery(),mDataset.getNextToken());
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				flag = firstVisibleItem > 0 && firstVisibleItem+visibleItemCount >= totalItemCount-1;
			}
		});
                
        //�˻���ư Ŭ���� �˻����ִ� ��ư
        mBtn_search.setOnClickListener(new View.OnClickListener() {
			//����Ʈ �˻� �� �ش� ����Ʈ ����ִ� ������
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String query = mET_search.getText().toString();
				new searchTask().execute(query, null);
			}
		});

		mAdapter = new ListAdapter(mFA_Act);
		mLV_listview.setAdapter(mAdapter);
        new searchTask().execute("", null);
	}
	
	// ���� ��� �޼ҵ�
	public void onCreate(Bundle arg0){
		super.onCreate(arg0);
		setContentView(R.layout.fragment_youtube);
		YouTubePlayerFragment player = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.tab2_youtube_fragment);
		player.initialize(Developerkey.S_googlekey, this);
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
			boolean arg2) {
		// TODO Auto-generated method stub
		if(!arg2){
			Intent intent = getIntent();
			arg1.loadVideo(intent.getStringExtra("videoid"));
		}
		
	}

	// �÷��̾� ���� �����ִ� �޼ҵ�?
	@Override
	protected Provider getYouTubePlayerProvider() {
		// TODO Auto-generated method stub
		return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.tab2_youtube_fragment);
	}
	
	private class searchTask extends AsyncTask<String, Void, Void>{
		
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			APIHandler handler = new APIHandler();
			if(arg0[1] == null){
				mDataset = handler.parsingYoutube(handler.getYoutube(arg0[0]));
				Log.d("youtube","searchlength = 1");
			}
			else{
				YoutubeDataset searchdata = handler.parsingYoutube(handler.getYoutube(arg0[0],arg0[1]));
				mDataset.setNextToken(searchdata.getNextToken());
				mDataset.addData(searchdata.getData());
				mDataset.setNowpage(mDataset.getNowpage()+1);
				Log.d("youtube","searchlength != 1");
			}
	        return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mAdapter.setData(mDataset.getData());
			mAdapter.notifyDataSetChanged();
		}
		
	}

}

/**
 * @class listadapter
 * @desc ���Z �˻� ����Ʈ �ѷ��ִ� ���
 * @author Lemoness
 *
 */
class ListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<YoutubeSearchData> mData;
	private Youtubelistview mView;

	public ListAdapter(Context x, ArrayList<YoutubeSearchData> data){
		mContext = x;
		mData = data;
	}
	public ListAdapter(Context x){
		mContext = x;
		mData = new ArrayList<YoutubeSearchData>();
	}

	public void setData(ArrayList<YoutubeSearchData> data){
		mData = data;
	}

	public void addData(ArrayList<YoutubeSearchData> data){
		for(int i=0 ; i<data.size() ; i++){
			mData.add(data.get(i));
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mData.isEmpty()) return 0;
		else return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mData.get(arg0);
	}

	public String getVideoid(int arg0){
		return mData.get(arg0).getVideoId();
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1 == null){
			mView = new Youtubelistview(mContext, mData.get(arg0));
		}
		else{
			mView = (Youtubelistview) arg1;
		}
		return mView;
	}

}

/**
 * @class youtubelistview
 * @desc ���Z ����Ʈ ������ Ŭ����, ���̷����� �𸣰ٴµ� xml �Ⱦ��� java�� �������...
 *       ���߿� �ٲٱ�
 * @author Lemoness
 *
 */
class Youtubelistview extends LinearLayout{

	private TextView mTitle;
	private TextView mSubtitle;
	private TextView mDate;

	public Youtubelistview(Context context, YoutubeSearchData data) {
		super(context);
		// TODO Auto-generated constructor stub

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.fragment_youtube_list, this, true);

		mTitle = (TextView) findViewById(R.id.tab2_ytlist_title);
		mTitle.setText(data.getTitle());

		mSubtitle = (TextView) findViewById(R.id.tab2_ytlist_subtitle);
		mSubtitle.setText(data.getVideoId());

		mDate = (TextView) findViewById(R.id.tab2_ytlist_date);
		mDate.setText(data.getPublishedAt());
	}

}
