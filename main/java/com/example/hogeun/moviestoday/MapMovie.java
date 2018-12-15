package com.example.hogeun.moviestoday;

import android.Manifest;
import android.content.Context;
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
import android.widget.Toast;


public class MapMovie extends AppCompatActivity implements LocationListener {
    private Location location;
    private LocationManager locationManager;
    private WebView webView;
    private WebSettings webSetting;
    private double mLatitude;
    private double mLongitude;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_movie);
        getSupportActionBar().setTitle("주변 영화관");

        webView = (WebView) findViewById(R.id.map);
        webView.setWebViewClient(new WebViewClient());
        webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //finish();
                return;
            }


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1,1,this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
            Log.d("좌표 테스트", mLatitude + "," +  mLongitude);
            url = "https://www.google.co.kr/maps/search/%EC%98%81%ED%99%94%EA%B4%80/@" + mLatitude + "," + mLongitude + ",15z";
            Log.d("테스트", url);
            webView.loadUrl(url);
        }catch (Exception e){
            Log.d("테스트","좌표 없음");
            Toast toast = Toast.makeText(MapMovie.this,"GPS가 잡히지 않습니다.",Toast.LENGTH_SHORT );
            toast.show();

            finish();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLongitude = location.getLongitude();
        mLatitude = location.getLatitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
