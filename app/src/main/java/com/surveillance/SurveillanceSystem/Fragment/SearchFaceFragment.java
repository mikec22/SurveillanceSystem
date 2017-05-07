package com.surveillance.SurveillanceSystem.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surveillance.SurveillanceSystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFaceFragment extends Fragment {


    public SearchFaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_search_face, container, false);

        return root;
    }

}
