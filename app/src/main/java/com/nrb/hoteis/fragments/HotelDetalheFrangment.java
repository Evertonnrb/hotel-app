package com.nrb.hoteis.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nrb.hoteis.R;
import com.nrb.hoteis.domain.Hotel;

public class HotelDetalheFrangment extends Fragment {

    public static final String TAG_DETALHE = "tagDetalhe";
    public static final String EXTRA_HOTEL = "hotel";

    TextView mTxtNome,mTxtEndereco;
    RatingBar mRatingBar;
    Hotel mHotel;

    public static HotelDetalheFrangment novaInstacia(Hotel hotel){
        Bundle parametros = new Bundle();
        parametros.putSerializable(EXTRA_HOTEL,hotel);
        HotelDetalheFrangment frangment = new HotelDetalheFrangment();
        frangment.setArguments(parametros);
        return frangment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotel = (Hotel)getArguments().getSerializable(EXTRA_HOTEL);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_detalhe_hotel,container,false);
        mTxtNome = (TextView)layout.findViewById(R.id.txtNome);
        mTxtEndereco = (TextView)layout.findViewById(R.id.txtEndereco);
        mRatingBar = (RatingBar)layout.findViewById(R.id.rtEstrelas);

        if(mHotel!=null){
            mTxtEndereco.setText(mHotel.nome);
            mTxtEndereco.setText(mHotel.endereco);
            mRatingBar.setRating(mHotel.estrelas);
        }
        return layout;
    }
}
