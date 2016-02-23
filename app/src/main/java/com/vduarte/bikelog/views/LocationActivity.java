package com.vduarte.bikelog.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.vduarte.bikelog.R;

import org.w3c.dom.Text;

import static com.google.android.gms.location.LocationServices.*;

public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private LocationManager mLocationManager;

    private TextView tvLat;
    private TextView tvLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // layout
        setContentView(R.layout.activity_location);

        tvLat = ((TextView) findViewById(R.id.tvLat));
        tvLat.setText("Location Text");

        tvLon = ((TextView) findViewById(R.id.tvLon));
        tvLon.setText("Go");

       this.mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(checkPlayServices() ){
            if(mGoogleApiClient.isConnected())
                displayLocation();
            else
                mGoogleApiClient.connect();
        }
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        tvLon.setText("Api Available:" + resultCode);

        if (resultCode != ConnectionResult.SUCCESS) {
            tvLat.setText("Location Text: Connection Success");
            if (apiAvailability.isUserResolvableError(resultCode)) {

                tvLat.setText("Show Error Diag");
                apiAvailability.getErrorDialog(this, resultCode, REQUEST_GOOGLE_PLAY_SERVICES).show();


            } else {
                tvLon.setText("Not Supported, code:" + resultCode);
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }

        tvLon.setText("Api Available. Start connecting!");
        return true;
    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.d("PERMISSION","NO PERMISSION!");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            tvLat.setText(latitude+"");
            tvLon.setText(longitude+"");
        } else {

            tvLon.setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(API).build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        tvLon.setText("Connected!");
        // Once connected with google api,
        // if gps is not connected ask for gps

        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
        else {
            // and then get the location
            displayLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        tvLon.setText("Suspended!");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        tvLon.setText("Failed!");
    }
}
