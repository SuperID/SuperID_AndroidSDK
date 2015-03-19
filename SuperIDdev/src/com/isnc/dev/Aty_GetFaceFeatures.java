package com.isnc.dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.isnc.facesdk.aty.Aty_FaceFeatures;
//无扫描界面人脸登录
public class Aty_GetFaceFeatures extends Aty_FaceFeatures {
	
	List<Map<String, Object>> list;
	private RelativeLayout rl;
	private LinearLayout ll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_faceemotion);
		initFaceEmotion();
		rl = (RelativeLayout) findViewById(R.id.facedatabg);
		ll = (LinearLayout) findViewById(R.id.facedataload);
		rl.setVisibility(View.VISIBLE);
		//执行人脸检测扫描
		faceDetecion();
	}
	//刷脸完成，开始请求数据
	@Override
	public void requestFaceData() {
		ll.setVisibility(View.VISIBLE);
		super.requestFaceData();
	}
	//处理获取到得数据
	@Override
	public void faceDataCallback(String arg0) {
		ll.setVisibility(View.GONE);
		rl.setVisibility(View.GONE);
		getIntentData(arg0);
		setlist();
		super.faceDataCallback(arg0);
	}
	//获取数据失败
	@Override
	public void getFaceDataFail() {
		ll.setVisibility(View.GONE);
		Toast.makeText(this, "获取人脸信息失败", Toast.LENGTH_SHORT).show();
		super.getFaceDataFail();
	}
	private void setlist() {
		ListView lv = (ListView) findViewById(R.id.datalist);
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.item_facedata, new String[] { "key", "value" },
				new int[] { R.id.key, R.id.value });
		lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	


	// 获取得到的表情数据
	private void getIntentData(String facedata) {
		try {
			JSONObject obj = new JSONObject(facedata);
			JSONArray attributes = obj.optJSONArray("attributes");
			list = new ArrayList<Map<String, Object>>();
			String key = "",result = "";
			int value = 0;
			if (attributes.length()>0) {
				for (int i = 0; i < attributes.length(); i++) {
					key = attributes.getJSONObject(i).optString("name");
					value = attributes.getJSONObject(i).getInt("result");
					if (key.equals("Eyeglasses")) {
						key = "眼镜";
						if (value==1) {
							result = "有戴";
						}else {
							result = "没戴";
						}
					}else if (key.equals("Male")) {
						key = "性别";
						if (value==1) {
							result = "男";
						}else {
							result = "女";
						}
					}else if (key.equals("Smiling")) {
						key = "微笑";
						if (value==1) {
							result = "是";
						}else {
							result = "否";
						}
					}
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("key", key);
					map.put("value",result);
					list.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	public void btn_back(View v){
		finish();
	}
}
