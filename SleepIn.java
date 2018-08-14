package com.example.keita.goodnight;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;


public class SleepIn extends AppCompatActivity {

    int nowPlaying = -1;
    ImageView[] iVs;
    SeekBar[] sBs;
    MediaPlayer[] mPs;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_2);

        mPs = new MediaPlayer[6];
        mPs[0]= MediaPlayer.create(this,R.raw.rain);
        mPs[1]= MediaPlayer.create(this,R.raw.river);
        mPs[2]= MediaPlayer.create(this,R.raw.train);
        mPs[3]= MediaPlayer.create(this,R.raw.traffic);
        mPs[4]= MediaPlayer.create(this,R.raw.underwater);
        mPs[5]= MediaPlayer.create(this,R.raw.fire);
        iVs = new ImageView[6];
        iVs[0] = (ImageView) findViewById(R.id.rain_image_cl);
        iVs[1] = (ImageView) findViewById(R.id.river_image_cl);
        iVs[2] = (ImageView) findViewById(R.id.train_image_cl);
        iVs[3] = (ImageView) findViewById(R.id.traffic_image_cl);
        iVs[4] = (ImageView) findViewById(R.id.underwater_image_cl);
        iVs[5] = (ImageView) findViewById(R.id.fire_image_cl);

        sBs = new SeekBar[6];
        sBs[0] = (SeekBar) findViewById(R.id.rain_bar);
        sBs[1] = (SeekBar) findViewById(R.id.river_bar);
        sBs[2] = (SeekBar) findViewById(R.id.train_bar);
        sBs[3] = (SeekBar) findViewById(R.id.traffic_bar);
        sBs[4] = (SeekBar) findViewById(R.id.underwater_bar);
        sBs[5] = (SeekBar) findViewById(R.id.fire_bar);

    }

    @Override
    public void onStart() {
        super.onStart();
        final SeekBar[] sB = {(SeekBar) findViewById(R.id.rain_bar),
                findViewById(R.id.river_bar),
                findViewById(R.id.train_bar),
                findViewById(R.id.traffic_bar),
                findViewById(R.id.underwater_bar),
                findViewById(R.id.fire_bar)};
        for (int i = 0; i < 6; i++) {
            sB[i].getThumb().mutate().setAlpha(0);
        }
        for (int i = 0; i < 6; i++) {
            final int index = i;
            sB[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int j, boolean b) {
                    mPs[index].setVolume(1-(float)(Math.log(seekBar.getMax()-j)/Math.log(seekBar.getMax())),
                            1-(float)(Math.log(seekBar.getMax()-j)/Math.log(seekBar.getMax())));
                    ConstraintLayout.LayoutParams lP = (ConstraintLayout.LayoutParams) iVs[index].getLayoutParams();
                    lP.width = (seekBar.getRootView().getWidth()-8)*seekBar.getProgress()/seekBar.getMax()+8;
                    iVs[index].setLayoutParams(lP);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (nowPlaying!=index) {
                        if (nowPlaying!=-1) mPs[nowPlaying].pause();
                        mPs[index].start();
                        if(nowPlaying!=-1){ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) iVs[nowPlaying].getLayoutParams();
                            params.width = 8;
                            iVs[nowPlaying].setLayoutParams(params);}
                        nowPlaying=index;
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

    }

}
