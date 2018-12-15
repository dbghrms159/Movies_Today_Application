package com.example.hogeun.moviestoday;

/**
 * Created by hogeun on 2017-06-15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SplashActivity extends Activity {
    @Override

    protected  void onCreate(Bundle savedInstanceState){
        super .onCreate(savedInstanceState);

        try{
            Thread.sleep(3000); // 대기초 설정 밀리세컨드임
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}
