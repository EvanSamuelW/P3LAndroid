package com.evansamuel.p3lmobile;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;

/**

 */
public class DetailMenuFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private TextView twNama, twHarga, twDeskripsi,twJenis;
    private String sNama, sHarga,sDeskripsi,sGambar,sJenis,sSatuan;
    private int sId;
    private ImageButton ibClose;
    private ImageView ivMenu;
    private MaterialButton btnCart;
    // TODO: Rename and change types of parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public DetailMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment DetailMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static DetailMenuFragment newInstance(String param1, String param2) {
//        DetailMenuFragment fragment = new DetailMenuFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_menu, container, false);
        ibClose = v.findViewById(R.id.ibClose);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        twNama = v.findViewById(R.id.twMenuName);
        twHarga = v.findViewById(R.id.twMenuPrice);
        twJenis =v.findViewById(R.id.twJenis);
        twDeskripsi = v.findViewById(R.id.twMenuDescription);
        ivMenu = v.findViewById(R.id.ivMenu);
        sId = getArguments().getInt("id",0);
        sNama = getArguments().getString("nama_menu","");
        sDeskripsi = getArguments().getString("deskripsi","");
        sHarga = getArguments().getString("harga","");
        sGambar = getArguments().getString("gambar","");
        sJenis = getArguments().getString("jenis","");
        sSatuan = getArguments().getString("satuan","");

        btnCart = v.findViewById(R.id.btnMenu);

        Glide.with(getContext())
                .load(API.URL_IMG+ sGambar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivMenu);
        twNama.setText(sNama);
        twJenis.setText(sJenis);
        twHarga.setText("Rp " + String.valueOf(Math.round(Double.valueOf(sHarga))) + " / " +sSatuan);
        twDeskripsi.setText(sDeskripsi);
        return v;
    }
}