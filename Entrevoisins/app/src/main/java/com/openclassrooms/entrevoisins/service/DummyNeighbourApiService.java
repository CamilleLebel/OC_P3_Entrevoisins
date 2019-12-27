package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    @Override
    public List<Neighbour> getFavoritesNeighbours() {
        List<Neighbour> favorites = new ArrayList<>();
        for (Neighbour neighbour : neighbours){
            if (neighbour.isFavorite()){
                favorites.add(neighbour);
            }
        }
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
        neighbours.get(neighbours.indexOf(neighbour)).setFavorite(!neighbour.isFavorite());
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
