package com.example.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.example.app.interf.IControlPlayMedia;
import com.example.app.interf.IUpdateInfoMedia;

import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, IControlPlayMedia, IUpdateInfoMedia {
    private MusicService musicService;
    private boolean musicBound;
    private Intent intentPlay;
    private ArrayList<Songs> listSong;
    Fragment fragment = null;
    private TextView mNameSongCurrent;
    private TextView mSingerSongCurrent;
    private SeekBar seekBar;
    private TextView currentDuration;
    private TextView totalDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_songs);
        listSong = new ArrayList<>();

        mNameSongCurrent = (TextView) findViewById(R.id.txtsong);
        mSingerSongCurrent = (TextView) findViewById(R.id.txtsinger);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        currentDuration = (TextView) findViewById(R.id.currentDuration);
        totalDuration = (TextView) findViewById(R.id.totalDuration);
        mNameSongCurrent.setEllipsize(TextUtils.TruncateAt.MARQUEE);

    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
            musicService = binder.getService();
            musicService.setList(listSong);
            setIUpdateInfoMedia();
            musicService.updateSeekBar(seekBar, currentDuration,totalDuration);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };

    /**
     * Bkav DucLQ set doi tuong IUpdateInfoMedia
     */
    private void setIUpdateInfoMedia() {
        if (musicService != null) {
            musicService.setIUpdateInfoMedia(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (intentPlay == null) {
            intentPlay = new Intent(this, MusicService.class);
            bindService(intentPlay, musicConnection, Context.BIND_AUTO_CREATE);
            startService(intentPlay);
        }
    }

    public void songPicked(View view) {
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.playSong();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void playSong(int pos) {
        musicService.setSong(pos);
        musicService.playSong();
    }

    private void displaySelectedScreen(int itemid) {

        switch (itemid) {
            case R.id.nav_songs:
                fragment = new FragmentSongs(this);

                break;
            case R.id.nav_artists:
                fragment = new FragmentArtists();
                break;
            case R.id.nav_albums:
                fragment = new FragmentAlbums();
                break;
            case R.id.nav_playlists:
                fragment = new FragmentPlaylists();
                break;

        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public void settListSong(ArrayList songs) {
        listSong = songs;
    }

    @Override
    public void onAttachFragment(Fragment f1) {

    }

    @Override
    public void updateInfoSong(Songs songs) {
        Log.d("Bkav", "Bkav DucLQ ten bai hat " + songs.getmName() + " ten ca si " + songs.getmSinger());
        mSingerSongCurrent.setText(songs.getmSinger());
        mNameSongCurrent.setText(songs.getmName());
    }

}
