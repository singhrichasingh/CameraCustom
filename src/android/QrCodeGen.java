package cordova.plugin.cameracustom;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

//package com.tuya.smart.android.demo.camera;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.common.utils.WiFiUtil;
import io.ionic.starter.R;
//import com.tuya.smart.android.demo.base.utils.ToastUtil;
import cordova.plugin.cameracustom.QRCodeUtil;
//import com.tuya.smart.android.demo.family.FamilyManager;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.builder.TuyaCameraActivatorBuilder;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaCameraDevActivator;
import com.tuya.smart.sdk.api.ITuyaSmartCameraActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;

public class QrCodeGen extends Activity implements ITuyaSmartCameraActivatorListener {

    private String wifiSSId = "";
    private String token = "";
    private String wifiPwd = "Tuya.140616";
    private ImageView mIvQr;
    private LinearLayout mLlInputWifi;
    private EditText mEtInputWifiSSid;
    private EditText mEtInputWifiPwd;
    private Button mBtnSave;
    private String ssid;


    private ITuyaCameraDevActivator mTuyaActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);
        mLlInputWifi = findViewById(R.id.ll_input_wifi);
        mEtInputWifiSSid = findViewById(R.id.et_wifi_ssid);
        mEtInputWifiPwd = findViewById(R.id.et_wifi_pwd);
        mBtnSave = findViewById(R.id.btn_save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQrcode();
            }
        });
        mIvQr = findViewById(R.id.iv_qrcode);
        init();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
 //deviceId = bundle.getString("deviceId");
 token= bundle.getString("token");
  ssid= bundle.getString("SSID");
        wifiSSId = WiFiUtil.getCurrentSSID(this);
        wifiSSId = mEtInputWifiSSid.getText().toString();
        Log.d("QR code","ssid"+wifiSSId);

          
        // TuyaHomeSdk.getActivatorInstance().getActivatorToken(FamilyManager.getInstance().getCurrentHomeId(), new ITuyaActivatorGetToken() {
        //     @Override
        //     public void onSuccess(String s) {
        //         token = s;
        //     }

        //     @Override
        //     public void onFailure(String s, String s1) {
        //         L.e("QrCodeConfigActivity", s);
        //     }
        // });
        mEtInputWifiSSid.setText(ssid);
    }

    private void createQrcode() {
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "token is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        wifiPwd = mEtInputWifiPwd.getText().toString();
        TuyaCameraActivatorBuilder builder = new TuyaCameraActivatorBuilder()
                .setToken(token).setPassword(wifiPwd).setSsid(ssid).setListener(this);
        mTuyaActivator = TuyaHomeSdk.getActivatorInstance().newCameraDevActivator(builder);
        mTuyaActivator.createQRCode();
        mTuyaActivator.start();
    }


    @Override
    public void onQRCodeSuccess(String s) {
        final Bitmap bitmap;
        try {
            bitmap = QRCodeUtil.createQRCode(s, 300);
            QrCodeGen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIvQr.setImageBitmap(bitmap);
                    mIvQr.setVisibility(View.VISIBLE);
                    mLlInputWifi.setVisibility(View.GONE);
                }
            });
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, s1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActiveSuccess(DeviceBean deviceBean) {
        Toast.makeText(this, "Configuration Success", Toast.LENGTH_SHORT).show();
        //ToastUtil.shortToast(QrCodeConfigActivity.this, " config success！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTuyaActivator) {
            mTuyaActivator.stop();
            mTuyaActivator.onDestroy();
        }
    }
}
