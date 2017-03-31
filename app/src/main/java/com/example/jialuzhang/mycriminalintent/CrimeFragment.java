package com.example.jialuzhang.mycriminalintent;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.ActionBarDrawerToggle;

import com.example.jialuzhang.mycriminalintent.camera.CrimeCameraActivity;
import com.example.jialuzhang.mycriminalintent.models.Crime;
import com.example.jialuzhang.mycriminalintent.models.CrimeLab;
import android.support.v7.app.ActionBar;

import java.util.Date;
import java.util.UUID;

/**
 * Created by jialuzhang on 2017/3/12.
 */

public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_PHONE = 2;
    Button dateButton;
    EditText crime_title;
    CheckBox crime_solved;
    ImageButton mImageButton;
    Button reportButton ;
    Button suspectButton;
    private Crime mCrime;

    public static CrimeFragment newInstance(UUID id){
        Bundle argc = new Bundle();
        argc.putSerializable("whichId",id);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(argc);
        return crimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID id = (UUID)getArguments().getSerializable("whichId");
        mCrime = CrimeLab.get(getActivity()).getCrime(id);
        Log.d(TAG,"CrimeFragment created");
    }
    @TargetApi(11)
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crime_detail,container,false);
        /*实现层级导航，左上角的返回按钮
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        dateButton = (Button)view.findViewById(R.id.crime_date_button);
        crime_solved = (CheckBox)view.findViewById(R.id.crime_solved);
        crime_title = (EditText)view.findViewById(R.id.crime_title);
        mImageButton = (ImageButton)view.findViewById(R.id.image_button);
        reportButton = (Button)view.findViewById(R.id.report_button);
        suspectButton = (Button)view.findViewById(R.id.suspect_button);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,"Crime Report");
                startActivity(intent);
            }
        });
        suspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,REQUEST_PHONE);
            }
        });
        if(mCrime.getmSuspect() != null){
            suspectButton.setText(mCrime.getmSuspect());
        }
        dateButton.setText(mCrime.getmDate().toString());
        crime_title.setText(mCrime.getmTitle());
        crime_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickeFragment datePickeFragment = DatePickeFragment.newInstance(mCrime.getmDate());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                datePickeFragment.setTargetFragment(CrimeFragment.this,REQUEST_CODE);
                datePickeFragment.show(fm,null);
            }
        });
        crime_solved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivity(intent);
            }
        });

        Log.d(TAG,"CrimeFragment onCreateView");
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == REQUEST_CODE){
            Date date = (Date)data.getSerializableExtra(DatePickeFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            dateButton.setText(mCrime.getmDate().toString());
            Log.d("RESULT","反馈开始");
        }
        else if (requestCode == REQUEST_PHONE){
            Uri uri = data.getData();
            String[] queryFiled = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor cursor = getActivity().getContentResolver().query(uri,queryFiled,null,null,null);
            if(cursor.getCount() == 0){
                cursor.close();
                return;
            }
            cursor.moveToFirst();
            String suspect = cursor.getString(0);
            mCrime.setmSuspect(suspect);
            suspectButton.setText(suspect);
            cursor.close();
        }

    }
    public String getReport(){
        String report = null;
        String dateFormate = "EEE,MMM dd";
        String dateString = DateFormat.format(dateFormate,mCrime.getmDate()).toString();
        String suspect = mCrime.getmSuspect();
        String solvedString = null;
        if(mCrime.ismSolved() == true){
            solvedString = getString(R.string.crime_solved);
        }
        else {
            solvedString = getString(R.string.crime_unsloved);
        }
        report = getString(R.string.crime_report,mCrime.getmTitle(),dateString,solvedString,suspect);
        return report;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"CrimeFragment onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"CrimeFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"CrimeFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"CrimeFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"CrimeFragment onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"CrimeFragment onDestroy");
    }
}
