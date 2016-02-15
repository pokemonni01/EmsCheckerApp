package app.wachirapong.emschecker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final private String ADDR = "172.17.81.96";

    private FloatingActionButton fab;
    private Button getTrack;
    private EditText trackText;
    private TextView trackTextView;

    private OkHttpClient client = new OkHttpClient();

    private ThaiPostTrackReader thaiPostTrackReader;

    public String JsonTest ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JsonTest = getApplicationContext().getResources().getString(R.string.jsonForTest);
        init();


    }

    private void init(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        thaiPostTrackReader = new ThaiPostTrackReader();

        trackText = (EditText) findViewById(R.id.trackEditext);
        trackTextView = (TextView) findViewById(R.id.trackDetail);

        getTrack = (Button) findViewById(R.id.getTrackBtn);
        getTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getTrackJsonFromUrl("http://"+ADDR+"/oom/getTrack?trackNumber="+trackText.getText().toString());
                getTrackJsonForTest();

            }
        });
    }//end init

    private void getTrackJsonFromUrl(final String url){
        final Handler handler = new Handler();
        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    thaiPostTrackReader.run(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while ( thaiPostTrackReader.getTrackJson() == null );
                try {
                    JSONArray tracks = thaiPostTrackReader.getTrackJson();
                    while ( tracks == null );
                    for(int i=0;i<tracks.length();i++)
                        Log.d("Main Activity",tracks.getJSONObject(i).getString("date").toString());
                    Intent i = new Intent(getApplicationContext(),ShowTrackResultActivity.class);
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }

    private void getTrackJsonForTest(){
        JSONArray tracks = thaiPostTrackReader.getJsonTrackForTest();
        Intent i = new Intent(getApplicationContext(),ShowTrackResultActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}// end class
