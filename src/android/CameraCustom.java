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
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.Hashtable;
import android.util.Base64;
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
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.android.common.utils.WiFiUtil;
import com.tuya.smart.home.sdk.builder.TuyaGwActivatorBuilder;
import com.tuya.smart.home.sdk.builder.TuyaCameraActivatorBuilder;
import com.tuya.smart.sdk.api.ITuyaSmartCameraActivatorListener;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.ITuyaCameraDevActivator;
import com.tuya.smart.camera.ipccamerasdk.p2p.ICameraP2P;
import com.tuya.smart.camera.middleware.p2p.TuyaSmartCameraP2PFactory;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;
import com.tuyasmart.camera.devicecontrol.model.PTZDirection;
import com.tuya.smart.camera.ipccamerasdk.bean.ConfigCameraBean;
import com.tuya.smart.android.network.http.BusinessResponse;
import com.tuya.smart.camera.middleware.p2p.ITuyaSmartCameraP2P;
import com.tuya.smart.camera.middleware.p2p.TuyaSmartCameraP2P;
import com.tuya.smart.camera.middleware.p2p.ICameraConfig;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import java.util.*;
import android.app.Activity;
import android.widget.Toast;
import org.apache.cordova.*;
import android.util.Log;
import java.util.List;
public class CameraCustom extends CordovaPlugin {
     private  final String TAG = "CameraCustom";
     public static CameraCustom instance = null;
       public  CordovaWebView cordovaWebView;
     public Activity activity;
    public CordovaInterface cordovaInterface;
   public Application application;
    private String appKey = "dt4jvymqr3kuvtg5ajcw";
    private String appSecret = "jkhtkxr98p8frqmwuffku3ht9qhy53ke";
     private String wifipass = "Tuya.140616";
     private String ssid = "";
    private String token1 = "";
    public String phoneCode;
    public String phoneNumber;
    public String password;
    public String otpCode;
    public long homeId;
    public Bitmap bitmap;
    private TuyaCameraView mVideoView;
    

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
        else if(action.equals("home")){
          this.queryHomeList( callbackContext);
        }
        else if(action.equals("initDevice")){
          getApplication().getResources().getIdentifier("viewname","id",getApplication().getPackageName());
          this.initDevice( callbackContext);
        }
         else if(action.equals("createhome")){
          String name = args.getJSONObject(0).getString("homeName");
          double lon = 0;
          double lat = 0;
          String geoName = "";
          String room = args.getJSONObject(0).getString("roomList");
          
          this.createHome( name, lon, lat, geoName, room, callbackContext);
        }
         else if(action.equals("qrGen")){
          this.generateQr( callbackContext);
        }
        else if(action.equals("createView")){
          //String view = args.getJSONObject("view");
          this.createView(callbackContext);
        }
        if (action.equals("search")) {
             Log.d(TAG, "Initializing heloop");
            return true;
        }
        return false;
    }
    private void getOtp(String phoneCode,String phoneNumber,CallbackContext callbackContext){
       TuyaHomeSdk.getUserInstance().getValidateCode(phoneCode,phoneNumber, new IValidateCallback() {
        @Override
        public void onSuccess() {
        activity = cordovaInterface.getActivity();
            Toast.makeText(activity, "1 createSuc", Toast.LENGTH_LONG).show();
          Log.d(TAG, "success");
          //Toast.makeText (mContext, "Success in obtaining verification code", Toast.LENGTH_SHORT) .show ();
          // Log.d(TAG, "Initializing heloop");
           // mHandler.sendEmptyMessage(MSG_SEND_VALIDATE_CODE_SUCCESS);
        }
     @Override
        public void onError(String s, String s1) {
          Log.d(TAG, "error"+s);
             activity = cordovaInterface.getActivity();
            Toast.makeText(activity, "1 errCreate"+s1, Toast.LENGTH_LONG).show();
          //  getValidateCodeFail(s, s1);
        }
    });
    
     }
 
  private void register(String phoneCode,String phoneNumber,String password,String otpCode,CallbackContext callbackContext){
TuyaHomeSdk.getUserInstance().registerAccountWithPhone(phoneCode, phoneNumber, password, otpCode, new IRegisterCallback() {
        @Override
        public void onSuccess(User user) {
           Log.d(TAG, "2suc "+user);
         activity = cordovaInterface.getActivity();
            Toast.makeText(activity, "reg createSuc"+user, Toast.LENGTH_LONG).show();
        }
        @Override
        public void onError(String errorCode, String errorMsg) {
           activity = cordovaInterface.getActivity();
            Toast.makeText(activity, "reg errCreate"+errorMsg, Toast.LENGTH_LONG).show();
                Log.d(TAG, "2 err  "+errorMsg);
        }
    });
}
 

 private void login(String phoneCode, String phoneNumber, String password,CallbackContext callbackContext){
   TuyaHomeSdk.getUserInstance().loginWithPhonePassword(phoneCode, phoneNumber, password,new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
          Log.d(TAG, "3suc "+user);
         activity = cordovaInterface.getActivity();
            Toast.makeText(activity, "log createSuc"+user, Toast.LENGTH_LONG).show();
        }
        @Override
        public void onError(String errorCode, String errorMsg) {
            activity = cordovaInterface.getActivity();
            Toast.makeText(activity, "log errCreate"+errorMsg, Toast.LENGTH_LONG).show();
            Log.d(TAG, "3 err  "+errorMsg);
        }
    });
   
 }
    void createHome(String name, double lon, double lat, String geoName, String rooms, CallbackContext callbackContext){
    List<String> roomlist = new ArrayList<String>();
          roomlist.add(rooms);
    TuyaHomeSdk.getHomeManagerInstance().createHome(name, lon, lat, geoName, roomlist, new ITuyaHomeResultCallback() {
      @Override
      public void onError(String errorCode, String errorMsg) {
        Log.d(TAG,"home"+errorMsg);
          activity = cordovaInterface.getActivity();
            Toast.makeText(activity, "home errCreate"+errorMsg, Toast.LENGTH_LONG).show();
      }
      @Override
      public void onSuccess(HomeBean bean) {
           Log.d(TAG,"home"+bean);
          activity = cordovaInterface.getActivity();
            Toast.makeText(activity, "home createSuc"+bean, Toast.LENGTH_LONG).show();
      }
  });
  }


     public void queryHomeList(CallbackContext callbackContext){
      TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
        
        @Override
        public void onSuccess(List<HomeBean> homeBeans) {
            Log.d(TAG, "dev  "+homeBeans );
            homeId=homeBeans.get(0).getHomeId();
            activity = cordovaInterface.getActivity();
            // Toast.makeText(activity, "home.."+homeBeans, Toast.LENGTH_LONG).show();
            TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeBeans.get(0).getHomeId(), new ITuyaActivatorGetToken() {
              @Override
              public void onSuccess(final String token) {
                activity = cordovaInterface.getActivity();
                 token1=token;
            Toast.makeText(activity, "homein"+token, Toast.LENGTH_LONG).show();
              
                  startConfigGW(token);
              }
             @Override
              public void onFailure(String errorCode, String errorMsg) {
                activity = cordovaInterface.getActivity();
                Toast.makeText(activity, "erHHOme"+errorMsg, Toast.LENGTH_LONG).show();
              }
          });
            
        }
        @Override
        public void onError(String errorCode, String error) {
            // do something
            // activity = cordovaInterface.getActivity();
            // Toast.makeText(activity, "errHomeeeeeeee", Toast.LENGTH_LONG).show();
        }
    });
    }

      private void startConfigGW(String token) {
      Context context=this.cordova.getActivity().getApplicationContext();
      TuyaHomeSdk.getActivatorInstance().newGwActivator(new TuyaGwActivatorBuilder()
              .setToken(token)
              .setContext(context)
              .setListener(new ITuyaSmartActivatorListener() {
                  @Override
                  public void onError(String errorCode, String errorMsg) {
                    activity = cordovaInterface.getActivity();
                    Toast.makeText(activity, "errconfig"+errorMsg, Toast.LENGTH_LONG).show();
                  }

                  @Override
                  public void onActiveSuccess(DeviceBean devResp) {
                      L.d(TAG, " devResp: " + devResp.getDevId());
                      activity = cordovaInterface.getActivity();
            Toast.makeText(activity, "config.."+devResp, Toast.LENGTH_LONG).show();
                  }

                  @Override
                  public void onStep(String step, Object data) {
                    activity = cordovaInterface.getActivity();
                    Toast.makeText(activity, "con_data"+data, Toast.LENGTH_LONG).show();
                  }
              }).setToken(token)).start();
  }

 private void generateQr(CallbackContext callbackContext){
   Context context=this.cordova.getActivity().getApplicationContext();
   ssid = WiFiUtil.getCurrentSSID(context);
  TuyaCameraActivatorBuilder builder = new TuyaCameraActivatorBuilder()
         .setContext(context)
         .setSsid(ssid)
         .setPassword("12345678")
         .setToken(token1)
         .setTimeOut(3000)
         .setListener(new ITuyaSmartCameraActivatorListener() {
             @Override
             public void onQRCodeSuccess(String qrcodeUrl) {
               Log.d(TAG,"QR"+qrcodeUrl);
                    activity = cordovaInterface.getActivity();
                    Toast.makeText(activity, "qr"+qrcodeUrl, Toast.LENGTH_LONG).show();
                  callbackContext.success(qrcodeUrl); 
                  }

             @Override
             public void onError(String errorCode, String errorMsg) {
                Log.d(TAG,"QR"+errorMsg);
                    activity = cordovaInterface.getActivity();
                    Toast.makeText(activity, "qr"+errorMsg, Toast.LENGTH_LONG).show();
             }

             @Override
             public void onActiveSuccess(DeviceBean devResp) {
                Log.d(TAG,"QR"+devResp);
                   activity = cordovaInterface.getActivity();
                    Toast.makeText(activity, "qr"+devResp, Toast.LENGTH_LONG).show();   
             }
         });
         ITuyaCameraDevActivator mTuyaActivator = TuyaHomeSdk.getActivatorInstance().newCameraDevActivator(builder);
         mTuyaActivator.createQRCode();
         mTuyaActivator.start();
 }
 // }
//init device
private void initDevice(CallbackContext callbackContext){
  TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
    @Override
    public void onSuccess(HomeBean bean) {
      // do something
      Log.d(TAG, " home-detail: "+bean );
      activity = cordovaInterface.getActivity();
        Toast.makeText(activity, "HomeDetailSUC"+bean, Toast.LENGTH_LONG).show();
        List<DeviceBean> deviceList = bean.getDeviceList();
        Log.d(TAG, " device-detail: "+deviceList );
        for (DeviceBean devbean : deviceList) {
          // bean.getDevId();
          gotoDeviceCommonActivity(devbean);
      }
        
    }
    @Override
    public void onError(String errorCode, String errorMsg) {
      // do something
      Log.d(TAG, "home-detailERR: "+errorMsg );
      activity = cordovaInterface.getActivity();
        Toast.makeText(activity, "HomeDetailERR"+errorMsg, Toast.LENGTH_LONG).show();
    }
});
} 

private void gotoDeviceCommonActivity(DeviceBean devBean) {
  if ("sp".equals(devBean.getProductBean().getCategory())) {
      //is smart camera
      Map<String, Object> map = devBean.getSkills();
      int p2pType = -1;
      if (map == null || map.size() == 0) {
          p2pType = -1;
      } else {
          p2pType = (Integer) (map.get("p2pType"));
      }
      Log.d(TAG,"smartcamera"+p2pType+"----"+devBean);
      ICameraP2P mCameraP2P = TuyaSmartCameraP2PFactory.generateTuyaSmartCamera(p2pType);
      generateTuyaSmartCamera(p2pType, devBean.getDevId());
     // Log.d(TAG,"smartp2p"+mCameraP2P);
      // generateTuyaSmartCamera(p2pType);
  } else{
    Log.d(TAG,"simple Device"+devBean);
  }
}


private void createView(CallbackContext callbackContext){
  TuyaCameraView.setCameraViewCallback(new TuyaCameraView.CreateVideoViewCallback() {
    @Override
    public void onCreated(Object o) {
      Log.d(TAG,"camera live "+o);
    }

    @Override
    public void videoViewClick() {

    }

    @Override
    public void startCameraMove(PTZDirection ptzDirection) {
    }

    @Override
    public void onActionUP() {

    }
});
}

///////newwwwwwfgcgvhjkl'nbvfxdghkl
public void generateTuyaSmartCamera(int sdkprovider, String devId){
  ICameraP2P mCameraP2P = TuyaSmartCameraP2PFactory.generateTuyaSmartCamera(sdkprovider);
  Log.d(TAG, devId+"smartp2p"+mCameraP2P);
   //connectp2p( mCameraP2P);
  requestCameraInfo( mCameraP2P, devId);

  // TuyaCameraView mVideoView = findViewById(fakeR.id.camera_video_view);
  // Context context=this.cordova.getActivity().getApplicationContext();
  // fakeR = new FakeR(context);
  // TuyaCameraView mVideoView = ((VideoView) getActionBar().getCustomView().findViewById(fakeR.getId("id", "camera_video_view")));
//     Log.d(TAG,"videoElement"+mVideoView);
//     mVideoView.setCameraViewCallback(new TuyaCameraView.CreateVideoViewCallback() {
//     @Override
//     public void onCreated(Object o) {
//         //4. when render view construction is completed, bind render view for ICameraP2P
//         mCameraP2P.generateCameraView(mVideoView.createdView());
//         Log.d(TAG,"videoGenerated"+o);
//     }

//     @Override
//     public void videoViewClick() {

//     }

//     // @Override
//     // public void startCameraMove(PTZDirection ptzDirection) {

//     // }

//     @Override
//     public void onActionUP() {

//     }
// });
}

public void requestCameraInfo(ICameraP2P mCameraP2P, String devId){
TuyaSmartCameraP2P mSmartCameraP2P = new TuyaSmartCameraP2P();
mSmartCameraP2P.requestCameraInfo(devId, new ICameraConfig() {
  @Override
  public void onSuccess(BusinessResponse var1, ConfigCameraBean var2, String var3) {
    Log.d(TAG,"cameraInfo"+var1+"---"+var2+"---"+var3);
    createDevice( mCameraP2P, var2);
  }

  @Override
  public void onFailure(BusinessResponse var1, ConfigCameraBean var2, String var3) {
    Log.d(TAG,"cameraInfoERR"+var1+"---"+var2+"---"+var3);
  }
});
}

public void createDevice(ICameraP2P mCameraP2P, ConfigCameraBean bean){
mCameraP2P.createDevice(new OperationDelegateCallBack() {
  @Override
  public void onSuccess(int sessionId, int requestId, String data) {
    Log.d(TAG,"createDevice"+data);
    connectp2p( mCameraP2P);
  }

  @Override
  public void onFailure(int sessionId, int requestId, int errCode) {
    Log.d(TAG,"createDeviceErr"+sessionId+"---"+requestId+"---"+errCode);
  }
}, bean);
}

public void connectp2p(ICameraP2P mCameraP2P){
mCameraP2P.connect(new OperationDelegateCallBack() {
  @Override
  public void onSuccess(int sessionId, int requestId, String data) {
    Log.d(TAG,"videoConnect"+data);
    previewp2p( mCameraP2P);
  }

  @Override
  public void onFailure(int sessionId, int requestId, int errCode) {
    Log.d(TAG,"videoconnectErr"+sessionId+"---"+requestId+"---"+errCode);
  }
});
}

public void previewp2p(ICameraP2P mCameraP2P){
mCameraP2P.startPreview(new OperationDelegateCallBack() {
  @Override
  public void onSuccess(int sessionId, int requestId, String data) {
    Log.d(TAG,"videodata"+data);
  }

  @Override
  public void onFailure(int sessionId, int requestId, int errCode) {
    Log.d(TAG,"videoErr"+sessionId+ errCode);
  }
});
}






}
