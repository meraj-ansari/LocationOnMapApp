package com.example.admin_user.locationonmapapp;

import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.EditText;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity
        extends AppCompatActivity
        implements OnMapReadyCallback
{
    final String TAG = "LOCATION_ON_MAP_APP";

    EditText      infoTv = null;
    StringBuilder str    = new StringBuilder();

    GoogleMap   mMap        = null;
    //SupportMapFragment mapFragment = null;
    MapFragment mapFragment = null;

    LocationManager locMgr      = null;
    String          locProvider = null;
    Location        mLocation   = null;

    LocationListener locListner = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location loc)
        {
            mLocation = loc;
            Log.d(TAG, "onLocationChanged : " + mLocation.toString());
            if (mLocation != null)
            {
                str.append(infoTv.getText());

                StringBuilder strMsg = new StringBuilder();
                strMsg.append("\n------------Location Changed------------");
                strMsg.append("\nLatitude : " + mLocation.getLatitude());
                strMsg.append("\nLogitude : " + mLocation.getLongitude());
                /*strMsg.append("\nProvider : " + mLocation.getProvider());
                strMsg.append("\nAccuracy : " + mLocation.getAccuracy());
                strMsg.append("\nAlltitude : " + mLocation.getAltitude());*/
                Date       date      = new Date(mLocation.getTime());
                DateFormat formatter = new SimpleDateFormat("HH:mm:SS:dd:MMM:yyyy");
                strMsg.append("\nTime : " + formatter.format(date));
                str.append(strMsg.toString());

                infoTv.setText(str.toString());

                displayLatLongOnMap(mLocation.getLatitude(), mLocation.getLongitude());

                locMgr.requestLocationUpdates(locProvider, 0, 0, locListner);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle)
        {
            Log.d(TAG, "onStatusChanged : " + s + ", " + ", i : " + i + bundle.toString());
        }

        @Override
        public void onProviderEnabled(String s)
        {
            Log.d(TAG, "onProviderEnabled : " + s);
        }

        @Override
        public void onProviderDisabled(String s)
        {
            Log.d(TAG, "onProviderDisabled : " + s);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTv = (EditText) findViewById(R.id.infotv);

        init();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void init()
    {
        try
        {
            locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
            locProvider = LocationManager.NETWORK_PROVIDER;

            locMgr.requestLocationUpdates(locProvider, 0, 0, locListner);

            /*mLocation = locMgr.getLastKnownLocation(locProvider);
            if (mLocation != null)
            {
                StringBuilder strMsg = new StringBuilder();
                strMsg.append("\nLatitude : " + mLocation.getLatitude());
                strMsg.append("\nLogitude : " + mLocation.getLongitude());
                Date       date      = new Date(mLocation.getTime());
                DateFormat formatter = new SimpleDateFormat("HH:mm:SS:dd:MM:yy");
                strMsg.append("\nTime : " + formatter.format(date));

                infoTv.setText(strMsg.toString());

                displayLatLongOnMap(mLocation.getLatitude(), mLocation.getLongitude());
            }*/
        }
        catch (Exception e)
        {
            infoTv.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        try
        {
            mMap = googleMap;
            if(mLocation != null)
            {
                displayLatLongOnMap(mLocation.getLatitude(), mLocation.getLongitude());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void displayLatLongOnMap(double lat, double lng)
    {
        try
        {
            if (mMap != null)
            {
                LatLng latLng = new LatLng(lat, lng);


                mMap.addMarker( new MarkerOptions().position(latLng).title("" + lat + ", " +  lng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
            }
            else
            {
                Log.d(TAG, "Map is null so can not display location!!");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
