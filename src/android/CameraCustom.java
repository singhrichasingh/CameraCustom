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
import com.tuya.smart.android.user.api.IValidateCallback;
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
     private String phoneCode = "+91";
      private String phoneNumber = "9393939393";
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
             Log.d(TAG, this+"here");
             Context context=this.cordova.getActivity().getApplicationContext();
             this.application= (Application) context;
         TuyaHomeSdk.init(application,appKey,appSecret);
           TuyaHomeSdk.setOnNeedLoginListener(new INeedLoginListener(){
               @Override
                public void onNeedLogin(Context context) {
                   Log.d(TAG,"whereeee"+ this); 
                  TuyaHomeSdk.getUserInstance().getValidateCode(phoneCode,phoneNumber, mIValidateCallback);
  String message = " :finished activity from native: ";
          callbackContext.success(message);
        //  TuyaHomeSdk.setDebugMode(true);
                }
    });
             return true;
        }
        if (action.equals("search")) {
             Log.d(TAG, "Initializing heloop");
            return true;
        }
        return false;
    }


    private IValidateCallback mIValidateCallback = new IValidateCallback() {
        @Override
        public void onSuccess() {
           // mHandler.sendEmptyMessage(MSG_SEND_VALIDATE_CODE_SUCCESS);
        }

        @Override
        public void onError(String s, String s1) {
          //  getValidateCodeFail(s, s1);
        }
    };

}
