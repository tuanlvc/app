package com.example.app;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.app.interf.IUpdateInfoMedia;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private MediaPlayer player;
    private ArrayList<Songs> song;
    private int songPosition;
    private final IBinder musicBind = new MusicBinder();
    private IUpdateInfoMedia mIUpdateInfoMedia;
    private SeekBar mSeekBar;
    private TextView mCurrentDuration;
    private TextView mTotalDuration;
    private int mInterval = 1000;
    private Runnable mProgressRunner = new Runnable() {
        @Override
        public void run() {
            if (mSeekBar != null) {
                mSeekBar.setProgress(player.getCurrentPosition());
                if (player.isPlaying()) {
                    mSeekBar.postDelayed(mProgressRunner, mInterval);
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        int duration = mediaPlayer.getDuration();
        mSeekBar.setMax(duration);
        mSeekBar.postDelayed(mProgressRunner, mInterval);
        mTotalDuration.setText(String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Songs> theSongs) {
        song = theSongs;
    }

    public class MusicBinder extends Binder {

        MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong() {
        player.reset();
        Songs playSong = song.get(songPosition);
        if (mIUpdateInfoMedia != null) {
            mIUpdateInfoMedia.updateInfoSong(playSong);
        }
        long currentSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);

        } catch (Exception e) {
            Log.d("MUSIC SERVICE", "ERROR ");
        }
        player.prepareAsync();
    }

    public void setSong(int indexSong) {
        songPosition = indexSong;
    }

    /**
     * @return
     */
    public IUpdateInfoMedia getIUpdateInfoMedia() {
        return mIUpdateInfoMedia;
    }

    /**
     * @param iUpdateInfoMedia
     */
    public void setIUpdateInfoMedia(IUpdateInfoMedia iUpdateInfoMedia) {
        this.mIUpdateInfoMedia = iUpdateInfoMedia;
    }
    public void updateSeekBar(SeekBar seekBar, TextView currentDuration, TextView totalDuration ){
        mSeekBar=seekBar;
        mCurrentDuration=currentDuration;
        mTotalDuration=totalDuration;
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    player.seekTo(i);
                }
                /**
                 * cập nhật textView hiển thị thời gian đúng định dạng 0:00
                 */
                mCurrentDuration.setText(String.format("%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(i),
                        TimeUnit.MILLISECONDS.toSeconds(i) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(i))
                ));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
