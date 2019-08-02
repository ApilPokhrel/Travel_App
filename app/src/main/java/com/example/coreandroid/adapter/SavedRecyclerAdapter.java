package com.example.coreandroid.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.asura.library.views.PosterSlider;
import com.example.coreandroid.DetailActivity;
import com.example.coreandroid.R;
import com.example.coreandroid.dao.PlaceDatabaseHelper;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.ProfileModel;
import com.example.coreandroid.entity.Variables;

import java.util.ArrayList;
import java.util.List;

public class SavedRecyclerAdapter extends RecyclerView.Adapter<SavedRecyclerAdapter.CardViewHolder>{
    PlaceDatabaseHelper placeDatabaseHelper;
    Variables variables = new Variables();
    List<PlaceModel> travelInfo;
    public SavedRecyclerAdapter(List<PlaceModel> travelInfo){
        this.travelInfo = travelInfo;
    }

    @NonNull
    @Override
    public SavedRecyclerAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.saved_card, parent, false);
        SavedRecyclerAdapter.CardViewHolder viewHolder = new SavedRecyclerAdapter.CardViewHolder(listItem);
        return viewHolder;      }



    @Override
    public void onBindViewHolder(@NonNull final SavedRecyclerAdapter.CardViewHolder holder, final int position) {
        holder.description.setText(travelInfo.get(position).getDescription());
        holder.title.setText(travelInfo.get(position).getTitle());

        List<Poster> posters=new ArrayList<>();

        List<ProfileModel> profiles = travelInfo.get(position).getProfiles();
        for(ProfileModel p: profiles) {
            posters.add(new RemoteImage(p.getName()));
        }
        holder.posterSlider.setPosters(posters);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (view.getContext(), DetailActivity.class);
                intent.putExtra("place", travelInfo.get(position));

                holder.itemView.getContext().startActivity(intent);

            }
        });

        holder.savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeDatabaseHelper = new PlaceDatabaseHelper(view.getContext());

                placeDatabaseHelper.deleteBy_id(travelInfo.get(position).get_id());
                travelInfo.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, travelInfo.size());
                holder.itemView.setVisibility(View.GONE);
                Toast.makeText(view.getContext(), "Removed from saved", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String convertProfilesToString(List<ProfileModel> profileModels){
        String profiles = "";
        for(ProfileModel p: profileModels){
            profiles += p.getName();
        }
        return profiles;
    }
    @Override
    public int getItemCount() {
        return travelInfo.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title, description;
        PosterSlider posterSlider;
        ImageView savePost;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.home_card);
            description = itemView.findViewById(R.id.tour_text);
            title = itemView.findViewById(R.id.tour_title);
            posterSlider = itemView.findViewById(R.id.poster_slider);
            savePost = itemView.findViewById((R.id.tour_save));
        }
    }
}
