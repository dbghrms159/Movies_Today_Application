package com.example.hogeun.moviestoday;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewMovie extends AppCompatActivity  {
    private WebView webView;
    private WebSettings webSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_movie);
        getSupportActionBar().setTitle("예매하기");
        Intent intent = getIntent();
        String url = intent.getStringExtra("data");

        webView = (WebView) findViewById(R.id.web);
        webView.setWebViewClient(new WebViewClient());
        webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);

        webView.loadUrl(url);
    }

}
