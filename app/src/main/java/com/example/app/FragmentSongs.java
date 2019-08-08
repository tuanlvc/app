package com.example.app;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.interf.IControlPlayMedia;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentSongs extends Fragment {
    //    private MusicService musicSrv;
//    private Intent playIntent;
//    private boolean musicBound = false;
//    //connect to the service
//    private ServiceConnection musicConnection = new ServiceConnection(){
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MusicBinder binder = (MusicBinder)service;
//            //get service
//            musicSrv = binder.getService();
//            //pass list
//            musicSrv.setList(songList);
//            musicBound = true;
//        }
//        @Override
//        protected void onStart() {
//            super.onStart();
//            if(playIntent==null){
//                playIntent = new Intent(this, MusicService.class);
//                bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
//                startService(playIntent);
//            }
//        }
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            musicBound = false;
//        }
//    };
    private ArrayList<Songs> mSongs;

    public FragmentSongs(IControlPlayMedia control) {
        this.iControlPlayMedia = control;
        this.mSongs = new ArrayList<>();
    }

    IControlPlayMedia iControlPlayMedia;

    public void setActivity(IControlPlayMedia control) {
        this.iControlPlayMedia = control;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.songList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        SongAdapter songAdapter = new SongAdapter(getContext(), mSongs);
        listSong();
        ImageButton btnshuffle = (ImageButton)view.findViewById(R.id.btnshuffle);
        btnshuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        recyclerView.setAdapter(songAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Songs");
    }

    public void listSong() {
        String select = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOder = MediaStore.Audio.Media.TITLE + " ASC";
        Log.d("ABC", "Bkav DucLQ test " + select);
        ContentResolver cr = getActivity().getContentResolver();
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,

        };
        Cursor cur = cr.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                select,
                null,
                null
        );


        while (cur.moveToNext()) {
            Songs songs = new Songs();
            songs.setId(cur.getLong(cur.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
            songs.setmName(cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
            songs.setmSinger(cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
//            HashMap<String, String> map = new HashMap<>();
//            map.put("ID", cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
//            map.put("artist", cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
//            map.put("title", cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
//            map.put("displayname", cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
//            map.put("duration", cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
//            SongAdapter.mSongs.add(map);
            mSongs.add(songs);
        }
        iControlPlayMedia.settListSong(mSongs);

    }


}
