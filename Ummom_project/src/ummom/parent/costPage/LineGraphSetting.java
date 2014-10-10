package ummom.parent.costPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraph;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraphVO;

/**
 * @author Administrator
 *	두번째 탭의 선형 그래프를 정의한 클래스
 */
public class LineGraphSetting {

	public LineGraphVO makeLineGraphDefaultSetting() {

		String[] legendArr = { "1", "2", "3", "4", "5" };
		float[] graph1 = { 500, 100, 300, 200, 100 };
		float[] graph2 = { 000, 100, 200, 100, 200 };
		float[] graph3 = { 200, 500, 300, 400, 000 };

		List<LineGraph> arrGraph = new ArrayList<LineGraph>();
		arrGraph.add(new LineGraph("android", 0xaa66ff33, graph1));
		arrGraph.add(new LineGraph("ios", 0xaa00ffff, graph2));
		arrGraph.add(new LineGraph("tizen", 0xaaff0066, graph3));

		LineGraphVO vo = new LineGraphVO(legendArr, arrGraph);
		return vo;
	}

	/**
	 * make line graph using options
	 * 
	 * @return
	 */
	public LineGraphVO makeLineGraphAllSetting() {
		// 레이아웃 세팅
		// padding
	//	int paddingBottom = LineGraphVO.DEFAULT_PADDING;
		int pad = 70;
		int paddingBottom = pad;
		int paddingTop = pad;
		int paddingLeft = pad+30;
		int paddingRight = pad;

		// graph margin
	//	int marginTop = LineGraphVO.DEFAULT_MARGIN_TOP;
		int marginTop = 10;
		int marginRight = 10;

		// max value
		int maxValue = 500000;

		// Y축 증가치
		int increment = 100000;

		// 그래프에 담길 컨텐츠 정의
		Monthly_Info mthread = new Monthly_Info();
		
		mthread.start();
		
		try {
			mthread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<HashMap<String, String>> value = mthread.getMonthly();
		int lastMonth = Integer.parseInt( mthread.getLastMonth() );
		
		int[] fiveMonth = new int[5];
		fiveMonth[0] = lastMonth;
		for(int i=0; i<4; i++)
		{
			if( fiveMonth[i]-1 == 0 ){
				fiveMonth[i+1] = 12;
				continue;
			}
			fiveMonth[i+1] = fiveMonth[i]-1;
		}
		
		String[] legendArr = { fiveMonth[4]+"", fiveMonth[3]+"",
				fiveMonth[2]+"", fiveMonth[1]+"", fiveMonth[0]+""};
		
		float[] graph1 = { Float.parseFloat(value.get(0).get(fiveMonth[4]+"")), 
				Float.parseFloat(value.get(1).get(fiveMonth[3]+"")),
				Float.parseFloat(value.get(2).get(fiveMonth[2]+"")),
				Float.parseFloat(value.get(3).get(fiveMonth[1]+"")),
				Float.parseFloat(value.get(4).get(fiveMonth[0]+"")) };

		List<LineGraph> arrGraph = new ArrayList<LineGraph>();

		arrGraph.add(new LineGraph("android", 0xaaff0066, graph1));

		LineGraphVO vo = new LineGraphVO(paddingBottom, paddingTop,
				paddingLeft, paddingRight, marginTop, marginRight, maxValue,
				increment, legendArr, arrGraph);

		// 애니메이션 설정
	//	vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION,	GraphAnimation.DEFAULT_DURATION));
		
		// 그래프 범례 표기 설정
	//	vo.setGraphNameBox(new GraphNameBox());
		
		// set draw graph region
		// vo.setDrawRegion(true);

		// use icon
		// arrGraph.add(new Graph(0xaa66ff33, graph1, R.drawable.icon1));
		// arrGraph.add(new Graph(0xaa00ffff, graph2, R.drawable.icon2));
		// arrGraph.add(new Graph(0xaaff0066, graph3, R.drawable.icon3));

		// LineGraphVO vo = new LineGraphVO(
		// paddingBottom, paddingTop, paddingLeft, paddingRight,
		// marginTop, marginRight, maxValue, increment, legendArr, arrGraph,
		// R.drawable.bg);
		return vo;
	}
	
	private class Monthly_Info extends Thread
	{
		private ArrayList<HashMap<String, String>> cost_array;
		private String lastMonth="";
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			
			String getURL = "http://14.63.212.236/index.php/cost/getRecentCost/?id="
					+ "pest";

			HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
			HttpGet get = new HttpGet(getURL);

			HttpResponse responseGet;
			
			try {
				responseGet = client.execute(get);
				HttpEntity resEn = responseGet.getEntity();
				JSONObject object = new JSONObject(EntityUtils.toString(resEn));
				
				JSONArray cost_info = new JSONArray(object.getString("cost"));
							
				cost_array = new ArrayList<HashMap<String,String>>();
				StringCalc calc = new StringCalc();
				
				for (int i=0; i<5; i++) {
					
					JSONArray arrayData = cost_info.getJSONArray(i);
					JSONObject data = arrayData.getJSONObject(0);
					
					if( data.get("expense").equals("No_data"))
						continue;

					lastMonth = data.getString("MONTH(reg_month)");
					String sum = "0원";
					String month = "";
					for( int j=0; j<arrayData.length(); j++)
					{
						JSONObject objectData = arrayData.getJSONObject(j);
						String cost = objectData.getString("expense");
						month = objectData.getString("MONTH(reg_month)");
						
						sum = calc.getStringSum(sum, cost);
						
					}
					// 원과 자릿수 구분을 제거
					sum = sum.substring(0, sum.length() - 1);
					sum = sum.replace(",", "");
					
					HashMap<String, String> value = new HashMap<String, String>();
					value.put(month, sum);
					
					Log.e("msg", month+", "+sum);
					
					cost_array.add(value);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return;
		}
		
		public ArrayList<HashMap<String, String>> getMonthly()
		{
			return cost_array;
		}
		
		public String getLastMonth()
		{
			return lastMonth;
		}
	}
}
