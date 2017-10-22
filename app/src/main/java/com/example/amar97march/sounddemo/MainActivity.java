package com.example.amar97march.sounddemo;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SoundPool sp;
    int idFX1=-1;
    int idFX2=-1;
    int idFX3=-1;
    int nowPlaying=-1;
    int repeats=2;
    float volume=.1f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonFX1=(Button) findViewById(R.id.btnFX1);
        buttonFX1.setOnClickListener(this);
        Button buttonFX2=(Button) findViewById(R.id.btnFX2);
        buttonFX2.setOnClickListener(this);
        Button buttonFX3=(Button) findViewById(R.id.btnFX3);
        buttonFX3.setOnClickListener(this);
        Button buttonStop=(Button) findViewById(R.id.btnStop);
        buttonStop.setOnClickListener(this);

        //initialize soundpool depending on the android version
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes=new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            sp=new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
        }else{
            sp=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }
        try{
            //creating objects of the two required classes
            AssetManager assetmanager= this.getAssets();
            AssetFileDescriptor descriptor;
            //Load our fix in memory ready for use
            descriptor=assetmanager.openFd("fx1.ogg");
            idFX1=sp.load(descriptor,0);
            descriptor=assetmanager.openFd("fx2.ogg");
            idFX2=sp.load(descriptor,0);
            descriptor=assetmanager.openFd("fx3.ogg");
            idFX3=sp.load(descriptor,0);

        }catch(IOException e){
            Log.e("error","failed to load sound files");
        }

        //Now setup the seekBar
        SeekBar seekBar=(SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.
                OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged (SeekBar seekBar,int value,boolean fromUser){
                volume=value/10f;
                sp.setVolume(nowPlaying,volume,volume);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekbar){

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){

            }
        }
        );
        final Spinner spinner=(Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView,View selectedItemView,int position,long id){
                String temp=String.valueOf(spinner.getSelectedItem());
                repeats=Integer.valueOf(temp);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView){

            }
        }
        );
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnFX1:
                sp.stop(nowPlaying);
                nowPlaying=sp.play(idFX1,volume,volume,0,repeats,1);
                break;
            case R.id.btnFX2:
                sp.stop(nowPlaying);
                nowPlaying=sp.play(idFX2,volume,volume,0,repeats,1);
                break;
            case R.id.btnFX3:
                sp.stop(nowPlaying);
                nowPlaying=sp.play(idFX3,volume,volume,0,repeats,1);
                break;
            case R.id.btnStop:
                sp.stop(nowPlaying);
                break;
        }
    }
}
