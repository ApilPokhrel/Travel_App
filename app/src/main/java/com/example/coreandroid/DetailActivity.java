package com.example.coreandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coreandroid.ApiService.ApiService;
import com.example.coreandroid.ApiService.RetrofitInstance;
import com.example.coreandroid.adapter.RatingAdapter;
import com.example.coreandroid.dao.DatabaseHelper;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.ProfileModel;
import com.example.coreandroid.entity.RatingModel;
import com.example.coreandroid.fragment.MapFragment;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {
     TextView title, description, lat, lng, address, tag;
     RatingBar ratingBar;
     EditText editText;
     Button submit;
     ApiService apiService;
     DatabaseHelper databaseHelper;
     RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
         ratingBar = findViewById(R.id.ratingBar);
         editText = findViewById(R.id.ratingEditText);
         submit = findViewById(R.id.ratingSubmitButton);
         databaseHelper = new DatabaseHelper(this);

        Retrofit retrofit = RetrofitInstance.getInstance();
        apiService = retrofit.create(ApiService.class);



        Bundle bundle = getIntent().getExtras();
        final PlaceModel placeModel = (PlaceModel) bundle.getSerializable("place");
        List<PlaceModel> placeModels = new ArrayList<>();
        placeModels.add(placeModel);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color=\"red\">" +  placeModel.getTitle() + "</font>"));
        }


        ViewPager viewPager = findViewById(R.id.image_slider);

        List<String> imageUrls = new ArrayList<>();
        for(ProfileModel p: placeModel.getProfiles()) {
            System.out.println("image  ===="+p.getName());
            if(p.getName().length() > 5) imageUrls.add(p.getName());
        }

        SliderAdapter sliderAdapter = new SliderAdapter(this, imageUrls);
        viewPager.setAdapter(sliderAdapter);

        description = findViewById(R.id.description);
        description.setText(placeModel.getDescription());


        tag = findViewById(R.id.tag);
        tag.setText(placeModel.getTag());

        recyclerView = findViewById(R.id.rating_recycler);
        recyclerView.setHasFixedSize(true);
        if(placeModel.getRatings() != null) {
            RatingAdapter ratingAdapter = new RatingAdapter(placeModel.getRatings());
            recyclerView.setAdapter(ratingAdapter);
            // 2. set layoutManger
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("token", databaseHelper.checkToken());
        Call<RatingModel> call2 = apiService.getRating(headers,placeModel.get_id());
        call2.enqueue(new Callback<RatingModel>() {
            @Override
            public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                RatingModel loginResponse = response.body();
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), "Try again", Toast.LENGTH_LONG).show();

                }else{
                    if(loginResponse != null){
                        editText.setText(loginResponse.getText());
                        ratingBar.setRating((float) loginResponse.getRating());
                    }
                }
            }

            @Override
            public void onFailure(Call<RatingModel> call, Throwable t) {
                Toast.makeText(getApplication(), "Are you sure you are connected to internet", Toast.LENGTH_LONG).show();
            }

        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float f =  ratingBar.getRating();
                if(f < 0.5){
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Rating Fault")
                            .setMessage("please provide valid rating")
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{

                    final RatingModel ratingModel = new RatingModel();
                    ratingModel.setRating(f);
                    ratingModel.setText(editText.getText().toString());
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("token", databaseHelper.checkToken());
                    Call<RatingModel> call1 = apiService.postRating(headers,placeModel.get_id(), ratingModel);
                    call1.enqueue(new Callback<RatingModel>() {
                        @Override
                        public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                            RatingModel loginResponse = response.body();
                            if(!response.isSuccessful()){
                                Toast.makeText(getApplication(), "Try again", Toast.LENGTH_LONG).show();

                            }else{
                                Toast.makeText(getApplication(), "you rated " + ratingModel.getRating(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RatingModel> call, Throwable t) {
                            Toast.makeText(getApplication(), "Are you sure you are connected to internet", Toast.LENGTH_LONG).show();
                        }


                    });
                }
            }
        });

        loadFragment(new MapFragment(placeModels, 10f));
    }

    public boolean loadFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment1, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public float calculateZoom(double lat, double lng){
        return 1f;
    }


    class SliderAdapter extends PagerAdapter {
        private List<String> images;
        private Context context;
        private LayoutInflater layoutInflater;

        public SliderAdapter(Context context,List<String> images){
            this.context = context;
            this.images = images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.image_slider, container, false);

            ImageView imageView = itemView.findViewById(R.id.imageView);
            OkHttpClient client = new OkHttpClient();
//            client.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String s, SSLSession sslSession) {
//                    return true;
//                }
//            });
//            TrustManager[] trustAllCerts = new  [] { new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(
//                        X509Certificate[] x509Certificates,
//                        String s) throws CertificateException {
//                }
//
//                @Override
//                public void checkServerTrusted(
//                        X509Certificate[] x509Certificates,
//                        String s) throws CertificateException {
//                }
//
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[] {};
//                }
//            } };
//            try {
//                SSLContext sc = SSLContext.getInstance("TLS");
//                sc.init(null, trustAllCerts, new SecureRandom());
//                client.setSslSocketFactory(sc.getSocketFactory());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(new Interceptor() {
//                        @Override
//                        public okhttp3.Response intercept(Chain chain) throws IOException {
//                            Request newRequest = chain.request().newBuilder()
//                                    .addHeader(":authority", "s-ec.bstatic.com")
//                                    .addHeader(":method", "GET")
//                                    .addHeader(":path", "/images/hotel/max400/205/205588915.jpg")
//                                    .addHeader(":scheme", "https")
//                                    .addHeader("if-none-match", "9961ce5ee18a18bf0abb471cb3db8adbd81e1df9")
//                                    .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
//                                    .addHeader("accept-encoding", "gzip, deflate, br")
//                                    .addHeader("accept-language", "en-US,en;q=0.9")
//                                    .addHeader("cache-control", "max-age=0")
//                                    .addHeader("upgrade-insecure-requests", "1")
//                                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
//                                    .build();
//                            return chain.proceed(newRequest);
//                        }
//                    })
//                    .build();
            Picasso picasso = new Picasso.Builder(itemView.getContext()).downloader(new OkHttp3Downloader(client)).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e("PICASSO", exception.getMessage());
                }
            })
                    .build();
            picasso.load(images.get(position)).fit().centerCrop().into(imageView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
