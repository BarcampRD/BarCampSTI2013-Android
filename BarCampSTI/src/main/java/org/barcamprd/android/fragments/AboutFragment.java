package org.barcamprd.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.barcamprd.android.R;

/**
 * Created by vinrosa on 10/18/13.
 */
public class AboutFragment extends SwipePagerFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_about, container, false);
        return view;
    }
}
