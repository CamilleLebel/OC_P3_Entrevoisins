package com.openclassrooms.entrevoisins.service;

import android.content.Context;

import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.utils.EntrevoisinsApp;
import com.openclassrooms.entrevoisins.utils.SharedPreference;

import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();
    private SharedPreference sharedPreference;

    private Context context = EntrevoisinsApp.getContext();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    @Override
    public List<Neighbour> getFavoritesNeighbours() {

        sharedPreference = new SharedPreference();
        List<Neighbour> favorites;
        favorites = sharedPreference.getFavorites(context);

        return favorites;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }

    @Override
    public void toggleFavoriteState(Neighbour neighbour) {
        if (neighbour.isFavorite()){
            neighbour.setFavorite(false);
            sharedPreference.removeFavorite(context , neighbour);
        } else {
            neighbour.setFavorite(true);
            sharedPreference.addFavorite(context, neighbour);
        }
    }

    @Override
    public Neighbour getNeighbour(Integer id) {

        for (Neighbour neighbour : this.neighbours) {
            if (neighbour.getId().equals(id)) {
                return neighbour;
            }
        }
        Neighbour neighbour = new Neighbour();
        neighbour.setId(Integer.MAX_VALUE);
        return neighbour;
    }
}
