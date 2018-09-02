package com.nrb.hoteis.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nrb.hoteis.R;
import com.nrb.hoteis.domain.Hotel;

public class HotelDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private static final String DIALOG_TAG = "editDialog";
    private static final String EXTRA_HOTEL = "hotel";

    private EditText txtNome, txtEndereco;
    private RatingBar rbEstrelas;
    private Hotel mHotel;

    public static HotelDialogFragment newInstance(Hotel hotel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_HOTEL, hotel);
        HotelDialogFragment dialog = new HotelDialogFragment();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotel = (Hotel) getArguments().getSerializable(EXTRA_HOTEL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fagment_dialogo_hotel, container, false);
        txtNome = (EditText) layout.findViewById(R.id.txtNome);
        txtNome.requestFocus();
        txtEndereco = (EditText) layout.findViewById(R.id.txtEndereco);
        txtEndereco.setOnEditorActionListener(this);
        rbEstrelas = (RatingBar) layout.findViewById(R.id.rsStars);
        if (mHotel != null) {
            txtNome.setText(mHotel.nome);
            txtEndereco.setText(mHotel.endereco);
            rbEstrelas.setRating(mHotel.estrelas);
        }
        //Abre o teclado virtual ao exibir o dialogo
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        );
        getDialog().setTitle(R.string.acao_novo);
        return layout;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            Activity activity = getActivity();
            if (activity instanceof AoSalvarHotel) {
                if (mHotel == null) {
                    mHotel = new Hotel(
                            txtNome.getText().toString(),
                            txtEndereco.getText().toString(),
                            rbEstrelas.getRating()
                    );
                } else {
                    mHotel.nome = txtNome.getText().toString();
                    mHotel.endereco = txtEndereco.getText().toString();
                    mHotel.estrelas = rbEstrelas.getRating();
                }
                AoSalvarHotel listener = (AoSalvarHotel) activity;
                listener.salvouHotel(mHotel);
                //fecha o dialogo
                dismiss();
                return true;
            }
        }
        return false;
    }

    public void abrir(FragmentManager fm){
        if(fm.findFragmentByTag(DIALOG_TAG) == null){
            show(fm,DIALOG_TAG);
        }
    }

    public interface AoSalvarHotel {
        void salvouHotel(Hotel hotel);
    }
}
