package com.zgame.zgame.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.zgame.zgame.R;
import static android.util.Log.d;

public class TransparentActivity extends Activity   {

    private boolean infinity = true;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        if (permissionGranted()) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            requestNewPermissions();
        }

        //startActivityService();

        //findViewById(R.id.startDummyService).setOnClickListener(view -> startActivityService());
        findViewById(R.id.checkInstance).setOnClickListener(view -> stopActivityService());

        //finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestNewPermissions() {
        this.requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE }, 100 );
    }

    private boolean permissionGranted() {
        String storageReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        String storageWritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        return checkCallingOrSelfPermission(storageReadPermission) == PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(storageWritePermission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       /* when(requestCode){

            100 -> checkAllowOrNot(permissions)

            102 -> {Toast.makeText(this,"PdfPicker",Toast.LENGTH_SHORT).show()}
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        d("OnDestroyCalled", "OnDestroyCalled");
    }

    private void startActivityService() {
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, TransparentActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        new Thread(() -> {
            while(infinity){
                d("Helloworld", "HelloWorld");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    d("Helloworld", "HelloWorld" + e.getMessage());
                }
            }

        }).start();
        //finish();
    }

    private void stopActivityService() {
        Toast.makeText(this, "hello world Activity is DEAD", Toast.LENGTH_SHORT).show();
    }
}
