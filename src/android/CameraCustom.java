package cordova.plugin.cameracustom;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class echoes a string called from JavaScript.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.network.IApiUrlProvider;
import com.tuya.smart.android.network.TuyaSmartNetWork;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.api.INeedLoginListener;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.api.IValidateCallback;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
 import android.app.Activity;
import android.widget.Toast;
import org.apache.cordova.*;
import android.util.Log;
public class CameraCustom extends CordovaPlugin {
     private  final String TAG = "CameraCustom";
     public static CameraCustom instance = null;
       public  CordovaWebView cordovaWebView;
     public Activity activity;
    public CordovaInterface cordovaInterface;
   public Application application;
    private String appKey = "dt4jvymqr3kuvtg5ajcw";
    private String appSecret = "jkhtkxr98p8frqmwuffku3ht9qhy53ke";
    public String phoneCode;
    public String phoneNumber;
    public String password;
    public String otpCode;

      public static final int MSG_SEND_VALIDATE_CODE_SUCCESS = 12;
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        instance = this;
        cordovaWebView = webView;
        cordovaInterface = cordova;
    }
     @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
         if(action.equals("init")){
             Context context=this.cordova.getActivity().getApplicationContext();
             this.application= (Application) context;
         TuyaHomeSdk.init(application,appKey,appSecret);
           TuyaHomeSdk.setOnNeedLoginListener(new INeedLoginListener(){
               @Override
                public void onNeedLogin(Context context) {
                }
                      });
             return true;
        }
       else if(action.equals("getOtp")) {
         //Log.d(TAG,args);
            phoneCode = args.getJSONObject(0).getString("code");
             phoneNumber = args.getJSONObject(0).getString("number");
             this.getOtp(phoneCode,phoneNumber,callbackContext);
              return true;
        }
        else if(action.equals("reg")) {
          // Log.d(TAG,args);
            phoneCode = args.getJSONObject(0).getString("code");
             phoneNumber = args.getJSONObject(0).getString("number");
             password = args.getJSONObject(0).getString("password");
             otpCode = args.getJSONObject(0).getString("otp");
             this.register(phoneCode,phoneNumber,password,otpCode,callbackContext);
              return true;
        }
        else if(action.equals("login")) {
          // Log.d(TAG,args);
            phoneCode = args.getJSONObject(0).getString("code");
             phoneNumber = args.getJSONObject(0).getString("number");
             password = args.getJSONObject(0).getString("password");
             this.login(phoneCode,phoneNumber,password,callbackContext);
              return true;
        }
        if (action.equals("search")) {
             Log.d(TAG, "Initializing heloop");
            return true;
        }
        return false;
    }
    private void getOtp(String phoneCode,String phoneNumber,CallbackContext callbackContext){
       String message = " :finished activity from otp ";
        callbackContext.success(message);
       //callbackContext.error(message);
       TuyaHomeSdk.getUserInstance().getValidateCode(phoneCode,phoneNumber, mIValidateCallback);
     }
    private IValidateCallback mIValidateCallback = new IValidateCallback() {
        @Override
        public void onSuccess() {
          //Toast.makeText (mContext, "Success in obtaining verification code", Toast.LENGTH_SHORT) .show ();
          // Log.d(TAG, "Initializing heloop");
           // mHandler.sendEmptyMessage(MSG_SEND_VALIDATE_CODE_SUCCESS);
        }
     @Override
        public void onError(String s, String s1) {
          //Log.d(TAG, "error");
          //  getValidateCodeFail(s, s1);
        }
    };
 
  private void register(String phoneCode,String phoneNumber,String password,String otpCode,CallbackContext callbackContext){
  String message = " :finished activity from register ";
        callbackContext.success(message);
TuyaHomeSdk.getUserInstance().registerAccountWithPhone(phoneCode, phoneNumber, password, otpCode, mIRegisterCallback);
}

 private IRegisterCallback mIRegisterCallback = new IRegisterCallback() {
        @Override
        public void onSuccess(User user) {
           // mHandler.sendEmptyMessage(MSG_REGISTER_SUCC);
        }

        @Override
        public void onError(String errorCode, String errorMsg) {
            //Message msg = MessageUtil.getCallFailMessage(MSG_REGISTER_FAIL, errorCode, errorMsg);
           // mHandler.sendMessage(msg);
        }
    };
 

 private void login(String phoneCode, String phoneNumber, String password,CallbackContext callbackContext){
   String message = " :finished activity from login ";
        callbackContext.success(message);
   TuyaHomeSdk.getUserInstance().loginWithPhonePassword(phoneCode, phoneNumber, password,mILoginCallback);
 }
  private ILoginCallback mILoginCallback = new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
           // loginSuccess();
        }

        public void onError(String errorCode, String errorMsg) {
            //Message msg = MessageUtil.getCallFailMessage(MSG_LOGIN_FAIL, errorCode, errorMsg);
           // mHandler.sendMessage(msg);
        }
    };
}
