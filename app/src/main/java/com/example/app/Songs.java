package com.example.app;

import java.util.ArrayList;

public class Songs {
    private long Id;
    private String mName;
    private String mSinger;

    public Songs() {
    }

    public Songs(long Id, String mName, String mSinger) {
        this.Id = Id;
        this.mName = mName;
        this.mSinger = mSinger;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSinger() {
        return mSinger;
    }

    public void setmSinger(String mSinger) {
        this.mSinger = mSinger;
    }

}
