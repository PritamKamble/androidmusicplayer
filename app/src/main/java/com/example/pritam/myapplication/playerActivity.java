package com.example.pritam.myapplication;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playerActivity extends AppCompatActivity {

    String sname;

    Button btn_next,btn_previous,btn_pause;
    TextView songlable;
    SeekBar sb;


    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateseekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next=(Button)findViewById(R.id.next);
        btn_pause=(Button)findViewById(R.id.pause);
        btn_previous=(Button)findViewById(R.id.previous);

        songlable=(TextView)findViewById(R.id.songlable);
        sb=(SeekBar)findViewById(R.id.seekbar);


        updateseekbar=new Thread(){

            @Override
            public void run() {

                int totalduration =myMediaPlayer.getDuration();
                int currentposition=0;

                while (currentposition<totalduration){

                    try {

                        sleep(500);
                        currentposition=myMediaPlayer.getCurrentPosition();
                        sb.setProgress(currentposition);
                    }
                    catch (InterruptedException e){

                        e.printStackTrace();

                    }

                }

            }
        };

        if(myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i=getIntent();
        Bundle bundle =i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");

        sname=mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songname");

        songlable.setText(songName);
        songlable.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri u=Uri.parse(mySongs.get(position).toString());

        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        sb.setMax(myMediaPlayer.getDuration());

        updateseekbar.start();

        sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.MULTIPLY);
        sb.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sb.setMax(myMediaPlayer.getDuration());

                if (myMediaPlayer.isPlaying()){

                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();

                }
                else {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();

                }

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position=((position+1)%mySongs.size());
                Uri u=Uri.parse(mySongs.get(position).toString());
                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName().toString();
                songlable.setText(sname);

                myMediaPlayer.start();

            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myMediaPlayer.stop();
                myMediaPlayer.release();

                position=((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u=Uri.parse(mySongs.get(position).toString());
                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName().toString();
                songlable.setText(sname);

                myMediaPlayer.start();


            }
        });
    }
}
