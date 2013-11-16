package org.barcamprd.android;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.barcamprd.android.fragments.ScheduleFragment;
import org.barcamprd.android.model.Schedule;
import org.barcamprd.android.model.Speaker;
import org.barcamprd.android.utils.ImageCache;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by vinrosa on 10/18/13.
 */
public class SpeakerDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speakers_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.speakers_detail_label);

        final Speaker speaker = (Speaker) getIntent().getExtras().get(Speaker.EXTRA_ID);

        View headerView = getLayoutInflater().inflate(R.layout.speaker_profile_card, null);
        final ImageView photo = (ImageView) headerView.findViewById(R.id.speaker_photo);

        ImageButton twitterButton = (ImageButton) headerView.findViewById(R.id.twitter_button);
        TextView name = (TextView) headerView.findViewById(R.id.speaker_name);
        TextView description = (TextView) headerView.findViewById(R.id.speaker_description);
        TextView twitter = (TextView) headerView.findViewById(R.id.speader_twitter);
        ListView schedules = (ListView) findViewById(R.id.speaker_schedules);

        name.setText(speaker.getFullName());
        description.setText(speaker.getDescription());
        twitter.setText("/@" + speaker.getTwitter());

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getOpenTwitterIntent(speaker.getTwitter()));
            }

            public Intent getOpenTwitterIntent(String handler) {
                try {
                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + handler));
                } catch (Exception e) {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/#!/" + handler));
                }
            }

        });
        schedules.addHeaderView(headerView);
        Drawable image = ImageCache.getCachedImage(speaker.getPhotoUrl());
        if (image == null) {
            image = getResources().getDrawable(R.drawable.ic_user);
            new AsyncTask<Speaker, Void, Drawable>() {
                @Override
                protected Drawable doInBackground(Speaker... speakers) {
                    try {
                        return ImageCache.getImage(speakers[0].getPhotoUrl());
                    } catch (Exception e) {
                        // handle it
                        Log.e("SpeakerDetailsActivity", e.getMessage(), e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Drawable drawable) {
                    super.onPostExecute(drawable);
                    if (drawable != null) {
                        photo.setImageDrawable(drawable);
                    }
                }
            }.execute(speaker);
        }
        photo.setImageDrawable(image);

        if (speaker.getSchedules() != null) {
            schedules.setOnItemClickListener(listener);
            schedules.setAdapter(new ScheduleFragment.ScheduleAdapter(this, R.id.schedule_list_view, speaker.getSchedules()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            view.setSelected(true);
            Object o = adapterView.getItemAtPosition(i);
            if (o instanceof Schedule) {
                Intent intent = new Intent(getBaseContext(), ScheduleDetailsActivity.class);
                intent.putExtra(Schedule.EXTRA_ID, (Schedule) o);
                startActivity(intent);
            }
        }
    };

}


