package com.darko.ecg_second;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends Activity {

    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int EXTERNAL_REQUEST = 138;

    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }

    private static final int REQUEST_PATH = 1;
    File curFile;
    EditText nameOfFile, answer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameOfFile = (EditText)findViewById(R.id.editText);
        answer = (EditText)findViewById(R.id.editText2);
        requestForPermission();
    }

    public void getFile(View view){
        Intent intent1 = new Intent(this, FileChooser.class);
        startActivityForResult(intent1, REQUEST_PATH);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_PATH){
            if (resultCode == RESULT_OK) {
                curFile = new File(data.getStringExtra("GetAbsolutePath"));
                nameOfFile.setText(curFile.getName());

                FileAnalysis fa = new FileAnalysis(curFile);
                answer.setText(fa.ECGvalidity());
            }
        }
    }
}
