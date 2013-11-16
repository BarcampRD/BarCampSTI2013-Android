package org.barcamprd.android;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.barcamprd.android.model.Schedule;

public class ScheduleDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.schedule_detail_label);

        TextView name = (TextView) findViewById(R.id.schedule_name);
        TextView place = (TextView) findViewById(R.id.schedule_place);
        TextView speaker = (TextView) findViewById(R.id.schedule_speaker);
        TextView description = (TextView) findViewById(R.id.schedule_description);

        Schedule schedule = (Schedule) getIntent().getExtras().get(Schedule.EXTRA_ID);

        name.setText(schedule.getName());
        place.setText(schedule.getPlace());
        speaker.setText(schedule.getSpeaker().getFullName());
        description.setText(schedule.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
