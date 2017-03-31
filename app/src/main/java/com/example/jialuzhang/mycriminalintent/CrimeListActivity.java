package com.example.jialuzhang.mycriminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.jialuzhang.mycriminalintent.models.CrimeLab;

/**
 * Created by jialuzhang on 2017/3/12.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment FragmentInit() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutRsId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

