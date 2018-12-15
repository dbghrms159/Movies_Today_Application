package com.example.hogeun.moviestoday;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;

public class youtub extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    //YouTube Data API v3 서비스를 사용하기 API 키 필용
    //새 키를 생성하려면   Google APIs Console 에서 발급
    //private static final String YoutubeDeveloperKey = "AIzaSyDEP5fH9dm3aN9ShwgnzwYi3YaZ4ODLFm8";

    private String URL ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtub);

        Log.d("youtube Test",
                "사용가능여부:"+YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this)); //SUCCSESS


        try {
            Intent intent = getIntent();
            URL = intent.getStringExtra("data");
            Log.d("테스트", URL);

            getYouTubePlayerProvider().initialize("AIzaSyA6kpohPinFEAxuol0fEgcvXr0d3kylwX8", this);

        }catch(Exception e){}
    }



    @Override
    public void onInitializationSuccess(Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            player.cueVideo(URL);

        }
    }

    //플레이어가 초기화되지 못할 때 호출됩니다.
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize("AIzaSyA6kpohPinFEAxuol0fEgcvXr0d3kylwX8", this);
        }
    }


}
