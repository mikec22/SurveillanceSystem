package com.surveillance.surveillancesystem.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surveillance.surveillancesystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoDetailsFragment extends Fragment {


    public VideoDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_video_details, container, false);


        return root;
    }

}
