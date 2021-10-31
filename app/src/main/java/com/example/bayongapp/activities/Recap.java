package com.example.bayongapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bayongapp.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Recap extends AppCompatActivity implements View.OnClickListener{

    String TAG = Recap.class.getSimpleName();
    Button btnverifyCaptcha;
    String SECRET_KEY = "6LcwzpocAAAAACj2518aTOZ21caDCkMSr44jW3ar";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);
        btnverifyCaptcha = findViewById(R.id.buttonVer);
        btnverifyCaptcha.setOnClickListener(this);


        queue = Volley.newRequestQueue(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        SafetyNet.getClient(this).verifyWithRecaptcha("6LcwzpocAAAAAAGH7Un3vKGo233lV6-dBY-uXJ-5")
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(@NonNull SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!Objects.requireNonNull(response.getTokenResult()).isEmpty()) {
                            handleSiteVerify(response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, e -> {
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        Log.d(TAG, "Error message: " +
                                CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                    } else {
                        Log.d(TAG, "Unknown type of error: " + e.getMessage());
                    }
                });

    }
    protected  void handleSiteVerify(final String responseToken){
        //it is google recaptcha siteverify server
        //you can place your server url
        String url = "https://www.google.com.google.recaptcha.androidapp";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getBoolean("success")){
                            Toast.makeText(getApplicationContext(),String.valueOf(jsonObject.getBoolean("success")),Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("error-codes"),Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        Log.d(TAG, "JSON exception: " + ex.getMessage());

                    }
                },
                error -> Log.d(TAG, "Error message: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SECRET_KEY);
                params.put("response", responseToken);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
