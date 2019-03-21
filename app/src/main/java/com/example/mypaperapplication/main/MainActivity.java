package com.example.mypaperapplication.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mypaperapplication.R;
import com.example.mypaperapplication.main.adapter.PluginAdapter;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLPluginPackage;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {
    private PluginAdapter mPluginAdapter;
    private ListView mListView;
    private MainContract.MainPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT>=23){
            ArrayList<String> lists = new ArrayList<>();
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                lists.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (lists.size()>=1){
                requestPermissions(lists.toArray(new String[lists.size()]),1);
            }else {
                init();
            }
        }else{
            init();
        }


    }
    private void init(){
        new MainPresenter(this).start();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, permissions[i]+"已授权", Toast.LENGTH_SHORT).show();
                    init();
                }
                else {
                    Toast.makeText(MainActivity.this,permissions[i]+"拒绝授权",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    public void setPresenter(MainContract.MainPresenter presenter) {
        mPresenter = presenter;
        mPresenter.findApk();
    }

    @Override
    public void showApkData(final PluginItem[] packages) {
        mListView = findViewById(R.id.lv_content);
        mPluginAdapter = new PluginAdapter(this);
        mListView.setAdapter(mPluginAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PluginItem item = packages[position];
                DLIntent intent = new DLIntent(item.packageInfo.packageName,item.launcherActivityName);
                DLPluginManager.getInstance(MainActivity.this).startPluginActivity(MainActivity.this,intent);
            }
        });
        mPluginAdapter.setData(packages);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showMessage(String message) {

    }
}
