package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.events.ToggleNeighbourFavoriteStateEvent;
import com.openclassrooms.entrevoisins.intents.IntentName;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_details.NeighbourDetailsActivity;
import com.openclassrooms.entrevoisins.utils.ItemClickSupport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoriteNeighbourFragment extends Fragment {

    private NeighbourApiService mApiService;
    private List<Neighbour> mFavoritesNeighbours;

    @BindView(R.id.tv_no_favorites)
    public TextView tvNoFavorites;

    @BindView(R.id.list_favorite_neighbours)
    public RecyclerView mRecyclerView;

    private MyNeighbourRecyclerViewAdapter adapter;

    /**
     * Create and return a new instance
     * @return @{@link FavoriteNeighbourFragment}
     */
    public static FavoriteNeighbourFragment newInstance() {

        Log.i("DEBUG", "FavoriteNeighbourFragment NewInstance");

        FavoriteNeighbourFragment fragment = new FavoriteNeighbourFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
        EventBus.getDefault().register(this);

        Log.i("DEBUG", "FavoriteNeighbourFragment onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_neighbour_list, container, false);
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
        mFavoritesNeighbours = mApiService.getFavoritesNeighbours();
        adapter = new MyNeighbourRecyclerViewAdapter(mFavoritesNeighbours);
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

    private void checkIfRecyclerViewIsEmpty() {
        if (this.adapter.getItemCount() == 0) {
            this.mRecyclerView.setVisibility(View.INVISIBLE);
            this.tvNoFavorites.setVisibility(View.VISIBLE);
        } else {
            this.mRecyclerView.setVisibility(View.VISIBLE);
            this.tvNoFavorites.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("DEBUG", "FavoriteNeighbourFragment onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("DEBUG", "FavoriteNeighbourFragment onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.i("DEBUG", "FavoriteNeighbourFragment onDestroy");
    }

    /**
     * Fired if the user clicks on fav button
     * @param event
     */
    @Subscribe
    public void onToggleFavoriteState(ToggleNeighbourFavoriteStateEvent event) {
        if (mFavoritesNeighbours.contains(event.neighbour)){
            mApiService.toggleFavoriteState(event.neighbour);
            Log.i("DEBUG", "toggle fav neighbour state into false");
            mFavoritesNeighbours.remove(event.neighbour);
            adapter.notifyDataSetChanged();
            checkIfRecyclerViewIsEmpty();
        } else {
            mApiService.toggleFavoriteState(event.neighbour);
            Log.i("DEBUG", "toggle fav neighbour state into true");
            mFavoritesNeighbours.add(event.neighbour);
            adapter.notifyDataSetChanged();
            checkIfRecyclerViewIsEmpty();
        }
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {

        if (mFavoritesNeighbours.contains(event.neighbour)){
            if (event.neighbour.isFavorite()){
                mApiService.toggleFavoriteState(event.neighbour);
            }
            mApiService.deleteNeighbour(event.neighbour);
            mFavoritesNeighbours.remove(event.neighbour);
            adapter.notifyDataSetChanged();
            checkIfRecyclerViewIsEmpty();

            Log.i("DEBUG", "Neighbour deleted from FavoriteNeighbourFragment");
        }
    }
}
