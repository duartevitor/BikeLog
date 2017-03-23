package com.vduarte.bikelog.views.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vduarte.bikelog.LocationService;
import com.vduarte.bikelog.R;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class ActivityFragment extends Fragment implements View.OnClickListener {

    private boolean mActivityStatus;
    private Context mContext;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // this is the portrait mode of configuration fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        /*Button btnOk = (Button) view.findViewById(R.id.btnSave);
        btnOk.setOnClickListener(this);*/

        // context
        this.mContext = getActivity().getBaseContext();

        // activity status
        this.mActivityStatus = false;

        Button btnStart = (Button) view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnStart:
                // returns to welcomes
                //((MainActivity)getActivity()).switchView(BikeLogConstants.SCREEN_WELCOME);
                break;
        }
    }

    public void startMyService() {
        getActivity().startService(new Intent(this.mContext, LocationService.class));
    }

    // Method to stop the service
    public void stopMyService() {
        getActivity().stopService(new Intent(this.mContext, LocationService.class));

    }
}
