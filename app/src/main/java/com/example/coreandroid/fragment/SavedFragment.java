package com.example.coreandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coreandroid.R;
import com.example.coreandroid.adapter.RecyclerViewAdapter;
import com.example.coreandroid.adapter.SavedRecyclerAdapter;
import com.example.coreandroid.dao.DatabaseHelper;
import com.example.coreandroid.dao.PlaceDatabaseHelper;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.ProfileModel;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    PlaceDatabaseHelper placeDatabaseHelper;
    private List<PlaceModel> travelInfoList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.saved_fragment, container, false);
        recyclerView = view.findViewById(R.id.saved_recycler);
        recyclerView.setHasFixedSize(true);

        databaseHelper = new DatabaseHelper(getContext());
        placeDatabaseHelper = new PlaceDatabaseHelper(getContext());

        // 2. set layoutManger
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        initializeData();
        initializeAdapter();
        return view;
    }

    public void initializeData(){
        List<PlaceModel> models = placeDatabaseHelper.getAll();
        travelInfoList = new ArrayList<>();
            for(PlaceModel p: models){
                travelInfoList.add(new PlaceModel(p.get_id(), p.getTitle(),p.getContact(), p.getAverage(), p.getDescription(),p.getLocation(), p.getProfiles(), p.getTag(), null));
            }

    }

    private void initializeAdapter(){
        SavedRecyclerAdapter adapter = new SavedRecyclerAdapter(travelInfoList);
        recyclerView.setAdapter(adapter);

    }

}
