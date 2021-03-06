package com.rai.umesh.cmrlhelper;

/*
* This Activity gets the location coordinates from the preceding activity and presents a screen to
* user for inputing the username and password. It uses Volley module to connect to URL and send
* request and get response. It uses UserLoginRequest for POST array. The response array is passed to
* next program in chain.
* B. Umesh Rai
 */

import android.content.Intent;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity {

    private String latitude = "";
    private String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView tvAssetcode = (TextView) findViewById(R.id.tvAssetcode);
        final TextView tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        final TextView tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        final Button btLogin = (Button) findViewById(R.id.btLogin);

        // Save values from predecessor
        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        tvLatitude.setText("Latitude: " + latitude);
        tvLongitude.setText("Longitude: " + longitude);

        // User can go Asset Decipher instead
        tvAssetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assetIntent = new Intent(UserActivity.this, AssetDecipherActivity.class);
                assetIntent.putExtra("latitude", latitude); // Save values for return
                assetIntent.putExtra("longitude", longitude);
                UserActivity.this.startActivity(assetIntent);
            }
        });

        // Start Login. JSON Request and Response
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store Username/Password
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            //Log.i("value", jsonResponse.toString());
                            boolean success = jsonResponse.getJSONObject("data").getBoolean("success");
                            String userid = jsonResponse.getJSONObject("data").getString("id");
                            String organisation = jsonResponse.getJSONObject("data").getString("organisation");
                            String auth_key = jsonResponse.getJSONObject("data").getString("auth_key");
                            //Log.i("value",  "success: " + String.valueOf(success) + ", auth_key: "+ auth_key);

                            if (success) {
                                //Pass to successor program
                                Intent intent = new Intent(UserActivity.this, UserDataActivity.class);
                                intent.putExtra("userid", userid);
                                intent.putExtra("username", username);
                                intent.putExtra("organisation", organisation);
                                intent.putExtra("latitude", latitude);
                                intent.putExtra("longitude", longitude);
                                intent.putExtra("auth_key", auth_key);
                                UserActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // Volley Request
                UserLoginRequest loginRequest = new UserLoginRequest(username, password, latitude, longitude,responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserActivity.this);
                queue.add(loginRequest);
                //Log.i("values", "username: "+username+"/"+password+" location: "+latitude+"/"+longitude );
            }
        });
    }
}