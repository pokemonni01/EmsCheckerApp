package app.wachirapong.emschecker;

import android.provider.BaseColumns;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wachirapong on 1/30/2016 AD.
 */
public class ThaiPostTrackReader {
    private static String CLASSNAME = "ThaiPostTrackReader";
    private OkHttpClient client = new OkHttpClient();
    private static JSONArray trackJson;

    private static String stringTest = "[{\"date\":\"พุธ 3 กุมภาพันธ์ 2559 15:58:01 น.\",\"location\":\"เพชรบุรี\",\"description\":\"รับเข้าระบบ\",\"status\":\"\"},{\"date\":\"พุธ 3 กุมภาพันธ์ 2559 17:05:19 น.\",\"location\":\"เพชรบุรี\",\"description\":\"ใส่ของลงถุง\",\"status\":\"\"},{\"date\":\"พุธ 3 กุมภาพันธ์ 2559 17:28:45 น.\",\"location\":\"เพชรบุรี\",\"description\":\"ปิดถุง\",\"status\":\"\"},{\"date\":\"พฤหัสบดี 4 กุมภาพันธ์ 2559 01:23:43 น.\",\"location\":\"ศป.อยุธยา\",\"description\":\"รับถุง\",\"status\":\"\"},{\"date\":\"พฤหัสบดี 4 กุมภาพันธ์ 2559 02:59:13 น.\",\"location\":\"ศป.อยุธยา\",\"description\":\"ใส่ของลงถุง\",\"status\":\"\"},{\"date\":\"พฤหัสบดี 4 กุมภาพันธ์ 2559 03:14:44 น.\",\"location\":\"ศป.อยุธยา\",\"description\":\"ปิดถุง\",\"status\":\"\"},{\"date\":\"พฤหัสบดี 4 กุมภาพันธ์ 2559 09:40:15 น.\",\"location\":\"สระโบสถ์\",\"description\":\"รับถุง\",\"status\":\"\"},{\"date\":\"พฤหัสบดี 4 กุมภาพันธ์ 2559 11:17:16 น.\",\"location\":\"สระโบสถ์\",\"description\":\"เตรียมการนำจ่าย\",\"status\":\"\"},{\"date\":\"พฤหัสบดี 4 กุมภาพันธ์ 2559 09:00-11:59 น.\",\"location\":\"สระโบสถ์\",\"description\":\"สถานะการนำจ่าย\",\"status\":\"อื่น ๆ\"},{\"date\":\"พฤหัสบดี 4 กุมภาพันธ์ 2559 09:00-11:59 น.\",\"location\":\"สระโบสถ์\",\"description\":\"สถานะการนำจ่าย\",\"status\":\"ผู้รับได้รับเรียบร้อย\"}]";

    public ThaiPostTrackReader() {

    }

    public static abstract class TrackTable implements BaseColumns {
        public static final String TABLE_NAME = "tracks_table";
        public static final String COLUMN_NAME_TRACK_ID = "trackid";
        public static final String COLUMN_NAME_TRACK_DATE = "date";
        public static final String COLUMN_NAME_TRACK_LOCATION = "location";
        public static final String COLUMN_NAME_TRACK_DESCRIPTION = "description";
    }

    public void run(String url) throws IOException {
        trackJson = null;
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                updateStatus("Error - " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    try {
                        updateStatus(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        updateStatus("Error - " + e.getMessage());
                    }
                } else {
                    updateStatus("Not Success - code : " + response.code());
                }
            }

            public void updateStatus(final String strResult) {
                try {

                    trackJson = new JSONArray(strResult);
                    Log.d("My App", "Get Track Json Success!");

                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + strResult + "\"");
                }
            }
        });

    }//end run

    private static void setDateAndTime(Boolean isTest){
        if(isTest) {
            try {
                trackJson = new JSONArray(stringTest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String[] dateArray;
        JSONArray bindingTrack;
        HashMap<String,JSONObject> hashmap = new HashMap<String,JSONObject>(); //รอทำต่อ ใช้ key เรียก json object
        try {
            dateArray = new String[ trackJson.length() ];
            bindingTrack = new JSONArray();
            String dateAndTime;
            String[] explodeDate;
            String location;
            String description;
            String status;
            JSONObject jsonObj;
            for(int i=0;i<trackJson.length();i++){
                dateAndTime = trackJson.getJSONObject(i).getString("date");
                explodeDate = dateAndTime.split(" ");
                location = trackJson.getJSONObject(i).getString("location");
                description = trackJson.getJSONObject(i).getString("description");
                status = trackJson.getJSONObject(i).getString("status");
                int index = 0;
                String date = "";
                String time = "";
                for( String word:explodeDate ){
                    if(index<4)
                        date += " "+word;
                    else
                        time += " "+word;
                    index++;
                }
                jsonObj = new JSONObject();
                jsonObj.put("date",date.trim());
                jsonObj.put("time",time.trim());
                jsonObj.put("location",location);
                jsonObj.put("description",description);
                jsonObj.put("status",status);
                bindingTrack.put(jsonObj);
            }
        Log.d(CLASSNAME,bindingTrack.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONArray getTrackJson(){
        if( trackJson != null )
            setDateAndTime(false);
        return trackJson;
    }

    public static JSONArray getJsonTrackForTest(){
        setDateAndTime(true);
        return trackJson;
    }

}//emd ThaiPOstTrack
