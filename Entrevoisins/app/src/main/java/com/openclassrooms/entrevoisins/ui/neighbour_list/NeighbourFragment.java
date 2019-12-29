package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.intents.IntentName;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_details.NeighbourDetailsActivity;
import com.openclassrooms.entrevoisins.utils.ItemClickSupport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NeighbourFragment extends Fragment {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;


    @BindView(R.id.tv_no_neighbours)
    public TextView tvNoNeighbours;

    @BindView(R.id.list_neighbours)
    public RecyclerView mRecyclerView;

    private MyNeighbourRecyclerViewAdapter adapter;

    /**
     * Create and return a new instance
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance() {

        Log.i("DEBUG", "NeighbourFragment NewInstance");

        NeighbourFragment fragment = new NeighbourFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
        EventBus.getDefault().register(this);

        Log.i("DEBUG", "NeighbourFragment onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        Context context = view.getContext();
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        initList();
        checkIfRecyclerViewIsEmpty();
        initClickOnItem();
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        mNeighbours = mApiService.getNeighbours();
        adapter = new MyNeighbourRecyclerViewAdapter(mNeighbours);
        mRecyclerView.setAdapter(adapter);
    }

    private void initClickOnItem(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_neighbour)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Neighbour selectedNeighbour = adapter.getNeighbour(position);
                    Intent intent = new Intent(getContext(), NeighbourDetailsActivity.class);
                    intent.putExtra(IntentName.INFORMATION_ACTIVITY_INTENT_NAME, selectedNeighbour.getId());
                    startActivity(intent);
                });
    }

    /**
     * Vérifie si la liste de voisin est vide.
     */
    private void checkIfRecyclerViewIsEmpty() {
        if (this.adapter.getItemCount() == 0) {
            this.mRecyclerView.setVisibility(View.INVISIBLE);
            this.tvNoNeighbours.setVisibility(View.VISIBLE);
        } else {
            this.mRecyclerView.setVisibility(View.VISIBLE);
            this.tvNoNeighbours.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("DEBUG", "NeighbourFragment onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("DEBUG", "NeighbourFragment onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.i("DEBUG", "NeighbourFragment onDestroy");
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        if (mNeighbours.contains(event.neighbour)){
            mApiService.deleteNeighbour(event.neighbour);
            adapter.notifyDataSetChanged();
            checkIfRecyclerViewIsEmpty();

            Log.i("DEBUG", "Neighbour deleted");
        }
    }
}
