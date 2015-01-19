package com.isnc.superiddemo;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.isnc.facesdk.FaceSDKMethod;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;
import com.umeng.update.UmengUpdateAgent;

public class Aty_Login extends Activity {
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_login);
		context = this;
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
	}

	// 一般登录
	public void btn_login(View v) {
		startActivity(new Intent(this, Aty_UserCenter.class));
		finish();

	}

	// 人脸登录
	public void btn_superidlogin(View v) {

		FaceSDKMethod.FaceLogin(this);
	}

	// 清除缓存
	public void btn_clear(View v) {
		Cache.clearCached(context);
		delete(new File(SDKConfig.TEMP_PATH));
		Intent intent = new Intent(this, Aty_Welcome.class);
		startActivity(intent);
		finish();
	}

	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	// 接口返回
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		// 授权成功
		case SDKConfig.AUTH_SUCCESS:
			System.out.println(Cache.getCached(context, SDKConfig.KEY_APPINFO));
			System.err.println("dddbb");
			Intent intent = new Intent(this, Aty_UserCenter.class);
			startActivity(intent);
			finish();
			break;
		// 取消授权
		case SDKConfig.AUTH_BACK:

			break;
		// 找不到该用户
		case SDKConfig.USER_NOTFOUND:

			break;
		// 登录成功
		case SDKConfig.LOGINSUCCESS:
			System.out.println(Cache.getCached(context, SDKConfig.KEY_APPINFO));
			Intent i = new Intent(this, Aty_UserCenter.class);
			startActivity(i);
			finish();
			break;
		// 登录失败
		case SDKConfig.LOGINFAIL:
			break;
		// 网络有误
		case SDKConfig.NETWORKFAIL:
			break;
		// 一登SDK版本过低
		case SDKConfig.SDKVERSIONEXPIRED:
			break;
		default:
			break;
		}

	}
}
