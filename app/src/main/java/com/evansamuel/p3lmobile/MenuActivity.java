package com.evansamuel.p3lmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.android.volley.Request.Method.GET;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterMenu adapterMenu;
    private List<Menu> menuList;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefresh;
    private FloatingActionButton fab,fab2,fab3;
    private int idTransaksi;
    private boolean clicked= false;
    private Animation anim1, anim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        searchView = findViewById(R.id.searchUser);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        idTransaksi = ((Reservasi) getApplication()).getIdTransaksi();
       anim1 = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        if(idTransaksi == 0)
        {
            fab.setVisibility(GONE);
        }

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), OrderPage.class);
                startActivity(i);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TransactionPage.class);
                startActivity(i);
            }
        });
        swipeRefresh.setRefreshing(true);
        setAdapter();
        getMahasiswa();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
getMahasiswa();
            }
        });

      fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
             setVisibility(clicked);
             setAnimation(clicked);
             clicked = !clicked;
          }
      });

    }

    private void setVisibility(boolean clicked)
    {
        if(!clicked) {
            fab2.setVisibility(View.VISIBLE);
            fab3.setVisibility(View.VISIBLE);
        }
        else
        {
            fab2.setVisibility(View.INVISIBLE);
            fab3.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked)
    {
        if(!clicked){
            fab2.startAnimation(anim1);
            fab3.startAnimation(anim1);
        }else {
            fab2.startAnimation(anim2);
            fab3.startAnimation(anim2);
        }
    }

    public void setAdapter() {
        menuList = new ArrayList<Menu>();
        recyclerView = findViewById(R.id.menu_rv);
        int idTransaksi = ((Reservasi) getApplication()).getIdTransaksi();
        adapterMenu = new AdapterMenu(this, menuList, idTransaksi);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MenuActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterMenu);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapterMenu.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterMenu.getFilter().filter(s);
                return false;
            }
        });
    }

    //Fungsi menampilkan data mahasiswa
    public void getMahasiswa() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());



        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, API.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!menuList.isEmpty())
                        menuList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        Integer id = jsonObject.optInt("id");
                        String nama_menu = jsonObject.optString("nama_menu");
                        String deskripsi = jsonObject.optString("deskripsi");
                        String tipe = jsonObject.optString("tipe");
                        String gambar = jsonObject.optString("gambar");
                        String satuan = jsonObject.optString("satuan");
                        Double harga = jsonObject.optDouble("harga");
                        Integer stok = jsonObject.optInt("stok");

                        Menu menu = new Menu(nama_menu, deskripsi, tipe, gambar, satuan, id, harga,stok);
                        menuList.add(menu);
                    }
                    swipeRefresh.setRefreshing(false);
                    adapterMenu.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), response.optString("message"),
                        Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(stringRequest);
    }
}