package com.example.plugin1.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.plugin1.R;
import com.github.mikephil.charting.formatter.IFillFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.io.File.separator;

/**
 * Created by Administrator on 2019/3/21.
 */

public class CollectionFragment extends Fragment implements SensorEventListener {
    //long duration = 5;
    ArrayList<Float> list_x = new ArrayList<>();
    ArrayList<Float> list_y = new ArrayList<>();
    ArrayList<Float> list_z = new ArrayList<>();
    ArrayList<Float> list_r = new ArrayList<>();
    private final String path = Environment.getExternalStorageDirectory() + separator;
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] floats = event.values;
        list_x.add(floats[0]);
        list_y.add(floats[1]);
        list_z.add(floats[2]);
        double x = floats[0];
        double y = floats[1];
        double z = floats[2];
        float r = (float) Math.sqrt(x*x+y*y+z*z);
        list_r.add(r);
        //list_r = Math.sqrt( floats[0]*floats[0]+floats[1]*floats[1]+floats[2]*floats[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    View mView;
    Button mButton_start;
    Button mButton_stop;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_fragment,container,false);
        mView = view;
        initView();
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView() {
        if (mView == null){
            return;
        }
        mButton_start = mView.findViewById(R.id.button_collect);
        mButton_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerService();
            }
        });
        mButton_stop = mView.findViewById(R.id.button_stop);
        mButton_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
    }

    private void stopService() {
        if (mSensorManager != null){
            mSensorManager.unregisterListener(this);
            // todo 写入
            prepareFile();
        }
    }

    private void prepareFile() {
       /* Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");*/
        String x ="collect_x.txt";
        String y = "collect_y.txt";
        String z = "collect_z.txt";
        String r = "collect_r.txt";
        File file_x = new File(path,x);
        File file_y = new File(path,y);
        File file_z = new File(path,z);
        File file_r = new File(path,r);
        writeContent(file_x,1);
        writeContent(file_y,2);
        writeContent(file_z,3);
        writeContent(file_r,4);
    }

    private void writeContent(File file,int index) {
        BufferedWriter writer = null;
        StringBuilder sb = new StringBuilder();
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));

            switch (index){

                case 1:
                    for (Float f: list_x){
                        sb = sb.append(f).append(" ");
                    }
                    break;
                case 2:
                    for (Float f: list_y){
                        sb = sb.append(f).append(" ");
                    }
                    break;
                case 3:
                    for (Float f: list_z){
                        sb = sb.append(f).append(" ");
                    }
                    break;
                case 4:
                    for (Float f: list_r){
                        sb = sb.append(f).append(" ");
                    }
                    break;
            }
            writer.write(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    SensorManager mSensorManager;
    Sensor mSensor;
    void registerService(){
        if (mSensorManager == null){
            mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this,mSensor,10000); //采用100HZ采样
        }
    }
}
