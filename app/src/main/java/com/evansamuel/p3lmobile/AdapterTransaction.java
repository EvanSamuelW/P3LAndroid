package com.evansamuel.p3lmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.PUT;

public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.adapterUserViewHolder> {

    private List<Order> menuList;
    private Context context;
    private View view;


    public AdapterTransaction(Context context, List<Order> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public AdapterTransaction.adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_transaction, parent, false);
        return new AdapterTransaction.adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTransaction.adapterUserViewHolder holder, int position) {
        final Order menu = menuList.get(position);
        Glide.with(context)
                .load(API.URL_IMG + menu.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivMenu);

        holder.twPrice.setText("Rp " + String.valueOf(Math.round(menu.getSubtotal())));
        holder.twJumlah.setText(String.valueOf(menu.getJumlah()));
        holder.twNama.setText(menu.getNama_menu());
        holder.twStatus.setText(menu.getStatus());


    }

    @Override
    public int getItemCount() {
        return (menuList != null) ? menuList.size() : 0;
    }

    public class adapterUserViewHolder extends RecyclerView.ViewHolder {
        private TextView twNama, twPrice, twStatus, twJumlah;
        private LinearLayout mParent;
        private ImageView ivMenu;

        public adapterUserViewHolder(@NonNull View itemView) {
            super(itemView);

            twNama = itemView.findViewById(R.id.twNama);
            twPrice = itemView.findViewById(R.id.twSubtotal);
            twStatus = itemView.findViewById(R.id.twStatus);
            twJumlah = itemView.findViewById(R.id.twJumlah);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            mParent = itemView.findViewById(R.id.linearLayout);


        }
    }


}
