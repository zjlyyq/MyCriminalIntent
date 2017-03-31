package com.example.jialuzhang.mycriminalintent.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by jialuzhang on 2017/3/8.
 */

public class Crime {

    //定义JSON格式的常量
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_SUSPECT = "suspect";
    private UUID mId;
    private String mTitle;
    private boolean mSolved;
    private Date mDate;
    private String mSuspect;

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public String toString() {
        return getmTitle();
    }

    public Crime(){
        //生成唯一标识符
        mId = UUID.randomUUID();
        mDate = new Date();

    }
    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public Crime(JSONObject jsonObject) throws JSONException {
        Log.d("Crime",jsonObject.toString());
        mId = UUID.fromString(jsonObject.getString(JSON_ID));
        Log.d("Crime",mId.toString());
        if(jsonObject.has(JSON_TITLE)){
            mTitle = jsonObject.getString(JSON_TITLE);
        }
        Log.d("Crime",mTitle);
        if(jsonObject.has(JSON_DATE)){
            mDate = new Date(jsonObject.getLong(JSON_DATE));
        }
        else{
            mDate = new Date();
        }
        //Log.d("Crime",mDate.toString());
        mSolved = jsonObject.getBoolean(JSON_SOLVED);
        Log.d("Crime","succeed");
        if(jsonObject.has(JSON_SUSPECT)){
            mSuspect = jsonObject.getString(JSON_SUSPECT);
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID,mId.toString());
        jsonObject.put(JSON_TITLE,mTitle);
        jsonObject.put(JSON_SOLVED,mDate.getTime());
        jsonObject.put(JSON_SOLVED,mSolved);
        jsonObject.put(JSON_SUSPECT,mSuspect);
        Log.d("CrimeClass","one jsonobject saved");
        return  jsonObject;
    }
}
