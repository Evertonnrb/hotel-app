package com.nrb.hoteis.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nrb.hoteis.R;
import com.nrb.hoteis.domain.Hotel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HotelListFragment extends ListFragment implements
        ActionMode.Callback,
        AdapterView.OnItemLongClickListener {

    List<Hotel> mHotels;
    ArrayAdapter<Hotel> mAdapter;
    ListView mListView;
    ActionMode mActionMode;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        mHotels = carregarHoteis();
        mListView = getListView();
        limpar();
        mAdapter = new ArrayAdapter<Hotel>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                mHotels
        );
        setListAdapter(mAdapter);

    }

    public void buscar(String s) {
        if (s == null || s.trim().equals("")) {
            limpar();
            return;
        }
        List<Hotel> hoteisEncontrados = new ArrayList<Hotel>(mHotels);
        for (int i = hoteisEncontrados.size() - 1; i >= 0; i--) {
            Hotel hoteEncontrado = hoteisEncontrados.get(i);
            if (!hoteEncontrado.nome.toUpperCase().contains(s.toUpperCase())) {
                hoteisEncontrados.remove(hoteEncontrado);
            }
        }
        mListView.setOnItemLongClickListener(null);
        mAdapter = new ArrayAdapter<Hotel>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                hoteisEncontrados
        );
        setListAdapter(mAdapter);
    }


    public void limpar() {

        mListView.setOnItemLongClickListener(this);
        ordenar();
        mAdapter = new ArrayAdapter<Hotel>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                mHotels
        );
        setListAdapter(mAdapter);
    }

    public void adiciona(Hotel hotel) {
        mHotels.add(hotel);
        ordenar();
        mAdapter.notifyDataSetChanged();
    }

    private List<Hotel> carregarHoteis() {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel("Ibis", "Av Atlantica", 4.5f));
        hotels.add(new Hotel("Dallas", "AV BRASIL", 4.0f));
        hotels.add(new Hotel("Canario", "Barra do Garça", 2.4f));
        hotels.add(new Hotel("Chicago", "Itatiaia", 1.4f));
        hotels.add(new Hotel("Copa Cabana Pallace", "Itatiaia", 4.8f));
        hotels.add(new Hotel("Copa Cabana Pallace", "Capabana", 4.8f));
        hotels.add(new Hotel("Royal", "Sao Bernardo", 5f));
        hotels.add(new Hotel("Belivery", "Sao migule", 3.2f));
        hotels.add(new Hotel("Itaime", "Sigule", 2.2f));
        hotels.add(new Hotel("Atlantic", "Belo horizonte", 4.2f));
        return hotels;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (mActionMode == null) {
            Activity activity = getActivity();
            if (activity instanceof AoClicarNoHotel) {
                Hotel hotel = (Hotel) l.getItemAtPosition(position);
                AoClicarNoHotel listener = (AoClicarNoHotel) activity;
                listener.cliclouNoHotel(hotel);
            }
        } else {
            atualizarItensMarcados(mListView, position);
            if (qtdItensMarcados() == 0) {
                mActionMode.finish();
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_delete_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.acao_delete) {
           /* SparseBooleanArray checked = mListView.getCheckedItemPositions();
            for (int i = checked.size() - 1; i >= 0; i--) {
                if (checked.valueAt(i)) {
                    mHotels.remove(checked.keyAt(i));
                }
            }
            actionMode.finish();
            return true;*/
            remover();
            actionMode.finish();
            return true;
        }

        return false;
    }

    private void remover() {
        final List<Hotel> hoteisExcluidos = new ArrayList<Hotel>();
        SparseBooleanArray checked = mListView.getCheckedItemPositions();
        for (int i = checked.size() - 1; i >= 0; i--) {
            if (checked.valueAt(i)) {
                int position = checked.keyAt(i);
                hoteisExcluidos.add(mHotels.remove(position));
            }
        }
        Snackbar.make(
                mListView,
                getString(R.string.mensagem_excluir, hoteisExcluidos.size()), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.acao_desfazer,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (Hotel hotel : hoteisExcluidos) {
                                    mHotels.add(hotel);
                                }
                                limpar();
                            }
                        })
                .setActionTextColor(getResources().getColor(R.color.colorBranca))
                .show();
    }

    private void ordenar(){
        Collections.sort(mHotels, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel o1, Hotel o2) {
                return o1.nome.compareTo(o2.nome);
            }
        });
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mActionMode = null;
        mListView.clearChoices();
        mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        limpar();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        boolean consumed = (mActionMode == null);
        if (consumed) {
            iniciarModoExclusao();
            mListView.setItemChecked(position, true);
            atualizarItensMarcados(mListView, position);
        }
        return consumed;
    }

    public interface AoClicarNoHotel {
        void cliclouNoHotel(Hotel hotel);
    }

    private void iniciarModoExclusao() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mActionMode = activity.startSupportActionMode(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void atualizarItensMarcados(ListView l, int position) {
        l.setItemChecked(position, l.isItemChecked(position));
        atualizarTudo();
    }

    private int qtdItensMarcados() {
        SparseBooleanArray checked = mListView.getCheckedItemPositions();
        int checkedCount = 0;
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i)) {
                checkedCount++;
            }
        }
        return checkedCount;
    }

    private void atualizarTudo() {
        int checkedCount = qtdItensMarcados();
        String selecionados = getResources().getQuantityString(
                R.plurals.numero_selecionado,
                checkedCount,
                checkedCount
        );
    }
}

