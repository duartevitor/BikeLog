package com.vduarte.bikelog.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.vduarte.bikelog.R;

import org.w3c.dom.Text;

import static com.google.android.gms.location.LocationServices.*;

public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private LocationManager mLocationManager;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private TextView tvLat;
    private TextView tvLon;

    private Button btnShowLoc;
    private Button btnLocUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // layout
        setContentView(R.layout.activity_location);

        tvLat = ((TextView) findViewById(R.id.tvLat));
        tvLat.setText("Lat:");

        tvLon = ((TextView) findViewById(R.id.tvLon));
        tvLon.setText("Lon:");

        btnShowLoc = (Button) findViewById(R.id.btnShowLoc);
        btnLocUpdates = (Button) findViewById(R.id.btnLocUpdates);

        // get location manager to check on GPS
        this.mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            // create location request
            createLocationRequest();
        }

        // click to show current location
        btnShowLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayLocation();
            }
        });

        // Location Updates Button
        btnLocUpdates
                .setText("Start Loc Updates");
        btnLocUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePeriodicLocationUpdates();
            }
        });
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

        if (checkPlayServices()) {
            if (mGoogleApiClient.isConnected())
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.d("PERMISSION", "NO PERMISSION!");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            tvLat.setText("Not Available");
            tvLon.setText("Not Available");

            return;
        }

        // if gps is not connected ask for gps
        // TODO create alert here with yes/No to start gps...
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

            startActivity(gpsOptionsIntent);
        }
        else
        {
            //
            mLastLocation = FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();

                tvLat.setText(latitude + "");
                tvLon.setText(longitude + "");
            } else {

                tvLon.setText("(Couldn't get the location. Make sure location is enabled on the device)");
            }
        }
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
            btnLocUpdates
                    .setText("Stop Loc Updates");

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d("TOGGLE TAG", "Periodic location updates started!");

        } else {
            // Changing the button text
            btnLocUpdates
                    .setText("Start Loc Updates");

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d("TOGGLE TAG", "Periodic location updates stopped!");
        }
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            tvLat.setText("Loc Updates Not Available");
            tvLon.setText("Loc Updates Not Available");

            return;
        }
        //
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
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
      //  if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        //    Intent gpsOptionsIntent = new Intent(
          //          android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
           // startActivity(gpsOptionsIntent);
        //}
        //else {
            // and then get the location
            displayLocation();
            // check location updates variable
            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            }

        //}
    }

    @Override
    public void onConnectionSuspended(int i) {
        tvLon.setText("Suspended!");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        tvLon.setText("Failed!");
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }
}
