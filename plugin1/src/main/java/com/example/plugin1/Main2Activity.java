package com.example.plugin1;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.DaggerActivity_MembersInjector;

public class Main2Activity extends AppCompatActivity {
    @BindView(R.id.button2)
    Button mButton;
    @OnClick(R.id.button2)
    public void submit(){

    }
    @Inject
    MyModel mMyModel;
    Unbinder mUnbinder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mUnbinder = ButterKnife.bind(this);
        DaggerMain2ActivityComponent.builder().build();
        //DaggerMain2Activity.builder().build();
        //DaggerActivity_MembersInjector
        //DraggerMain2ActivityComponent.builder().build();
    }
}
