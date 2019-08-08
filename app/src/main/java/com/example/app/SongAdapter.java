package com.example.app;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.interf.IControlPlayMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    static ArrayList<Songs> mSongs = new ArrayList<>();

    private IControlPlayMedia iControlPlayMedia;
    private MediaPlayer player;

    public SongAdapter(Context context, ArrayList<Songs> arrayList) {
        this.iControlPlayMedia = (IControlPlayMedia) context;
        this.mSongs = arrayList;
    }


    @Override
    public SongAdapter.SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View songView = inflater.inflate(R.layout.itemsongs, parent, false);
//        SongViewHolder songViewHolder = new SongViewHolder(songView);
        return new SongViewHolder(songView);
    }

    @Override
    public void onBindViewHolder(SongAdapter.SongViewHolder viewHolder, final int position) {

//        viewHolder.txtsong.setText(mSongs.get(position).getmName()
//        viewHolder.txtsinger.setText(mSongs.get(position).get("artist"));
        viewHolder.txtsong.setText(mSongs.get(position).getmName());
        viewHolder.txtsinger.setText(mSongs.get(position).getmSinger());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iControlPlayMedia.playSong(position);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public TextView txtsong;
        public TextView txtsinger;

        public SongViewHolder(final View itemView) {
            super(itemView);
            txtsong = (TextView) itemView.findViewById(R.id.txtsong);
            txtsinger = (TextView) itemView.findViewById(R.id.txtsinger);
        }

    }
}