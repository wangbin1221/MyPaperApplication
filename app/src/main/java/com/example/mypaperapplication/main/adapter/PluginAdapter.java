package com.example.mypaperapplication.main.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypaperapplication.R;
import com.example.mypaperapplication.main.PluginItem;
import com.ryg.utils.DLUtils;

import java.io.File;

/**
 * Created by Administrator on 2019/3/21.
 */

public class PluginAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private PluginItem[] mPluginItems;
    private Context mContext;
    public PluginAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    public void setData(PluginItem[] pluginItems){
        mPluginItems = pluginItems;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(mPluginItems== null || mPluginItems.length == 0){
            return 0;
        }
        return mPluginItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mPluginItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.plugin_item, parent, false);
            holder = new ViewHolder();
            holder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.appName = (TextView) convertView.findViewById(R.id.app_name);
            holder.apkName = (TextView) convertView.findViewById(R.id.apk_name);
            holder.packageName = (TextView) convertView.findViewById(R.id.package_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PluginItem item = mPluginItems[position];
        PackageInfo packageInfo = item.packageInfo;
        holder.appIcon.setImageDrawable(DLUtils.getAppIcon(mContext, item.pluginPath));
        holder.appName.setText(DLUtils.getAppLabel(mContext, item.pluginPath));
        holder.apkName.setText(item.pluginPath.substring(item.pluginPath.lastIndexOf(File.separatorChar) + 1));
        holder.packageName.setText(packageInfo.applicationInfo.packageName + "\n" +
                item.launcherActivityName + "\n" +
                item.launcherServiceName);
        return convertView;
    }

private static class ViewHolder {
    public ImageView appIcon;
    public TextView appName;
    public TextView apkName;
    public TextView packageName;
}

}

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PluginItem item = mPluginItems.get(position);
        DLPluginManager pluginManager = DLPluginManager.getInstance(this);
        pluginManager.startPluginActivity(this, new DLIntent(item.packageInfo.packageName, item.launcherActivityName));

        //如果存在Service则调用起Service
        if (item.launcherServiceName != null) {
            //startService
            DLIntent intent = new DLIntent(item.packageInfo.packageName, item.launcherServiceName);
            //startService
//	        pluginManager.startPluginService(this, intent);

            //bindService
//	        pluginManager.bindPluginService(this, intent, mConnection = new ServiceConnection() {
//                public void onServiceDisconnected(ComponentName name) {
//                }
//
//                public void onServiceConnected(ComponentName name, IBinder binder) {
//                    int sum = ((ITestServiceInterface)binder).sum(5, 5);
//                    Log.e("MainActivity", "onServiceConnected sum(5 + 5) = " + sum);
//                }
//            }, Context.BIND_AUTO_CREATE);
        }*/



