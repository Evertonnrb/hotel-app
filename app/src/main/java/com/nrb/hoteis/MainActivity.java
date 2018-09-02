package com.nrb.hoteis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.nrb.hoteis.activitys.HotelDetalheActivity;
import com.nrb.hoteis.domain.Hotel;
import com.nrb.hoteis.fragments.HotelDetalheFrangment;
import com.nrb.hoteis.fragments.HotelDialogFragment;
import com.nrb.hoteis.fragments.HotelListFragment;

public class MainActivity extends AppCompatActivity implements HotelListFragment.AoClicarNoHotel,
        SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener,
HotelDialogFragment.AoSalvarHotel{

    private FragmentManager mFragmanager;
    private HotelListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoteis);

        mFragmanager = getSupportFragmentManager();
        mListFragment = (HotelListFragment) mFragmanager.findFragmentById(R.id.fragLista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hotel, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.hint_buscar));
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_info:
                SobreDialogFragment dialogFragment = new SobreDialogFragment();
                dialogFragment.show(getSupportFragmentManager(),"sobre");
                break;
            case R.id.action_new:
                HotelDialogFragment hotelDialogFragment =  HotelDialogFragment.newInstance(null);
                hotelDialogFragment.abrir(getSupportFragmentManager());
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void cliclouNoHotel(Hotel hotel) {

        if (isTablet()) {
            HotelDetalheFrangment frangment = HotelDetalheFrangment.novaInstacia(hotel);
            FragmentTransaction tf = mFragmanager.beginTransaction();
            tf.replace(R.id.detalhe, frangment, HotelDetalheFrangment.TAG_DETALHE);
            tf.commit();
        } else {
            Intent it = new Intent(this, HotelDetalheActivity.class);
            it.putExtra(HotelDetalheActivity.EXTRA_HOTEL, hotel);
            startActivity(it);
        }
    }

    private boolean isTablet() {
        return findViewById(R.id.detalhe) != null;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;//expande a view
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        mListFragment.limpar();
        return true;//para voltar ao normal
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mListFragment.buscar(s);
        return false;
    }

    @Override
    public void salvouHotel(Hotel hotel) {
        mListFragment.adiciona(hotel);
    }

    public void adiconarHotel(View view) {
        HotelDialogFragment fragment = HotelDialogFragment.newInstance(null);
        fragment.abrir(getSupportFragmentManager());
    }

    public static class SobreDialogFragment extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == DialogInterface.BUTTON_NEGATIVE){
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/Evertonnrb"));
                        startActivity(intent);

                    }
                }
            };

            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.str_titulo))
                    .setMessage(getString(R.string.str_msg))
                    .setPositiveButton(getString(R.string.bt_ok),null)
                    .setNegativeButton(getString(R.string.str_site),listener)
                    .create();
            return dialog;
        }
    }
}

