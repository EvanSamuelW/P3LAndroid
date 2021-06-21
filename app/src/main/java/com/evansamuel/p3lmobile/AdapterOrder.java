package com.evansamuel.p3lmobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.adapterUserViewHolder> {

    private List<Order> menuList;
    private Context context;
    private View view;



    public AdapterOrder(Context context, List<Order> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public AdapterOrder.adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_order, parent, false);
        return new AdapterOrder.adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrder.adapterUserViewHolder holder, int position) {
        final Order menu = menuList.get(position);

        holder.txtHarga.setText("Rp " + String.valueOf(menu.getSubtotal()));
        holder.txtNama.setText(menu.getNama_menu());
        holder.edtNumber.setText(String.valueOf(menu.getJumlah()));
        if(menu.getJumlah() == 1)
        {
            holder.btnMin.setEnabled(false);
        }

        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Hapus Pesanan")
                        .setMessage("Anda ingin menghapus pesanan ini?")
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteOrder(menu.getId());
                                if (menuList.size() == 1) {
                                    Intent intent = new Intent(context, MenuActivity.class);
                                    context.startActivity(intent);
                                }


                            }
                        }).setNegativeButton("Tidak", null)
                        .create().show();
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jumlah = Integer.valueOf(holder.edtNumber.getText().toString()) + 1;
                holder.edtNumber.setText(String.valueOf(jumlah));
                editOrder(Integer.valueOf(holder.edtNumber.getText().toString()), menu.getId_transaksi(), menu.getId_menu(), menu.getId());

            }
        });

        holder.btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jumlah = Integer.valueOf(holder.edtNumber.getText().toString()) - 1;
                holder.edtNumber.setText(String.valueOf(jumlah));
                editOrder(Integer.valueOf(holder.edtNumber.getText().toString()), menu.getId_transaksi(), menu.getId_menu(), menu.getId());
            }
        });
        Glide.with(context)
                .load(API.URL_IMG + menu.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivMenu);


    }

    @Override
    public int getItemCount() {
        return (menuList != null) ? menuList.size() : 0;
    }

    public class adapterUserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtHarga,  edtNumber;
        private Button btnAdd, btnMin;
        private LinearLayout mParent;
        private ImageView ivMenu;
        private ImageButton trash;

        public adapterUserViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHarga = itemView.findViewById(R.id.harga);
            txtNama = itemView.findViewById(R.id.namaBuku);
            btnAdd = itemView.findViewById(R.id.btnPlus);
            btnMin = itemView.findViewById(R.id.btnMin);
            edtNumber = itemView.findViewById(R.id.jumlah);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            trash = itemView.findViewById(R.id.btnHapus);
            mParent = itemView.findViewById(R.id.linearLayout);


        }
    }

    public void editOrder(final Integer jumlah, final Integer id_transaksi, final Integer id_menu, final Integer id) {
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah Pesanan");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(PUT, API.URL_PUT_ORDER + String.valueOf(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                ((OrderPage) context).swipeRefresh.setRefreshing(true);
//                    ((OrderPage) context).swipeRefresh.setRefreshing(false);
                ((OrderPage) context).getMahasiswa();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
                    ((OrderPage) context).swipeRefresh.setRefreshing(true);
                    ((OrderPage) context).getMahasiswa();
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();


                } catch (UnsupportedEncodingException | JSONException e) {
                    // exception
                }

                //do stuff with the body...
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_menu", String.valueOf(id_menu));
                params.put("id_transaksi", String.valueOf(id_transaksi));
                params.put("jumlah", String.valueOf(jumlah));
                params.put("status", "not paid");


                return params;
            }
        };
        queue.add(stringRequest);

    }

    public void deleteOrder(final Integer id) {
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus Pesanan");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, API.URL_DELETE_ORDER + String.valueOf(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    ((OrderPage) context).swipeRefresh.setRefreshing(true);

                    ((OrderPage) context).getMahasiswa();
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();


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
