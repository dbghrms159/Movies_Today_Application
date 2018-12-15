package com.example.hogeun.moviestoday;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static android.R.id.list;

public class Reservation extends AppCompatActivity {
    ListView list_v ;
    ArrayList<String> list;
    String url = "http://movie.naver.com/movie/running/premovie.nhn?order=interestRate";
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        getSupportActionBar().setTitle("개봉예정작");
        list_v = (ListView) findViewById(R.id.nextList);
        list_v.setOnItemClickListener(onClickListItem);

        JsoupTask task = new JsoupTask();
        task.execute();
    }

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

                for(int i = 0;i < next.size();++i){

                    list.add(next.get(i).text());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.listtextview,list);

            list_v.setAdapter(adapter);
        }
    }

    public AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //Intent intents = new Intent();
            //intents.putExtra("movie",list.get(position));
            YouTube tube = new YouTube(list.get(position));
            tube.execute();
            /*Intent intent = new Intent(getApplicationContext(), YouTube.class);
            intent.putExtra("data",list.get(position));
            startActivity(intent);*/
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
