package cordova.plugin.cameracustom;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import java.nio.ByteBuffer;
import io.ionic.starter.R;
import com.tuya.smart.camera.camerasdk.typlayer.callback.IRegistorIOTCListener;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationCallBack;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import com.tuya.smart.camera.ipccamerasdk.msgvideo.ITYCloudVideo;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OnP2PCameraListener;
import com.tuya.smart.camera.ipccamerasdk.msgvideo.TYCloudVideoPlayer;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;

public class CloudVideoActivity extends AppCompatActivity {

    private final int OPERATE_SUCCESS = 1;
    private final int OPERATE_FAIL = 0;
    private final int MSG_CLOUD_VIDEO_DEVICE = 1000;

    private ProgressBar mProgressBar;
    private TuyaCameraView mCameraView;

    private ITYCloudVideo mcloudCamera;
    private String playUrl;
    private boolean playState;
    private String encryptKey;
    private int playDuration;
    private String cachePath;
    private String mDevId;


    private void startplay() {
        mcloudCamera.playVideo(playUrl, 0, encryptKey, new OperationCallBack() {
            @Override
            public void onSuccess(int i, int i1, String s, Object o) {
                Log.d("mcloudCamera", "onsuccess");
            }

            @Override
            public void onFailure(int i, int i1, int i2, Object o) {

            }
        }, new OperationCallBack() {
            @Override
            public void onSuccess(int i, int i1, String s, Object o) {
                Log.d("mcloudCamera", "finish onsuccess");
            }

            @Override
            public void onFailure(int i, int i1, int i2, Object o) {

            }
        });
    }


    private void pauseVideo() {
        mcloudCamera.pauseVideo(new OperationCallBack() {
            @Override
          public void onSuccess(int i, int i1,String data, Object camera) {
            //playState = CloudPlayState.STATE_PAUSED;
           // mHandler.sendMessage(MessageUtil.getMessage(ICameraVideoPlayModel.MSG_CLOUD_VIDEO_PAUSE, ICameraVideoPlayModel.OPERATE_SUCCESS))
          }
          @Override
           public void onFailure(int i,int i1, int errCode,Object Camera) {
            //mHandler.sendMessage(MessageUtil.getMessage(ICameraVideoPlayModel.MSG_CLOUD_VIDEO_PAUSE, ICameraVideoPlayModel.OPERATE_FAIL))
          }
        });
      }
  
      private void resumeVideo() {
        mcloudCamera.resumeVideo(new OperationCallBack() {
          @Override
          public void onSuccess(int i, int i1, String s, Object o) {
              Log.d("mcloudCamera", "onsuccess");
          }

          @Override
          public void onFailure(int i, int i1, int i2, Object o) {

          }
        });
      } 
    
      private void stopVideo() {
        mcloudCamera.stopVideo(new OperationCallBack() {
          @Override
          public void onSuccess(int i, int i1, String s, Object o) {
              Log.d("mcloudCamera", "onsuccess");
          }

          @Override
          public void onFailure(int i, int i1, int i2, Object o) {

          }
        });
      }
      
      private void setCloudVideoMute(int voiceMode) {
        mcloudCamera.setCloudVideoMute(voiceMode, new OperationDelegateCallBack() {
          @Override
          public void onSuccess(int sessionId, int requestId, String data) {
              //startplay();
          }

          @Override
          public void onFailure(int sessionId, int requestId, int errCode) {

          }
        });
      }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_cloud_video);
        initview();
        initData();
        initCloudCamera();
    }

    private void initData() {
        playUrl = getIntent().getStringExtra("playUrl");
        encryptKey = getIntent().getStringExtra("encryptKey");
        playDuration = getIntent().getIntExtra("playDuration", 0);
        cachePath = getApplication().getCacheDir().getPath();

    }

    private void initCloudCamera() {
        mcloudCamera = new TYCloudVideoPlayer();
        mcloudCamera.registorOnP2PCameraListener(p2pCameraListener);
        mcloudCamera.generateCloudCameraView((IRegistorIOTCListener) mCameraView.createdView());
        mcloudCamera.createCloudDevice(cachePath, mDevId, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                startplay();
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {

            }
        });

    }

    private OnP2PCameraListener p2pCameraListener = new OnP2PCameraListener() {
        @Override
        public void receiveFrameDataForMediaCodec(int i, byte[] bytes, int i1, int i2, byte[] bytes1, boolean b, int i3) {

        }

        @Override
        public void onReceiveFrameYUVData(int i, ByteBuffer byteBuffer, ByteBuffer byteBuffer1, ByteBuffer byteBuffer2, int i1, int i2, int i3, int i4, long l, long l1, long l2, Object o) {

        }

        @Override
        public void onSessionStatusChanged(Object o, int i, int i1) {

        }

        @Override
        public void onReceiveAudioBufferData(int i, int i1, int i2, long l, long l1, long l2) {

        }

        @Override
        public void onReceiveSpeakerEchoData(ByteBuffer byteBuffer, int i) {

        }
      };


    private void initview() {
        mProgressBar = findViewById(R.id.cloud_progressbar);
        mCameraView = findViewById(R.id.cloud_view);
        mCameraView.createVideoView(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mcloudCamera) {
            mcloudCamera.deinitCloudVideo();
        }
    }
}
