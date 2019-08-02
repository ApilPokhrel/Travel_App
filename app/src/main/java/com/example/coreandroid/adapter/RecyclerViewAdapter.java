package com.example.coreandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.asura.library.views.PosterSlider;
import com.example.coreandroid.DetailActivity;
import com.example.coreandroid.R;
import com.example.coreandroid.dao.PlaceDatabaseHelper;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.ProfileModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.*;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CardViewHolder> {
    PlaceDatabaseHelper placeDatabaseHelper;
    List<PlaceModel> travelInfo;
    public RecyclerViewAdapter(List<PlaceModel> travelInfo){
        this.travelInfo = travelInfo;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.home_card, parent, false);
        CardViewHolder viewHolder = new CardViewHolder(listItem);
        return viewHolder;      }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {
        holder.description.setText(travelInfo.get(position).getDescription());
        holder.title.setText(travelInfo.get(position).getTitle());

//        List<Poster> posters=   new ArrayList<>();
        //add poster using remote url

        List<ProfileModel> profiles = travelInfo.get(position).getProfiles();
        List<String> imageUrls = new ArrayList<>();
        for(ProfileModel p: profiles) {
            imageUrls.add(p.getName());
        }

        SliderAdapter sliderAdapter = new SliderAdapter(holder.viewPager.getContext(), imageUrls);
        holder.viewPager.setAdapter(sliderAdapter);

//         posters.add((new RemoteImage(profiles.get(0).getName())));
         holder.ratingBar.setRating(travelInfo.get(position).getAverage());
         if(travelInfo.get(position).getRatings() != null) {
             holder.rating_text.setText(travelInfo.get(position).getAverage() + "(" + travelInfo.get(position).getRatings().size() + ")");
         }
         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (view.getContext(), DetailActivity.class);
                 intent.putExtra("place", (Serializable) travelInfo.get(position));
                 holder.itemView.getContext().startActivity(intent);

            }
        });

         holder.savePost.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 holder.savePost.setImageResource(R.drawable.ic_favorite_border_red_24dp);
                 placeDatabaseHelper = new PlaceDatabaseHelper(view.getContext());
                 placeDatabaseHelper.insertData(travelInfo.get(position).get_id(),
                         travelInfo.get(position).getTitle(),
                         travelInfo.get(position).getContact(),
                         travelInfo.get(position).getAverage(),
                         travelInfo.get(position).getLocation().getCoordinates().get(1),
                         travelInfo.get(position).getLocation().getCoordinates().get(0),
                         travelInfo.get(position).getLocation().getAddress(),
                         travelInfo.get(position).getDescription(),
                         travelInfo.get(position).getTag(),
                         convertProfilesToString(travelInfo.get(position).getProfiles()));
                 travelInfo.remove(position);
                 notifyItemRemoved(position);
                 notifyItemRangeChanged(position, travelInfo.size());
                 holder.itemView.setVisibility(View.GONE);
                 Toast.makeText(view.getContext(), "Add to saved", Toast.LENGTH_SHORT).show();
             }
         });
    }


    public String convertProfilesToString(List<ProfileModel> profileModels){
        String profiles = "";
        for(ProfileModel p: profileModels){
            profiles += p.getName()+",";
        }
        return profiles;
    }
    @Override
    public int getItemCount() {
        return travelInfo.size();
    }

    public static class CardViewHolder extends ViewHolder{
          CardView cardView;
          TextView title, description, rating_text;
           ImageView savePost;
           RatingBar ratingBar;
           ViewPager viewPager;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.home_card);
            description = itemView.findViewById(R.id.tour_text);
            title = itemView.findViewById(R.id.tour_title);
            viewPager = itemView.findViewById(R.id.image_slider);
            savePost = itemView.findViewById((R.id.tour_save));
            ratingBar = itemView.findViewById(R.id.card_rating);
            rating_text = itemView.findViewById(R.id.rating_text);
        }
    }

    public void addPost(List<PlaceModel> placeModels){
        for(PlaceModel pm : placeModels){
            travelInfo.add(pm);
        }
    }

    class SliderAdapter extends PagerAdapter{
        private List<String> images;
        private Context context;
        private LayoutInflater layoutInflater;

        public SliderAdapter(Context context,List<String> images){
            this.context = context;
            this.images = images;
        }
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
           ImageView imageView = new ImageView(context);
            Picasso.get().load(images.get(position)).fit().centerCrop().into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
           container.removeView((View) object);
        }
    }
}


