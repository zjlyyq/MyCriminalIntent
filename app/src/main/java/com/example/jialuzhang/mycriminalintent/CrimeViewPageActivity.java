package com.example.jialuzhang.mycriminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jialuzhang.mycriminalintent.models.Crime;
import com.example.jialuzhang.mycriminalintent.models.CrimeLab;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jialuzhang on 2017/3/12.
 */

public class CrimeViewPageActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;
    //创建选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //创建选项菜单响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                UUID id = (UUID)getIntent().getSerializableExtra("whichId");
                for(int i = 0;i < mCrimes.size();i ++){
                    Crime crime = mCrimes.get(i);
                    if(crime.getmId().equals(id)){
                        CrimeLab.get(this).deleteCrime(crime);
                        break;
                    }
                }
                //删除成功后应该直接返回上一级即可,但是这样有一个问题，就是返回上一级后再按返回键又会回来当前的编辑界面，这样会不会重复添加呢？
                // 答案是不会的，但是为什么呢？
                Intent intent = new Intent(this,CrimeListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);
        mCrimes = CrimeLab.get(this).getmCrimes();
        //为ViewPAger设置adapter
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getmId());
            }
            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(mCrimes.get(position).getmTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        UUID id = (UUID)getIntent().getSerializableExtra("whichId");
        for(int i = 0;i < mCrimes.size();i ++){
            Crime crime = mCrimes.get(i);
            if(crime.getmId().equals(id)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
