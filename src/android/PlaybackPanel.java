package cordova.plugin.cameracustom;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.tuya.smart.camera.middleware.p2p.ITuyaSmartCameraP2P;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;
import io.ionic.starter.R;
import android.view.WindowManager;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import java.util.List;
import java.util.Map;
import android.os.Message;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tuya.smart.android.camera.timeline.TuyaTimelineView;
import cordova.plugin.cameracustom.TimePieceBean;
import cordova.plugin.cameracustom.RecordInfoBean;
import android.os.Environment;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.camera.timeline.OnBarMoveListener;
import com.tuya.smart.android.camera.timeline.OnSelectedTimeListener;
import com.tuya.smart.android.camera.timeline.TimeBean;
import com.tuya.smart.android.camera.timeline.TuyaTimelineView;
import com.tuya.smart.android.common.utils.L;
import com.tuyasmart.camera.devicecontrol.api.ITuyaCameraDeviceControlCallback;
import com.tuyasmart.camera.devicecontrol.ITuyaCameraDevice;
import com.tuyasmart.camera.devicecontrol.TuyaCameraDeviceControlSDK;
import com.tuyasmart.camera.devicecontrol.bean.DpSDStatus;
import com.tuya.smart.camera.camerasdk.typlayer.callback.AbsP2pCameraListener;
import com.tuya.smart.camera.ipccamerasdk.bean.MonthDays;
import com.tuya.smart.camera.ipccamerasdk.p2p.ICameraP2P;
import com.tuya.smart.camera.middleware.p2p.TuyaSmartCameraP2PFactory;
import com.tuya.smart.camera.middleware.widget.AbsVideoViewCallback;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;
import com.tuya.smart.camera.utils.AudioUtils;
import com.tuyasmart.camera.devicecontrol.model.DpNotifyModel;
import com.tuyasmart.camera.devicecontrol.bean.DpSDFormat;
import com.tuyasmart.camera.devicecontrol.bean.DpSDFormatStatus;
import java.nio.ByteBuffer;
import java.io.File;
import android.Manifest;
import android.content.pm.PackageManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
public class PlaybackPanel extends Activity implements View.OnClickListener {
    private static final String TAG = "CameraPlaybackActivity";
    private Toolbar toolbar;
    private TuyaCameraView mVideoView;
    private ImageView muteImg;
    private EditText dateInputEdt;
    private RecyclerView queryRv;
    private TuyaTimelineView timelineView;
    private Button queryBtn, startBtn, pauseBtn, resumeBtn, stopBtn;
    
    private ITuyaSmartCameraP2P mCameraP2P;
    private static final int ASPECT_RATIO_WIDTH = 9;
    private static final int ASPECT_RATIO_HEIGHT = 13;
    private String deviceId;
    private PlaybackTime adapter;
    private List<TimePieceBean> queryDateList;
   public int startTime, endTime;
    private boolean isPlayback = false;
    public String packageName;
    private String picPath, videoPath;
    private ITuyaCameraDevice mDeviceControl;
    protected Map<String, List<String>> mBackDataMonthCache;
   protected Map<String, List<TimePieceBean>> mBackDataDayCache;
    private int mPlaybackMute = ICameraP2P.MUTE;
    private int p2pType;
    private boolean isSpeaking=false;
    private boolean isPlay = false;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        packageName = getApplication().getPackageName();
        setContentView(getApplication().getResources().getIdentifier("activity_camera_playback", "layout", packageName));
        initView();
        initData();
        initListener();
    }

   public void initView(){
    toolbar = findViewById(R.id.toolbar_view);
    timelineView = findViewById(R.id.timeline);
   // setSupportActi(toolbar);
    mVideoView = findViewById(R.id.camera_video_view);
    muteImg = findViewById(R.id.camera_mute);
    dateInputEdt = findViewById(R.id.date_input_edt);
    queryBtn = findViewById(R.id.query_btn);
    startBtn = findViewById(R.id.start_btn);
    pauseBtn = findViewById(R.id.pause_btn);
    resumeBtn = findViewById(R.id.resume_btn);
    stopBtn = findViewById(R.id.stop_btn);
    queryRv = findViewById(R.id.query_list);

    WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
    int width = windowManager.getDefaultDisplay().getWidth();
    int height = width * ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT;
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
    layoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar_view);
    findViewById(R.id.camera_video_view_Rl).setLayoutParams(layoutParams);

    timelineView.setOnBarMoveListener(new OnBarMoveListener() {
        @Override
        public void onBarMove(long l, long l1, long l2) {

        }

        @Override
        public void onBarMoveFinish(long startTime, long endTime, long currentTime) {
            timelineView.setCanQueryData();
            timelineView.setQueryNewVideoData(false);
            if (startTime != -1 && endTime != -1) {
                playback((int)startTime, (int)endTime, (int)currentTime);
            }
        }

        @Override
        public void onBarActionDown() {

        }
    });
    timelineView.setOnSelectedTimeListener(new OnSelectedTimeListener() {
        @Override
            public void onDragging(long selectStartTime, long selectEndTime) {

            }
    });
    }

    public void initData(){
        mBackDataMonthCache = new HashMap<>();
       mBackDataDayCache = new HashMap<>();
        Bundle bundle = getIntent().getExtras();
        deviceId = bundle.getString("deviceId");
        p2pType= Integer.parseInt(bundle.getString("P2PType"));
        mDeviceControl = TuyaCameraDeviceControlSDK.getCameraDeviceInstance(deviceId);
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
         LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
         queryRv.setLayoutManager(mLayoutManager);
         queryRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
         queryDateList = new ArrayList<>();
        adapter = new PlaybackTime(this, queryDateList);
         queryRv.setAdapter(adapter);

        mCameraP2P = TuyaSmartCameraP2PFactory.createCameraP2P(p2pType, deviceId);
        mVideoView.setViewCallback(new AbsVideoViewCallback() {
            @Override
            public void onCreated(Object o) {
                super.onCreated(o);
                if (mCameraP2P != null) {
                    mCameraP2P.generateCameraView(mVideoView.createdView());
                }
            }
        });
        mVideoView.createVideoView(p2pType);
        if (!mCameraP2P.isConnecting()) {
            mCameraP2P.connect(deviceId, new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int i, int i1, String s) {

                }

                @Override
                public void onFailure(int i, int i1, int i2) {

                }
            });
        }

        muteImg.setSelected(true);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(System.currentTimeMillis());
        dateInputEdt.setText(simpleDateFormat.format(date));
    }
  private void showResult(int O){
      if(O==1){
        Toast.makeText(getApplicationContext(),"Normal",Toast.LENGTH_SHORT).show();   
      }
     else if(O==2){
        Toast.makeText(getApplicationContext(),"Abnormal(Broken/Wrong Format)",Toast.LENGTH_SHORT).show(); 
      }
     else if(O==3){
        Toast.makeText(getApplicationContext(),"Insufficient Space",Toast.LENGTH_SHORT).show(); 
      }
      else if(O==4){
        Toast.makeText(getApplicationContext(),"Formatting",Toast.LENGTH_SHORT).show(); 
      }
      else{
        Toast.makeText(getApplicationContext(),"No SD Card",Toast.LENGTH_SHORT).show(); 
      }
  }
    private void initListener() {
        muteImg.setOnClickListener(this);
        queryBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        resumeBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        adapter.setListener(new PlaybackTime.OnTimeItemListener() {
            @Override
            public void onClick(TimePieceBean timePieceBean) {
                playback(timePieceBean.getStartTime(), timePieceBean.getEndTime(), timePieceBean.getStartTime());
            }
        });
    }

    private void playback(int startTime, int endTime, int playTime) {
        mCameraP2P.startPlayBack(startTime,
                endTime,
                playTime, new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        isPlayback = true;
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {
                        isPlayback = false;
                    }
                }, new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        isPlayback = false;
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {
                        isPlayback = false;
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_mute:
                muteClick();
                break;
            case R.id.query_btn:
                queryDayByMonthClick();
                break;
            case R.id.start_btn:
                startPlayback();
                break;
            case R.id.pause_btn:
                pauseClick();
                break;
            case R.id.resume_btn:
                resumeClick();
                break;
            case R.id.stop_btn:
                stopClick();

                break;
            default:
                break;
        }
    }
    private void handleDataDay(Message msg) {
        if (msg.arg1 == 1) {
            queryDateList.clear();
            //Timepieces with data for the query day
            List<TimePieceBean> timePieceBeans = mBackDataDayCache.get(mCameraP2P.getDayKey());
            Log.d(TAG,"Start 4 query  "+timePieceBeans);
            if (timePieceBeans != null) {
                //queryDateList.addAll(timePieceBeans);
                List<TimeBean> timelineData = new ArrayList<>();
                for(TimePieceBean bean: timePieceBeans) {
                    TimeBean b = new TimeBean();
                    b.setStartTime(bean.getStartTime());
                    b.setEndTime(bean.getEndTime());
                    timelineData.add(b);
                    Log.d(TAG,"Start 5 query  "+timelineData);
                }
                timelineView.setCurrentTimeConfig(timePieceBeans.get(0).getEndTime()*1000L);
                timelineView.setRecordDataExistTimeClipsList(timelineData);
            } else {
                //showErrorToast();
            }
            adapter.notifyDataSetChanged();
        } else {

        }
    }

    private void handleDataDate(Message msg) {
        if (msg.arg1 == 0) {
            List<String> days = mBackDataMonthCache.get(mCameraP2P.getMonthKey());

            try {
                if (days.size() == 0) {
                    //showErrorToast();
                    return;
                }
                final String inputStr = dateInputEdt.getText().toString();
                if (!TextUtils.isEmpty(inputStr) && inputStr.contains("/")) {
                    String[] substring = inputStr.split("/");
                    int year = Integer.parseInt(substring[0]);
                    int month = Integer.parseInt(substring[1]);
                    int day = Integer.parseInt(substring[2]);
                    mCameraP2P.queryRecordTimeSliceByDay(year, month, day, new OperationDelegateCallBack() {
                        @Override
                        public void onSuccess(int sessionId, int requestId, String data) {
                            Log.d(TAG,"Start 2 query  "+data);
                            L.e(TAG, inputStr + " --- " + data);
                            parsePlaybackData(data);
                        }

                        @Override
                        public void onFailure(int sessionId, int requestId, int errCode) {
                            Message msg=new Message();
                            msg.what=11;
                            msg.arg1=1;
                            handleDataDay(msg);
                           // mHandler.sendEmptyMessage(MSG_DATA_DATE_BY_DAY_FAIL);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    private void parsePlaybackData(Object obj) {
        RecordInfoBean recordInfoBean = JSONObject.parseObject(obj.toString(), RecordInfoBean.class);
        if (recordInfoBean.getCount() != 0) {
            List<TimePieceBean> timePieceBeanList = recordInfoBean.getItems();
            Log.d(TAG,"Start 3 query  "+timePieceBeanList);
            if (timePieceBeanList != null && timePieceBeanList.size() != 0) {
                mBackDataDayCache.put(mCameraP2P.getDayKey(), timePieceBeanList);
            }
            //mHandler.sendMessage(MessageUtil.getMessage(MSG_DATA_DATE_BY_DAY_SUCC, ARG1_OPERATE_SUCCESS));
        } else { 
             Message msg=new Message();
            msg.what=11;
            msg.arg1=0;

              handleDataDay(msg);
           // mHandler.sendMessage(MessageUtil.getMessage(MSG_DATA_DATE_BY_DAY_FAIL, ARG1_OPERATE_FAIL));
        }
    }

    private void startPlayback() {
        if (null != queryDateList && queryDateList.size() > 0) {
            TimePieceBean timePieceBean = queryDateList.get(0);
           if (null != timePieceBean) {
                mCameraP2P.startPlayBack(startTime, endTime, startTime, new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        isPlayback = true;
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {

                    }
                }, new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        isPlayback = false;
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {

                    }
                });
            }
        } else
         {
            //ToastUtil.shortToast(this, "No data for query date");
        }
    }

    private void stopClick() {
        mCameraP2P.stopPlayBack(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {

            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {

            }
        });
        isPlayback = false;
    }
    
    private void resumeClick() {
        mCameraP2P.resumePlayBack(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                isPlayback = true;
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {

            }
        });
    }
    private void pauseClick() {
        mCameraP2P.pausePlayBack(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                isPlayback = false;
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {

            }
        });
    }

    private void queryDayByMonthClick() {
      
        if (!mCameraP2P.isConnecting()) {
            //ToastUtil.shortToast(CameraPlaybackActivity.this, "please connect device first");
            return;
        }
        String inputStr = dateInputEdt.getText().toString();
        if (TextUtils.isEmpty(inputStr)) {
            return;
        }
        if (inputStr.contains("/")) {
            String[] substring = inputStr.split("/");
            if (substring.length > 2) {
                try {
                    int year = Integer.parseInt(substring[0]);
                    int month = Integer.parseInt(substring[1]);
                    mCameraP2P.queryRecordDaysByMonth(year, month, new OperationDelegateCallBack() {
                        @Override
                        public void onSuccess(int sessionId, int requestId, String data) {
                            MonthDays monthDays = JSONObject.parseObject(data, MonthDays.class);
                            mBackDataMonthCache.put(mCameraP2P.getMonthKey(), monthDays.getDataDays());
                            L.e(TAG,   "MonthDays --- " + data);
                            Message msg=new Message();
                           msg.what=11;
                           msg.arg1=0;
                           Log.d(TAG,"Start query  "+data);
                            handleDataDate(msg);
                            //mHandler.sendMessage(MessageUtil.getMessage(MSG_DATA_DATE, ARG1_OPERATE_SUCCESS));
                        }

                        @Override
                        public void onFailure(int sessionId, int requestId, int errCode) {
        
                        }
                    });
                } catch (Exception e) {
                    //ToastUtil.shortToast(CameraPlaybackActivity.this, "Input Error");
                }
            }
        }
    }

    private void muteClick() {
        int mute;
        mute = mPlaybackMute == ICameraP2P.MUTE ? ICameraP2P.UNMUTE : ICameraP2P.MUTE;
        mCameraP2P.setMute(ICameraP2P.PLAYMODE.PLAYBACK, mute, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                mPlaybackMute = Integer.valueOf(data);
                muteImg.setSelected(mPlaybackMute == ICameraP2P.MUTE);
                //mHandler.sendMessage(MessageUtil.getMessage(MSG_MUTE, ARG1_OPERATE_SUCCESS));
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                //mHandler.sendMessage(MessageUtil.getMessage(MSG_MUTE, ARG1_OPERATE_FAIL));
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();
        if (null != mCameraP2P) {
            AudioUtils.getModel(this);
            mCameraP2P.registerP2PCameraListener(p2pCameraListener);
            mCameraP2P.generateCameraView(mVideoView.createdView());
        }
    }

    private AbsP2pCameraListener p2pCameraListener = new AbsP2pCameraListener() {
        @Override
        public void onReceiveFrameYUVData(int i, ByteBuffer byteBuffer, ByteBuffer byteBuffer1, ByteBuffer byteBuffer2, int i1, int i2, int i3, int i4, long l, long l1, long l2, Object o) {
            super.onReceiveFrameYUVData(i, byteBuffer, byteBuffer1, byteBuffer2, i1, i2, i3, i4, l, l1, l2, o);
            timelineView.setCurrentTimeInMillisecond(l*1000L);
        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onPause();
        if (isPlayback) {
            mCameraP2P.stopPlayBack(null);
        }
        if (null != mCameraP2P) {
            mCameraP2P.removeOnP2PCameraListener();
            if (isFinishing()) {
                mCameraP2P.disconnect(new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int i, int i1, String s) {

                    }

                    @Override
                    public void onFailure(int i, int i1, int i2) {

                    }
                });
                mCameraP2P.destroyP2P();
            }
        }
        AudioUtils.changeToNomal(this);
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
        mCameraP2P.snapshot(picPath, PlaybackPanel.this, ICameraP2P.PLAYMODE.PLAYBACK, new OperationDelegateCallBack() {
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


    public void recordClick(){
        if (!isRecording) {
            if (ContextCompat.checkSelfPermission(PlaybackPanel.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                String picPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Camera/";
                File file = new File(picPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String fileName = System.currentTimeMillis() + ".mp4";
                videoPath = picPath + fileName;
                mCameraP2P.startRecordLocalMp4(picPath, PlaybackPanel.this, new OperationDelegateCallBack() {
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
                //recordStatue(true);
            } else {
                ActivityCompat.requestPermissions(PlaybackPanel.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
            }
        } else {
            mCameraP2P.stopRecordLocalMp4(new OperationDelegateCallBack() {
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
            //recordStatue(false);
        }
      }

}
