package com.example.jialuzhang.mycriminalintent;

import android.support.v4.app.Fragment;
import android.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String TAG = "CrimeActivity";

    @Override
    public Fragment FragmentInit() {
        UUID id = (UUID)getIntent().getSerializableExtra("whichId");
        return CrimeFragment.newInstance(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"CrimeActivity onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"CrimeActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"CrimeActivity onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"CrimeActivity onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"CrimeActivity onResume");
    }
}
