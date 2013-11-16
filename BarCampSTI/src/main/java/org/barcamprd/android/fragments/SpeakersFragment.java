package org.barcamprd.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.barcamprd.android.R;
import org.barcamprd.android.ScheduleDetailsActivity;
import org.barcamprd.android.SpeakerDetailsActivity;
import org.barcamprd.android.model.Schedule;
import org.barcamprd.android.model.Speaker;
import org.barcamprd.android.services.BarCampService;
import org.barcamprd.android.utils.ImageCache;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by vinrosa on 10/18/13.
 */
public class SpeakersFragment extends SwipePagerFragment {
    public static final String TAG = "SpeakersFragment";
    private ListView list;
    private AsyncTask<Speaker, Void, Drawable> imageAsyncTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_speakers, container, false);
        list = (ListView) view.findViewById(R.id.speakers_list_view);
        list.setOnItemClickListener(listener);

        List<Speaker> speakers = BarCampService.getInstance().getSpeakers();

        if (speakers != null) {
            list.setAdapter(new SpeakersAdapter(getActivity(), R.layout.speaker_list_view_item, speakers));
            list.invalidate();
        }
        return view;
    }

    public class SpeakersAdapter extends ArrayAdapter<Speaker> {

        public SpeakersAdapter(Context context, int textViewResourceId, List<Speaker> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.speaker_list_view_item, null);
            }
            final ImageView photo = (ImageView) convertView.findViewById(R.id.speaker_photo);

            TextView name = (TextView) convertView.findViewById(R.id.full_name_label);
            TextView description = (TextView) convertView.findViewById(R.id.description_label);

            Speaker speaker = getItem(position);
            name.setText(speaker.getFullName());
            description.setText(speaker.getDescription());

            Drawable image = ImageCache.getCachedImage(speaker.getPhotoUrl());
            if (image == null) {
                image = getResources().getDrawable(R.drawable.ic_user);
                imageAsyncTask = new LoadImageAsyncTask();
                imageAsyncTask.execute(speaker);
            }
            photo.setImageDrawable(image);

            return convertView;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageAsyncTask != null) {
            imageAsyncTask.cancel(true);
        }
    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            view.setSelected(true);
            Object o = adapterView.getItemAtPosition(i);
            if (o instanceof Speaker) {
                Intent intent = new Intent(getActivity(), SpeakerDetailsActivity.class);
                intent.putExtra(Speaker.EXTRA_ID, (Speaker) o);
                getActivity().startActivityForResult(intent, 0);
            }
        }
    };


    private class LoadImageAsyncTask extends  AsyncTask<Speaker, Void, Drawable> {
        @Override
        protected Drawable doInBackground(Speaker... speakers) {
            try {
                String url = speakers[0].getPhotoUrl();
                if (url != null && !url.trim().equals(""))
                    return ImageCache.getImage(url);
            } catch (Exception e) {
                Log.e("SpeakerDetailsActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            if (drawable != null) {
                list.invalidateViews();
            }
        }
    }
}
