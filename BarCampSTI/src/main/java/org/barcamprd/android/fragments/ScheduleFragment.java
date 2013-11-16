package org.barcamprd.android.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.barcamprd.android.R;
import org.barcamprd.android.ScheduleDetailsActivity;
import org.barcamprd.android.model.Schedule;
import org.barcamprd.android.services.BarCampService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by vinrosa on 10/18/13.
 */
public class ScheduleFragment extends SwipePagerFragment {
    public static final String TAG = "ScheduleFragment";
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_schedule, container, false);
        list = (ListView) view.findViewById(R.id.schedule_list_view);
        list.setOnItemClickListener(listener);
        List<Schedule> schedules = BarCampService.getInstance().getSchedules();
        if (schedules != null) {
            list.setAdapter(new ScheduleAdapter(getActivity(), R.layout.schedule_list_view_item, schedules));
            list.invalidate();
        }
        return view;
    }

    public static class ScheduleAdapter extends ArrayAdapter<Schedule> {

        public ScheduleAdapter(Context context, int textViewResourceId, List<Schedule> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.schedule_list_view_item, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.name_label);
            TextView shortDescription = (TextView) convertView.findViewById(R.id.short_description_label);
            TextView scheduleLabel = (TextView) convertView.findViewById(R.id.schedule_label);
            TextView scheduleTimeLabel = (TextView) convertView.findViewById(R.id.schedule_time);

            name.setText(getItem(position).getName());
            shortDescription.setText(getItem(position).getDescription());
            scheduleTimeLabel.setText(sdf.format(getItem(position).getSchedule()));
            sdf.applyPattern("dd/MM/yyyy");

            scheduleLabel.setText(sdf.format(getItem(position).getSchedule()));

            return convertView;
        }
    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            view.setSelected(true);
            Object o = adapterView.getItemAtPosition(i);
            if (o instanceof Schedule) {
                Intent intent = new Intent(getActivity(), ScheduleDetailsActivity.class);
                intent.putExtra(Schedule.EXTRA_ID, (Schedule) o);
                getActivity().startActivityForResult(intent, 0);
            }
        }
    };

}
