package com.example.hogeun.moviestoday;

import android.content.Intent;
import android.net.Uri;
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
/*
public class CGV extends AppCompatActivity {

    ListView list_v ;
    ArrayList<String> list,list_url;
    String url = "http://www.cgv.co.kr/movies/";
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgv);


        getSupportActionBar().setTitle("CGV");
        list_v = (ListView) findViewById(R.id.cgvList);
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
                list_url = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                Elements root = doc.select("div[id=contents]");
                Elements next = root.select("div").select("ol").select("div[class=box-contents]").select("a");

                for(int i = 0;i < next.size();++i){
                    if(i%2 == 0) {
                        list.add(next.get(i).text());
                        list_url.add(next.get(i).attr("href"));
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,list);
            list_v.setAdapter(adapter);
        }
    }

    public AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cgv.co.kr"+list_url.get(position)));

            //intent.putExtra("data",lists.get(position));
            startActivity(intent);
        }
    };
}

*/