package com.example.jialuzhang.mycriminalintent.models;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/11.
 */

public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context context, String filename){
        this.mContext = context;
        this.mFilename = filename;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
        JSONArray jsonArray = new JSONArray();
        for(Crime c : crimes){
            jsonArray.put(c.toJSON());
        }
        Log.d("CriminalIntentJSONSerializer","保存中");
        Writer write =  null;
        try {
            OutputStream outputStream = mContext.openFileOutput(mFilename,Context.MODE_PRIVATE);
            //该方法接受文件名 以及文件操作模式参数，会自动将传入的文件名附加到应用沙盒文件目录路径之后，形成一个新路径，然后在新路径下创建并打开文件，等等待数据写入
            write = new OutputStreamWriter(outputStream);
            write.write(jsonArray.toString());
            Log.d("CriminalIntentJSONSerializer",jsonArray.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(write != null) {
                write.close();
                Log.d("Crime","关闭writer");
                InputStream in = mContext.openFileInput(mFilename);
                Log.d("Crime",in.toString());
                BufferedReader bufferedReader = null;
                bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;
                if(bufferedReader.readLine() == null){
                    Log.d("Crime","bufferreader是空的");
                }
                else{
                    Log.d("Crime","bufferreader不是空的");
                }
                bufferedReader.close();
            }
        }
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        Log.d("Crime","取数据1");
        BufferedReader bufferedReader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            Log.d("Crime",in.toString());
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            Log.d("Crime","取数据3");
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine())!=null){
                jsonString.append(line);
                //Log.d("Crime",line);
            }
            Log.d("Crime",jsonString.toString());
            Log.d("Crime","取数据5");
            try {
                JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                Log.d("Crime","jsonArray:"+jsonArray.toString());
                for(int i = 0;i < jsonArray.length();i ++){
                    Log.d("Crime","怎么得不到数组里的元素？");
                    crimes.add(new Crime(jsonArray.getJSONObject(i)));
                    Log.d("Crime","已经得到数组里的元素了？");
                }
            }catch (Exception e){
                Log.d("Crime","取数据6",e);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (bufferedReader!=null){
                bufferedReader.close();
            }
        }
        return crimes;
    }
}
