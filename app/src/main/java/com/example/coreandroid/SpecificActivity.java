package com.example.coreandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coreandroid.ApiService.ApiService;
import com.example.coreandroid.ApiService.RetrofitInstance;
import com.example.coreandroid.adapter.RecyclerViewAdapter;
import com.example.coreandroid.dao.DatabaseHelper;
import com.example.coreandroid.dao.PlaceDatabaseHelper;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.ProfileModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SpecificActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Context context;
    DatabaseHelper databaseHelper;
    PlaceDatabaseHelper placeDatabaseHelper;
    ApiService apiService;
    ProgressBar progressBar;
    RecyclerViewAdapter adapter;
    int start = 0;
    int limit = 5;
    String lat = null;
    String lng = null;
    String filter = null;
    String distance = "10000";
    String tag = null;
    TextView netText;
    ImageView netImage;
    Button netButton;
    FloatingActionButton refresh;
    Spinner spinner;
    private List<PlaceModel> travelInfoList;
    private boolean isLoading;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total = 0;
    private int view_threshold = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific);


        Bundle bundle = getIntent().getExtras();
        tag = bundle.getString("tag");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color=\"red\">" + tag + "</font>"));
        }
        // 1. get a reference to recyclerView
        netImage = findViewById(R.id.net_image);
        netText = findViewById(R.id.net_text);
        netButton = findViewById(R.id.net_button);
        netText.setVisibility(View.GONE);
        netImage.setVisibility(View.GONE);
        netButton.setVisibility(View.GONE);
        refresh = findViewById(R.id.refresh);

        recyclerView = findViewById(R.id.home_recycler);
        recyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.progressBar);

        spinner = findViewById(R.id.mySpinner);

        String filters[] = {"Around 10km", "Around 5km", "Around 1km"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filters);
        spinner.setAdapter(adapter);

        if(checkPermission()){
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lng = String.valueOf(location.getLongitude());
            lat = String.valueOf(location.getLatitude());

             final LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    lng = String.valueOf(location.getLongitude());
                    lat = String.valueOf(location.getLatitude());
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }

            };

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }

        databaseHelper = new DatabaseHelper(this);
        placeDatabaseHelper = new PlaceDatabaseHelper(this);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        Retrofit retrofit = RetrofitInstance.getInstance();
        apiService = retrofit.create(ApiService.class);
        progressBar.setVisibility(View.VISIBLE);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        distance = "10000";
                        pagination();
                        break;

                    case 1:
                        distance = "5000";
                        pagination();
                        break;

                    case 2:
                        distance = "1000";
                        pagination();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        pagination();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limit++;
                pagination();
                linearLayoutManager.setReverseLayout(true);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if(dy>0){
                    if(isLoading){
                        if(totalItemCount > previous_total){
                            isLoading = false;
                            previous_total = totalItemCount;
                        }
                    }

                    if(!isLoading&&(totalItemCount-visibleItemCount)<=(pastVisibleItems+view_threshold)){
                        limit++;
                        pagination();
                        isLoading = true;
                    }
                }
            }
        });


        netButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagination();
            }
        });


}

    public void initializeData( List<PlaceModel> models ){
        travelInfoList = new ArrayList<>();
        if(models == null) {
            ArrayList<ProfileModel> profiles = new ArrayList<>();
            profiles.add(new ProfileModel("image/jpeg", "1bab80ce-c6bf-40bb-8a47-f6bec5bae241.jpeg"));
            travelInfoList.add(new PlaceModel( "id","ewqeqeqwewe","no contact", 3.5f,"Considered to be the largest cave in the country",null, profiles, "country", null));
            recyclerView.setVisibility(View.GONE);
            netText.setVisibility(View.VISIBLE);
            netImage.setVisibility(View.VISIBLE);
            netButton.setVisibility(View.VISIBLE);

        }else{
            for(PlaceModel p: models){
                travelInfoList.add(new PlaceModel(p.get_id(), p.getTitle(),p.getContact(), p.getAverage(), p.getDescription(),p.getLocation(), p.getProfiles(), p.getTag(), p.getRatings()));
            }
        }
    }

    private void initializeAdapter(){
        adapter = new RecyclerViewAdapter(travelInfoList);
        recyclerView.setAdapter(adapter);

    }

    private void pagination(){
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("token", databaseHelper.checkToken());
        Call<List<PlaceModel>> call1 = apiService.getAllPlace(headers, start, limit, filter, tag, lat, lng, distance);
        call1.enqueue(new Callback<List<PlaceModel>>() {
            @Override
            public void onResponse(Call<List<PlaceModel>> call, Response<List<PlaceModel>> response) {
                List<PlaceModel> loginResponse = response.body();
                initializeData(loginResponse);
                initializeAdapter();
                netText.setVisibility(View.GONE);
                netImage.setVisibility(View.GONE);
                netButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<PlaceModel>> call, Throwable t) {
                netText.setVisibility(View.VISIBLE);
                netImage.setVisibility(View.VISIBLE);
                netButton.setVisibility(View.VISIBLE);

            }
        });
    }


    public boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else{
            return true;
        }
    }
}
