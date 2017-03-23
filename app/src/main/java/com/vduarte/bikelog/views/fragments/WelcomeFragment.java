package com.vduarte.bikelog.views.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.vduarte.bikelog.MainActivity;
import com.vduarte.bikelog.R;
import com.vduarte.bikelog.constants.BikeLogConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment implements View.OnClickListener {

   public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // this is the portrait mode of configuration fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        Button btnQuit = (Button) view.findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnOk:
                // load next view --> Settings
                ((MainActivity)getActivity()).switchView(BikeLogConstants.SCREEN_SETTINGS);
                break;
            case R.id.btnQuit:
                // show quit dialog ( ? ) and act upon button
                Toast.makeText(getActivity(),"quiiiitiiingg!!!",Toast.LENGTH_SHORT).show();

                this.getActivity().finishAffinity();
                break;
        }
    }
}
