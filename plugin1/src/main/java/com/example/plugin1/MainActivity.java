package com.example.plugin1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.plugin1.fragment.CollectionFragment;
import com.example.plugin1.fragment.MyStepFrequencyFragment;

public class MainActivity extends AppCompatActivity {
    private FrameLayout contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        contentView = (FrameLayout) findViewById(R.id.fl_content);
        ((RadioGroup) findViewById(R.id.bottom_tabs)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_samping:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new MyStepFrequencyFragment())
                                .commit();
                        break;
                    // todo 小论文实验
                    case R.id.tab_forecast:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new CollectionFragment()).commit();
                        break;
                    default:
                        break;

                }
            }
        });
        ((RadioGroup) findViewById(R.id.bottom_tabs)).check(R.id.tab_samping);
    }
    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} ,1);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){

        }
    }
}
