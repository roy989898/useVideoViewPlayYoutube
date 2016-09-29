package poly.pom.tryeasyplayer;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.MediaController;
import android.widget.VideoView;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private int pos;
    private String KEY_TIME = "VideoTime";
    private SharedPreferences spref;
    private SharedPreferences.Editor editor;
    private String downloadUrl;


    @Override
    protected void onRestart() {
        pos = spref.getInt(KEY_TIME, 0);
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (downloadUrl != null)
            palyYouTube(downloadUrl, pos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        videoView = (VideoView)
                findViewById(R.id.videoView);
        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        String youtubeLink = "https://www.youtube.com/watch?v=JF37uBJHiy8";

        spref = getPreferences(MODE_PRIVATE);
        editor = spref.edit();
        if (savedInstanceState != null) {
            pos = savedInstanceState.getInt("pos");
            downloadUrl = savedInstanceState.getString("link");

        } else {
            YouTubeUriExtractor ytEx = new YouTubeUriExtractor(this) {
                @Override
                public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                    if (ytFiles != null) {
                        int itag = 22;
                        downloadUrl = ytFiles.get(itag).getUrl();
                        Log.d("MainActivity", downloadUrl);
                        palyYouTube(downloadUrl, pos);

                    }
                }
            };

            ytEx.execute(youtubeLink);
        }

    }

    private void palyYouTube(String downloadUrl, int pos) {
        videoView.setVideoPath(downloadUrl);
        videoView.seekTo(pos);
        videoView.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int time = videoView.getCurrentPosition();
        if (videoView.isPlaying()) outState.putInt("pos", time);
        outState.putString("link", downloadUrl);
        super.onSaveInstanceState(outState);
//        int time=videoView.getCurrentPosition();

    }

    @Override
    protected void onPause() {
        int time = videoView.getCurrentPosition();
        editor.putInt(KEY_TIME, time);
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
