package com.isnc.dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		JSONObject obj;
		try {
			obj = new JSONObject(facedata);
			list = new ArrayList<Map<String, Object>>();

			double sex = obj.getJSONObject("male").getInt("result");
			Map<String, Object> map = new HashMap<String, Object>();
			if (sex == 1.0) {
				map.put("key", "性别");
				map.put("value", "男");
			} else {
				map.put("key", "性别");
				map.put("value", "女");
			}
			list.add(map);

			double age = obj.getDouble("age");
			map = new HashMap<String, Object>();
			map.put("key", "年龄");
			map.put("value", (int) age);
			list.add(map);

			double smile = obj.getJSONObject("smiling").getDouble("score");
			map = new HashMap<String, Object>();
			map.put("key", "微笑值");
			map.put("value", String.format("%d", (int) (smile * 100)));
			list.add(map);
			int glasses = obj.getJSONObject("eyeglasses").getInt("result");
			map = new HashMap<String, Object>();
			map.put("key", "眼镜");
			if (glasses == 1.0) {
				map.put("value", "有戴");
			} else {
				map.put("value", "没戴");
			}
			list.add(map);

			int sunglasses = obj.getJSONObject("eyeglasses").getInt("result");
			map = new HashMap<String, Object>();
			map.put("key", "太阳眼镜");
			if (sunglasses == 1.0) {
				map.put("value", "有戴");
			} else {
				map.put("value", "没戴");
			}
			list.add(map);

			double mustache = obj.getJSONObject("mustache").getInt("score");
			map = new HashMap<String, Object>();
			map.put("key", "胡须密度");
			map.put("value", mustache);
			list.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void btn_back(View v) {
		finish();
	}
}
