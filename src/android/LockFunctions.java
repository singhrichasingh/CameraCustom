package cordova.plugin.cameracustom;

import android.content.Context;
import com.tuya.smart.sdk.api.ITuyaActivator;
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;
import java.util.ArrayList;
import java.io.File;
import android.os.Environment;
import android.content.Context;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;    
import java.util.Date; 
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.home.sdk.builder.ActivatorBuilder;
import com.tuya.smart.sdk.enums.ActivatorModelEnum;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.optimus.lock.api.ITuyaLockManager;
import com.tuya.smart.optimus.lock.api.ITuyaWifiLock;
import com.tuya.smart.optimus.lock.api.bean.WifiLockUser;
import com.tuya.smart.optimus.lock.api.bean.UnlockRelation;
import com.tuya.smart.optimus.lock.api.TuyaUnlockType;
import com.tuya.smart.optimus.lock.api.TempPasswordBuilder;
import com.tuya.smart.optimus.lock.api.bean.TempPassword;
import com.tuya.smart.optimus.lock.api.callback.RemoteUnlockListener;
import com.tuya.smart.optimus.lock.api.bean.Record;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import static com.tuya.smart.sdk.enums.ActivatorModelEnum.TY_EZ;
import com.tuya.smart.sdk.enums.ActivatorEZStepCode;
import java.text.SimpleDateFormat;
import org.apache.cordova.CallbackContext;

public class LockFunctions {
  public String TAG ="Lock functions";
   private String devId;
   private ITuyaLockManager tuyaLockMng;
   private ITuyaWifiLock tuyaLockDevice;
   private ActivatorModelEnum mModelEnum;
   private int second=5;
   private boolean lockResult=false;
  private ArrayList<UnlockRelation> unlockRelations = new ArrayList<>();

    protected void startRemoteUnlock(String devId,ITuyaLockManager tuyaLockManager,CallbackContext callbackContext) {
        Log.i(TAG, "inithere");
        tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
        Log.i(TAG, "inithere"+tuyaLockDevice);
         tuyaLockDevice.setRemoteUnlockListener(new RemoteUnlockListener() {
            @Override
            public void onReceive(String devId, int second) {
                Log.i(TAG, "inithere"+devId);
                // if (second == 0) {
                //     Log.i(TAG, "remote unlock request onReceive");
                //     callbackContext.success("Time Out");
                //  }
                //  else 
                if(second !=0){
                    callbackContext.success("RemoteUnlock");
                 }
            }
        });
    }
     
    public void replyRemoteUnlockRequest(boolean allow,String devId,ITuyaLockManager tuyaLockManager,Context context) {
        tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
        tuyaLockDevice.replyRemoteUnlock(allow, new ITuyaResultCallback<Boolean>() {
            @Override
            public void onError(String code, String message) {
                Log.e(TAG, "reply remote unlock failed: code = " + code + "  message = " + message);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
    
            @Override
            public void onSuccess(Boolean result) {
                Toast.makeText(context,"remote unlock success", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "reply remote unlock success");
            }
        });
    }




    public void init(String devId,ITuyaLockManager tuyaLockManager){
        tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
        
    }
    
   public void getLockUser(String devId,ITuyaLockManager tuyaLockManager,CallbackContext callbackContext){
    tuyaLockMng=tuyaLockManager;
    tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    tuyaLockDevice.getLockUsers(new ITuyaResultCallback<List<WifiLockUser>>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "get lock users failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(List<WifiLockUser> wifiLockUser) {
            try{
                JSONArray jsonArray = new JSONArray();
              for (WifiLockUser lockUser : wifiLockUser) {
                JSONObject lockUserList = new JSONObject();
                    //Log.d(TAG,"device "+devbean);
                    lockUserList.put("userName",lockUser.userName);
                    lockUserList.put("id",lockUser.userId);
                   String userType= getUserType(lockUser.userType);
                    lockUserList.put("userType",userType);
                    lockUserList.put("userIcon",lockUser.avatarUrl);
                   jsonArray.put(lockUserList);
                   //Log.d(TAG,"device "+jsonArray);
              }
              callbackContext.success(jsonArray);
              }catch(JSONException e){}
            Log.i(TAG, "get lock users success: wifiLockUser = " + wifiLockUser);
        }
    });
   } 
  private String getUserType(int type){
       if(type==1){
         return "Family Member";
       }
       else{
         return "Non Family Member";
       }
   }

  public void createLockUser(String name,String devId,ITuyaLockManager tuyaLockManager){
    tuyaLockMng=tuyaLockManager;
    tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    UnlockRelation unlockRelation = new UnlockRelation();
    unlockRelation.unlockType = TuyaUnlockType.PASSWORD;
    unlockRelation.passwordNumber = 1;
    unlockRelations.add(unlockRelation);
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    File avatarFile = new File(path, "1.png");
    tuyaLockDevice.addLockUser(name, avatarFile , unlockRelations, new ITuyaResultCallback<String>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "add lock user failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(String userId) {
            Log.i(TAG, "add lock user success: " + userId);
        }
    });
  } 

  public void updateLockUser(String userId, String name,String devId,ITuyaLockManager tuyaLockManager){
    tuyaLockMng=tuyaLockManager;
    tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    UnlockRelation unlockRelation = new UnlockRelation();
    unlockRelation.unlockType = TuyaUnlockType.PASSWORD;
    unlockRelation.passwordNumber = 345;
    unlockRelations.add(unlockRelation);
    tuyaLockDevice.updateLockUser(userId, name, null, unlockRelations, new ITuyaResultCallback<Boolean>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "update lock user failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(Boolean aBoolean) {
            Log.i(TAG, "update lock user success");
        }
    });
  }

public void updateLockFamilyMember(String userId,String devId,ITuyaLockManager tuyaLockManager){
    tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    UnlockRelation unlockRelation = new UnlockRelation();
    unlockRelation.unlockType = TuyaUnlockType.PASSWORD;
    unlockRelation.passwordNumber = 459;
    unlockRelations.add(unlockRelation);
    tuyaLockDevice.updateFamilyUserUnlockMode("your_family_user_id", unlockRelations, new ITuyaResultCallback<Boolean>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "update family user failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(Boolean aBoolean) {
            Log.i(TAG, "update family user success");
        }
    });
}

 public void deleteLockUser(String userId,String devId,ITuyaLockManager tuyaLockManager){
    tuyaLockMng=tuyaLockManager;
    tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    tuyaLockDevice.deleteLockUser(userId, new ITuyaResultCallback<Boolean>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "delete lock user failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(Boolean result) {
            Log.i(TAG, "delete lock user failed success");
        }
    });
 }

 public void createTempPassword(CallbackContext callbackContext,String name,String password,long startTime,long endTime,String devId,ITuyaLockManager tuyaLockManager){
       tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    TempPasswordBuilder tempPasswordBuilder = new TempPasswordBuilder()
    .name(name)
    .password(password)
    .effectiveTime(startTime)
    .invalidTime(endTime);
tuyaLockDevice.createTempPassword(tempPasswordBuilder, new ITuyaResultCallback<Boolean>() {
@Override
public void onError(String code, String message) {
    callbackContext.error(code);
    Log.e(TAG, "create lock temp password: code = " + code + "  message = " + message);
}

@Override
public void onSuccess(Boolean result) {
    callbackContext.success("Added sucessfully");
    Log.i(TAG, "add temp password success");
}
});
 }

public void getTempPassword(CallbackContext callbackContext,String devId,ITuyaLockManager tuyaLockManager){
    tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    tuyaLockDevice.getTempPasswords(new ITuyaResultCallback<List<TempPassword>>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "get lock temp passwords failed: code = "+ message);
        }
    
        @Override
        public void onSuccess(List<TempPassword> tempPasswords) {
            Log.i(TAG, "get lock temp passwords success: tempPasswords" + tempPasswords); 
            try{
                JSONArray jsonArray = new JSONArray();
              for (TempPassword tempPass : tempPasswords) {
                JSONObject tempList = new JSONObject();
                   String status= tempStatus(tempPass.status);
                    tempList.put("name", tempPass.name);
                    tempList.put("passId", tempPass.id);
                    Timestamp start=new Timestamp(tempPass.effectiveTime);  
                    Date dateStart=new Date(start.getTime());  
                    Timestamp end=new Timestamp(tempPass.invalidTime);  
                    Date dateEnd=new Date(end.getTime());  
                    tempList.put("startTime", dateStart);
                    tempList.put("endTime", dateEnd);
                    tempList.put("status", status);
                   jsonArray.put(tempList);
              }
              callbackContext.success(jsonArray);
              }catch(JSONException e){}
         callbackContext.success("tempassword"+tempPasswords);
        }
    });
}
public String tempStatus(int val){
if(val==0){
return "Removed";
}
else if(val==1){
return "Invalid";
}
else if(val==2){
 return  "To_Be_Publish" ;
}
else if(val==3){
   return  "Working" ;
}
else if(val==4){
  return  "To_Be_Deleted" ;
}
else{
 return "Expired";
}
}

public void deleteTempPassword(int passId,String devId,ITuyaLockManager tuyaLockManager){
    tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    tuyaLockDevice.deleteTempPassword(passId, new ITuyaResultCallback<Boolean>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "delete lock temp password failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(Boolean result) {
            Log.i(TAG, "Deleted Temporary Password");
        }
    });
}

public void getDynamicPassword(CallbackContext callbackContext,String devId,ITuyaLockManager tuyaLockManager){
    Log.i(TAG, "DynamicPassword = "+devId+"   " + callbackContext);
    tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    tuyaLockDevice.getDynamicPassword(new ITuyaResultCallback<String>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "get lock dynamic password failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(String dynamicPassword) {
            Log.i(TAG, "DynamicPassword = " + dynamicPassword);
            callbackContext.success(dynamicPassword);
        }
    });
}

public void getRecords(String devId,ITuyaLockManager tuyaLockManager,CallbackContext callbackContext){
    tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    ArrayList<String> dpCodes = new ArrayList<>();
    dpCodes.add("alarm_lock");
    dpCodes.add("hijack");
    dpCodes.add("doorbell");
    tuyaLockDevice.getRecords(dpCodes, 0, 10, new ITuyaResultCallback<Record>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "get unlock records failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(Record recordBean) {
            Log.i(TAG, "get unlock records success: recordBean = " + recordBean);
            try{
                JSONArray jsonArray = new JSONArray();
              for (Record.DataBean dataBean : recordBean.datas) {
                JSONObject dataList = new JSONObject();
                   String status= dataStatus(dataBean.tags);
                    dataList.put("head", "Lock Records");
                    dataList.put("Id", dataBean.devId);
                    dataList.put("Icon", dataBean.avatarUrl);
                    dataList.put("UserName", dataBean.userName);
                    dataList.put("UserId", dataBean.userId);
                    dataList.put("DpId", dataBean.dpId);
                    dataList.put("UnlockType", dataBean.unlockType);
                    dataList.put("UnlockRelation", dataBean.unlockRelation);
                    dataList.put("Status", status);
                    Timestamp time=new Timestamp(dataBean.createTime);  
                    Date createTime=new Date(time.getTime());   
                    dataList.put("createTime", createTime);
                    
                   jsonArray.put(dataList);
              }
              callbackContext.success(jsonArray);
              }catch(JSONException e){}
        }
    });
}
public String dataStatus(int tags){
if(tags==1){
    return "Hijacked";
}
else{
    return "Not Hijacked";
}
}

public void getUnlockRecords(String devId,ITuyaLockManager tuyaLockManager,CallbackContext callbackContext){
    tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    tuyaLockDevice.getUnlockRecords(0, 10, new ITuyaResultCallback<Record>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "get unlock records failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(Record recordBean) {
            Log.i(TAG, "get unlock records success: recordBean = " + recordBean);
            try{
                JSONArray jsonArray = new JSONArray();
              for (Record.DataBean dataBean : recordBean.datas) {
                JSONObject dataList = new JSONObject();
                   String status= dataStatus(dataBean.tags);
                    dataList.put("head", "Unlock Records");
                    dataList.put("Id", dataBean.devId);
                    dataList.put("Icon", dataBean.avatarUrl);
                    dataList.put("UserName", dataBean.userName);
                    dataList.put("UserId", dataBean.userId);
                    dataList.put("DpId", dataBean.dpId);
                    dataList.put("UnlockType", dataBean.unlockType);
                    dataList.put("UnlockRelation", dataBean.unlockRelation);
                    dataList.put("Status", status);
                    Timestamp time=new Timestamp(dataBean.createTime);  
                    Date createTime=new Date(time.getTime());   
                    dataList.put("createTime", createTime);
                    
                   jsonArray.put(dataList);
              }
              callbackContext.success(jsonArray);
              }catch(JSONException e){}
        }
    });
}

public void getHijackRecords(String devId,ITuyaLockManager tuyaLockManager, CallbackContext callbackContext){
    tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    tuyaLockDevice.getHijackRecords(0, 10, new ITuyaResultCallback<Record>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "get lock hijack records failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(Record hijackingRecord) { 
            Log.i(TAG, "get lock hijack records success: hijackingRecord = " + hijackingRecord);
            try{
                JSONArray jsonArray = new JSONArray();
              for (Record.DataBean dataBean : hijackingRecord.datas) {
                JSONObject dataList = new JSONObject();
                   String status= dataStatus(dataBean.tags);
                    dataList.put("head", "Hijack Records");
                    dataList.put("Id", dataBean.devId);
                    dataList.put("Icon", dataBean.avatarUrl);
                    dataList.put("UserName", dataBean.userName);
                    dataList.put("UserId", dataBean.userId);
                    dataList.put("DpId", dataBean.dpId);
                    dataList.put("UnlockType", dataBean.unlockType);
                    dataList.put("UnlockRelation", dataBean.unlockRelation);
                    dataList.put("Status", status);
                    Timestamp time=new Timestamp(dataBean.createTime);  
                    Date createTime=new Date(time.getTime());   
                    dataList.put("createTime", createTime);
                    
                   jsonArray.put(dataList);
              }
              callbackContext.success(jsonArray);
              }catch(JSONException e){}
        }
    });
}
public void setHijackingConfig(String devId,ITuyaLockManager tuyaLockManager){
    tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    UnlockRelation unlockRelation = new UnlockRelation();
    unlockRelation.unlockType = TuyaUnlockType.PASSWORD;
    unlockRelation.passwordNumber = 54;
    //unlockRelations.add(unlockRelation);
    tuyaLockDevice.setHijackingConfig(unlockRelation, new ITuyaResultCallback<Boolean>() {
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "get lock hijack records failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(Boolean result) {
            Log.i(TAG, "get lock hijack records success: hijackingRecord = " + result);
        }
    });
}

public void removeHijackingConfig(String devId,ITuyaLockManager tuyaLockManager){
    tuyaLockMng=tuyaLockManager;
        tuyaLockDevice = tuyaLockMng.getWifiLock(devId);
    UnlockRelation unlockRelation = new UnlockRelation();
    unlockRelation.unlockType = TuyaUnlockType.PASSWORD;
    unlockRelation.passwordNumber = 1;
    tuyaLockDevice.removeHijackingConfig(unlockRelation, new ITuyaResultCallback<Boolean>(){
        @Override
        public void onError(String code, String message) {
            Log.e(TAG, "get lock hijack records failed: code = " + code + "  message = " + message);
        }
    
        @Override
        public void onSuccess(Boolean result) {
            Log.i(TAG, "get lock hijack records success: hijackingRecord = " + result);
        }
    });
}


 public void addSmartLock(String ssid, String password,String token,Context context,ITuyaLockManager tuyaLockManager){
    Log.d("lockfunc", ssid+"...."+password +" wifitoken: " + token);
    long CONFIG_TIME_OUT=100;
    mModelEnum = TY_EZ;
    Activity activity= new Activity();
 ITuyaActivator mTuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(new ActivatorBuilder()
            .setContext(context)
            .setSsid(ssid)
            .setPassword(password)
            .setActivatorModel(TY_EZ)
            .setTimeOut(CONFIG_TIME_OUT)
            .setToken(token)
            .setListener(new ITuyaSmartActivatorListener() {
            @Override
                  public void onError(String errorCode, String errorMsg) {
                    Log.d("lockfunc", "errMsg:"+errorMsg+"....errCode:"+errorCode);
                    switch (errorCode) {
                        case "1004":
                        Log.d("lockfunc", "errMsg:"+errorMsg+"....errCode:"+errorCode);
                            return;
                    }
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                    Log.d("lockfunc", "errMsg:"+errorMsg+"....errCode:"+errorCode);
                  }
                @Override
                  public void onActiveSuccess(DeviceBean devResp) {
                    Log.d("lockfunc", " wifilock: " + devResp.getDevId());
                    devId=devResp.getDevId();
                    tuyaLockMng=tuyaLockManager;
                     init(devResp.getDevId(),tuyaLockManager);
                  }
                    @Override
                  public void onStep(String step, Object data) {
                    Log.d("lockfunc", " wifilockdata: " + data );
                    switch (step) {
                        case ActivatorEZStepCode.DEVICE_BIND_SUCCESS:
                        Log.d("lockfunc", " wifilockdata: " + data+" "+ 0x08);
                        Toast.makeText(context, "Configuration Success", Toast.LENGTH_SHORT).show();
                            break;
                        case ActivatorEZStepCode.DEVICE_FIND:
                        Log.d("lockfunc", " wifilockdata: " + data+" "+ 0x07);
                            break;
                    }
                  }
              }));
          mTuyaActivator.start(); 

         
 }
}


















