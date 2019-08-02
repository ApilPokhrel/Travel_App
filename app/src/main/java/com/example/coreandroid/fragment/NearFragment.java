package com.example.coreandroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.coreandroid.R;
import com.example.coreandroid.SpecificActivity;


public class NearFragment extends Fragment {

    CardView hotel, atm, latrine, hospital, park, religion,vechile, other;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.near_fragment, container, false);
        hotel = view.findViewById(R.id.hotel);
        atm = view.findViewById(R.id.atm);
        latrine = view.findViewById(R.id.latrine);
        religion = view.findViewById(R.id.religion);
        hospital = view.findViewById(R.id.hospital);
        park = view.findViewById(R.id.park);
        vechile = view.findViewById(R.id.vechile);
        other = view.findViewById(R.id.other);

        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), SpecificActivity.class);
                intent.putExtra("tag", "hotel");
                view.getContext().startActivity(intent);            }
        });

        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), SpecificActivity.class);
                intent.putExtra("tag", "atm");
                view.getContext().startActivity(intent);
            }
        });


        latrine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), SpecificActivity.class);
                intent.putExtra("tag", "latrine");
                view.getContext().startActivity(intent);
            }
        });

        religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), SpecificActivity.class);
                intent.putExtra("tag", "religion");
                view.getContext().startActivity(intent);
            }
        });


        park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), SpecificActivity.class);
                intent.putExtra("tag", "park");
                view.getContext().startActivity(intent);
            }
        });


        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), SpecificActivity.class);
                intent.putExtra("tag", "hospital");
                view.getContext().startActivity(intent);
            }
        });


        vechile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), SpecificActivity.class);
                intent.putExtra("tag", "vechile");
                view.getContext().startActivity(intent);
            }
        });


        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), SpecificActivity.class);
                intent.putExtra("tag", "other");
                view.getContext().startActivity(intent);
            }
        });


        return view;
    }
}
