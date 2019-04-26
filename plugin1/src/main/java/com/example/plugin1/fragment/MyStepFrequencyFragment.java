package com.example.plugin1.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plugin1.R;
import com.example.plugin1.StepDector;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.hardware.SensorManager.SENSOR_DELAY_GAME;
import static java.io.File.separator;

/**
 * Created by Administrator on 2019/3/21.
 */

public class MyStepFrequencyFragment extends Fragment implements SensorEventListener, StepDector.StepCountListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private StepDector mStepDector = new StepDector();
    private TextView mTextView; //步数tv
    private final String path = Environment.getExternalStorageDirectory() + separator;
    private BarChart barChart;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_frequency, container, false);
        // todo
        barChart = (BarChart)view.findViewById(R.id.chart1);
        view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initService();
            }
        });
        mTextView = (TextView) view.findViewById(R.id.textView);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                flag_target = false;
                current = 0;
                currentStep = 0;
                mStepFrequencyData.clear();
                for (int i= 0;i< 20;i++){
                    mStepFrequencyData.put(i,0);
                }
            }
        });
        initView();
        //initService();
        return view;

    }

    private void initService() {
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mStepDector != null) {
                mStepDector.initListener(this);
            }
            mSensorManager.registerListener(mStepDector, mSensor, 10000);
        }
        mStepFrequencyData.clear();
        for (int i= 0;i< 20;i++){
            mStepFrequencyData.put(i,0);
        }
        current = 0;
        currentStep = 0;
        flag_target = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }
    //test
    public ArrayList<Integer> printMatrix(int [][] matrix) {
        if (matrix == null){
            return null;
        }
        ArrayList<Integer> list = new ArrayList<>();
        if(matrix==null || matrix.length==0) { return list ; }
        int end_y = matrix.length; //y
        int end_x = matrix[0].length; // x
        int start_x = 0;
        int start_y = 0;
        //ArrayList<Integer> list = new ArrayList<>();
        while(end_x>=start_x && end_y >= start_y){
            list.addAll(print(matrix,start_x,start_y,end_x,end_y));
            start_x++;
            start_y++;
            end_x--;
            end_y--;
        }
        return list;
    }

    private ArrayList<Integer> print(int[][] matrix,int topLeft_x,int topLeft_y,int bottomRight_x,int bottomRight_y){
        ArrayList<Integer> list = new ArrayList<>();
        for (int i= topLeft_x;i<=topLeft_x;i++){
            list.add(matrix[i][topLeft_y]);
        }
        if (topLeft_y == bottomRight_y){
            return list;
        }
        //topLeft_x = topLeft_x+1;
        for (int i = topLeft_y+1;i<bottomRight_y;i++){
            list.add(matrix[bottomRight_x][i]);
        }
        for (int i = bottomRight_x;i>=topLeft_x;i--){
            list.add(matrix[i][bottomRight_y]);
        }
        for (int i = bottomRight_y-1;i>topLeft_y;i--){
            list.add(matrix[topLeft_x][i]);
        }
        return list;
    }
    private void initView() {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // 缓存步频分布，停止之后才写入数据库,暂0.4-。6上200个小区间
    private static TreeMap<Integer, Integer> mStepFrequencyData = new TreeMap<>();
    private  static List<Integer> targetList = new ArrayList<>();
    private  static List<Integer> currentList = new ArrayList<>();
    private TreeMap<Integer,Integer> temp_ui = new TreeMap<>();
    static {
        for (int i= 0;i<= 20;i++){
            mStepFrequencyData.put(i,0);
        }
        /*for (int i= 0;i<= 20;i++){
            targetList.add(new Integer(0));
        }
        for (int i= 0;i<= 20;i++){
            currentList.add(new Integer(0));
        }*/
    }
    private int current = 0;
    private int currentStep = 0;
    private int index = 0;

    @Override
    public void countStep(long dur) {
        //currentStep++;
        int duration =Math.abs( Math.round(dur - 400)/10);
        if (duration>= 0 && duration<=20){
            currentStep++;
            mTextView.setText(String.valueOf(currentStep));
        }else {
            return;
        }

        //Toast.makeText(getActivity(), String.valueOf(duration), Toast.LENGTH_SHORT).show();
        if (current < 200) {
            int count = 1;
                if (mStepFrequencyData.get(duration) != null) {
                    count = mStepFrequencyData.get(duration) + 1;
                }
                mStepFrequencyData.put(duration, count);

            current++;
        } else {
            // todo 清空写入文件
            Toast.makeText(getActivity(), "到达500步了，清一下缓存吧亲~", Toast.LENGTH_LONG).show();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String s = format.format(date)+"-"+String.valueOf(index++);
            File file = new File(path,s+"step.txt");

            try {
                //FileOutputStream outputStream = new FileOutputStream(file,true);
                StringBuilder builder = new StringBuilder();
                Set<Integer> set = mStepFrequencyData.keySet();
                for (Integer integer : set) {
                    int stepcount = mStepFrequencyData.get(integer);
                    builder.append(String.valueOf(integer)+"  :").append(String.valueOf(stepcount)).append("\n");
                }
                prepareFile(file,builder.toString());
                //outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //flag = false;
                //current = 0;

                if (flag_target){

                    copyToTarget(mStepFrequencyData,targetList);

                }else{
                    copyToTarget(mStepFrequencyData,currentList);
                    mTextView.setText(String.valueOf(KLUtils.getKLDistance(targetList,currentList)));
                }
                mStepFrequencyData.clear();
                for (int i= 0;i< 20;i++){
                    mStepFrequencyData.put(i,0);
                }
                currentStep = 0;
            }

        }
    }

    // 拷贝到目标里去
    private void copyToTarget(TreeMap<Integer, Integer> data,List<Integer> list) {
        Iterator<Map.Entry<Integer,Integer>> iterator = data.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer,Integer> map = iterator.next();
            list.add(new Integer(map.getValue()));
            temp_ui.put(map.getKey(),map.getValue());
        }
        initBarChart(temp_ui);
    }

    private boolean flag_target = true;
    private void prepareFile(File file, String content) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            //writer.newLine();
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // lru缓存
    private void lruCache(){
        SharedPreferences preferences  = getActivity().getSharedPreferences("ss",Context.MODE_PRIVATE);
        //LinkedHashMap
    }

    @Override
    public void callbackFragment(SensorEvent event) {

    }

    private void initBarChart(TreeMap<Integer,Integer> hashMap){
        if (hashMap == null){
            return;
        }
        Description description = barChart.getDescription();
        description.setText("步频分布");
        description.setTextSize(10f);
        barChart.setNoDataText("no data.");
        // 集双指缩放
        barChart.setPinchZoom(false);
        barChart.animateY(2000);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        Iterator<Map.Entry<Integer,Integer>> iterator = hashMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer,Integer> entry = iterator.next();
            barEntries.add(new BarEntry(entry.getKey(),entry.getValue()));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "error times");

        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(true);//是否显示柱子的数值
        barData.setValueTextSize(10f);//柱子上面标注的数值的字体大小
        barData.setBarWidth(0.8f);//每个柱子的宽度
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);//是否显示x坐标的数据
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x坐标数据的位置
        xAxis.setDrawGridLines(false);//是否显示网格线中与x轴垂直的网格线
        xAxis.setLabelCount(20, true);//设置x轴显示的标签的个数
        final List<String> xValue = new ArrayList<>();
        for (int i =0;i<21;i++){
            xValue.add(String.valueOf(i));
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValue));//设置x轴标签格式化器

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setDrawGridLines(false);
        rightYAxis.setEnabled(true);//设置右侧的y轴是否显示。包括y轴的那一条线和上面的标签都不显示
        rightYAxis.setDrawLabels(false);//设置y轴右侧的标签是否显示。只是控制y轴处的标签。控制不了那根线。
        rightYAxis.setDrawAxisLine(false);//这个方法就是专门控制坐标轴线的

        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setEnabled(true);
        leftYAxis.setDrawLabels(true);
        leftYAxis.setDrawAxisLine(true);
        leftYAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftYAxis.setDrawGridLines(false);//只有左右y轴标签都设置不显示水平网格线，图形才不会显示网格线
        //leftYAxis.setDrawGridLinesBehindData(true);//设置网格线是在柱子的上层还是下一层（类似Photoshop的层级）
        leftYAxis.setGranularity(1f);//设置最小的间隔，防止出现重复的标签。这个得自己尝试一下就知道了。
        leftYAxis.setAxisMinimum(0);//设置左轴最小值的数值。如果IndexAxisValueFormatter自定义了字符串的话，那么就是从序号为2的字符串开始取值。
        leftYAxis.setSpaceBottom(0);//左轴的最小值默认占有10dp的高度，如果左轴最小值为0，一般会去除0的那部分高度
        //自定义左侧标签的字符串，可以是任何的字符串、中文、英文等
        leftYAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{ "0", "1", "2", "3", "4", "5"}));

    }
}
