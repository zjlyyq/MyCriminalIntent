package com.example.jialuzhang.mycriminalintent.models;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jialuzhang on 2017/3/9.
 */

public class CrimeLab {
    private static final String TAG= "CrimeLab";
    private static CriminalIntentJSONSerializer mSerializer;
    private static  CrimeLab sCrimLab;
    private  Context mAppContext;
    private ArrayList<Crime> mCrimes;

    private CrimeLab(Context appContext){
        mAppContext = appContext;
        mCrimes = new ArrayList<Crime>();
        mSerializer = new CriminalIntentJSONSerializer(mAppContext,"crimes.json");
        try {
            Log.d(TAG,"开始取数据");
            mCrimes = mSerializer.loadCrimes();
        } catch (IOException e) {
            mCrimes = new ArrayList<Crime>();
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static CrimeLab get(Context c){
        if(sCrimLab == null){
            Log.d(TAG,"sCrimLab is null");
            sCrimLab = new CrimeLab(c.getApplicationContext());
        }
        else{
            Log.d(TAG,"sCrimLab is not null");
        }
        return sCrimLab;
    }
    public void addCrime(Crime c){
        mCrimes.add(c);
    }
    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }
    public ArrayList<Crime> getmCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime c : mCrimes){
            if (c.getmId().equals(id)){     //不能用==，要用equal
                return c;
            }
        }
        return null;
    }
    public boolean saveCrimes() throws IOException {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG,"crimes saved to file.");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"Error saving crimes:",e);
            return  false;
        }
    }
}
