package com.example.jialuzhang.mycriminalintent.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jialuzhang.mycriminalintent.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by jialuzhang on 2017/3/13.
 */

public class CrimeCameraFragment extends Fragment {
    private Camera mCamera;
    private SurfaceView mSurfaceView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_camera,null);
        Button takeButton = (Button)view.findViewById(R.id.take_button);
        mSurfaceView = (SurfaceView)view.findViewById(R.id.crime_camera_surfaceView);

        takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();    //按下快门后当然是介绍当前的Activity，将图片传回上一个activity
            }
        });
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);    //已经废弃的代码，但还是先使用着
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(mCamera != null){
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(mCamera == null)return;
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPictureSizes(),width,height);

                parameters.setPreviewSize(s.width,s.height);
                if(mCamera!=null){
                    try {
                        mCamera.startPreview();
                    }catch (Exception e){
                        mCamera.release();
                        mCamera = null;
                    }

                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(mCamera!=null){
                    mCamera.stopPreview();
                }
            }
        });
        return view;
    }
    //获得最大适合尺寸
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes,int width,int heigth){
        Camera.Size bestSize = sizes.get(0);
        int bestArea = bestSize.width*bestSize.height;
        for(Camera.Size s : sizes){
            int area = s.height*s.width;
            if(area > bestArea){
                bestArea = area;
                bestSize = s;
            }
        }
        return bestSize;
    }
    @Override
    public void onResume() {
        super.onResume();
        //打开相机
        mCamera = android.hardware.Camera.open(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera!=null){
            mCamera.release();
            mCamera = null;
        }
    }
}
