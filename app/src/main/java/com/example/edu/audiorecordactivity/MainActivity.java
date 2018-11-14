package com.example.edu.audiorecordactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button play, stop, record;
    MediaRecorder myAudioRecorder;
    String outputFile = null;
//성공
    final int REQUEST_CODE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
             if(permission !=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
        }

        int permission1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission1 !=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }


        record= findViewById(R.id.record);
        record.setOnClickListener(this);
        stop=findViewById(R.id.stop);
        stop.setOnClickListener(this);
        play=findViewById(R.id.play);
        play.setOnClickListener(this);

        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile=Environment.getExternalStorageDirectory().getAbsolutePath()+"/recording.3gp";//mp3으로 바꿔도 해보고 Internal Storage로 바꿔서 내부에 저장되는지 확인.
        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//MPEG_4 로 바꿔도 해보고.
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 ||

                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("", "Permission has been granted by user");
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record:
                //record되는 영역.
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                    record.setEnabled(false);
                    stop.setEnabled(true);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.stop:
                //stop 되는 영역
                stop.setEnabled(false);
                play.setEnabled(true);
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder=null;
                break;

            case R.id.play:
                //녹음된 파일이 play되는 영역
                MediaPlayer m =new MediaPlayer();
                try {
                    m.setDataSource(outputFile);
                    m.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }



    }
}
