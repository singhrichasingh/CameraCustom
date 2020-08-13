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
 import android.app.Activity;
import android.widget.Toast;
import org.apache.cordova.*;
import android.util.Log;
public class CameraCustom extends CordovaPlugin {
     Context context=this.cordova.getActivity().getApplicationContext();
        // Context context=this;
     private  final String TAG = "CameraCustom";
     public static CameraCustom instance = null;
       public  CordovaWebView cordovaWebView;
    public CordovaInterface cordovaInterface;
    // public Application application=this.cordova.getActivity().getApplicationContext();
    private String appKey = "dt4jvymqr3kuvtg5ajcw";
    private String appSecret = "jkhtkxr98p8frqmwuffku3ht9qhy53ke";
     public TuyaHomeSdk tuya=TuyaHomeSdk;
     private String applicationId;
    //public static Activity mainActivity=CameraCustom.instance.cordovaInterface.getActivity();
    //     @Override
    // public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    //     super.initialize(cordova, webView);
    //       Log.d(TAG, this+"Initializing MyCordovaPlugin");
    //     TuyaHomeSdk.setDebugMode(true);
    //     //    Application application=cordova.getActivity().getApplicationContext();
    //     //    TuyaHomeSdk.init(application);

    //     instance = this;
    //     cordovaWebView = webView;
    //     cordovaInterface = cordova;
    // }
    // @Override
    //  public void onCreate() {
    //     super.onCreate();
    //     context = this;
        

    //     TuyaHomeSdk.setDebugMode(true);
    //     TuyaHomeSdk.init(this);
    //     TuyaHomeSdk.setOnNeedLoginListener(new INeedLoginListener() {
    //         @Override
    //         public void onNeedLogin(Context context) {
    //             Intent intent = new Intent(context, LoginActivity.class);
    //             if (!(context instanceof Activity)) {
    //                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //             }
    //             startActivity(intent);
    //         }
    //     });
    // }
     @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
         this.applicationId = (String) BuildHelper.getBuildConfigValue(cordova.getActivity(), "APPLICATION_ID");
        this.applicationId = preferences.getString("applicationId", this.applicationId);
        // if(action.equals('initialize')){
        //      Log.d(TAG, "nnnnnnnnnnnn heloop");
        //      return true;
        //  Log.d(TAG,this+"noe");
        // //      return true;
        // // }
          if(action.equals("init")){
             Log.d(TAG, this+"here");
            // Context context=this.cordova.getActivity().getApplicationContext();
             // Application application=tuya;
         TuyaHomeSdk.init(this.applicationId,appKey,appSecret);
           
            return true;
        }
        if (action.equals("search")) {
             Log.d(TAG, "Initializing heloop");
            // Toast.makeText(mainActivity, "req timeout, please try again", Toast.LENGTH_LONG).show();
           // String message = args.getString(0);
           // this.echo(message, callbackContext);
            return true;
        }
        return false;
    }



}
