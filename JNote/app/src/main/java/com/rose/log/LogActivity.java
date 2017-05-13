/*
 *  Copyright (C) 2015, Jhuster, All Rights Reserved
 *
 *  Author:  Jhuster(lujun.hust@gmail.com)
 *  
 *  https://github.com/Jhuster/JNote
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 */
package com.rose.log;

import java.io.File;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class LogActivity extends BaseActivity {

    private static final String DEFAULT_DIR = Environment.getExternalStorageDirectory() + File.separator + "JNote";

    private TextView mTextView;
    private ScrollView mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_display);
        mRootView = (ScrollView) findViewById(R.id.DisplayRootView);
        mTextView = (TextView) findViewById(R.id.DisplayTextView);
    }

    @Override
    protected void loadData() {
        mTextView.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.rms_menu){
            sendRequestWithHttpClient("http://122.152.199.180/error.log");
        }
        if(item.getItemId() == R.id.rosepay_menu){
            sendRequestWithHttpClient("http://122.152.202.150/error.log");
        }
        if(item.getItemId() == R.id.openapi_menu){
            sendRequestWithHttpClient("http://122.152.197.128/error.log");
        }
        if(item.getItemId() == R.id.admin_menu){
            sendRequestWithHttpClient("http://122.152.199.219/error.log");
        }
        if(item.getItemId() == R.id.front_menu){
            sendRequestWithHttpClient("http://122.152.204.219/error.log");
        }
        if(item.getItemId() == R.id.member_menu){
            sendRequestWithHttpClient("http://122.152.199.126/error.log");
        }
        if(item.getItemId() == R.id.task_menu){
            sendRequestWithHttpClient("http://122.152.202.217/error.log");
        }
        if(item.getItemId() == R.id.order_menu){
            sendRequestWithHttpClient("http://122.152.200.28/error.log");
        }
        if(item.getItemId() == R.id.clear_menu){
          mTextView.setText("");
        }
        return super.onOptionsItemSelected(item);
    }

    //方法：发送网络请求，获取百度首页的数据。在里面开启线程
    private void sendRequestWithHttpClient(final String url) {
          new Thread(new Runnable() {
              @Override
              public void run() {
                  try {
                      Message msg0 = new Message();
                      msg0.what = 0;
                      mHandler.sendMessage(msg0);

                      HttpClient httpCient = new DefaultHttpClient();
                      // 请求超时
                      httpCient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
                      // 读取超时
                      httpCient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000    );
                      HttpGet httpGet = new HttpGet(url);
                      HttpResponse httpResponse = httpCient.execute(httpGet);
                      if (httpResponse.getStatusLine().getStatusCode() == 200) {
                          HttpEntity entity = httpResponse.getEntity();
                          String response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                          Message msg = new Message();
                          msg.what = 2;
                          Bundle data = new Bundle();
                          data.putString("msg",response);
                          msg.setData(data);
                          mHandler.sendMessage(msg);
                      }
                  }catch (Exception e){
                      Message msg = new Message();
                      msg.what = 1;
                      Bundle data = new Bundle();
                      data.putString("msg",e.getMessage());
                      msg.setData(data);
                      mHandler.sendMessage(msg);
                  }finally {
                      Message msg4 = new Message();
                      msg4.what = 4;
                      mHandler.sendMessage(msg4);
                  }
              }
          }).start();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setTitle("JLog         日志读取中...");
                    break;
                case 1:
                    Toast.makeText(LogActivity.this, msg.getData().get("msg").toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    mTextView.setText(msg.getData().get("msg").toString());
                    int offset = mTextView.getMeasuredHeight() - mRootView.getHeight();
                    if (offset < 0) {
                        offset = 0;
                    }
                    mRootView.scrollTo(0, offset);
                    break;
                case 4:
                    setTitle("JLog");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initVariables() {
    }

}
