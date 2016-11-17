package com.mi.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mi.test.mypiece.R;


public class Tour_four extends Fragment{

    int mCurrentPage;
    TextView tipsHead;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("::::::::::::Tour Four ::::::::::::::::::::");
        Bundle data = getArguments();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tour_frag_four, container,false);

        return v;

    }
}
