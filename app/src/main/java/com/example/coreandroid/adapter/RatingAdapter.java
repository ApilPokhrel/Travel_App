package com.example.coreandroid.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coreandroid.ApiService.ApiService;
import com.example.coreandroid.ApiService.RetrofitInstance;
import com.example.coreandroid.R;
import com.example.coreandroid.dao.DatabaseHelper;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.RatingModel;
import com.example.coreandroid.entity.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.CardViewHolder> {

    List<RatingModel> ratings;
    ApiService apiService;
     DatabaseHelper databaseHelper;

    public RatingAdapter(ArrayList<RatingModel> ratings) {
        this.ratings = ratings;
    }

    @NonNull
    @Override
    public RatingAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.rating_card, parent, false);
        CardViewHolder viewHolder = new CardViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RatingAdapter.CardViewHolder holder, int position) {
        databaseHelper = new DatabaseHelper(holder.cardView.getContext());

        Retrofit retrofit = RetrofitInstance.getInstance();
        apiService = retrofit.create(ApiService.class);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("token", databaseHelper.checkToken());
        Call<UserModel> call1 = apiService.getUser(headers, ratings.get(position).getUser());
        call1.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel loginResponse = response.body();
                holder.name.setText(loginResponse.getUsername());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(holder.cardView.getContext(),"Internet Problem", Toast.LENGTH_LONG).show();
            }

        });

        holder.ratingBar.setRating((float) ratings.get(position).getRating());
        holder.rating_text.setText(ratings.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name, rating_text;
        RatingBar ratingBar;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.rating_card);
            name = itemView.findViewById(R.id.rater_name);
            ratingBar = itemView.findViewById(R.id.rating_stat);
            rating_text = itemView.findViewById(R.id.rating_text);
        }
    }
}
