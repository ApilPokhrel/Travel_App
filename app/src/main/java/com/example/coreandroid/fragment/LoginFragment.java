package com.example.coreandroid.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.coreandroid.ApiService.ApiService;
import com.example.coreandroid.ApiService.RetrofitInstance;
import com.example.coreandroid.MainActivity;
import com.example.coreandroid.R;
import com.example.coreandroid.dao.DatabaseHelper;
import com.example.coreandroid.entity.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {
     DatabaseHelper databaseHelper;
    Context context;
    TextView register;
    ApiService apiService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    EditText address;
    EditText password;
    Button submit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        context = view.getContext();
        register = view.findViewById(R.id.go_register);
        databaseHelper = new DatabaseHelper(getActivity());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    RegisterFragment fragment2 = new RegisterFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_auth, fragment2);
                    fragmentTransaction.commit();

            }
        });

        address = view.findViewById(R.id.address);
        password = view.findViewById(R.id.password);
        submit = view.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userModel = new UserModel();
                userModel.setAddress(address.getText().toString());

                userModel.setPassword(password.getText().toString());

                Retrofit retrofit = RetrofitInstance.getInstance();
                apiService = retrofit.create(ApiService.class);
                Call<UserModel> call1 = apiService.loginUser(userModel);
                call1.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        UserModel loginResponse = response.body();
                        if(response.code() != 200){
                            new AlertDialog.Builder(context)
                                    .setTitle("Login Failed")
                                    .setMessage("Invalid User Name or Password")

//                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // Continue with delete operation
//                                        }
//                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else{
                            String token = loginResponse.getTokens().get(loginResponse.getTokens().size() -1 ).getToken();
                            String _id = loginResponse.get_id();
                            databaseHelper.insertData(_id, token);
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                            getActivity().finish();

                        }

                    }
                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        new AlertDialog.Builder(context)
                                .setTitle("Login Failed")
                                .setMessage("Are you sure you are connected to Internet")
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }
        });
        return view;
    }



}
