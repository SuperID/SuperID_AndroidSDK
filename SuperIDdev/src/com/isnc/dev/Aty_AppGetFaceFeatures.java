package com.isnc.dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

//有扫描界面人脸登录获取数据处理
public class Aty_AppGetFaceFeatures extends Activity {

	@SuppressWarnings("unused")
	private String emotion;
	double maxValue = 0.00;
	List<Map<String, Object>> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_faceemotion);

		setlist();
	}

	private void setlist() {
		ListView lv = (ListView) findViewById(R.id.datalist);
		SimpleAdapter adapter = new SimpleAdapter(this, getIntentData(), R.layout.item_facedata, new String[] { "key",
				"value" }, new int[] { R.id.key, R.id.value });
		lv.setAdapter(adapter);
	}


	// 获取得到的表情数据
	private List<Map<String, Object>> getIntentData() {
		String facedata = getIntent().getExtras().getString("facedata");
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
		return list;
	}

	public void btn_back(View v) {
		finish();
	}
}
