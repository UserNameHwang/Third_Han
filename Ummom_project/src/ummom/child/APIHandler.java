package ummom.child;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ummom.child.schedule.ScheduleDataset;
import ummom.child.youtube.YoutubeDataset;
import ummom.child.youtube.YoutubeSearchData;


import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.util.Log;

/**
 * @class apihandler
 * @desc ���� API �����ϴ� �޼��带 ��Ƴ��.
 *       sendgetrequest, sendpostrequest�� �ð��� �Ǹ� ������� ���༭ �� Ŭ������ ������ �̳�Ŭ���� ����°� ������.
 * @author Lemoness
 *
 */
public class APIHandler {
	
	private String mS_ID;
	
	public APIHandler(String input){
		mS_ID = input;
	}
	public APIHandler(){
		mS_ID="tester";
	}
	
	/**
	 * @def youtube api
	 * @param S_query
	 * @return ArrayList<YoutubeSearchData>
	 */
	
	/*
	 * @ref http://ondestroy.tistory.com/49
	 * @ref Httpget����1
	 */
	public JSONObject getYoutube(String S_query){
		String S_sendquery= 
				/*q : ���� ����
				 * maxResults : ������ ������ ���� �ִ� 50��
				 * pageToken : �������� ��ū ���� �˻�. ���������� ǥ���Ҷ� ����ҵ�
				 */
				"https://www.googleapis.com/youtube/v3/search?part=snippet&q="+ S_query +
							"&maxResults=20" + 
							"&key=" + Developerkey.S_googlekey;
		return sendGETRequest(S_sendquery);
	}
	public JSONObject getYoutube(String S_query, String S_token){
		String S_sendquery= 
				/*q : ���� ����
				 * maxResults : ������ ������ ���� �ִ� 50��
				 * pageToken : �������� ��ū ���� �˻�. ���������� ǥ���Ҷ� ����ҵ�
				 */
				"https://www.googleapis.com/youtube/v3/search?part=snippet&q="+ S_query +
							"&maxResults=20" + 
							"&key=" + Developerkey.S_googlekey + 
							"&pageToken=" + S_token;
		return sendGETRequest(S_sendquery);
	}
	/*
	 * @ref http://ondestroy.tistory.com/49
	 */
	public YoutubeDataset parsingYoutube(JSONObject JO_input){
		Log.d("youtube","query result : " + JO_input.toString());
		YoutubeDataset result = new YoutubeDataset();
		try {
			// �˻� ���� ����
			result.setNextToken(JO_input.getString("nextPageToken"));
			JSONObject JO_pageinfo = JO_input.getJSONObject("pageInfo");
			result.setTotalResult(JO_pageinfo.getInt("totalResults"));
			result.setResultperPage(JO_pageinfo.getInt("resultsPerPage"));
			
			// �˻��� ������ ����
			JSONArray JA_data = JO_input.getJSONArray("items");
			int max = Integer.parseInt(JO_input.getJSONObject("pageInfo").getString("resultsPerPage"));
			for(int i=0 ; i<max ; i++){
				YoutubeSearchData C_temp = new YoutubeSearchData();
				JSONObject innerdata = JA_data.getJSONObject(i);
				C_temp.setVideoId(innerdata.getJSONObject("id").getString("videoId"));
				C_temp.setTitle(innerdata.getJSONObject("snippet").getString("title"));
				C_temp.setPublishedAt(innerdata.getJSONObject("snippet").getString("publishedAt").substring(0,10));
				C_temp.setUrl(innerdata.getJSONObject("snippet").
						getJSONObject("thumbnails").
						getJSONObject("default").
						getString("url"));
				result.addData(C_temp);
			}
		} catch (JSONException e) {}
		return result;
	}
	
	/**
	 * @def gallery api
	 * @param S_query
	 * @return 
	 */	
	public JSONObject getGallery(){
		String S_sendquery = "http://14.63.212.236/index.php/api_c/getAlbum";
		return sendGETRequest(S_sendquery);
	}
	public JSONObject getGallery(String S_query){
		String S_sendquery = "http://14.63.212.236/index.php/album/getAlbumDetail/" +
							 "?thumbnail=" + S_query;
		return sendGETRequest(S_sendquery);
	}
	public ArrayList<String> parsingGallery(JSONObject JO_input){
		ArrayList<String> ALS_result = new ArrayList<String>();
		Log.d("gallery","getJSON : " + JO_input.toString());
		try {
			JSONObject innerdata = JO_input.getJSONObject("album");
			int max = Integer.parseInt(innerdata.getString("img_count"));
			Log.d("gallery","getcount : " + max);
			for(int i=0 ; i<max ; i++){
				ALS_result.add("http://14.63.212.236/" +innerdata.getString("img"+(i+1)));
			}
		} catch (JSONException e) {}
		return ALS_result;
	}
	public JSONObject getAlbum(String S_query){
		String S_sendquery = "http://14.63.212.236/index.php/album/getAlbum/" +
							 "?id=" + S_query;
		return sendGETRequest(S_sendquery);
	}
	public Bundle parsingAlbum(JSONObject JO_input){
		Bundle result = new Bundle();
		try {
			JSONArray JA_innerdata = JO_input.getJSONArray("album_list");
			ArrayList<String> album_urllist = new ArrayList<String>();
			ArrayList<String> album_reglist = new ArrayList<String>();
			ArrayList<String> album_titlelist = new ArrayList<String>();
			result.putInt("count", JO_input.getInt("count"));
			for(int i=0, max=JO_input.getInt("count") ; i<max ; i++){
				JSONObject JO_innerdata = JA_innerdata.getJSONObject(i);
				album_urllist.add("http://14.63.212.236/" + JO_innerdata.getString("album_url"));
				album_reglist.add(JO_innerdata.getString("album_reg"));
				album_titlelist.add(JO_innerdata.getString("title"));
			}
			result.putStringArrayList("album_url", album_urllist);
			result.putStringArrayList("album_reg",album_reglist);
			result.putStringArrayList("title", album_titlelist);
		} catch (JSONException e) {}
		
		return result;
	}
	

	/**
	 * @def album post api
	 * @param S_query
	 * @return boolean �������, 400�� ����
	 */
	
	private boolean parsingPostAlbumResult(JSONObject JO_input){
		try {
			if("400".equals(JO_input.getString("result"))) return true;
		} catch (JSONException e) { return false; }
		return false;
	}
	
	public boolean postAlbum(String S_id, String S_Albumname, File F_image){
		String S_postquery = "http://14.63.212.236/index.php/album/upload_album";
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			entity.addPart("id", new StringBody(S_id, Charset.forName("UTF-8")));
			entity.addPart("title", new StringBody(S_Albumname, Charset.forName("UTF-8")));
			entity.addPart("userfile", new FileBody(F_image));
			return parsingPostAlbumResult(sendPOSTRequest(S_postquery, entity));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * @def Weather api
	 * @param S_query
	 * @return Bundle
	 */
	public JSONObject getWeather_Forecast(LatLng latlng, String S_timeinfo){
		String S_sendquery = "http://apis.skplanetx.com/weather/forecast/6days?" + 
				"appKey=" + Developerkey.S_SKplanetkey + 
				"&version=" + "1" + 
				"&lat=" + "32"+//latlng.latitude + 
				"&lon=" + "126";//latlng.longitude;
		return sendGETRequest(S_sendquery);
	}
	public Bundle parsingWeatherInfo(JSONObject JO_input){

		Bundle result = new Bundle();
		try{
			JSONObject JO_innerdata = JO_input.getJSONObject("result");
			if(!JO_innerdata.getString("message").equals("성공")){
				return null;
			}
			JO_innerdata = JO_input.getJSONObject("weather").getJSONArray("forecast6days").getJSONObject(0);
			JSONObject JO_location = JO_innerdata.getJSONObject("grid");
			JSONObject JO_sky = JO_innerdata.getJSONObject("sky");
			JSONObject JO_tempture = JO_innerdata.getJSONObject("temperature");
			result.putString("location", JO_location.getString("city") + " " + 
										JO_location.getString("county") + " " +  
										JO_location.getString("village"));
			result.putString("time",JO_innerdata.getString("timeRelease"));
			ArrayList<String> skylist = new ArrayList<String>();
			ArrayList<String> tempminlist = new ArrayList<String>();
			ArrayList<String> tempmaxlist = new ArrayList<String>();
			
			for(int i=2 ; i<11 ; i++){
				skylist.add(JO_sky.getString("pmCode"+i+"day"));
				tempminlist.add(JO_tempture.getString("tmin"+i+"day"));
				tempmaxlist.add(JO_tempture.getString("tmax"+i+"day"));
			}
			result.putStringArrayList("sky", skylist);
			result.putStringArrayList("tmin", tempminlist);
			result.putStringArrayList("tmax", tempmaxlist);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * @def schedule api
	 * @param S_query
	 * @return ArrayList<ScheduleDataset>
	 * @waring ���� ������
	 */
	
	public JSONObject getSchedule(String S_query){
		String S_sendquery = "http://14.63.212.236/index.php/api_C/getScheduleToday";
		return sendGETRequest(S_sendquery);
	}
	
	
	public ArrayList<ScheduleDataset> parsingSchduleToday(JSONObject JO_input){
		ArrayList<ScheduleDataset> ALS_result = new ArrayList<ScheduleDataset>();
		try {
			JSONObject JO_innerdata = JO_input.getJSONObject("now");
			ALS_result.add(new ScheduleDataset(JO_innerdata.getString("time"),
												JO_innerdata.getString("title"),
												JO_innerdata.getString("desc")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ALS_result;
	}
	
	public JSONObject sendGETRequest(String S_query){
		JSONObject JO_result = null;
		HttpGet HG_httpget = new HttpGet(S_query);
		HttpClient HC_client = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = HC_client.execute(HG_httpget);
			HttpEntity HE_entity = response.getEntity();
			JO_result = new JSONObject(EntityUtils.toString(HE_entity));
		} catch (ClientProtocolException e) {} 
		catch (IOException e) {}
		catch (JSONException e) {}
		return JO_result;
	}
	
	public JSONObject sendPOSTRequest(String S_query, MultipartEntity entity){
		JSONObject JO_result = null;
		HttpPost HP_httppost = new HttpPost(S_query);
		HttpClient HC_client = new DefaultHttpClient();
		HttpResponse response;
		try {
			HP_httppost.setEntity(entity);
			response = HC_client.execute(HP_httppost);
			HttpEntity respentity = response.getEntity();
			JO_result = new JSONObject(EntityUtils.toString(respentity));
		}
		catch (ClientProtocolException e) {}
		catch (ParseException e) {} 
		catch (JSONException e) {}
		catch (IOException e) {}
		return JO_result;
	}
}
