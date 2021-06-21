package com.evansamuel.p3lmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.adapterUserViewHolder> {

    private List<Menu> menuList;
    private List<Menu> menuListFiltered;
    private Context context;
    private View view;
    private int idTransaksi;


    public AdapterMenu(Context context, List<Menu> menuList, int idTransaksi) {
        this.context = context;
        this.menuList = menuList;
        this.menuListFiltered = menuList;
        this.idTransaksi = idTransaksi;

    }

    @NonNull
    @Override
    public adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_menu, parent, false);
        return new adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterUserViewHolder holder, int position) {
        final Menu menu = menuListFiltered.get(position);

        if(menu.getStok()<1)
        {
            holder.txtStok.setVisibility(View.VISIBLE);
            holder.ivOrder.setVisibility(View.GONE);
        }

        holder.txtHarga.setText("Rp " + String.valueOf(Math.round(menu.getHarga())) + " / " +menu.getSatuan());
        holder.txtNama.setText(menu.getNama_menu());
        holder.txtjenis.setText(menu.getTipe());
        Glide.with(context)
                .load(API.URL_IMG + menu.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivGambar);

        holder.ivOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahOrder(menu.getIdMenu(), idTransaksi);

            }
        });

        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                DetailMenuFragment dialog = new DetailMenuFragment();
                dialog.show(manager, "dialog");


                Bundle args = new Bundle();
                args.putInt("id", menu.getIdMenu());
                args.putString("nama_menu", menu.getNama_menu());
                args.putString("harga", menu.getHarga().toString());
                args.putString("deskripsi", menu.getDeskripsi());
                args.putString("gambar", menu.getGambar());
                args.putString("satuan", menu.getSatuan());
                args.putString("jenis", menu.getTipe());
                dialog.setArguments(args);
            }
        });


    }

    @Override
    public int getItemCount() {
        return (menuListFiltered != null) ? menuListFiltered.size() : 0;
    }

    public class adapterUserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtHarga,txtjenis,txtStok;
        private ImageView ivGambar;
        private MaterialButton ivOrder;
        private LinearLayout mParent;

        public adapterUserViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHarga = itemView.findViewById(R.id.txtHarga);
            txtNama = itemView.findViewById(R.id.MenuName);
            txtjenis = itemView.findViewById(R.id.MenuJenis);
            txtStok = itemView.findViewById(R.id.txtStok);
            ivGambar = itemView.findViewById(R.id.ivMenu);
            ivOrder = itemView.findViewById(R.id.edit);
            mParent = itemView.findViewById(R.id.linearLayout);

            if(idTransaksi == 0)
            {
                ivOrder.setVisibility(View.GONE);
            }

        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString().toLowerCase();
                if (userInput.isEmpty()) {
                    menuListFiltered = menuList;
                } else {
                    List<Menu> filteredList = new ArrayList<>();
                    for (Menu mahasiswa : menuList) {
                        if (mahasiswa.getNama_menu().toLowerCase().contains(userInput) ||
                                mahasiswa.getDeskripsi().toLowerCase().contains(userInput)) {
                            filteredList.add(mahasiswa);
                        }
                    }
                    menuListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = menuListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                menuListFiltered = (ArrayList<Menu>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void tambahOrder(final Integer id_menu, final Integer id_transaksi) {
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan Pesanan");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, API.URL_ADD_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan status dari response
//                    if(obj.getString("message").equals("Order berhasil ditambahkan"))
//                    {
//                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
//                    }

                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                    body = new String(error.networkResponse.data,"UTF-8");
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
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_menu", String.valueOf(id_menu));
                params.put("id_transaksi", String.valueOf(id_transaksi));
                params.put("jumlah", String.valueOf(1));


                return params;
            }
        };
        queue.add(stringRequest);

    }

}