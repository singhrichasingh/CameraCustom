package cordova.plugin.cameracustom;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;
import java.util.HashMap;
import java.nio.ByteBuffer;
import android.os.Build;
import com.alibaba.fastjson.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Environment;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import java.util.Hashtable;
import android.util.Base64;
import com.tuya.smart.android.camera.api.ITuyaHomeCamera;
import com.tuya.smart.home.sdk.callback.ITuyaGetMemberListCallback;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.network.IApiUrlProvider;
import com.tuya.smart.android.network.TuyaSmartNetWork;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.TuyaSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tuya.smart.sdk.api.INeedLoginListener;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.api.IValidateCallback;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.bean.scene.SceneTask;
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
import com.tuya.smart.camera.ipccamerasdk.bean.ConfigCameraBean;
import com.tuya.smart.android.network.http.BusinessResponse;
import com.tuya.smart.camera.middleware.p2p.ITuyaSmartCameraP2P;
import com.tuya.smart.camera.middleware.p2p.TuyaSmartCameraP2P;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.home.sdk.bean.SharedUserInfoBean;
import com.tuya.smart.sdk.bean.ShareIdBean;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.home.sdk.bean.ShareSentUserDetailBean;
import com.tuya.smart.home.sdk.bean.ShareReceivedUserDetailBean;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import com.tuyasmart.camera.devicecontrol.ITuyaCameraDevice;
import com.tuya.smart.home.sdk.bean.scene.SceneBean;
import com.tuya.smart.home.sdk.bean.scene.condition.ConditionListBean;
import com.tuya.smart.home.sdk.bean.scene.PlaceFacadeBean;
import com.tuya.smart.android.camera.api.bean.CameraPushDataBean;
import com.tuya.smart.sdk.api.ITuyaGetBeanCallback;
import com.tuya.smart.sdk.api.ITuyaDataCallback;
import com.tuya.smart.camera.middleware.cloud.bean.CloudDayBean;
import com.tuya.smart.sdk.bean.GroupDeviceBean;
import com.tuya.smart.home.sdk.bean.MemberWrapperBean;
import com.tuya.smart.home.sdk.bean.MemberBean;
import com.tuya.smart.sdk.bean.message.MessageBean;
import com.tuya.sdk.home.bean.InviteMessageBean;
import com.tuya.smart.android.user.api.IBooleanCallback;
import com.tuya.smart.android.user.api.ILogoutCallback;
import com.tuya.smart.home.sdk.bean.scene.SceneCondition;
import com.tuya.smart.home.sdk.bean.scene.condition.rule.Rule;
import com.tuya.smart.home.sdk.bean.scene.condition.rule.ValueRule;
import com.tuya.smart.home.sdk.bean.scene.condition.rule.BoolRule;
import com.tuya.smart.home.sdk.bean.scene.condition.rule.TimerRule;
import com.tuya.smart.home.sdk.bean.scene.dev.TaskListBean;
import com.tuya.smart.camera.middleware.cloud.CameraCloudSDK;
import com.tuya.smart.camera.middleware.cloud.bean.TimePieceBean;
import com.tuya.smart.camera.middleware.cloud.bean.TimeRangeBean;
import com.tuya.smart.camera.middleware.cloud.ICloudCacheManagerCallback;
import com.tuyasmart.camera.devicecontrol.TuyaCameraDeviceControlSDK;
import com.tuya.smart.camera.middleware.widget.AbsVideoViewCallback;
import com.tuyasmart.camera.devicecontrol.bean.DpSDFormat;
import com.tuyasmart.camera.devicecontrol.bean.DpSDFormatStatus;
import com.tuyasmart.camera.devicecontrol.bean.DpSDRecordModel;
import com.tuyasmart.camera.devicecontrol.bean.DpSDRecordSwitch;
import com.tuyasmart.camera.devicecontrol.bean.DpSDStatus;
import com.tuyasmart.camera.devicecontrol.bean.DpSDStorage;
import com.tuyasmart.camera.devicecontrol.model.RecordMode;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.IDevListener;
import java.util.ArrayList;
import com.tuya.smart.camera.camerasdk.typlayer.callback.AbsP2pCameraListener;
import com.tuyasmart.camera.devicecontrol.model.DpNotifyModel;
import com.tuyasmart.camera.devicecontrol.api.ITuyaCameraDeviceControlCallback;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.optimus.lock.api.ITuyaLockManager;
import cordova.plugin.cameracustom.LockFunctions;
import cordova.plugin.cameracustom.Setting;
import java.util.*;
import android.app.Activity;
import android.widget.Toast;
import org.apache.cordova.*;
import android.util.Log;
import java.util.List;
import com.tuya.smart.camera.ipccamerasdk.cloud.ITYCloudCamera;
import com.tuya.smart.home.sdk.builder.ActivatorBuilder;

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
    public String email;
    public String phoneNumber;
    public String password;
    public String otpCode;
    public String deviceId;
    public long homeId;
    public String packageName;
    private ITuyaCameraDevice mDeviceControl;
    public Bitmap bitmap;  
    public  ShareIdBean  beanList;
    private TuyaCameraView mVideoView;
    public String devIdList;
    public View view = null;
    public long memberId;
    public ITuyaSmartCameraP2P cameraP2P;
    private ITYCloudCamera cloudCamera;
    public PlaceFacadeBean cityList;
    public PlaceFacadeBean cityId;
      public static final int MSG_SEND_VALIDATE_CODE_SUCCESS = 12;
      private ITuyaHomeCamera homeCamera;
      private ITuyaDevice mTuyaDevice;
      public String loginErr;
      public SceneCondition sceneCondition;
      public SceneTask sceneTask;
     public String background;
     public int p2pType;
     private String picPath, videoPath;
     private int year,month,day;
     private CallbackContext callbackContext;
     private CameraCloudSDK cameraCloudSDK;
     private ITuyaLockManager tuyaLockManager;
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
         TuyaHomeSdk.init(this.application,appKey,appSecret);
         Fresco.initialize(this.application);
         TuyaOptimusSdk.init(this.application);
         tuyaLockManager = TuyaOptimusSdk.getManager(ITuyaLockManager.class);
    TuyaHomeSdk.setOnNeedLoginListener(new INeedLoginListener(){
               @Override
                public void onNeedLogin(Context context) {
                       }
                      });   
             return true;
        }
       else if(action.equals("getOtp")) {
            phoneCode = args.getJSONObject(0).getString("code");
             phoneNumber = args.getJSONObject(0).getString("number");
             email = args.getJSONObject(0).getString("mailId");
             this.getOtp(phoneCode,phoneNumber,email,callbackContext);
              return true;
        }
        else if(action.equals("reg")) {
            phoneCode = args.getJSONObject(0).getString("code");
             phoneNumber = args.getJSONObject(0).getString("number");
             password = args.getJSONObject(0).getString("password");
             email = args.getJSONObject(0).getString("mailId");
             otpCode = args.getJSONObject(0).getString("otp");
             this.register(phoneCode,phoneNumber,password,email,otpCode,callbackContext);
              return true;
        }
        else if(action.equals("login")) {
            phoneCode = args.getJSONObject(0).getString("code");
             phoneNumber = args.getJSONObject(0).getString("number");
             password = args.getJSONObject(0).getString("password");
             email = args.getJSONObject(0).getString("mailId");
             this.login(phoneCode,phoneNumber,password,email,callbackContext);
              return true;
        }
          else if(action.equals("home")){
      this.queryHomeList( callbackContext);
    return true;
  }
      else if(action.equals("devicelist")){
    this.getHomeDetail(homeId, callbackContext);
  return true;
  }
  else if(action.equals("findDeviceType")){
    String id = args.getJSONObject(0).getString("id");
    this.findDeviceType(id,callbackContext);
    return true;
  }
    else if(action.equals("initDevice")){
          String id = args.getJSONObject(0).getString("id");
          this.initDevice(id,callbackContext);
          return true;
        }
        else if(action.equals("playbackStatus")){
          String id = args.getJSONObject(0).getString("id");
          int num   =Integer.parseInt(args.getJSONObject(0).getString("num"));
          playbackStatus(callbackContext,num,id);
          return true;
        }
        
        else if(action.equals("playback")){
          String id = args.getJSONObject(0).getString("id");
          // year = Integer.parseInt(args.getJSONObject(0).getString("id"));
          // month  = Integer.parseInt(args.getJSONObject(0).getString("id"));
          //   day = Integer.parseInt(args.getJSONObject(0).getString("id"));
          this.playback(id);
          return true;
        }
        else if(action.equals("messagePanel")){
          String id = args.getJSONObject(0).getString("id");
          // year = Integer.parseInt(args.getJSONObject(0).getString("id"));
          // month  = Integer.parseInt(args.getJSONObject(0).getString("id"));
          //   day = Integer.parseInt(args.getJSONObject(0).getString("id"));
          openMessageActivity(id);
          return true;
        }
        
      else if(action.equals("createhome")){
          String name = args.getJSONObject(0).getString("homeName");
          double lon = 0;
          double lat = 0;
          String geoName = "";
          String room = args.getJSONObject(0).getString("roomList");
          this.createHome( name, lon, lat, geoName, room, callbackContext);
        }
        else if(action.equals("deleteDevice")){
          deviceId=args.getJSONObject(0).getString("devicelist");
          this.deleteDevice(deviceId,callbackContext);
        }
        else if(action.equals("renameDevice")){
          deviceId=args.getJSONObject(0).getString("devicelist");
          String titleName=args.getJSONObject(0).getString("titleName");
          this.renameDevice( callbackContext, deviceId, titleName);
        }
        else if(action.equals("qrGen")){
          String wifiname = args.getJSONObject(0).getString("ssid");
          //String wifipass = args.getJSONObject(0).getString("password");
          this.generateQr(wifiname, callbackContext);
          return true;
        }
        else if (action.equals("search")) {
             Log.d(TAG, "Initializing heloop");
            return true;
        }else  if (action.equals("shareDevice")) {
          String condition = args.getJSONObject(0).getString("condition");
          String homeId = args.getJSONObject(0).getString("homeId");
          String memberId =args.getJSONObject(0).getString("memberId");
          List <String> devicelist =Arrays.asList(args.getJSONObject(0).getString("devicelist"));
          String userAcc = args.getJSONObject(0).getString("userAcc");
          long homeId1=new Long(homeId);
          long memberId1=new Long(memberId);
           this.shareDevice(callbackContext,condition,memberId1,devicelist,homeId1,userAcc);
         return true;
     }else if (action.equals("getShare")) {
      String condition = args.getJSONObject(0).getString("condition");
      String homeId = args.getJSONObject(0).getString("homeId");
      String memberId =args.getJSONObject(0).getString("memberId");
      String devicelist =args.getJSONObject(0).getString("devicelist");
      long homeId1=new Long(homeId);
      long memberId1=new Long(memberId);
       this.getShare(callbackContext,condition,memberId1,devicelist,homeId1);
     return true;
 } 
 else if (action.equals("delShare")) {
  String condition = args.getJSONObject(0).getString("condition");
  String memberId =args.getJSONObject(0).getString("memberId");
  String devicelist =args.getJSONObject(0).getString("devicelist");
  long memberId1=new Long(memberId);
   this.deleteShare(callbackContext,condition,memberId1,devicelist);
 return true;
}  else if (action.equals("modifyShare")) {
  String condition = args.getJSONObject(0).getString("condition");
  String memberId =args.getJSONObject(0).getString("memberId");
  String name =args.getJSONObject(0).getString("name");
  long memberId1=new Long(memberId);
   this.modifyShare(callbackContext,condition,memberId1,name);
 return true;
} else if(action.equals("addTimer")){
  //this.addTuyaTimer(callbackContext);
return true;
} else if(action.equals("timerStatus")){
  //this.timerStatus(callbackContext);
return true;
} else if(action.equals("updateTimer")){
  //this.updateTimer(callbackContext);
return true;
} else if(action.equals("getTimerTask")){
  //this.getTimerTask(callbackContext);
return true;
} else if(action.equals("updateSchedule")){
  //this.updateAllScheduleStatus(callbackContext);
return true;
}else if(action.equals("getAllScene")){
  String homeId = args.getJSONObject(0).getString("homeId");
  long homeId1=new Long(homeId);
  this.getAllScene(homeId1,callbackContext);
return true;
}
else if(action.equals("getConditionList")){
  String homeId = args.getJSONObject(0).getString("homeId");
  long homeId1=new Long(homeId);
  this.getConditionList(callbackContext);
return true;
}else if(action.equals("createTempCondition")){
  String lat = args.getJSONObject(0).getString("lat");
  String lang = args.getJSONObject(0).getString("lang");
  this.createTempCondition(lang, lat, callbackContext);
return true;
}else if(action.equals("getDeviceTaskOperationList")){
  String devicelist =args.getJSONObject(0).getString("devicelist");
  this.getDeviceTaskOperationList(devicelist,callbackContext);
return true;
}
else if(action.equals("createSceneTask")){
  String devicelist =args.getJSONObject(0).getString("devicelist");
  this.createSceneTask(devicelist);
return true;
}

else if(action.equals("createDevCondition")){
  String homeId =args.getJSONObject(0).getString("homeId");
  long homeId1=new Long(homeId);
  this.createDevCondition(homeId1,callbackContext);
return true;
}
else if(action.equals("createTimingCondition")){
  // String homeId =args.getJSONObject(0).getString("homeId");
  // long homeId1=new Long(homeId);
  this.createTimingCondition(callbackContext);
return true;
}
else if(action.equals("createScene")){
  String homeId =args.getJSONObject(0).getString("homeId");
  long homeId1=new Long(homeId);
  String devicelist =args.getJSONObject(0).getString("devicelist");
  this.createScene(homeId1,devicelist,callbackContext);
return true;
}
else if(action.equals("modifyScene")){
  String homeId =args.getJSONObject(0).getString("homeId");
  long homeId1=new Long(homeId);
  this.modifyScene(homeId1,callbackContext);
return true;
}
else if(action.equals("deleteScene")){
  String homeId =args.getJSONObject(0).getString("homeId");
  long homeId1=new Long(homeId);
  this.deleteScene(homeId1,callbackContext);
return true;
}
else if(action.equals("executeScene")){
  String homeId =args.getJSONObject(0).getString("homeId");
  long homeId1=new Long(homeId);
  this.executeScene(homeId1,callbackContext);
return true;
}
else if(action.equals("getDeviceConditionOperationList")){
  deviceId =args.getJSONObject(0).getString("devicelist");
  // String homeId =args.getJSONObject(0).getString("homeId");
  // long homeId1=new Long(homeId);
  this.getDeviceConditionOperationList(deviceId,callbackContext);
return true;
}

else if(action.equals("memberlist")){
  String homeId = args.getJSONObject(0).getString("homeId");
  long homeId1=new Long(homeId);
       this.queryMemberList(homeId1,callbackContext);
     return true;
     }
     else if(action.equals("getmessage")){
       this.getMessageList( callbackContext);
       return true;
     }
     else if(action.equals("deletemessage")){
       // this.deleteMessages( callbackContext);
       return true;
     }
   
     else if(action.equals("dismiss")){
       this.dismissHome();
       return true;
     }
     else if(action.equals("wifilock")){
       String ssid = args.getJSONObject(0).getString("ssid");
       String password = args.getJSONObject(0).getString("password");
       this.configSmartLock(ssid, password, callbackContext);
       return true;
     } 
     else if(action.equals("addmember")){
       this.inviteMember(callbackContext);
       return true;
     }
     else if(action.equals("invitecode")){
       String code = args.getJSONObject(0).getString("invitecode");
       this.joinHomeByCode(code, callbackContext);
       return true;
     }
     else if(action.equals("pushReg")){
       String aliasId = args.getJSONObject(0).getString("fcmtoken");
       this.pushRegDev(aliasId, "fcm");
       return true;
     }
    else if(action.equals("remoteUnlock")){
      String id = args.getJSONObject(0).getString("id");
      boolean allow=Boolean.parseBoolean(args.getJSONObject(0).getString("allow"));  
      //String aliasId = args.getJSONObject(0).getString("fcmtoken");
      remoteUnlockStart(id,allow);
      return true;
    }
     else if(action.equals("createTempPass")){
      String id = args.getJSONObject(0).getString("id");
      String name = args.getJSONObject(0).getString("name");
      String password = args.getJSONObject(0).getString("password");
      long startTime = new Long(args.getJSONObject(0).getString("startTime"));
      long endTime = new Long(args.getJSONObject(0).getString("endTime"));
      createTempPassword(id,name,password,startTime,endTime,callbackContext);
      return true;
    }
     else if(action.equals("getTempPass")){
      String id = args.getJSONObject(0).getString("id");
      //String aliasId = args.getJSONObject(0).getString("fcmtoken");
      getTempPassword(id,callbackContext);
      return true;
    }
    else if(action.equals("delTempPass")){
      String id = args.getJSONObject(0).getString("id");
      //Integer.parseInt(args.getJSONObject(0).getString("num"))
      int passId = Integer.parseInt(args.getJSONObject(0).getString("passId"));
      this.deleteTempPassword(id,passId);
      return true;
    }
    else if(action.equals("createDynaPass")){
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.createDynamicPassword(id,callbackContext);
      return true;
    }
    else if(action.equals("addLockUser")){
      String name = args.getJSONObject(0).getString("name");
      //String id = args.getJSONObject(0).getString("id");
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.addLockUser(name,id, callbackContext);
      return true;
    }
    else if(action.equals("updateLockUser")){
      String name = args.getJSONObject(0).getString("name");
      String userId = args.getJSONObject(0).getString("userId");
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.updateLockUser(userId,name,id,callbackContext);
      return true;
    }
    else if(action.equals("getLockUser")){
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.getLockUser(id,callbackContext);
      return true;
    }
    else if(action.equals("updateFamilyLockUser")){
      String userId = args.getJSONObject(0).getString("userId");
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.updateFamilyUserUnlockMode(userId,id, callbackContext);
      return true;
    }
    else if(action.equals("deleteLockUser")){
      String userId = args.getJSONObject(0).getString("userId");
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.deleteLockUser(userId,id,callbackContext);
      return true;
    }
    else if(action.equals("getRecords")){
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.getRecords(id, callbackContext);
      return true;
    }
    else if(action.equals("getUnlockRecords")){
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.getUnlockRecords(id,callbackContext);
      return true;
    }
    else if(action.equals("getHijackRecords")){
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.getHijackRecords(id, callbackContext);
      return true;
    }
    else if(action.equals("setHijackConfig")){
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.setHijackingConfig(id,callbackContext);
      return true;
    }
    else if(action.equals("rmHijackConfig")){
      String id = args.getJSONObject(0).getString("id");
     // String aliasId = args.getJSONObject(0).getString("fcmtoken");
      this.removeHijackingConfig( id,callbackContext);
      return true;
    }
     else if(action.equals("cloudSer")){
      String devId= args.getJSONObject(0).getString("id");
      String homeId= args.getJSONObject(0).getString("homeId");
      this.cloudSer(devId,homeId);
      return true;
    }
    else if(action.equals("logout")){
      this.logout();
      return true;
    }
     else if (action.equals("group")) {
       String group_function = args.getJSONObject(0).getString("group_fun");
       switch (group_function) {
         case "create" :{
             break;
         }
         case "get" :{
           break;
         }
         case "delete" :{
           break;
         }
         case "update" :{
           break;
         }
       }
         return true;
     }
        return false;
    }

    private void getOtp(String phoneCode,String phoneNumber,String email,CallbackContext callbackContext){
      if(email==null){
       TuyaHomeSdk.getUserInstance().getValidateCode(phoneCode,phoneNumber, new IValidateCallback() {
        @Override
        public void onSuccess() {
        activity = cordovaInterface.getActivity();
            // Toast.makeText(activity, "1 createSuc", Toast.LENGTH_LONG).show();
          Log.d(TAG, "success");
        }
     @Override
        public void onError(String s, String s1) {
          Log.d(TAG, "error"+s);
        }
    });
  }
  else{
  TuyaHomeSdk.getUserInstance().getRegisterEmailValidateCode(phoneCode,email, new IResultCallback() {
    @Override
    public void onError(String s, String s1) {
        getValidateCodeFail(s, s1);
    }

    @Override
    public void onSuccess() {
       // mHandler.sendEmptyMessage(MSG_SEND_VALIDATE_CODE_SUCCESS);
    }
});
}
     }
 
     protected void getValidateCodeFail(String errorCode, String errorMsg) {
      //Message msg = MessageUtil.getCallFailMessage(MSG_SEND_VALIDATE_CODE_ERROR, errorCode, errorMsg);
      //mHandler.sendMessage(msg);
      //mSend = false;
  }

  private void register(String phoneCode,String phoneNumber,String password,String email,String otpCode,CallbackContext callbackContext){
 if(email==null){
 TuyaHomeSdk.getUserInstance().registerAccountWithPhone(phoneCode, phoneNumber, password, otpCode, new IRegisterCallback() {
        @Override
        public void onSuccess(User user) {
           Log.d(TAG, "2suc "+user);
        }
        @Override
        public void onError(String errorCode, String errorMsg) {
                Log.d(TAG, "2 err  "+errorMsg);
        }
    });
  }
  else{
    TuyaHomeSdk.getUserInstance().registerAccountWithEmail(phoneCode,email,password,otpCode,new IRegisterCallback() {
      @Override
      public void onSuccess(User user) {
         Log.d(TAG, "2suc "+user);
      }
      @Override
      public void onError(String errorCode, String errorMsg) {
              Log.d(TAG, "2 err  "+errorMsg);
      }
  });
  }
}
 

 private void login(String phoneCode, String phoneNumber, String password,String email,CallbackContext callbackContext){
   Log.d(TAG,"email  "+email.isEmpty() +" "+phoneNumber+" "+phoneCode);
   if(email=="null"){
    Log.d(TAG, "3suc "+email);
   TuyaHomeSdk.getUserInstance().loginWithPhonePassword(phoneCode, phoneNumber, password,new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
          Log.d(TAG, "3suc   null email");
          registerCameraPushListener();
         callbackContext.success("3"+user);
        }
        @Override
        public void onError(String errorCode, String errorMsg) {
            Log.d(TAG, "3 err no onne "+errorMsg);
            loginErr = (errorCode+"-"+errorMsg);
        }
    });
  }
  if(phoneNumber=="null"){
    Log.d(TAG, "3suc null number");
    TuyaHomeSdk.getUserInstance().loginWithEmail(phoneCode,email, password, new ILoginCallback () {
      @Override
      public void onSuccess (User user) {
        Log.d(TAG, "3suc "+user);
        registerCameraPushListener();
       callbackContext.success("3"+user);
      }
  
      @Override
      public void onError (String code, String error) {
        Log.d(TAG, "3 err hello "+error);
        loginErr = (code+"-"+error);
      }
  });
  }
   }


   private void openNewActivity(Context context,String deviceId) {
    Intent intent = new Intent(context, NewActivity.class);
   String P2PType= String.valueOf(p2pType);
Bundle bundle = new Bundle();
bundle.putString("deviceId",deviceId);
bundle.putString("P2PType",P2PType);
intent.putExtras(bundle);
this.cordova.getActivity().startActivity(intent);
    Log.d(TAG,"new intent "+intent);
    
}
private void openMessageActivity(String deviceId) {
  Context context=this.cordova.getActivity().getApplicationContext();
  Intent intent = new Intent(context, MessagePanel.class);
 //String P2PType= String.valueOf(p2pType);
Bundle bundle = new Bundle();
bundle.putString("deviceId",deviceId);
//bundle.putString("P2PType",P2PType);
intent.putExtras(bundle);
this.cordova.getActivity().startActivity(intent);
  Log.d(TAG,"new intent "+intent);
  
}

private void openNewActivity1(Context context,String deviceId) {
  Intent intent = new Intent(context, PlaybackPanel.class);
 String P2PType= String.valueOf(p2pType);
Bundle bundle = new Bundle();
bundle.putString("deviceId",deviceId);
bundle.putString("P2PType",P2PType);
intent.putExtras(bundle);
this.cordova.getActivity().startActivity(intent);
  Log.d(TAG,"new intent "+intent);
  
}

 void createHome(String name, double lon, double lat, String geoName, String rooms, CallbackContext callbackContext){
    List<String> roomlist = new ArrayList<String>();
          roomlist.add(rooms);
    TuyaHomeSdk.getHomeManagerInstance().createHome(name, lon, lat, geoName, roomlist, new ITuyaHomeResultCallback() {
      @Override
      public void onError(String errorCode, String errorMsg) {
        Log.d(TAG,"home"+errorMsg); 
      }
      @Override
      public void onSuccess(HomeBean bean) {
           Log.d(TAG,"home"+bean);
          activity = cordovaInterface.getActivity();
            // Toast.makeText(activity, "home createSuc"+bean, Toast.LENGTH_LONG).show();
      }
  });
  }
    public void queryHomeList(CallbackContext callbackContext) throws JSONException {
      Context context=this.cordova.getActivity().getApplicationContext();
      this.application= (Application) context;
      TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
        @Override
        public void onSuccess(List<HomeBean> homeBeans) {
        Log.d(TAG, "dev  "+homeBeans );
            homeId=homeBeans.get(0).getHomeId();
            activity = cordovaInterface.getActivity();
            TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeBeans.get(0).getHomeId(), new ITuyaActivatorGetToken() {
              @Override
              public void onSuccess(final String token) {
                activity = cordovaInterface.getActivity();
                 token1=token;
                 Log.d(TAG, "token..."+token);
                //registerCameraPushListener();
                callbackContext.success("HOMEID"+homeId);
              }
             @Override
              public void onFailure(String errorCode, String errorMsg) {
                activity = cordovaInterface.getActivity();
                callbackContext.error("homeERR");
              }
          }); 
        }
        @Override
        public void onError(String errorCode, String error) {
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
                  }

                  @Override
                  public void onActiveSuccess(DeviceBean devResp) {
                      L.d(TAG, " devResp: " + devResp.getDevId());
                  }

                  @Override
                  public void onStep(String step, Object data) {
                  }
              }).setToken(token)).start();
  }
private void generateQr(String wifiname,  CallbackContext callbackContext){
  Context context=this.cordova.getActivity().getApplicationContext();
  Intent intent = new Intent(context, QrCodeGen.class);
 //String P2PType= String.valueOf(p2pType);
Bundle bundle = new Bundle();
//bundle.putString("deviceId",deviceId);
bundle.putString("token",token1);
bundle.putString("SSID",wifiname);
intent.putExtras(bundle);
this.cordova.getActivity().startActivity(intent);
  Log.d(TAG,"new intent "+intent);
}

public void deleteDevice(String deviceId, CallbackContext callbackContext){
  TuyaHomeSdk.newDeviceInstance(deviceId).removeDevice(new IResultCallback() {
  @Override
  public void onError(String errorCode, String errorMsg) {
    //callbackContext.error(errorMsg);
  }
  @Override
  public void onSuccess() {
    callbackContext.success("Deleted Successfully");
  }
});
}
public void renameDevice(CallbackContext callbackContext,String deviceId,String titleName){
  mTuyaDevice= TuyaHomeSdk.newDeviceInstance(deviceId);
  mTuyaDevice.renameDevice(titleName, new IResultCallback() {
    @Override
    public void onError(String code, String error) {
        Log.d(TAG,error);
        callbackContext.error(error);
    }

    @Override
    public void onSuccess() {
      callbackContext.success("Device name Changed");
    }
});
}

// TO GET HOME DETAIL...........................................................
private void getHomeDetail(long homeid, CallbackContext callbackContext) throws JSONException {
  TuyaHomeSdk.newHomeInstance(homeid).getHomeDetail(new ITuyaHomeResultCallback() {
    @Override
    public void onSuccess(HomeBean bean) {
      Log.d(TAG, " home-detail: "+bean );
      activity = cordovaInterface.getActivity();
        List<DeviceBean> deviceList = bean.getDeviceList();
        Log.d(TAG, " device-detail: "+deviceList );
        try{
          JSONArray jsonArray = new JSONArray();
        for (DeviceBean devbean : deviceList) {
          JSONObject devDetailsJson = new JSONObject();
              //Log.d(TAG,"device "+devbean);
              devDetailsJson.put("bean",devbean);
               
              devDetailsJson.put("id", devbean.getDevId());
              devDetailsJson.put("name", devbean.getName());
              devDetailsJson.put("icon", devbean.getIconUrl());
              //deviceBean.getIconUrl()
             jsonArray.put(devDetailsJson);
             Log.d(TAG,"device "+jsonArray);
        }
        callbackContext.success(jsonArray);
        }catch(JSONException e){}
        }
    @Override
    public void onError(String errorCode, String errorMsg) {
      // do something
      Log.d(TAG, "home-detailERR: "+errorMsg );
        activity = cordovaInterface.getActivity();
        // Toast.makeText(activity, "HomeDetailERR"+errorMsg, Toast.LENGTH_LONG).show();
        callbackContext.error(errorMsg);
    }
});
}


//init device
private void initDevice(String deviceId,CallbackContext callbackContext){
 // checkStatus(deviceId);
  TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
    @Override
    public void onSuccess(HomeBean bean) {
      Log.d(TAG, " home-detail: "+bean );
      activity = cordovaInterface.getActivity();
        List<DeviceBean> deviceList = bean.getDeviceList();
        for (DeviceBean devbean : deviceList) {
          if (!devbean.getIsOnline()) {
            activity = cordovaInterface.getActivity();
         Toast.makeText(activity, "Device is Offline", Toast.LENGTH_LONG).show();
            //showDevIsNotOnlineTip(deviceBean);
            return;
           // Log.d(TAG,"device Status offline");
        }
        else{
           devIdList=devbean.getDevId();
           if(deviceId.equals(devbean.getDevId())){
            gotoDeviceCommonActivity(devbean,callbackContext);
           }
          }
        }
      }
    @Override
    public void onError(String errorCode, String errorMsg) {
      Log.d(TAG, "home-detailERR: "+errorMsg );
      activity = cordovaInterface.getActivity();
    }
});
}

private void findDeviceType(String deviceId,CallbackContext callbackContext){
   TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
     @Override
     public void onSuccess(HomeBean bean) {
       Log.d(TAG, " home-detail: "+bean );
       activity = cordovaInterface.getActivity();
         List<DeviceBean> deviceList = bean.getDeviceList();
         for (DeviceBean devbean : deviceList) {
           if (!devbean.getIsOnline()) {
             activity = cordovaInterface.getActivity();
          Toast.makeText(activity, "Device is Offline", Toast.LENGTH_LONG).show();
             return;
         }
         else{
            devIdList=devbean.getDevId();
            if(deviceId.equals(devbean.getDevId())){
             showDeviceType(devbean,callbackContext);
            }
           }
         }
       }
     @Override
     public void onError(String errorCode, String errorMsg) {
       Log.d(TAG, "home-detailERR: "+errorMsg );
       activity = cordovaInterface.getActivity();
     }
 });
 }

 private void showDeviceType(DeviceBean devBean,CallbackContext callbackContext) {
  if ("sp".equals(devBean.getProductBean().getCategory())) {
      Map<String, Object> map = devBean.getSkills();
       p2pType = -1;
      if (map == null || map.size() == 0) {
          p2pType = -1;
      } else {
          p2pType = (Integer) (map.get("p2pType"));
      }
      Log.d(TAG,"smartcamera"+p2pType+"----"+devBean);
      callbackContext.success("camera");
      
  } else{
LockFunctions lockfunction= new LockFunctions();
Context context=this.cordova.getActivity().getApplicationContext();
lockfunction.init(devBean.getDevId(),tuyaLockManager);
callbackContext.success("lock");
//lockfunction.startRemoteUnlock(devBean.getDevId(),tuyaLockManager,context);   
  }
}


// SMART CAMERA...........................................................
private void gotoDeviceCommonActivity(DeviceBean devBean,CallbackContext callbackContext) {
  if ("sp".equals(devBean.getProductBean().getCategory())) {
      Map<String, Object> map = devBean.getSkills();
       p2pType = -1;
      if (map == null || map.size() == 0) {
          p2pType = -1;
      } else {
          p2pType = (Integer) (map.get("p2pType"));
      }
      Log.d(TAG,"smartcamera"+p2pType+"----"+devBean);
      createView(devBean.getDevId());
      //callbackContext.success("camera");
      
  } else{
    LockFunctions lockfunction= new LockFunctions();
Context context=this.cordova.getActivity().getApplicationContext();
lockfunction.init(devBean.getDevId(),tuyaLockManager); 
lockfunction.startRemoteUnlock(devBean.getDevId(),tuyaLockManager,callbackContext);
  }
}


 private void remoteUnlockStart(String devId,Boolean allow){
 Context context=this.cordova.getActivity().getApplicationContext();
 LockFunctions lockfunction= new LockFunctions();
 lockfunction.replyRemoteUnlockRequest(allow,devId,tuyaLockManager,context);
 }

private void gotoDeviceCommonActivity2(DeviceBean devBean) {
  if ("sp".equals(devBean.getProductBean().getCategory())) {
      Map<String, Object> map = devBean.getSkills();
       p2pType = -1;
      if (map == null || map.size() == 0) {
          p2pType = -1;
      } else {
          p2pType = (Integer) (map.get("p2pType"));
      }
      Log.d(TAG,"smartcamera"+p2pType+"----"+devBean);
      createView2(devBean.getDevId());
      
  } else{
    Log.d(TAG,"simple Device"+devBean);
  }
}

private void createView(String deviceId){
Context context=this.cordova.getActivity().getApplicationContext();
openNewActivity(context,deviceId);
}

private void createView2(String deviceId){
  Context context=this.cordova.getActivity().getApplicationContext();
  playbackStatus(callbackContext,1,deviceId);
  openNewActivity1(context,deviceId);
}



public void dismissHome(){
  TuyaHomeSdk.newHomeInstance(homeId).dismissHome(new IResultCallback() {
    @Override
    public void onError(String code, String error) {
        // do something
        Log.d(TAG, " dismisHomeERR: "+error );
    }
    @Override
    public void onSuccess() {
        Log.d(TAG, " dismiss home success: ");
    }
});
}
public void activatorToken(long homeId){
  TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, 
        new ITuyaActivatorGetToken() {
           @Override
            public void onSuccess(String token) {
              token1 = token;
              Log.d(TAG, " act_token: "+token );
            }
            @Override
            public void onFailure(String s, String s1) {
              Log.d(TAG, " act_tokenERR: "+s+s1 );
            }
        });
}

  // ADD MEMBER.........................
  public void inviteMember(CallbackContext callbackContext){
    TuyaHomeSdk.getMemberInstance().getInvitationMessage(homeId,new ITuyaDataCallback<InviteMessageBean>(){
      @Override
      public void onSuccess(InviteMessageBean result) {
        Log.d(TAG, "inviteResult"+ result.getInvitationMsgContent()+"..code.."+result.getInvitationCode());
        callbackContext.success(result.getInvitationMsgContent());
      }

      @Override
      public void onError ( String errorCode, String errorMessage) {
        Log.d(TAG, "inviteErr"+errorMessage);
        callbackContext.error(errorMessage);
      }
    });
  }

  public void joinHomeByCode(String code, CallbackContext callbackContext){
    TuyaHomeSdk.getHomeManagerInstance().joinHomeByInviteCode( code, new IResultCallback(){
      @Override
      public void onError(String code, String error) {
          Log.d(TAG, "memberAddErr"+error);
          callbackContext.error("addErr"+ error);
      }
      @Override
      public void onSuccess() {
          Log.d(TAG, "memberAdd");
          callbackContext.success("addSuccess");
      }
    });
  }

public void queryMemberList( long homeId,CallbackContext callbackContext) throws JSONException {
  JSONArray membersArr = new JSONArray();
  TuyaHomeSdk.getMemberInstance().queryMemberList(homeId, new ITuyaGetMemberListCallback() {
    @Override
    public void onError(String errorCode, String error) {
        // do something
        Log.d(TAG, "allMembersErr"+error);
        callbackContext.error("allMembersErr"+ error);
    }
    @Override
    public void onSuccess(List<MemberBean> memberBeans) {
        // do something
        Log.d(TAG, "memberBeans.."+memberBeans);
      try{
        for (MemberBean member : memberBeans) {
          JSONObject members = new JSONObject();
          memberId=member.getMemberId();
          members.put("id", member.getMemberId());
          members.put("role", member.getRole());
          members.put("account", member.getAccount());
          membersArr.put(members);
          Log.d(TAG, "memberBeansList.."+members);
          callbackContext.success(members);
        }
        Log.d(TAG, "memberBeansArr.."+membersArr);
      } catch(JSONException e){}
        //callbackContext.success("hdfghjjk");
        callbackContext.success(membersArr);
    }
  });
}

public void shareDevice(CallbackContext callbackContext,String condition,long memberId1,List <String> devicelist,long homeId1, String userAccount){
  String countryCode="91";
  boolean autoSharing=true;
  List<String> devsIdList = Arrays.asList(devIdList);
   Log.d(TAG,"nicooooooo    "+ memberId1+"   "+devicelist+"   "+homeId1+"   "+userAccount);
  switch (condition) {
    case "byMember" :{
      TuyaHomeSdk.getDeviceShareInstance().addShareWithMemberId(memberId1, devicelist, new IResultCallback() {
        @Override
        public void onError(String errorCode, String errorMsg) {
          callbackContext.error("err  "+errorMsg);
        }
        @Override
        public void onSuccess() {
          callbackContext.success("Shared Device Successfully");
        }
    });
        break;
    }
    case "byAcc" :{
      TuyaHomeSdk.getDeviceShareInstance().addShareWithHomeId(homeId1, countryCode,userAccount, devicelist, new ITuyaResultCallback<SharedUserInfoBean>() {
          @Override
          public void onSuccess(SharedUserInfoBean sharedUserInfoBean) {
            callbackContext.success(JSON.toJSONString(sharedUserInfoBean));
          }
          @Override
          public void onError(String errorCode, String errorMsg) {   
            callbackContext.error("shared err"+errorMsg);                    
          }
    });
      break;
    }
  }
}
 
public void getShare(CallbackContext callbackContext,String condition,long memberId1,String devicelist,long homeId1){
  switch (condition) {
    case "allUser" :{
      TuyaHomeSdk.getDeviceShareInstance().queryUserShareList(homeId1, new ITuyaResultCallback<List<SharedUserInfoBean>>() {
        @Override
        public void onSuccess(List<SharedUserInfoBean> sharedUserInfoBeans) {
          try{
            JSONArray jsonArray = new JSONArray();
          for (SharedUserInfoBean sharebean : sharedUserInfoBeans) {
            JSONObject shareJson = new JSONObject();
                shareJson.put(" Share List", JSON.toJSONString(sharebean));
                jsonArray.put(shareJson);
          }
          callbackContext.success(jsonArray);
          }catch(JSONException e){}
        }
        @Override
        public void onError(String errorCode, String errorMsg) {
          callbackContext.error("get err"+errorMsg);
        }
    });
      break;
    }
    case "userRec" :{
      TuyaHomeSdk.getDeviceShareInstance().queryShareReceivedUserList(new ITuyaResultCallback<List<SharedUserInfoBean>>() {
        @Override
        public void onSuccess(List<SharedUserInfoBean> sharedUserInfoBeans) {
          try{
            JSONArray jsonArray = new JSONArray();
          for (SharedUserInfoBean sharebean : sharedUserInfoBeans) {
            JSONObject shareJson = new JSONObject();
               shareJson.put("Share List", JSON.toJSONString(sharebean));
                jsonArray.put(shareJson);
          }
          callbackContext.success(jsonArray);
          }catch(JSONException e){}
        }
        @Override
        public void onError(String errorCode, String errorMsg) {
          callbackContext.error("get err"+errorMsg);
        }
    });
      break;
    }
    case "singleUser" :{
      TuyaHomeSdk.getDeviceShareInstance().getUserShareInfo(memberId1, new ITuyaResultCallback<ShareSentUserDetailBean>() {
        @Override
        public void onSuccess(ShareSentUserDetailBean shareSentUserDetailBean) {
          callbackContext.success(JSON.toJSONString(shareSentUserDetailBean));
        }
        @Override
        public void onError(String errorCode, String errorMsg) {
          callbackContext.error("get err"+errorMsg);
        }
    });

      break;
    }
    case "singleShare" :{
      TuyaHomeSdk.getDeviceShareInstance().getReceivedShareInfo(memberId1, new ITuyaResultCallback<ShareReceivedUserDetailBean>() {
        @Override
        public void onSuccess(ShareReceivedUserDetailBean shareReceivedUserDetailBean) {
          callbackContext.success(JSON.toJSONString(shareReceivedUserDetailBean));
        }
        @Override
        public void onError(String errorCode, String errorMsg) {
          callbackContext.error("get err"+errorMsg);
        }
    });
      break;
    }
    case "singleDevice" :{
      TuyaHomeSdk.getDeviceShareInstance().queryDevShareUserList(devicelist, new ITuyaResultCallback<List<SharedUserInfoBean>>() {
        @Override
        public void onError(String errorCode, String errorMsg) {
          callbackContext.error("get err"+errorMsg);
        }
        @Override
        public void onSuccess(List<SharedUserInfoBean> shareUserBeen) {
          callbackContext.success(JSON.toJSONString(shareUserBeen));
        }
    });
      break;
    }
}
}
public void deleteShare(CallbackContext callbackContext,String  condition,long memberId1,String deviceId){
  switch (condition) {
    case "delShare" :{
      TuyaHomeSdk.getDeviceShareInstance().removeUserShare(memberId1, new IResultCallback() {
        @Override
        public void onError(String code, String error) {
          callbackContext.error("get err"+error);
        }
        @Override
        public void onSuccess() {
          callbackContext.success("delete");
        }
    });
      break;
    }
    case "delReceive" :{
      TuyaHomeSdk.getDeviceShareInstance().removeReceivedUserShare(memberId1, new IResultCallback() {
        @Override
        public void onError(String code, String error) {
          callbackContext.error("get err"+error);
        }
        @Override
        public void onSuccess() {
          callbackContext.success("delete");
        }
    });
      break;
    }
    case "delSingle" :{
      TuyaHomeSdk.getDeviceShareInstance().disableDevShare (deviceId, memberId1, new IResultCallback() {
        @Override
        public void onError(String code, String error) {
          callbackContext.error("get err"+error);
        }
        @Override
        public void onSuccess() {
          callbackContext.success("delete");
        }
    });
      break;
    }
    case "delShareDev" :{
      TuyaHomeSdk.getDeviceShareInstance().removeReceivedDevShare(deviceId,new IResultCallback() {
        @Override
        public void onError(String code, String error) {
          callbackContext.error("get err"+error);
        }
        @Override
        public void onSuccess() {
          callbackContext.success("delete");
        }
    });
      break;
    }
  }
}

public void modifyShare(CallbackContext callbackContext,String condition,long memberId1,String name){
  switch (condition) {
    case "actShare" :{
      TuyaHomeSdk.getDeviceShareInstance().renameShareNickname(memberId1,name, new IResultCallback() {
        @Override
        public void onError(String s, String s1) {
          callbackContext.error("get err"+s1);
        }
        @Override
        public void onSuccess() {
          callbackContext.success("modified");
        }
    });
      break;
    }
    case "reShare" :{
      TuyaHomeSdk.getDeviceShareInstance().renameReceivedShareNickname(memberId1, name, new IResultCallback() {
        @Override
        public void onError(String s, String s1) {
          callbackContext.error("get err"+s1);
        }
        @Override
        public void onSuccess() {
          callbackContext.success("modified");
        }
    });

      break;
    }
  }
}
// CONFIG DEVICE AP MODE....................
public void configSmartLock(String ssid, String password, CallbackContext callbackContext){
LockFunctions lockfunction= new LockFunctions();
Context context=this.cordova.getActivity().getApplicationContext();
lockfunction.addSmartLock(ssid,password,token1,context,tuyaLockManager);
}

private ITuyaGetBeanCallback<CameraPushDataBean> mTuyaGetBeanCallback = new ITuyaGetBeanCallback<CameraPushDataBean>() {
  @Override
  public void onResult(CameraPushDataBean o) {
      Log.d(TAG, "onMqtt_43_Result on callback");
      Log.d(TAG, "timestamp=" + o.getTimestamp());
      Log.d(TAG, "devid=" + o.getDevId());
      Log.d(TAG, "msgid=" + o.getEdata());
      Log.d(TAG, "msgtype=" + o.getEtype());
  }
};
/**
 * Register listening after successful account login
 */
public void registerCameraPushListener() {
    homeCamera = TuyaHomeSdk.getCameraInstance();
    if (homeCamera != null) {
        homeCamera.registerCameraPushListener(mTuyaGetBeanCallback);
    }
}


public void pushRegDev(String aliasId, String pushProvider){
TuyaHomeSdk.getPushInstance().registerDevice( aliasId, pushProvider, new IResultCallback() {
  @Override
  public void onError(String code, String error) {
    Log.d(TAG, "regDeviceErr" + error);
  }
  @Override
  public void onSuccess() {
    Log.d(TAG, "regDeviceSucc");
  }
});
}

public void getMessageList( CallbackContext callbackContext ){
TuyaHomeSdk.getMessageInstance().getMessageList(new ITuyaDataCallback<List<MessageBean>>() {
  @Override
  public void onSuccess(List<MessageBean> result) {
    Log.d(TAG, "messageSucc"+ JSON.toJSONString(result));
    try{
      JSONArray jsonArray = new JSONArray();
    for (MessageBean msgbean : result) {
      JSONObject msgJson = new JSONObject();
      
          //msgJson.put("messages", msgbean);
           msgJson.put("time", msgbean.getDateTime());
           msgJson.put("msg", msgbean.getMsgTypeContent());
           Log.d(TAG,"messages  "+msgJson);
          jsonArray.put(msgJson);
    }
    callbackContext.success(jsonArray);
    }catch(JSONException e){}

     //callbackContext.success(JSON.toJSONString(result));
  }
  @Override
  public void onError(String errorCode, String errorMessage) {
    Log.d(TAG, "messageErr"+ errorMessage);
  }
});
}
public void createGroup(String productId, String name, List selectedDeviceIds){
  TuyaHomeSdk.newHomeInstance(homeId).createGroup(productId, name, selectedDeviceIds, 
        new ITuyaResultCallback<Long>() {
            @Override
            public void onSuccess(Long groupId) {
                    //return groupId
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
            }
        });
}

public void getGroup(long groupId, String productId){
  TuyaHomeSdk.newHomeInstance(homeId).queryDeviceListToAddGroup(groupId, productId, 
        new ITuyaResultCallback<List<GroupDeviceBean>>() {
               @Override
               public void onSuccess(List<GroupDeviceBean> arrayList) {
               }

               @Override
               public void onError(String errorCode, String errorMsg) {
               }
        });
}


public void queryPlayBackdata(int year,int month,int day){
   cameraP2P.queryRecordTimeSliceByDay(year, month, day, new OperationDelegateCallBack() {
    @Override
    public void onSuccess(int sessionId, int requestId, String data) {                    
    }
  
    @Override
    public void onFailure(int sessionId, int requestId, int errCode) {
      
    }
  });
}

public void playback(String deviceId){
 // checkStatus(deviceId);
  TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
    @Override
    public void onSuccess(HomeBean bean) {
      Log.d(TAG, " home-detail: "+bean );
      activity = cordovaInterface.getActivity();
        List<DeviceBean> deviceList = bean.getDeviceList();
        for (DeviceBean devbean : deviceList) {
          if (!devbean.getIsOnline()) {
            activity = cordovaInterface.getActivity();
         Toast.makeText(activity, "Device is Offline", Toast.LENGTH_LONG).show();
            //showDevIsNotOnlineTip(deviceBean);
            return;
//            Log.d(TAG,"device Status offline");
        }
        else{
          devIdList=devbean.getDevId();
          if(deviceId.equals(devbean.getDevId())){
            gotoDeviceCommonActivity2(devbean);
           }
          }
        }
      }
    @Override
    public void onError(String errorCode, String errorMsg) {
      Log.d(TAG, "home-detailERR: "+errorMsg );
      activity = cordovaInterface.getActivity();
    }
});
}

public void addTuyaTimer(CallbackContext callbackContext){

}

public void checkStatus(String deviceId){
  mTuyaDevice= TuyaHomeSdk.newDeviceInstance(deviceId);
  // mTuyaDevice.registerDevListener(new IDevListener() {
  //   @Override
  //   void onStatusChanged(String deviceId, boolean online){
  //     Log.d(TAG,"status"+online);
  //   }
  //   @Override
  //   void onNetworkStatusChanged(String deviceId, boolean status){
  //      Log.d(TAG,"netwok changed "+status);
  //   }
  // });
}

public void getAllScene(long homeId1, CallbackContext callbackContext){
  TuyaHomeSdk.getSceneManagerInstance().getSceneList(homeId1, new ITuyaResultCallback<List<SceneBean>>() {
    @Override
    public void onSuccess(List<SceneBean> result) {
      Log.d(TAG, "onSuccess: " + homeId1);
      Log.d(TAG, "onSuccess: " + result);
      
      try{
        JSONArray jsonArray = new JSONArray();
      for (SceneBean sceneBean : result) {
        JSONObject sceneJson = new JSONObject();
        sceneJson.put("SceneId", JSON.toJSONString(sceneBean.getId()));
            jsonArray.put(sceneJson);
      }
      callbackContext.success(jsonArray);
      }catch(JSONException e){}
    }
 @Override
    public void onError(String errorCode, String errorMessage) {
    }
});
}

private void getConditionList(CallbackContext callbackContext) {
  TuyaHomeSdk.getSceneManagerInstance().getConditionList(false, new ITuyaResultCallback<List<ConditionListBean>>() {
      @Override
      public void onSuccess(List<ConditionListBean> conditionActionBeans) {
          Log.d(TAG, "onSuccess: " + conditionActionBeans);
          callbackContext.success(JSON.toJSONString(conditionActionBeans));
      }
      @Override
      public void onError(String errorCode, String errorMessage) {
      }
  });
}

public void createTempCondition(String longitude,String latitude,  CallbackContext callbackContext){
  Log.d(TAG,"long"+longitude);
  Log.d(TAG,"lat"+latitude);
TuyaHomeSdk.getSceneManagerInstance().getCityByLatLng(
    longitude, //Longitude
    latitude,   //Latitude
    new ITuyaResultCallback<PlaceFacadeBean>() {
        @Override
        public void onSuccess(PlaceFacadeBean placeFacadeBean) {
          
          ValueRule tempRule = ValueRule.newInstance(
    "temp", 
    ">",    
    20       
);
    sceneCondition = SceneCondition.createWeatherCondition(
     placeFacadeBean,   
    "temp",            
    tempRule          
);
callbackContext.success("Created weather condition");
        }

        @Override
        public void onError(String errorCode, String errorMessage) {
        }
});
}
public void createDevCondition(long homeId, CallbackContext callbackContext){
  BoolRule boolRule = BoolRule.newInstance(
    "dp",   
    true   
);
  TuyaHomeSdk.getSceneManagerInstance().getConditionDevList(homeId ,new ITuyaResultCallback<List<DeviceBean>>() {
    @Override
    public void onSuccess(List<DeviceBean> deviceBeans) {
      for (DeviceBean devbean : deviceBeans) {
        sceneCondition = SceneCondition.createDevCondition(
          devbean,   
    "1",       
    boolRule    
);
callbackContext.success("Created Device Condition");
    }
       }
 @Override
    public void onError(String errorCode, String errorMessage) {
    }
});
}

public void createTimingCondition(CallbackContext callbackContext){
  TimerRule timerRule = TimerRule.newInstance("Asia/Calcutta","0111110","16:00","20201212");
  sceneCondition=SceneCondition.createTimerCondition(
      "Monday, Tuesday, Wednesday, Thursday, Friday",
      "Working day timing",
      "timer",
      timerRule
      );
   callbackContext.success("Created Timing Type Condition");
}
public void getDeviceConditionOperationList(String deviceId,CallbackContext callbackContext){
 }
public void getBackground(){
  TuyaHomeSdk.getSceneManagerInstance().getSceneBgs(new ITuyaResultCallback<ArrayList<String>>() {
    @Override
    public void onSuccess(ArrayList<String> strings) {
      background= strings.get(0);
    }

    @Override
    public void onError(String s, String s1) {

    }
});
}
 public void createSceneTask(String deviceId){
  Log.d(TAG,"devvbb"+deviceId);
  HashMap<String, Object> taskMap = new HashMap<>();
taskMap.put("1", true); //Turn on the device
sceneTask =TuyaHomeSdk.getSceneManagerInstance().createDpTask(deviceId, taskMap);
//       devId,      //Device id
//       taskMap     //Device actionpublic
// );
Log.d(TAG,"devvbb"+sceneTask);
}
public void createScene(long homeId,String deviceId,CallbackContext callbackContext){
  List<SceneCondition> sceneList = new ArrayList<SceneCondition>();
 sceneList.add(sceneCondition);
List<SceneTask> taskList= new ArrayList<SceneTask>();
taskList.add(sceneTask);
this.getBackground();
//  List<SceneCondition> sceneList =Arrays.asList(sceneCondition);
//  List<SceneTask> taskList =Arrays.asList(sceneTask);
 Log.d(TAG, "onSuccess: " + sceneList+"        "+taskList);
   TuyaHomeSdk.getSceneManagerInstance().createScene(
       homeId,
      "Morning", //scene name
      background,
     sceneList, //condition  
     taskList,
      0, //Execution condition type
      new ITuyaResultCallback<SceneBean>() {
          @Override
          public void onSuccess(SceneBean sceneBean) {
              callbackContext.success("Created Scene");
          }
  
          @Override
          public void onError(String errorCode, String errorMessage) {
            callbackContext.error("failed");
          }
  });
}
public void modifyScene(long homeId,CallbackContext callbackContext){
  TuyaHomeSdk.getSceneManagerInstance().getSceneList(homeId, new ITuyaResultCallback<List<SceneBean>>() {
    @Override
    public void onSuccess(List<SceneBean> sceneBeans) {
        Log.d(TAG, "onSuccess: " + JSON.toJSONString(sceneBeans));
        SceneBean sceneBean = sceneBeans.get(0);
        TuyaHomeSdk.newSceneInstance(sceneBean.getId())
                .modifyScene(sceneBean, new ITuyaResultCallback<SceneBean>() {
                    @Override
                    public void onSuccess(SceneBean sceneBean) {
                        //L.d("modifyScene", "onSuccess: " + JSON.toJSONString(sceneBean));
                        callbackContext.success("Modified Scene");
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        //L.e("modifyScene", "error" + errorCode + ":" + errorMessage);
                        callbackContext.error("failed");
                    }
                });
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
    }
});
 }
public void deleteScene(long homeId,CallbackContext callbackContext){
  TuyaHomeSdk.getSceneManagerInstance().getSceneList(homeId, new ITuyaResultCallback<List<SceneBean>>() {
    @Override
    public void onSuccess(List<SceneBean> sceneBeans) {
        Log.d(TAG, "onSuccess: " + JSON.toJSONString(sceneBeans));
        SceneBean sceneBean = sceneBeans.get(0);
        TuyaHomeSdk.newSceneInstance(sceneBean.getId()).deleteScene(new 
  IResultCallback() {
      @Override
      public void onSuccess() {
          callbackContext.success( "Delete Scene Success");
      }
  
      @Override
      public void onError(String errorCode, String errorMessage) {
      }
  });
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
    }
});
}
public void executeScene(long homeId,CallbackContext callbackContext){
  TuyaHomeSdk.getSceneManagerInstance().getSceneList(homeId, new ITuyaResultCallback<List<SceneBean>>() {
    @Override
    public void onSuccess(List<SceneBean> sceneBeans) {
        Log.d(TAG, "onSuccess: " + JSON.toJSONString(sceneBeans));
        SceneBean sceneBean = sceneBeans.get(0);
        TuyaHomeSdk.newSceneInstance(sceneBean.getId()).executeScene(new IResultCallback() {
          @Override
          public void onSuccess() {
              callbackContext.success("Excute Scene Success");
          }
      
          @Override
          public void onError(String errorCode, String errorMessage) {
          }
      });
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
    }
});
}
public void getDeviceTaskOperationList(String deviceId,CallbackContext callbackContext){
TuyaHomeSdk.getSceneManagerInstance().getDeviceTaskOperationList(
  deviceId, //device id
    new ITuyaResultCallback<List<TaskListBean>>() {
        @Override
        public void onSuccess(List<TaskListBean> conditionActionBeans) {
          callbackContext.success(JSON.toJSONString(conditionActionBeans));

        }

        @Override
        public void onError(String errorCode, String errorMessage) {
        }
});
}


public void playbackStatus(CallbackContext callbackContext,int num, String deviceId){
  mDeviceControl = TuyaCameraDeviceControlSDK.getCameraDeviceInstance(deviceId);
  switch (num){
  case 1: 
   if (mDeviceControl.isSupportCameraDps(DpSDStatus.ID)) {
    mDeviceControl.registorTuyaCameraDeviceControlCallback(DpSDStatus.ID, new ITuyaCameraDeviceControlCallback<Integer>() {
        @Override
        public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Integer o) {
         showResult(o);
        }
      
        @Override
        public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {
      
        }
      });
      mDeviceControl.publishCameraDps(DpSDStatus.ID, null);
    }
    break;
    case 2:
    if (mDeviceControl.isSupportCameraDps(DpSDFormat.ID)) {
      Log.d(TAG,"1");
      mDeviceControl.registorTuyaCameraDeviceControlCallback(DpSDFormat.ID, new ITuyaCameraDeviceControlCallback<Boolean>() {
          @Override
          public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Boolean o) {
            Log.d(TAG,"1");
          }

          @Override
          public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

          }
      });
      mDeviceControl.publishCameraDps(DpSDFormat.ID, true);
  }
break;
case 3:
if (mDeviceControl.isSupportCameraDps(DpSDFormatStatus.ID)) {
  Log.d(TAG,"2");
  mDeviceControl.registorTuyaCameraDeviceControlCallback(DpSDFormatStatus.ID, new ITuyaCameraDeviceControlCallback<Integer>() {
      @Override
      public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Integer o) {
        Log.d(TAG,"2");
      }

      @Override
      public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

      }
  });
  mDeviceControl.publishCameraDps(DpSDFormatStatus.ID, null);
}
break;
case 4:
if (mDeviceControl.isSupportCameraDps(DpSDRecordModel.ID)) {
  Log.d(TAG,"3"); 
  String o = mDeviceControl.queryStringCurrentCameraDps(DpSDRecordModel.ID);
  
  mDeviceControl.registorTuyaCameraDeviceControlCallback(DpSDRecordModel.ID, new ITuyaCameraDeviceControlCallback<String>() {
      @Override
      public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String o) {
        Log.d(TAG,"3"); 
      }

      @Override
      public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

      }
  });
  mDeviceControl.publishCameraDps(DpSDRecordModel.ID, RecordMode.EVENT.getDpValue());
}
break;
case 5:
if (mDeviceControl.isSupportCameraDps(DpSDStorage.ID)) {
  Log.d(TAG,"4");
  String o = mDeviceControl.queryStringCurrentCameraDps(DpSDStorage.ID);
  mDeviceControl.registorTuyaCameraDeviceControlCallback(DpSDStorage.ID, new ITuyaCameraDeviceControlCallback<String>() {
      @Override
      public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String o) {
        Log.d(TAG,"4");
      }

      public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

      }
  });
  mDeviceControl.publishCameraDps(DpSDStorage.ID, null);
}
break;
case 6:
if (mDeviceControl.isSupportCameraDps(DpSDRecordSwitch.ID)) {
  Log.d(TAG,"5"); 
  boolean o = mDeviceControl.queryBooleanCameraDps(DpSDRecordSwitch.ID);
  mDeviceControl.registorTuyaCameraDeviceControlCallback(DpSDRecordSwitch.ID, new ITuyaCameraDeviceControlCallback<Boolean>() {
      @Override
      public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Boolean o) {
        Log.d(TAG,"5"); 
      }

      @Override
      public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

      }
  });
  mDeviceControl.publishCameraDps(DpSDRecordSwitch.ID, true);
}
break;
}
}
private void showResult(int O){
  Context context=this.cordova.getActivity().getApplicationContext();
  if(O==1){
    Toast.makeText(context,"Normal",Toast.LENGTH_SHORT).show();   
  }
 else if(O==2){
    Toast.makeText(context,"Abnormal(Broken/Wrong Format)",Toast.LENGTH_SHORT).show(); 
  }
 else if(O==3){
    Toast.makeText(context,"Insufficient Space",Toast.LENGTH_SHORT).show(); 
  }
  else if(O==4){
    Toast.makeText(context,"Formatting",Toast.LENGTH_SHORT).show(); 
  }
  else{
    Toast.makeText(context,"No SD Card",Toast.LENGTH_SHORT).show(); 
  }
}

private void cloudSer(String deviceId,String homeId){
  Context context=this.cordova.getActivity().getApplicationContext();
  Intent intent = new Intent(context, CameraCloud.class);
   String P2PType= String.valueOf(p2pType);
Bundle bundle = new Bundle();
bundle.putString("deviceId",deviceId);
bundle.putString("P2PType",P2PType);
bundle.putString("homeId",homeId);
intent.putExtras(bundle);
this.cordova.getActivity().startActivity(intent);
    Log.d(TAG,"new intent "+intent);
  // cameraCloudSDK = new CameraCloudSDK();
  // cameraCloudSDK.getCameraCloudInfo(TuyaHomeSdk.getDataInstance().getDeviceBean(devId),cloudCallback);
}



public void logout(){
  TuyaHomeSdk.getUserInstance().logout(new ILogoutCallback() {
    @Override
    public void onSuccess () {
      if (homeCamera != null) {
        homeCamera.unRegisterCameraPushListener(mTuyaGetBeanCallback);
    }
    homeCamera = null;
      // Sign out successfully
    }
  
    @Override
    public void onError (String errorCode, String errorMsg) {
    }
  });
}
 public void createTempPassword(String devId,String name,String password,long startTime,long endTime,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  lockfunction.createTempPassword(callbackContext,name, password, startTime, endTime,devId,tuyaLockManager);
 }
 public void getTempPassword(String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  lockfunction.getTempPassword(callbackContext,devId,tuyaLockManager);
 }
 public void deleteTempPassword(String devId,int passId){
  LockFunctions lockfunction= new LockFunctions();
  lockfunction.deleteTempPassword(passId,devId,tuyaLockManager);
 }
 public void createDynamicPassword(String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.getDynamicPassword(callbackContext,devId,tuyaLockManager);
 }
 public void getLockUser(String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.getLockUser(devId,tuyaLockManager,callbackContext);
 }
 public void addLockUser(String name,String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.createLockUser(name,devId, tuyaLockManager);
 }
 public void updateLockUser(String userId,String name,String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.updateLockUser( userId, name, devId, tuyaLockManager);
 }
 public void updateFamilyUserUnlockMode(String userId,String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.updateLockFamilyMember( userId, devId, tuyaLockManager);
 }
 public void deleteLockUser(String userId,String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.deleteLockUser(userId, devId, tuyaLockManager);
 }
 public void getRecords(String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.getRecords(devId,tuyaLockManager,callbackContext);
 }
 public void getUnlockRecords(String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.getUnlockRecords(devId,tuyaLockManager,callbackContext);
 }
 public void getHijackRecords(String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.getHijackRecords(devId, tuyaLockManager,callbackContext);
 }
 public void setHijackingConfig(String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.setHijackingConfig(devId, tuyaLockManager);
 }
 public void removeHijackingConfig(String devId,CallbackContext callbackContext){
  LockFunctions lockfunction= new LockFunctions();
  Log.d(TAG,"get lockfunction"+lockfunction);
  lockfunction.removeHijackingConfig(devId,tuyaLockManager);
 }
}


