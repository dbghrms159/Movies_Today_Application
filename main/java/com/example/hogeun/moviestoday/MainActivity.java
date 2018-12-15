package com.example.hogeun.moviestoday;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ArrayList<String> list, lists;
    String url = "http://movie.naver.com/movie/running/current.nhn?view=list&tab=normal&order=reserve";
    ArrayAdapter<String> adapter;
    ListView list_v, list_w;

    LocationManager locationManager;
    double mLatitude;
    double mLongitude;
    //ImageView ;
    ImageButton go,back,aniGo,aniBack, imageView,aniImage;//역대 영화& 애니메이션 best 이미지 버튼 밎 이동 키
    int contnum=0,countani = 0;
    String movies[] = {"spQtwggaCy4", "BBEDtovULHY", "El8gg8XH7X4", "zS0BA8FnnKc", "GzAuXSB3108"};// 역대 영화 흥행률 youtube url
    String ani[] = {"bbh1NIpDo-c", "Bp9gE41mcmw", "1KGZtWbZtq8", "dc_xyYxTy-U", "XDncZq_zVNs"};//애니메이션 영화 흥행률 youtube url
    boolean popup = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(popup) {
            startActivity(new Intent(MainActivity.this, Popup.class));
            popup = !popup;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        //GPS가 켜져있는지 체크
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
            finish();
        }

        //마시멜로 이상이면 권한 요청하기
        if (Build.VERSION.SDK_INT >= 23) {
            //권한이 없는 경우
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            //권한이 있는 경우
            else {
                requestMyLocation();
            }
        }
        //마시멜로 아래
        else {
            requestMyLocation();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        list_v = (ListView) findViewById(R.id.mainList);
        list_v.setOnItemClickListener(onClickListItem);
       //list_w = (ListView) findViewById(R.id.mainLists);


        //list_w.setAdapter(adapter);
        JsoupTask task = new JsoupTask();
        task.execute();

        imageView = (ImageButton) findViewById(R.id.no1);
        imageView.setOnClickListener(this);

        aniImage = (ImageButton) findViewById(R.id.aniNum);
        aniImage.setOnClickListener(this);

        go = (ImageButton)findViewById(R.id.go);
        back = (ImageButton)findViewById(R.id.back);
        go.setOnClickListener(this);
        back.setOnClickListener(this);

        aniGo = (ImageButton)findViewById(R.id.aniGo);
        aniBack = (ImageButton)findViewById(R.id.aniBack);
        aniGo.setOnClickListener(this);
        aniBack.setOnClickListener(this);
        //AdView ad = (AdView) findViewById(R.id.adView3);
        //AdRequest adRequ = new AdRequest.Builder().build();
        //ad.loadAd(adRequ);
    };




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //ACCESS_COARSE_LOCATION 권한
        if (requestCode == 1) {
            //권한받음
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestMyLocation();
            }
            //권한못받음
            else {
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //나의 위치 요청
    public void requestMyLocation() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //요청
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
    }

    locationListener locationListener = new locationListener();


    //위치정보 구하기 리스너
    public class locationListener implements LocationListener {
        public locationListener() {
        }

        Location location;

        public void onLocationChanged(Location location) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //나의 위치를 한번만 가져오기 위해
            locationManager.removeUpdates(locationListener);
            this.location = location;
            //위도 경도
            mLatitude = location.getLatitude();   //위도
            mLongitude = location.getLongitude(); //경도

        }

        public Double getLat() {

            return mLatitude;

        }

        public Double getLong() {

            return mLongitude;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("gps", "onStatusChanged");

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    // 상영중인 영화 파싱
    public class JsoupTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                list = new ArrayList<String>();
                Document doc = Jsoup.connect(url).get();
                Elements root = doc.select("div[id=content]");
                Elements next = root.select("div").select("dt").select("a");


                for (int i = 0; i < 5; ++i) {

                    list.add(next.get(i).text());


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listtextview, list);
            list_v.setAdapter(adapter);

        }
    }
    @Override // 이미지 버튼에 대한 이벤트 처리
    public void onClick(View v) {
        if(v == go){
            if(contnum<4)
                contnum++;
            else{
                contnum = 0;
            }
        }
        if(v == back){
            if(contnum>0)
                contnum--;
            else{
                contnum = 4;
            }
        }

        if(v == aniGo){
            if(countani<4)
                countani++;
            else{
                countani = 0;
            }
        }
        if(v == aniBack){
            if(countani>0)
                countani--;
            else{
                countani = 4;
            }
        }

        if(v == imageView){
            Intent intent = new Intent(getApplicationContext(), youtub.class);

            intent.putExtra("data",movies[contnum]);
            startActivity(intent);
        }

        if(v == aniImage){
            Intent intent = new Intent(getApplicationContext(), youtub.class);

            intent.putExtra("data",ani[countani]);
            startActivity(intent);
        }
        switch (contnum){
            case 0:
                imageView.setImageResource(R.drawable.no1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.no2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.no3);
                break;
            case 3:
                imageView.setImageResource(R.drawable.no4);
                break;
            case 4:
                imageView.setImageResource(R.drawable.no5);
                break;
        }
        switch (countani){
            case 0:
                aniImage.setImageResource(R.drawable.ani1);
                break;
            case 1:
                aniImage.setImageResource(R.drawable.ani2);
                break;
            case 2:
                aniImage.setImageResource(R.drawable.ani3);
                break;
            case 3:
                aniImage.setImageResource(R.drawable.ani4);
                break;
            case 4:
                aniImage.setImageResource(R.drawable.ani5);
                break;
        }
        imageView.invalidate();
        aniImage.invalidate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            Intent intent = new Intent(getApplicationContext(), Reservation.class);
            // Intent intent  = getIntent();
            //lists = new ArrayList();
            //lists = intent.getStringArrayListExtra("movie");
            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, lists);
            startActivity(intent);
        } else if (id == R.id.nav_cgv) {//CGV 웹뷰
            Intent intent = new Intent(getApplicationContext(), WebViewMovie.class);
            intent.putExtra("data", "http://www.cgv.co.kr/movies");
            startActivity(intent);
        } else if (id == R.id.nav_lotte) {
            Intent intent = new Intent(getApplicationContext(), WebViewMovie.class);
            intent.putExtra("data", "http://www.lottecinema.co.kr/");
            startActivity(intent);
        } else if (id == R.id.nav_maga) {
            Intent intent = new Intent(getApplicationContext(), WebViewMovie.class);
            intent.putExtra("data", "http://www.megabox.co.kr/?menuId=movie");
            startActivity(intent);
        } else if (id == R.id.nav_gps) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return true;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
            Intent intent = new Intent(getApplicationContext(), MapMovie.class);

            startActivity(intent);
        }else if(id == R.id.nav_now){
            Intent intent = new Intent(getApplicationContext(), movies.class);

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            YouTube you  = new YouTube(list.get(position));
            you.execute();
        }
    };

    public class YouTube extends AsyncTask<Void, Void, Void> {

        private String url ;
        private Document doc ;
        private Elements links ,linkss ;
        private String linksss ,urls = "https://www.youtube.com/results?search_query=";//"https://www.youtube.com/results?search_query=";
        private int a ;
        String linked ;
        public YouTube(String urls){
            //doInBackground();
            this.urls += urls;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                //urls += intent.getStringExtra("data");

                doc = Jsoup.connect(urls).get();
                links = doc.select("div[id=content]");
                linkss = links.select("div").select("h3").select("a");
                //for(int a = 0; a < linkss.size();a++)
                linksss = linkss.get(a).attr("href")+"\n";
                //linksss += linkss.get(a+1).attr("href")+"\n";
                a = linksss.indexOf("=");
                linksss = linksss.substring(a+1);
                linked = linksss.trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(getApplicationContext(), com.example.hogeun.moviestoday.youtub.class);
            intent.putExtra("data",linked);
            startActivity(intent);

        }

    }

}