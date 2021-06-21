package com.evansamuel.p3lmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.PUT;

public class OrderPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterOrder adapterOrder;
    private List<Order> menuList;
    private TextView totalBiaya;
    public SwipeRefreshLayout swipeRefresh;
    private Integer total;
    private MaterialButton btnCheck;
    AlertDialog.Builder builder;
    private ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        totalBiaya = findViewById(R.id.totalBiaya);
        btnCheck = findViewById(R.id.btnMenu);
        builder = new AlertDialog.Builder(this);
        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        int idTransaksi = ((Reservasi) getApplication()).getIdTransaksi();
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Lanjut Pesanan")
                        .setMessage("Anda ingin melanjutkan pesanan?")
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                proceedOrder(idTransaksi);


                            }
                        }).setNegativeButton("Tidak", null)
                        .create().show();
            }


        });

        swipeRefresh.setProgressViewOffset(false, -200, -200);
        setAdapter();
        getMahasiswa();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMahasiswa();
            }
        });


    }


    public void setAdapter() {
        menuList = new ArrayList<Order>();
        recyclerView = findViewById(R.id.recycler_view);
        adapterOrder = new AdapterOrder(this, menuList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderPage.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterOrder);

    }

    //Fungsi menampilkan data mahasiswa
    public void getMahasiswa() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        int idTransaksi = ((Reservasi) getApplication()).getIdTransaksi();
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, API.URL_GET_ORDER + String.valueOf(idTransaksi)
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                total = 0;

                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!menuList.isEmpty())
                        menuList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        Integer id = jsonObject.optInt("id");
                        Integer id_menu = jsonObject.optInt("id_menu");
                        Integer id_transaksi = jsonObject.optInt("id_transaksi");
                        Integer jumlah = jsonObject.optInt("jumlah");
                        Integer subtotal = jsonObject.optInt("subtotal");
                        String photo = jsonObject.optString("photo");
                        String status = jsonObject.optString("status");
                        String nama_menu = jsonObject.optString("nama_menu");
                        Double harga = jsonObject.optDouble("harga");

                        Order order = new Order(status, photo, id, id_menu, id_transaksi, jumlah, subtotal, nama_menu, harga);
                        total += subtotal;
                        totalBiaya.setText("Rp. " + total.toString());
                        menuList.add(order);
                    }
                    swipeRefresh.setRefreshing(false);
                    adapterOrder.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                    JSONObject obj = new JSONObject(body);
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (UnsupportedEncodingException | JSONException e) {
                    // exception
                }

            }

        });
        queue.add(stringRequest);
    }

    public void proceedOrder(final Integer id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Menambahkan data buku");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(PUT, API.URL_CHECKOUT_ORDER + String.valueOf(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                    JSONObject obj = new JSONObject(body);
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                } catch (UnsupportedEncodingException | JSONException e) {
                    // exception
                }

                //do stuff with the body...
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        queue.add(stringRequest);

    }
}