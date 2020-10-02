package com.example.lrc;

import java.util.List;
import com.example.lrc.LrcView.MedCallBack;
import android.R.plurals;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.view.Menu;

public class MainActivity extends Activity implements MedCallBack {

    private LrcRows lrcRows = new LrcRows();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean timeFlag = true;
    private LrcView lrcView;
    String Path="/storage/emulated/0/music_2/Good Time - Owl City,Carly Rae Jepsen.mp3";
    String Path2="/storage/emulated/0/music_2/Good Time - Owl City,Carly Rae Jepsen.lrc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                int time = mediaPlayer.getCurrentPosition();
                lrcView.LrcToPlayer(time);//根据播放的进度，时时跟新歌词
            }
        }

        ;
    };

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
// TODO Auto-generated method stub
            while (timeFlag) {

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                handler.sendEmptyMessage(0);
            }
        }
    });


    private void initview() {
// TODO Auto-generated method stub
        List<LrcRow> list = lrcRows.BuildList(Path2);
        lrcView = (LrcView) findViewById(R.id.mylrcview);
        lrcView.setLrc(list);
        lrcView.setCall(this);

        try {
//从assets打开
           // AssetFileDescriptor fileDescriptor = getAssets().openFd("Good Time - Owl City,Carly Rae Jepsen.mp3");

            mediaPlayer.setDataSource(Path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            thread.start();
        } catch (Exception e) {
// TODO: handle exception
        }
    }


    //歌曲播放时，根据拖动跨越的行数里面的时间快进或快退带时间对应的播放进度
    @Override
    public void call(long time) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo((int) time);
        }

    }
}