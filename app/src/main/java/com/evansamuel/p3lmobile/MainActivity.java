package com.evansamuel.p3lmobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.android.volley.Request.Method.POST;

public class MainActivity extends AppCompatActivity {
    Button scanBtn,menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = findViewById(R.id.btnScan);
        menuBtn = findViewById(R.id.btnMenu);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
            }
        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        MainActivity.this
                );

                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(ScanActivity.class);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );

        if (intentResult.getContents() != null) {
            scanqr(intentResult.getContents());
        } else {
            Toast.makeText(getApplicationContext(), "Scan fail", Toast.LENGTH_LONG);
        }
    }

    public void scanqr(final String result) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.next_progress_bar);

        progressBar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(POST, API.URL_SCAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getString("message").equals("Reservasi ditemukan")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        JSONObject reservasi = jsonArray.getJSONObject(0);

                        ((Reservasi) getApplication()).setId(reservasi.getInt("id"));
                        ((Reservasi) getApplication()).setIdUser(reservasi.getInt("id_pelanggan"));
                        ((Reservasi) getApplication()).setIdTransaksi(reservasi.getInt("id_transaksi"));
                        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(i);
                    }

                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                    JSONObject obj = new JSONObject(body);
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (UnsupportedEncodingException | JSONException e) {
                    // exception
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("qrcode", result);
                return params;


            }
        };
        queue.add(stringRequest);

    }

}