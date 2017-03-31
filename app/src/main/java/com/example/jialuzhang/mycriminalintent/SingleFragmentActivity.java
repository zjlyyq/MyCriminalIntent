package com.example.jialuzhang.mycriminalintent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jialuzhang on 2017/3/12.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    public abstract Fragment FragmentInit();
    protected int getLayoutRsId(){
        return R.layout.activity_fragment;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRsId());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = FragmentInit();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
