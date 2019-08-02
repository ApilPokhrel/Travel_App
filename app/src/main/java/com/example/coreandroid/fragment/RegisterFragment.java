package com.example.coreandroid.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.coreandroid.ApiService.ApiService;

import com.example.coreandroid.ApiService.RetrofitInstance;
import com.example.coreandroid.R;
import com.example.coreandroid.entity.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterFragment extends Fragment {
     TextView login;
     EditText username, address, password, confirm_password;
     RadioGroup gender;
     RadioButton male, female, other;
     Button submit;
   static  ApiService apiInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        login = view.findViewById(R.id.go_login);
        //REGISTER todo INitiALIZE

        username = view.findViewById(R.id.username);
        address = view.findViewById(R.id.address);
        password = view.findViewById(R.id.password);
        confirm_password = view.findViewById(R.id.confirm_password);
        gender = view.findViewById(R.id.gender);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        other = view.findViewById(R.id.other);
        submit = view.findViewById(R.id.submit);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               toLogin();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean doProcess = true;
                UserModel userModel = new UserModel();
                userModel.setUsername(username.getText().toString());
                try{
                    if(checkAddress(address.getText().toString()).equalsIgnoreCase("email")){
                        userModel.setEmail(address.getText().toString());
                    } else{
                        userModel.setPhone(address.getText().toString());
                    }
                }catch(Exception ex){}

                userModel.setPassword(password.getText().toString());
                userModel.setGender(getGender());
                if(userModel.getUsername().length() < 2){
                    doProcess = false;
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Register Failed")
                            .setMessage("username must be provided")
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                if(!confirm_password.getText().toString().equalsIgnoreCase(userModel.getPassword())){
                    doProcess = false;
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Register Failed")
                            .setMessage("Password didnot matched")
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                if(address.getText().toString().length() < 5){
                    doProcess = false;
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Register Failed")
                            .setMessage("Address must be valid")
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                if(getGender() == null || getGender().length() < 2){
                    doProcess = false;
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Register Failed")
                            .setMessage("Gender must be provided")
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }


                Retrofit retrofit = RetrofitInstance.getInstance();
                 apiInterface = retrofit.create(ApiService.class);
                 if(doProcess) {
                     Call<UserModel> call1 = apiInterface.insertAllUser(userModel);
                     call1.enqueue(new Callback<UserModel>() {
                         @Override
                         public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                             UserModel loginResponse = response.body();
                             if (!response.isSuccessful()) {
                                 Toast.makeText(getActivity(), "If not Logged In try registering again", Toast.LENGTH_LONG).show();
                                 toLogin();

                             } else {
                                 Toast.makeText(getActivity(), "Login to enter", Toast.LENGTH_LONG).show();
                                 toLogin();
                             }
                         }

                         @Override
                         public void onFailure(Call<UserModel> call, Throwable t) {
                             new AlertDialog.Builder(getActivity())
                                     .setTitle("Registration Failed")
                                     .setMessage("Are you sure you are connected to Internet")
                                     .setNegativeButton(android.R.string.no, null)
                                     .setIcon(android.R.drawable.ic_dialog_alert)
                                     .show();
                         }


                     });
                 }
            }
        });
        return view;
    }

    public String getGender(){
        try {
            if (male.isChecked()) return "male";
            if (female.isChecked()) return "female";
            if (other.isChecked()) return "other";
        }catch (Exception ex){}

        return null;
    }

    public String checkAddress(String address) throws Exception {
        if(address.trim().isEmpty()){
            throw new Exception("Address is not valid");
        }
        if(address.contains("@")) return "email";
        else return "phone";
    }



    public void toLogin(){
        LoginFragment fragment2 = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_auth, fragment2);
        fragmentTransaction.commit();
    }

}
