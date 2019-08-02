package com.example.coreandroid.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.coreandroid.ApiService.ApiService;
import com.example.coreandroid.ApiService.RetrofitInstance;
import com.example.coreandroid.MainActivity;
import com.example.coreandroid.R;
import com.example.coreandroid.adapter.RecyclerViewAdapter;
import com.example.coreandroid.dao.DatabaseHelper;
import com.example.coreandroid.dao.PlaceDatabaseHelper;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.ProfileModel;
import com.example.coreandroid.entity.TravelInfo;
import com.example.coreandroid.entity.UserModel;
import com.example.coreandroid.entity.Variables;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    Context context;
    DatabaseHelper databaseHelper;
    PlaceDatabaseHelper placeDatabaseHelper;
    ApiService apiService;
    ProgressBar progressBar;
    RecyclerViewAdapter adapter;
    int start = 0;
    int limit = 5;
    String filter = null;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = null;
        try {
            view = inflater.inflate(R.layout.home_fragment, container, false);

            // 1. get a reference to recyclerView
            netImage = view.findViewById(R.id.net_image);
            netText = view.findViewById(R.id.net_text);
            netButton = view.findViewById(R.id.net_button);
            netText.setVisibility(View.GONE);
            netImage.setVisibility(View.GONE);
            netButton.setVisibility(View.GONE);
            refresh = view.findViewById(R.id.refresh);

             recyclerView = view.findViewById(R.id.home_recycler);
             recyclerView.setHasFixedSize(true);
             progressBar = view.findViewById(R.id.progressBar);

             spinner = view.findViewById(R.id.mySpinner);

             String filters[] = {"latest", "Top rated", "Related"};

             ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, filters);
             spinner.setAdapter(adapter);



             databaseHelper = new DatabaseHelper(getContext());
             placeDatabaseHelper = new PlaceDatabaseHelper(getContext());

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
                          filter = "latest";
                          pagination(apiService);
                          break;

                      case 1:
                          filter = "rated";
                          pagination(apiService);
                          break;

                      case 2:
                          filter = "related";
                          pagination(apiService);
                          break;
                  }
              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {

              }
          });
          pagination(apiService);

          refresh.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  limit++;
                  pagination(apiService);
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
                        pagination(apiService);
                        isLoading = true;
                    }
                }
            }
        });


        netButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagination(apiService);
            }
        });
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }



    public List<PlaceModel> filterDataWithSavedOne(List<PlaceModel> placeModels){
        List<PlaceModel> places =  placeDatabaseHelper.getAll();
        if(places != null && placeModels != null) {
            for (int i = 0; i < placeModels.size(); i++) {
                for (int j = 0; j < places.size(); j++) {
                    if(placeModels.size() > i && i >= 0) {
                        if (placeModels.get(i).get_id().equalsIgnoreCase(places.get(j).get_id())) {
                            placeModels.remove(i);
                            if(i > 0) {
                                i--;
                            }
                        }
                    }
                }
            }
        }
        return placeModels;
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


    private void pagination(ApiService apiService){
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("token", databaseHelper.checkToken());
        Call<List<PlaceModel>> call1 = apiService.getAllPlace(headers, start, limit, filter, tag, null, null, null);
        call1.enqueue(new Callback<List<PlaceModel>>() {
            @Override
            public void onResponse(Call<List<PlaceModel>> call, Response<List<PlaceModel>> response) {
                List<PlaceModel> loginResponse = filterDataWithSavedOne(response.body());
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



}
