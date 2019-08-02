package com.example.coreandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coreandroid.ApiService.ApiService;
import com.example.coreandroid.ApiService.RetrofitInstance;
import com.example.coreandroid.dao.DatabaseHelper;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.fragment.HomeFragment;
import com.example.coreandroid.fragment.MapFragment;
import com.example.coreandroid.fragment.NearFragment;
import com.example.coreandroid.fragment.SavedFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.coreandroid.entity.Variables.ERROR_DIALOG_REQUEST;
import static com.example.coreandroid.entity.Variables.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.coreandroid.entity.Variables.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView textView;
    boolean mLocationPermissionGranted = false;
    private static  final String TAG = "MainActivity";
    DatabaseHelper databaseHelper;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
         actionBar.setTitle(Html.fromHtml("<font color=\"red\">" + getString(R.string.app_name) + "</font>"));
        }
        databaseHelper = new DatabaseHelper(this);
        Retrofit retrofit = RetrofitInstance.getInstance();
        apiService = retrofit.create(ApiService.class);
        loadFragment(new HomeFragment());
        bottomNavigationView = findViewById(R.id.navigation);
        textView = findViewById(R.id.text);

      bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                  switch (menuItem.getItemId()){
                      case R.id.home:
                          loadFragment(new HomeFragment());
                          break;
                      case R.id.saved:
                          loadFragment(new SavedFragment());
                          break;
                      case R.id.near:
                          loadFragment(new NearFragment());
                          break;
                      case R.id.map:
                          Map<String, String> headers = new HashMap<String, String>();
                          headers.put("token", databaseHelper.checkToken());
                          Call<List<PlaceModel>> call1 = apiService.getAllPlace(headers, 0, 50, null, null, null, null, null);
                          call1.enqueue(new Callback<List<PlaceModel>>() {
                              @Override
                              public void onResponse(Call<List<PlaceModel>> call, Response<List<PlaceModel>> response) {
                                  List<PlaceModel> placeModels =  response.body();
                                  loadFragment(new MapFragment(placeModels, 8f));
                              }

                              @Override
                              public void onFailure(Call<List<PlaceModel>> call, Throwable t) {
                                  Toast.makeText(getApplication(),"No Internet Connection", Toast.LENGTH_LONG).show();
                              }

                          });
                          break;
                  }
                 return true;
               }
              });
    }



    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
//             loadFragment(new MapFragment());
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
//                    getChatrooms();
//                    loadFragment(new HomeFragment());

                }
                else{
                    getLocationPermission();
                }
            }
        }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case R.id.logout:
                databaseHelper.deleteByToken(databaseHelper.checkToken());
                Intent i = new Intent(this, AuthActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }


    @Override
     protected void onResume(){
        super.onResume();
        if(checkMapServices()){
            if(mLocationPermissionGranted){
//                 loadFragment(new HomeFragment());
            }else{
                 getLocationPermission();
            }
        }
    }



    public void refreshFragment(String tag){
        Fragment frg = null;
        frg = getSupportFragmentManager().findFragmentByTag(tag);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }




}
