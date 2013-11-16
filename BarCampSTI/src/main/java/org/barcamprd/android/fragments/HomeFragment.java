package org.barcamprd.android.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.barcamprd.android.R;

/**
 * Created by vinrosa on 10/18/13.
 */
public class HomeFragment extends SwipePagerFragment implements View.OnClickListener {
    private Button googleMaps;
    private ImageButton facebook;
    private ImageButton twitter;
    private Button register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_home, container, false);

        twitter = (ImageButton) view.findViewById(R.id.twitter_button);
        facebook = (ImageButton) view.findViewById(R.id.facebook_button);
        googleMaps = (Button) view.findViewById(R.id.google_maps_button);
        register = (Button) view.findViewById(R.id.register_button);

        twitter.setOnClickListener(this);
        facebook.setOnClickListener(this);
        googleMaps.setOnClickListener(this);
        register.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == twitter) {
            startActivity(getOpenTwitterIntent());
        } else if (view == facebook) {
            startActivity(getOpenFacebookIntent());
        } else if (view == googleMaps) {
            startActivity(getOpenGoogleMapsIntent());
        }else if (view == register){
            startActivity(getOpenEvenbrite());
        }
    }


    public Intent getOpenFacebookIntent() {
        try {
            getActivity().getBaseContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/624957794214604"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/barcampsti"));
        }
    }

    public Intent getOpenTwitterIntent() {
        try {
            getActivity().getPackageManager().getPackageInfo("com.twitter.android",0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=barcampsti"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/#!/barcampsti"));
        }
    }


    public Intent getOpenGoogleMapsIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/maps?q=PUCMM%2CSantiago%2CDominican+Republic"));
    }

    public Intent getOpenEvenbrite(){
        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://barcampsti.eventbrite.com/"));
    }
}
