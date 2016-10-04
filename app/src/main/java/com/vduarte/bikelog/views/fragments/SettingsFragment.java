package com.vduarte.bikelog.views.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vduarte.bikelog.MainActivity;
import com.vduarte.bikelog.R;
import com.vduarte.bikelog.constants.BikeLogConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // this is the portrait mode of configuration fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button btnOk = (Button) view.findViewById(R.id.btnSave);
        btnOk.setOnClickListener(this);

        Button btnQuit = (Button) view.findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSave:
                // if all requisite data is inserted then save and load next view -->
                if(checkRequisites().equals("")){
                    Toast.makeText(getActivity(),"Lets Continue.",Toast.LENGTH_SHORT).show();
                }else{
                    // show need fields dialog and act upon button
                    Toast.makeText(getActivity(),checkRequisites(),Toast.LENGTH_SHORT).show();
                }

                // ((MainActivity)getActivity()).switchView(BikeLogConstants.SCREEN_SETTINGS);
                break;
            case R.id.btnQuit:
                // show quit dialog and act upon button
                Toast.makeText(getActivity(),"quiiiitiiingg!!!",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // check all text fields
    private String checkRequisites() {

        String error = "";

        EditText txtName =  (EditText)getActivity().findViewById(R.id.txtName);

        EditText txtPhone = (EditText) getActivity().findViewById(R.id.txtPhone);

        error = (txtName.getText().length() > 0)?(""):("Fill your name.\n");

        error = error + ((txtPhone.getText().length() == 9)?(""):("Number of phone digits incorrect!\n"));


        error = error + ((txtPhone.getText().toString().startsWith("9"))?(""):("Phone number does not start with 9!\n"));

        return error;
    }

}
