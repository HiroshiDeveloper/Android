package com.example.hiroshi.twittermap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // google map
    GoogleMap mMap;
    // map fragment
    SupportMapFragment mapFragment;
    // call back for google map
    OnMapReadyCallback cb;

    // save data from twitter
    // lat : latitude
    // lon : longtitude
    // text : twiiter text
    // date : tweet date
    static ArrayList<Double> lat;
    static ArrayList<Double> lon;
    static ArrayList<String> text;
    static ArrayList<String> date;

    public MapFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MapFragment newInstance(int sectionNumber,
                                          ArrayList latInstance,
                                          ArrayList lonInstance,
                                          ArrayList textInstance,
                                          ArrayList dateInstance) {

        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        lat = latInstance;
        lon = lonInstance;
        text = textInstance;
        date = dateInstance;
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        cb = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (int i = 0; i < lat.size(); i++) {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.addMarker(new MarkerOptions()
                            .snippet(date.get(i))
                            .title(text.get(i))
                            .position(new LatLng(lat.get(i), lon.get(i))));
                    mMap = googleMap;
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat.get(0), lon.get(0)), 8));
            }
        };
        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);

        // only after users authorize their account, google map is read
        if (lat.size() != 0 && lon.size() != 0){
            mapFragment.getMapAsync(cb);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment f = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
            Log.d("TESTTEST", "CALLING1");
    }

}
