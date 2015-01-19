package com.isnc.superiddemo;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.isnc.facesdk.FaceSDKMethod;
import com.isnc.facesdk.FaceSDKMethod.IntFailCallback;
import com.isnc.facesdk.FaceSDKMethod.IntSuccessCallback;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;
import com.isnc.facesdk.common.Utils;
import com.isnc.facesdk.net.AsyncBitmapLoader;
import com.isnc.facesdk.net.AsyncBitmapLoader.ImageCallBack;
import com.isnc.superiddemo.R;

public class Aty_UserCenter extends Activity {

	private Context context;
	private Button btn_spbundle;
	private ImageView icon_sp, avatarimg;
	private TextView tv_phonenum, tv_name;
	private String userinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_usercenter);
		context = this;
		btn_spbundle = (Button) findViewById(R.id.btn_spbundle);
		icon_sp = (ImageView) findViewById(R.id.icon_sp);
		avatarimg = (ImageView) findViewById(R.id.avatarimg);
		tv_phonenum = (TextView) findViewById(R.id.tv_phonenum);
		tv_name = (TextView) findViewById(R.id.tv_name);

		// 判断当前用户是否授权，由于本demo没有自己的用户体系及uid，所以用以记录在SharedPreferences里的SDKConfig.KEY_ACCESSTOKEN判断。
		// 开发者检测当前用户是否授权时，应调用FaceSDKMethod.UidAuthorized方法，示例如下：
//				FaceSDKMethod.UidAuthorized(context, "uid123456", new FaceSDKMethod.IntSuccessCallback() {
		//
//					@Override
//					public void onSuccess(int arg0) {
//						switch (arg0) {
//						case SDKConfig.ISAUTHORIZED:
//							Toast.makeText(context, "已授权", Toast.LENGTH_SHORT).show();
//							break;
//						case SDKConfig.NOAUTHORIZED:
//							Toast.makeText(context, "未授权", Toast.LENGTH_SHORT).show();
//							break;
//						default:
//							break;
//						}
		//
//					}
//				}, new FaceSDKMethod.IntFailCallback() {
		//
//					@Override
//					public void onFail(int arg0) {
//						switch (arg0) {
//						case SDKConfig.SDKVERSION_EXPIRED:
//							Toast.makeText(context, "当前一登sdk版本号过低，无法继续执行操作", Toast.LENGTH_SHORT).show();
//							break;
//						case SDKConfig.APPTOKEN_EXPIRED:
//							Toast.makeText(context, "apptoken过期，您已经太久没操作，请重新登录", Toast.LENGTH_SHORT).show();
//							break;
//						default:
//							break;
//						}
		//
//					}
//				});
		if (!Cache.getCached(context, SDKConfig.KEY_ACCESSTOKEN).equals("")) {
			btn_spbundle.setText("解除绑定");
			btn_spbundle.setTextColor(getResources().getColor(R.color.s_color_font_contant));
			icon_sp.setImageDrawable(getResources().getDrawable(R.drawable.superid_demo_binding_superid_ico_normal));
		} else {
			btn_spbundle.setText("点击绑定");
			btn_spbundle.setTextColor(getResources().getColor(R.color.s_demo_color_background_red));
			icon_sp.setImageDrawable(getResources().getDrawable(R.drawable.superid_demo_binding_superid_ico_disable));
		}
		// 开发者应用自己用户的信息
		userinfo = FaceSDKMethod.SendinfoToSuperID(this, "name", "Someone", "email", "someone@superid.me", "avatar",
				"http://spapi1.qiniudn.com/res/avatar.jpg");

		// 使用人脸登录后获取的一登用户信息
		String appinfo = Cache.getCached(context, SDKConfig.KEY_APPINFO);
		if (!appinfo.equals("")) {
			try {
				JSONObject obj = new JSONObject(appinfo);
				tv_phonenum.setText(obj.getString(SDKConfig.KEY_PHONENUM));
				tv_name.setText(Utils.judgeChina(obj.getString(SDKConfig.KEY_NAME), 10));

				AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
				asyncBitmapLoader.loadBitmap(this, Cache.getCached(context, SDKConfig.KEY_PHONENUM), avatarimg,
						obj.getString(SDKConfig.KEY_AVATAR), new ImageCallBack() {

							public void imageLoad(ImageView imageView, Bitmap bitmap) {
								imageView.setImageBitmap(Utils.getRoundedCornerBitmap(bitmap, 480));
							}
						});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// 开发者应用的用户信息
			tv_name.setText("Someone");
			AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
			asyncBitmapLoader.loadBitmap(this, "superidavatar", avatarimg, "http://spapi1.qiniudn.com/res/avatar.jpg",
					new ImageCallBack() {

						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							imageView.setImageBitmap(Utils.getRoundedCornerBitmap(bitmap, 480));
						}
					});
		}


	}

	
	public void btn_unbundle(View v) {
		if (!Cache.getCached(context, SDKConfig.KEY_ACCESSTOKEN).equals("")) {
			// 解除绑定
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("是否解除与一登账号的绑定？").setCancelable(false)
					.setPositiveButton("是", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							// 解绑
							FaceSDKMethod.Unbundle(context, new IntSuccessCallback() {

								@Override
								public void onSuccess(int arg0) {
									Cache.deleCached(context, SDKConfig.KEY_ACCESSTOKEN);
									Cache.deleCached(context, "demo_phone");
									btn_spbundle.setText("点击绑定");
									btn_spbundle.setTextColor(getResources().getColor(
											R.color.s_demo_color_background_red));
									icon_sp.setImageDrawable(getResources().getDrawable(
											R.drawable.superid_demo_binding_superid_ico_disable));
								}
							}, new IntFailCallback() {

								@Override
								public void onFail(int error) {
									Toast.makeText(context, "解绑失败", Toast.LENGTH_SHORT).show();
								}
							});

						}
					}).setNegativeButton("否", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					}).show();

		} else {
			//绑定一登账号 FaceBundle(Activity activity, String uid, String userinfo)
			FaceSDKMethod.FaceBundle(Aty_UserCenter.this, String.valueOf(System.currentTimeMillis()), userinfo);
		}

	}

	// 有刷脸界面获取表情接口返回请查看onActivityResult，数据处理请查看Aty_AppGetFaceEmotion.class
	public void btn_facedata(View v) {
		FaceSDKMethod.GetFaceEmotion(this);
	}

	// 无刷脸界面获取表情，接口返回及数据处理请查看Aty_GetFaceEmotion.class
	public void btn_sciencefacedata(View v) {

		startActivity(new Intent(this, Aty_GetFaceEmotion.class));
	}

	// 退出登录
	public void btn_logout(View v) {
		FaceSDKMethod.FaceLogout(this);
		Intent intent = new Intent(this, Aty_Login.class);
		startActivity(intent);
		finish();
	}

	// 接口返回
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		// 授权成功
		case SDKConfig.AUTH_SUCCESS:
			File file = new File(SDKConfig.TEMP_PATH + "/" + Cache.getCached(context, SDKConfig.KEY_PHONENUM) + ".JPEG");
			if (file != null && file.exists()) {
				Bitmap bm = BitmapFactory.decodeFile(SDKConfig.TEMP_PATH + "/"
						+ Cache.getCached(context, SDKConfig.KEY_PHONENUM) + ".JPEG");
				avatarimg.setImageBitmap(Utils.getRoundedCornerBitmap(bm, 480));
			}
			// 授权成功后 获取一登用户信息
			String appinfo = Cache.getCached(context, SDKConfig.KEY_APPINFO);
			if (!appinfo.equals("")) {
				try {
					JSONObject obj = new JSONObject(appinfo);
					tv_phonenum.setText(obj.getString(SDKConfig.KEY_PHONENUM));
					tv_name.setText(Utils.judgeChina(obj.getString(SDKConfig.KEY_NAME), 10));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			btn_spbundle.setText("解除绑定");
			btn_spbundle.setTextColor(getResources().getColor(R.color.s_color_font_contant));
			icon_sp.setImageDrawable(getResources().getDrawable(R.drawable.superid_demo_binding_superid_ico_normal));
			break;

		// 网络连接失败
		case SDKConfig.NETWORKFAIL:
			break;
		// 有界面刷脸获取人脸表情成功
		case SDKConfig.GETEMOTIONRESULT:
			Intent intent = new Intent(this, Aty_AppGetFaceEmotion.class);
			intent.putExtra("facedata", data.getStringExtra(SDKConfig.FACEDATA));
			startActivity(intent);
			break;
		// 有界面刷脸获取人脸表情失败
		case SDKConfig.GETEMOTION_FAIL:
			Toast.makeText(context, "获取人脸信息失败", Toast.LENGTH_SHORT).show();
			break;
		// 取消授权
		case SDKConfig.AUTH_BACK:
			break;
		// 一登SDK版本过低
		case SDKConfig.SDKVERSIONEXPIRED:
			break;
		default:
			break;
		}

	}

}
