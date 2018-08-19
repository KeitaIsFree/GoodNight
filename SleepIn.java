package com.example.keita.goodnight;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class SleepIn extends AppCompatActivity {

    int nowPlaying = -1;
    MediaPlayer[] mPs;
    EditText[] textBk;
    EditText[] textCl;
    SeekBar[] sBs;
    int[] audioIds;
    String[] audioNames;
    int[] textBkIds;
    int[] textClIds;
    int[] sBsIds;
    int maxTextWidth;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_2);

        audioIds = new int[] {
                R.raw.rain,
                R.raw.river,
                R.raw.train,
                R.raw.traffic,
                R.raw.underwater,
                R.raw.fire
        };
        audioNames = new String[] {
                "RAIN",
                "RIVER",
                "TRAIN",
                "TRAFFIC",
                "WATER",
                "FIRE"
        };


        int count = audioIds.length;
        mPs = new MediaPlayer[count];
        textBk = new EditText[count];
        textCl = new EditText[count];
        sBs = new SeekBar[count];
        textBkIds = new int[count];
        textClIds = new int[count];
        sBsIds = new int[count];
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);

        ColorDrawable transparent = new ColorDrawable(getResources().getColor(android.R.color.transparent));

        for (int i=0; i<count; i++) {
            //setting media players
            mPs[i] = MediaPlayer.create(this, audioIds[i]);
            //setting black text
            textBk[i] = new EditText(this);
            textBkIds[i] = View.generateViewId();
            textBk[i].setId(textBkIds[i]);
            textBk[i].setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT));
            textBk[i].setText(audioNames[i]);
            textBk[i].setTextColor(Color.rgb(0,0xff,0x80));
            textBk[i].setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
            textBk[i].setTypeface(Typeface.DEFAULT_BOLD);
            textBk[i].setInputType(InputType.TYPE_NULL);
            textBk[i].setEnabled(false);
            textBk[i].setKeyListener(null);
            layout.addView(textBk[i]);

            textCl[i] = new EditText(this);
            textClIds[i] = View.generateViewId();
            textCl[i].setId(textClIds[i]);
            textCl[i].setLayoutParams(new ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT));
            textCl[i].setText(audioNames[i]);
            textCl[i].setTextColor(Color.rgb(0x16,0x16,0x16));
            textCl[i].setBackground(new ColorDrawable(Color.rgb(0,0xFF,0x80)));
            textCl[i].setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,24,getResources().getDisplayMetrics()));
            textCl[i].setTypeface(Typeface.DEFAULT_BOLD);
            textCl[i].setInputType(InputType.TYPE_NULL);
            textCl[i].setEnabled(false);
            textCl[i].setKeyListener(null);
            layout.addView(textCl[i]);

            //setting seekbars
            maxTextWidth = textBk[0].getLayoutParams().width;
            final int index = i;
            sBs[i] = new SeekBar(this);
            sBsIds[i] = View.generateViewId();
            sBs[i].setId(sBsIds[i]);
            sBs[i].setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            ));
            //sBs[i].setProgressDrawable(transparent);
            sBs[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                ConstraintLayout.LayoutParams pams = (ConstraintLayout.LayoutParams) textCl[index].getLayoutParams();
                @Override
                public void onProgressChanged(SeekBar seekBar, int j, boolean b) {
                    int max = seekBar.getMax();
                    mPs[index].setVolume(1-(float)(Math.log(max-j)/Math.log(max)),
                            1-(float)(Math.log(max-j)/Math.log(max)));
                    //change this text accordingly
                    pams.width = maxTextWidth*j/max;
                    textCl[index].setLayoutParams(pams);
                    }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (nowPlaying!=index) {
                        if (nowPlaying!=-1) mPs[nowPlaying].pause();
                        mPs[index].start();
                        if(nowPlaying!=-1){// change previously playing text to default text layout
                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) textCl[index].getLayoutParams();
                            params.width = 0;
                            textCl[index].setLayoutParams(params);}
                        nowPlaying=index;
                    }
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            layout.addView(sBs[i]);
        }

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        for (int i=0; i < count; i++) {
            //textbk constraints
            if (i == 0) set.connect(textBkIds[i],ConstraintSet.TOP,R.id.layout,ConstraintSet.TOP);
            else set.connect(textBkIds[i],ConstraintSet.TOP,textBkIds[i-1],ConstraintSet.BOTTOM);
            set.connect(textBkIds[i],ConstraintSet.START,R.id.layout,ConstraintSet.START);
            //textcl constraints
            set.connect(textClIds[i],ConstraintSet.TOP,textBkIds[i],ConstraintSet.TOP);
            set.connect(textClIds[i],ConstraintSet.START,R.id.layout,ConstraintSet.START);
            //seekbars
            set.connect(sBsIds[i],ConstraintSet.TOP,textBkIds[i],ConstraintSet.TOP);
            set.connect(sBsIds[i],ConstraintSet.START,R.id.layout,ConstraintSet.START);
            set.connect(sBsIds[i],ConstraintSet.END,R.id.layout,ConstraintSet.END);
            sBs[i].bringToFront();
        }
        set.applyTo(layout);

    }

    /*@Override
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

    }*/

}
