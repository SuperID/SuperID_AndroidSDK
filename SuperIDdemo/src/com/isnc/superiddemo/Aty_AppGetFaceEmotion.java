package com.isnc.superiddemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

//有扫描界面人脸登录获取数据处理
public class Aty_AppGetFaceEmotion extends Activity {

	private double sex, glasses, beauty, sunglasses, mouth_open_wide, eye_closed, age, beard, smile, happy, angry,
			calm, surprised, confused, sad, disgust;
	@SuppressWarnings("unused")
	private String emotion;
	double maxValue = 0.00;
	private String maxKey = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_faceemotion);

		getIntentData();
		setlist();
	}

	private void setlist() {
		ListView lv = (ListView) findViewById(R.id.datalist);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.item_facedata, new String[] { "key",
				"value" }, new int[] { R.id.key, R.id.value });
		lv.setAdapter(adapter);
	}

	// 表情数据处理示例
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", "性别");
		if (sex == 1.0) {
			map.put("value", "男");
		} else {
			map.put("value", "女");
		}
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("key", "年龄");
		map.put("value", (int) age);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("key", "心情");
		if (maxKey.equals("happy")) {
			map.put("value", "愉快" + "(" + String.format("%.2f", (float) maxValue * 100) + "%)");
		} else if (maxKey.equals("angry")) {
			map.put("value", "愤怒" + "(" + String.format("%.2f", (float) maxValue * 100) + "%)");
		} else if (maxKey.equals("calm")) {
			map.put("value", "平静" + "(" + String.format("%.2f", (float) maxValue * 100) + "%)");
		} else if (maxKey.equals("surprised")) {
			map.put("value", "惊讶" + "(" + String.format("%.2f", (float) maxValue * 100) + "%)");
		} else if (maxKey.equals("confused")) {
			map.put("value", "困惑" + "(" + String.format("%.2f", (float) maxValue * 100) + "%)");
		} else if (maxKey.equals("sad")) {
			map.put("value", "悲伤" + "(" + String.format("%.2f", (float) maxValue * 100) + "%)");
		} else if (maxKey.equals("disgust")) {
			map.put("value", "恐惧" + "(" + String.format("%.2f", (float) maxValue * 100) + "%)");
		}

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("key", "美貌值");
		map.put("value", String.format("%.2f", (float) beauty * 100));
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("key", "眼镜");
		if (glasses == 1.0) {
			map.put("value", "有戴");
		} else {
			map.put("value", "没戴");
		}
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("key", "太阳镜");
		if (sunglasses == 1.0) {
			map.put("value", "有戴");
		} else {
			map.put("value", "没戴");
		}
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("key", "微笑值");
		map.put("value", String.format("%.2f", (float) smile));
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("key", "胡须密度");
		map.put("value", String.format("%.2f", (float) beard * 100) + "%");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("key", "嘴巴张开度");
		map.put("value", String.format("%.2f", (float) mouth_open_wide * 100) + "%");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("key", "眼睛闭合度");
		map.put("value", String.format("%.2f", (float) eye_closed * 100) + "%");
		list.add(map);

		return list;
	}

	// 获取得到的表情数据
	private void getIntentData() {
		String facedata = getIntent().getExtras().getString("facedata");
		System.out.println(facedata);
		try {
			JSONObject obj = new JSONObject(facedata);
			sex = obj.getDouble("sex");
			glasses = obj.getDouble("glasses");
			beauty = obj.getDouble("beauty");
			sunglasses = obj.getDouble("sunglasses");
			mouth_open_wide = obj.getDouble("mouth_open_wide");
			eye_closed = obj.getDouble("eye_closed");
			emotion = obj.getString("emotion");
			age = obj.getDouble("age");
			beard = obj.getDouble("beard");
			smile = obj.getDouble("smile");

			happy = obj.getJSONObject("emotion").optDouble("happy");
			angry = obj.getJSONObject("emotion").optDouble("angry");
			calm = obj.getJSONObject("emotion").optDouble("calm");
			surprised = obj.getJSONObject("emotion").optDouble("surprised");
			confused = obj.getJSONObject("emotion").optDouble("confused");
			sad = obj.getJSONObject("emotion").optDouble("sad");
			disgust = obj.getJSONObject("emotion").optDouble("disgust");

			HashMap<String, Double> emotionMap = new HashMap<String, Double>();
			emotionMap.put("happy", happy);
			emotionMap.put("angry", angry);
			emotionMap.put("calm", calm);
			emotionMap.put("surprised", surprised);
			emotionMap.put("confused", confused);
			emotionMap.put("sad", sad);
			emotionMap.put("disgust", disgust);

			Iterator<?> it = emotionMap.entrySet().iterator();
			for (int i = 0; i < emotionMap.size(); i++) {
				@SuppressWarnings("unchecked")
				Entry<String, Double> entry = (Entry<String, Double>) it.next();
				double value = Double.parseDouble(entry.getValue().toString());
				if (value > maxValue) {
					maxValue = value;
					maxKey = entry.getKey().toString();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void btn_back(View v) {
		finish();
	}
}
