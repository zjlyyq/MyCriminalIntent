package com.example.jialuzhang.mycriminalintent.camera;

import android.support.v4.app.Fragment;

import com.example.jialuzhang.mycriminalintent.SingleFragmentActivity;

/**
 * Created by jialuzhang on 2017/3/13.
 */

public class CrimeCameraActivity extends SingleFragmentActivity{

    @Override
    public Fragment FragmentInit() {
        return new CrimeCameraFragment();
    }
}
