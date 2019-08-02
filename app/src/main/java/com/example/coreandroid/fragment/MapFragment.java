package com.example.coreandroid.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.coreandroid.R;
import com.example.coreandroid.entity.ClusterMarker;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.Variables;
import com.example.coreandroid.utils.MyClusterManagerRenderer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;



import static androidx.constraintlayout.widget.Constraints.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    Variables variables = new Variables();
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private  float DEFAULT_ZOOM = 15f;
    GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private List<PlaceModel> mPlaceLocations;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MapFragment(){

    }

    public MapFragment(List<PlaceModel> mPlaceLocations, float DEFAULT_ZOOM){
        this.DEFAULT_ZOOM = DEFAULT_ZOOM;
        this.mPlaceLocations = mPlaceLocations;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        mMapView =  view.findViewById(R.id.tour_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        initGoogleMap(savedInstanceState);

        return view;
    }
    private void addMapMarkers(){

        if(mGoogleMap != null){
          if(getActivity() != null) {
              if (mClusterManager == null) {

                  mClusterManager = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), mGoogleMap);

              }
              if (mClusterManagerRenderer == null) {
                  mClusterManagerRenderer = new MyClusterManagerRenderer(
                          getActivity(),
                          mGoogleMap,
                          mClusterManager
                  );
                  mClusterManager.setRenderer(mClusterManagerRenderer);
              }
              if(mPlaceLocations != null) {
                  for (PlaceModel placeLocation : mPlaceLocations) {

                      try {
                          String imageUrl = "null";
                          String snippet = placeLocation.getTitle();
                          int avatar = R.drawable.amu_bubble_mask; // set the default avatar
                          try {
                              imageUrl = "uploads/" + placeLocation.getProfiles().get(0).getName();

                          } catch (Exception e) {
                              System.out.println("==============error" + e.getMessage());
                          }
                          ClusterMarker newClusterMarker = new ClusterMarker(
                                  new LatLng(placeLocation.getLocation().getCoordinates().get(1), placeLocation.getLocation().getCoordinates().get(0)),
                                  placeLocation.getTitle(),
                                  snippet,
                                  avatar,
                                  imageUrl,
                                  placeLocation
                          );
                          mClusterManager.addItem(newClusterMarker);
                          mClusterMarkers.add(newClusterMarker);

                      } catch (NullPointerException e) {
                          Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage());
                      }

                  }
              }

            mClusterManager.cluster();
          }

        }
    }


    private void initGoogleMap(Bundle savedInstanceState) {

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(variables.MAP_KEY);
        }
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(variables.MAP_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(variables.MAP_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        getDeviceLocation();
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        if(mPlaceLocations != null) {
            addMapMarkers();
        }

        map.setMyLocationEnabled(true);
    }


    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void getDeviceLocation() {
        try {
            if (checkPermission()) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                       if(task.isSuccessful()){
                           currentLocation = (Location) task.getResult();
                           moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),DEFAULT_ZOOM);
                           mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Marker"));

                       }else{
                           Toast.makeText(getActivity(), "could not get current location", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
            }
        }catch (Exception ex){}
    }


public boolean checkPermission(){
    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
        return false;
    }else{
        return true;
    }
}


public void moveCamera(LatLng latLng, float zoom){
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
}



}
