package com.example.jialuzhang.mycriminalintent;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jialuzhang.mycriminalintent.models.Crime;
import com.example.jialuzhang.mycriminalintent.models.CrimeLab;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/12.
 */

public class CrimeListFragment extends ListFragment {
    private static final String TAG = "CrimeListFragment";
    private ArrayList<Crime> mCrimes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG,"onCreate");
        mCrimes = CrimeLab.get(getActivity()).getmCrimes();
        setListAdapter(new ArrayAdapter<Crime>(getActivity(),0,mCrimes) {
            @Override
            public int getCount() {
                return mCrimes.size();
            }

            @Nullable
            @Override
            public Crime getItem(int position) {
                return mCrimes.get(position);
            }

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
                }
                Crime crime = mCrimes.get(position);
                TextView textView = (TextView) convertView.findViewById(R.id.crime_title);
                TextView textView1 = (TextView) convertView.findViewById(R.id.crime_data);
                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.crime_solved);
                textView.setText(crime.getmTitle());
                textView1.setText(crime.getmDate().toString());
                checkBox.setChecked(crime.ismSolved());
                return convertView;
            }
        });
    }
    //实例化选项菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
    }
    //实例化上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
    }

    //选项菜单的响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = new Intent(getActivity(),CrimeViewPageActivity.class);
                intent.putExtra("whichId",crime.getmId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //上下文菜单的响应

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = info.position;
        Crime crimes = (Crime) getListAdapter().getItem(pos);
        Log.d("Menu","选中菜单");
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crimes);
                ((ArrayAdapter<Crime>)getListAdapter()).notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        ListView listView = (ListView)view.findViewById(android.R.id.list);
        registerForContextMenu(listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);    //设置列表试图的多选模式
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.crime_list_item_context,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_delete_crime:
                        ArrayAdapter<Crime> adapter = (ArrayAdapter<Crime>) getListAdapter();
                        CrimeLab crimeLab = CrimeLab.get(getActivity());
                        for(int i = adapter.getCount()-1;i >= 0;i--){
                            if(getListView().isItemChecked(i)){
                                crimeLab.deleteCrime(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        adapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime crime = (Crime)getListAdapter().getItem(position);
        Log.d(TAG,crime.getmTitle() + "is clicked");
        Intent intent = new Intent(getActivity(),CrimeViewPageActivity.class);
        intent.putExtra("whichId",crime.getmId());
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        ((ArrayAdapter<Crime>)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            CrimeLab.get(getActivity()).saveCrimes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
