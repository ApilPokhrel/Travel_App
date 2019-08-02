package com.example.coreandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;

import com.example.coreandroid.dao.DatabaseHelper;
import com.example.coreandroid.fragment.LoginFragment;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color=\"red\">" + getString(R.string.app_name) + "</font>"));
        }
        databaseHelper = new DatabaseHelper(this);
        String token = checkToken();
        if(token != null){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }else {
            loadFragment(new LoginFragment());
        }
    }

    public boolean loadFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_auth, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    public String checkToken(){
        List<String> tokens = databaseHelper.getAll();
        if(!tokens.isEmpty()){
            return tokens.get(tokens.size()-1);
        }else{
            return null;
        }
    }
}
