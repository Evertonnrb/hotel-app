package com.nrb.hoteis.activitys;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nrb.hoteis.fragments.HotelDetalheFrangment;
import com.nrb.hoteis.R;
import com.nrb.hoteis.domain.Hotel;

public class HotelDetalheActivity extends AppCompatActivity {

    public static final String EXTRA_HOTEL = "hotel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detalhe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null){
            Intent intent = getIntent();
            Hotel hotel = (Hotel)intent.getSerializableExtra(EXTRA_HOTEL);
            HotelDetalheFrangment frangment = HotelDetalheFrangment.novaInstacia(hotel);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.detalhe,frangment,HotelDetalheFrangment.TAG_DETALHE);
            ft.commit();
        }
    }
}
