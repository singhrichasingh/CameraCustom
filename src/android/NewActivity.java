package cordova.plugin.cameracustom;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;
import com.tuya.smart.camera.middleware.p2p.ITuyaSmartCameraP2P;
import com.tuya.smart.camera.camerasdk.typlayer.callback.AbsP2pCameraListener;
import com.tuya.smart.camera.middleware.p2p.TuyaSmartCameraP2PFactory;
import com.tuya.smart.camera.middleware.widget.AbsVideoViewCallback;
import com.tuyasmart.camera.devicecontrol.ITuyaCameraDevice;
import com.tuyasmart.camera.devicecontrol.TuyaCameraDeviceControlSDK;
import com.tuya.smart.camera.ipccamerasdk.p2p.ICameraP2P;
import com.tuyasmart.camera.devicecontrol.bean.DpSDStatus;
import com.tuyasmart.camera.devicecontrol.model.DpNotifyModel;
import androidx.appcompat.app.AppCompatActivity;
import com.tuyasmart.camera.devicecontrol.api.ITuyaCameraDeviceControlCallback;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OnRenderDirectionCallback;
import com.tuyasmart.camera.devicecontrol.bean.DpPTZControl;
import com.tuyasmart.camera.devicecontrol.bean.DpPTZStop;
import com.tuyasmart.camera.devicecontrol.model.PTZDirection;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.Manifest;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.util.Log;
import java.nio.ByteBuffer;
import org.apache.cordova.*;
import io.ionic.starter.R;
import android.view.WindowManager;
import android.os.Environment;
import java.io.File;
import com.tuya.smart.camera.utils.AudioUtils;
public class NewActivity extends Activity implements View.OnClickListener {
private  final String TAG = "NewActivity";
 public TuyaCameraView mVideoView;
 public ITuyaSmartCameraP2P cameraP2P;
 private String packageName;
 private int p2pType;
 public String deviceId;
 private ITuyaCameraDevice mDeviceControl;
 private ImageView muteImg;
    private TextView qualityTv;
    private TextView speakTxt, recordTxt, photoTxt, replayTxt, settingTxt, cloudStorageTxt,messageCenterTxt;
    private boolean isSpeaking=false;
    private boolean isPlay = false;
    private boolean isRecording = false;
    private int previewMute = ICameraP2P.MUTE;
    private int videoClarity = ICameraP2P.HD;
    private String picPath, videoPath;
    private static final int ASPECT_RATIO_WIDTH = 9;
    private static final int ASPECT_RATIO_HEIGHT = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         packageName = getApplication().getPackageName();
        setContentView(getApplication().getResources().getIdentifier("camera_panel", "layout", packageName));
        initView();
        initData();
        if(mDeviceControl != null && mDeviceControl.isSupportCameraDps(DpPTZControl.ID)) {
            mVideoView.setOnRenderDirectionCallback(new OnRenderDirectionCallback() {

                @Override
                public void onLeft() {
                    mDeviceControl.publishCameraDps(DpPTZControl.ID, PTZDirection.LEFT.getDpValue());
                }

                @Override
                public void onRight() {
                    mDeviceControl.publishCameraDps(DpPTZControl.ID,PTZDirection.RIGHT.getDpValue());

                }

                @Override
                public void onUp() {
                    mDeviceControl.publishCameraDps(DpPTZControl.ID,PTZDirection.UP.getDpValue());

                }

                @Override
                public void onDown() {
                    mDeviceControl.publishCameraDps(DpPTZControl.ID,PTZDirection.DOWN.getDpValue());

                }

                @Override
                public void onCancel() {
                    mDeviceControl.publishCameraDps(DpPTZStop.ID,true);

                }
            });
        }
        //initListener();
    }
   
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    handleClarity(msg);
                    break;
                case 10:
                    handleMute(msg);
                    break;
            }
        }


    private void handleClarity(Message msg) {
        if (msg.arg1 == 0) {
            qualityTv.setText(videoClarity == ICameraP2P.HD ? "HD" : "SD");
        }
    }
    private void handleMute(Message msg) {
        if (msg.arg1 ==0) {
            muteImg.setSelected(previewMute == ICameraP2P.MUTE);
        }
    }

    public void initView(){
        mVideoView =(TuyaCameraView) findViewById(R.id.camera_video_view);
        muteImg = findViewById(R.id.camera_mute);
        qualityTv = findViewById(R.id.camera_quality);
        speakTxt = findViewById(R.id.speak_Txt);
         recordTxt = findViewById(R.id.record_Txt);
        photoTxt = findViewById(R.id.photo_Txt);
        //settingTxt = findViewById(R.id.setting_Txt);
        // cloudStorageTxt = findViewById(R.id.cloud_Txt);
         //messageCenterTxt =  findViewById(R.id.message_center_Txt);
         muteImg.setOnClickListener(this);
         qualityTv.setOnClickListener(this);
         speakTxt.setOnClickListener(this);
        recordTxt.setOnClickListener(this);
         photoTxt.setOnClickListener(this);
         //cloudStorageTxt.setOnClickListener(this);
         //messageCenterTxt.setOnClickListener(this);
        
         WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
         int width = windowManager.getDefaultDisplay().getWidth();
         int height = width * ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT;
         RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
         layoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar_view);
         findViewById(R.id.camera_video_view_Rl).setLayoutParams(layoutParams);

        muteImg.setSelected(true);
    }
    
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_mute:
                muteClick();
                break;
            case R.id.camera_quality:
                setVideoClarity();
                break;
            case R.id.speak_Txt:
                speakClick();
                break;
            case R.id.record_Txt:
                recordClick();
                break;
            case R.id.photo_Txt:
                snapShotClick();
                break;
        }
    }
 public void  initData(){
Bundle bundle = getIntent().getExtras();
 deviceId = bundle.getString("deviceId");
 p2pType= Integer.parseInt(bundle.getString("P2PType")); 
cameraP2P = TuyaSmartCameraP2PFactory.createCameraP2P(p2pType, deviceId);
 mVideoView.setViewCallback(new AbsVideoViewCallback() {
  @Override
  public void onCreated(Object o) {
      super.onCreated(o);
       if (null != cameraP2P){
        Log.d(TAG,"set view "+o);
         cameraP2P.generateCameraView(o);
        }
  }
  @Override
  public void videoViewClick() {
      Log.d(TAG,"video Clicked");
}
@Override
public void startCameraMove(int var1) {
    Log.d(TAG,"video move");
}
@Override
public void onActionUP() {
    Log.d(TAG,"video action up");
}
});
mVideoView.createVideoView(p2pType);
if (null != cameraP2P) {
  cameraP2P.registerP2PCameraListener(p2pCameraListener);
cameraP2P.generateCameraView(mVideoView.createdView());
  mDeviceControl = TuyaCameraDeviceControlSDK.getCameraDeviceInstance(deviceId);
  startCamera(deviceId);
}
}

private AbsP2pCameraListener p2pCameraListener = new AbsP2pCameraListener() {
        @Override
        public void onReceiveSpeakerEchoData(ByteBuffer pcm, int sampleRate) {
          Log.d(TAG, "receiveSpeakerEchoData pcmlength " + " sampleRate " + sampleRate);
            if (null != cameraP2P){
                int length = pcm.capacity();
                Log.d(TAG, "receiveSpeakerEchoData pcmlength " + length + " sampleRate " + sampleRate);
                byte[] pcmData = new byte[length];
                pcm.get(pcmData, 0, length);
                cameraP2P.sendAudioTalkData(pcmData,length);
            }
        }
        @Override
              public void onReceiveFrameYUVData(int i, ByteBuffer byteBuffer, ByteBuffer byteBuffer1, ByteBuffer byteBuffer2, int i1, int i2, int i3, int i4, long l, long l1, long l2, Object o) {
                  super.onReceiveFrameYUVData(i, byteBuffer, byteBuffer1, byteBuffer2, i1, i2, i3, i4, l, l1, l2, o);
                  Log.d(TAG, "yuv data    " + byteBuffer + " sampleRate " + byteBuffer2);
              }
              @Override
              public void onSessionStatusChanged(Object o, int i, int i1) {
                super.onSessionStatusChanged(o, i, i1);
                Log.d(TAG,"session status changed  "+o);
                if(i1==-3){
                    Toast.makeText(getApplicationContext(), "Connection time out" , Toast.LENGTH_SHORT).show();
                }
                else if(i1==-12){
                    Toast.makeText(getApplicationContext(), "Connection closed by the device" , Toast.LENGTH_SHORT).show();
                }
                else if(i1==-13){
                    Toast.makeText(getApplicationContext(), "Connection closed without response timeout" , Toast.LENGTH_SHORT).show();
                }
                //call back when the connection status changes
            }
      };

      private void startCamera(String deviceId ){
        Log.d(TAG, "my dev id "+cameraP2P.isConnecting());
        if (cameraP2P.isConnecting()){
          cameraP2P.startPreview(new OperationDelegateCallBack() {
              @Override
              public void onSuccess(int sessionId, int requestId, String data) {
                  Log.d(TAG,"start preview  "+data);
                  isPlay=true;
              }
        
              @Override
              public void onFailure(int sessionId, int requestId, int errCode) {
                  Log.d(TAG, "start preview onFailure, errCode: " + errCode);
              }
          });
        }
      if (!cameraP2P.isConnecting()) {
        cameraP2P.connect(deviceId, new OperationDelegateCallBack() {
              @Override
              public void onSuccess(int i, int i1, String s) {
                  Log.d(TAG,"connect val "+ s);
                  startPreview();
              }
      
              @Override
              public void onFailure(int i, int i1, int i2) {
                Log.d(TAG, "connect onFailure, errCode: " + i2);
              }
          });
      }
      }

      public void startPreview(){
        cameraP2P.startPreview(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                Log.d(TAG,"start preview  "+data);
                isPlay = true;
            }
      
            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                Log.d(TAG, "start preview onFailure, errCode: " + errCode);
                isPlay = false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();
        if (null != cameraP2P) {
            AudioUtils.getModel(this);
            cameraP2P.registorOnP2PCameraListener(p2pCameraListener);
            cameraP2P.generateCameraView(mVideoView.createdView());
            if (cameraP2P.isConnecting()) {
                cameraP2P.startPreview(new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        Log.d(TAG, "start preview part resume 1, errCode: " +data);
                        isPlay = true;
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {
                        Log.d(TAG, "start preview onFailure, errCode: " + errCode);
                    }
                });
            }
            if (!cameraP2P.isConnecting()) {
                cameraP2P.connect(deviceId, new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int i, int i1, String s) {
                        Log.d(TAG, "start connect success: " + i);
                        startPreview();
                    }

                    @Override
                    public void onFailure(int i, int i1, int i2) {
                        Log.d(TAG, "start connect onFailure, errCode: " + i1);
                    }
                });
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onPause();
        if (isSpeaking) {
            cameraP2P.stopAudioTalk(null);
        }
        if (isPlay) {
            cameraP2P.stopPreview(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int sessionId, int requestId, String data) {

                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {

                }
            });
            isPlay = false;
        }
        if (null != cameraP2P) {
              cameraP2P.disconnect(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int i, int i1, String s) {

                }

                @Override
                public void onFailure(int i, int i1, int i2) {

                }
            });
        }
        AudioUtils.changeToNomal(this);
    }

    private void speakClick() {
        if (isSpeaking){
            cameraP2P.stopAudioTalk(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
                    Log.d(TAG,"speak stop "+data);
                    isSpeaking = false;
                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {
                    isSpeaking = false;
                    Log.d(TAG,"speak stop ERR "+errCode);
                }
            });
        } else {
            if (ContextCompat.checkSelfPermission(NewActivity.this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                cameraP2P.startAudioTalk(new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        isSpeaking = true;
                        Log.d(TAG,"speak start "+data);
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {
                        isSpeaking = false;
                        Log.d(TAG,"speak start Err "+errCode);
                    }
                });
            } else {
                ActivityCompat.requestPermissions(NewActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},11);
            }
        }
    }
    // public void getVideoClarity(){
    //     mCameraP2P.getVideoClarity(new OperationDelegateCallBack() {
    //         ​    @Override
    //         ​    public void onSuccess(int sessionId, int requestId, String data) {
            
    //         ​    }
            
    //         ​    @Override
    //         ​    public void onFailure(int sessionId, int requestId, int errCode) {
            
    //         ​    }
    //         });
    // }
    public void setVideoClarity(){
        cameraP2P.setVideoClarity(videoClarity == ICameraP2P.HD ? ICameraP2P.STANDEND : ICameraP2P.HD, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                videoClarity = Integer.valueOf(data);
                Log.d(TAG,"video quality "+data);
                Message msg=new Message();
                msg.what=11;
                msg.arg1=0;
                handleMessage(msg);
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
               Log.d(TAG,"video quality ERR"+errCode);
            }
        });
    }
 
  public void muteClick(){
        int mute;
    mute = previewMute == ICameraP2P.MUTE ? ICameraP2P.UNMUTE : ICameraP2P.MUTE;
    cameraP2P.setMute(ICameraP2P.PLAYMODE.LIVE, mute, new OperationDelegateCallBack() {
        @Override
        public void onSuccess(int sessionId, int requestId, String data) {
            previewMute = Integer.valueOf(data);
            Log.d(TAG,"mute live "+data);
            Message msg=new Message();
            msg.what=10;
            msg.arg1=0;
            handleMessage(msg);
        }

        @Override
        public void onFailure(int sessionId, int requestId, int errCode) {
            Log.d(TAG,"mute live ERR"+errCode);
        }
    });
  }  
 
  public void recordClick(){
    if (!isRecording) {
        if (ContextCompat.checkSelfPermission(NewActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String picPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Camera/";
            File file = new File(picPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".mp4";
            videoPath = picPath + fileName;
            cameraP2P.startRecordLocalMp4(picPath, NewActivity.this, new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
                    isRecording = true;
                    Log.d(TAG,"record start "+data);

                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {
                    Log.d(TAG,"record start err "+errCode);
                }
            });
            recordStatue(true);
        } else {
            ActivityCompat.requestPermissions(NewActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
        }
    } else {
        cameraP2P.stopRecordLocalMp4(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                isRecording = false;
                Log.d(TAG,"record stop "+data);
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                isRecording = false;
                Log.d(TAG,"record stop err "+errCode);
            }
        });
        recordStatue(false);
    }
  }

  private void recordStatue(boolean isRecording) {
     speakTxt.setEnabled(!isRecording);
     photoTxt.setEnabled(!isRecording);
    // replayTxt.setEnabled(!isRecording);
    recordTxt.setEnabled(true);
    recordTxt.setSelected(isRecording);
}

  public void snapShotClick(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Camera/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        picPath = path;
    }
    cameraP2P.snapshot(picPath, NewActivity.this, ICameraP2P.PLAYMODE.LIVE, new OperationDelegateCallBack() {
        @Override
        public void onSuccess(int sessionId, int requestId, String data) {
            Log.d(TAG,"snapSot live "+data);
        }

        @Override
        public void onFailure(int sessionId, int requestId, int errCode) {
            Log.d(TAG,"snapshot live err  "+errCode);
        }
    });
}

@Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != cameraP2P) {
            //cameraP2P.removeOnP2PCameraListener();
            cameraP2P.destroyP2P();
        }
    }

}