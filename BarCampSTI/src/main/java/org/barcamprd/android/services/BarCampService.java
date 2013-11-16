package org.barcamprd.android.services;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.barcamprd.android.HomeActivity;
import org.barcamprd.android.R;
import org.barcamprd.android.model.Schedule;
import org.barcamprd.android.model.Speaker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by vinrosa on 10/18/13.
 */
public class BarCampService {
    private static final String TAG = "BarCampService";
    private static BarCampService s;
    private Resources resources;
    private boolean runOnce;

    private Map<Integer, Schedule> schedules;
    private Map<Integer, Speaker> speakers;

    private BarCampService() {
        super();
        schedules = new HashMap<Integer, Schedule>();
        speakers = new HashMap<Integer, Speaker>();
    }

    public static BarCampService getInstance() {
        if (s == null) s = new BarCampService();
        return s;
    }

    public List<Schedule> getSchedules() {
        Collection<Schedule> values = schedules.values();
        List<Schedule> s = new ArrayList<Schedule>();
        for (Schedule schedule : values) {
            s.add(schedule);
        }

        Collections.sort(s,new Comparator<Schedule>() {
            @Override
            public int compare(Schedule schedule, Schedule schedule2) {
                if (schedule.getSchedule().after(schedule2.getSchedule())){
                    return 1;
                }else if(schedule.getSchedule().before(schedule2.getSchedule())){
                    return -1;
                }
                return 0;
            }
        });

        return s;
    }

    public List<Speaker> getSpeakers() {
        Collection<Speaker> values = speakers.values();
        List<Speaker> s = new ArrayList<Speaker>();
        for (Speaker speaker : values) {
            s.add(speaker);
        }

        Collections.sort(s, new Comparator<Speaker>() {
            @Override
            public int compare(Speaker speaker, Speaker speaker2) {
                return speaker.getFullName().toString().compareTo(speaker2.getFullName().toString());
            }
        });

        return s;
    }


    public void start(BarCampServiceListener listener) {
        if (!runOnce) {
            this.reload(listener);
            runOnce = true;
        }else{
            listener.loadingFinished();
        }
        return;
    }

    public void reload(final BarCampServiceListener listener) {
        if (listener instanceof Activity) {
            this.resources = ((Activity) listener).getResources();
        }

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                listener.loadingStarted();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    String url = "http://barcamp.vinrosa.com/cms/mobile/info";
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet(url);
                    HttpResponse response;
                    response = httpclient.execute(httpget);
                    Log.i(TAG, response.getStatusLine().toString());
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String result = convertStreamToString(instream);
                        JSONObject json = new JSONObject(result);
                        speakers.clear();
                        schedules.clear();
                        mapSpeakers(json.getJSONObject("response").getJSONArray("speakers"));
                        mapSchedules(json.getJSONObject("response").getJSONArray("schedules"));
                        findRelationScheduleSpeaker();
                        findRelationSpeakerSchedules();
                        instream.close();
                    }
                } catch (Exception e) {
                    Log.e("BarCampService", "error", e);
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    listener.loadingFinished();
                } else {
                    listener.loadingFailed();
                }
            }
        }.execute();
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    private void findRelationSpeakerSchedules() {
        Collection<Schedule> values = schedules.values();
        for (Schedule schedule : values) {
            Speaker speaker = speakers.get(schedule.getSpeakerId());
            List<Schedule> ss = speaker.getSchedules();
            if (ss == null) {
                ss = new ArrayList<Schedule>();
                speaker.setSchedules(ss);
            }
            ss.add(schedule);
        }
    }

    private void findRelationScheduleSpeaker() {
        Collection<Schedule> values = schedules.values();
        for (Schedule schedule : values) {
            schedule.setSpeaker(speakers.get(schedule.getSpeakerId()));
        }
    }


    public interface BarCampServiceListener {
        void loadingStarted();

        void loadingFinished();

        void loadingFailed();
    }


    private void mapSchedules(JSONArray array) throws JSONException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        for (int i = 0; i < array.length(); i++) {
            JSONObject j = array.getJSONObject(i).getJSONObject("Schedule");
            Schedule schedule = new Schedule();
            schedule.setId(j.getInt("id"));
            schedule.setName(j.getString("name"));
            schedule.setDescription(j.getString("description"));
            try {
                schedule.setSchedule(sdf.parse(j.getString("schedule")));
            } catch (ParseException e) {
                schedule.setSchedule(new Date());
            }
            schedule.setPlace(j.getString("place"));
            schedule.setSpeakerId(j.getInt("speaker_id"));

            schedules.put(schedule.getId(), schedule);
        }
    }

    private void mapSpeakers(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject j = array.getJSONObject(i).getJSONObject("Speaker");
            Speaker speaker = new Speaker();
            speaker.setFirstName(j.getString("first_name"));
            speaker.setLastName(j.getString("last_name"));
            speaker.setDescription(j.getString("description"));
            speaker.setTwitter(j.getString("twitter"));
            speaker.setId(j.getInt("id"));
            speaker.setPhotoUrl(j.getString("photo_url"));

            speakers.put(speaker.getId(), speaker);
        }
    }

}
