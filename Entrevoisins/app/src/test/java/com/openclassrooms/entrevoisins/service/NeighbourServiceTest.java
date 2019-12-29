package com.openclassrooms.entrevoisins.service;

import android.app.Activity;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.SharedPreference;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;
    private Activity activity;
    private SharedPreference sharedPreference;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
        sharedPreference = mock(SharedPreference.class);
        activity = mock(ListNeighbourActivity.class);

    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void getANeighbourWithSuccess() {
        List<Neighbour> expectedNeighbours = service.getNeighbours();
        Neighbour neighbour = expectedNeighbours.get(1);
        Neighbour neighbourToTest = service.getNeighbour(neighbour.getId());
        assertEquals(neighbour, neighbourToTest);
    }

    @Test
    public void addToFavoritesWithSuccess() {
        ArrayList<Neighbour> favorites = sharedPreference.getFavorites(activity);
        Neighbour neighbourToFav = service.getNeighbour(1);
        favorites.add(neighbourToFav);
        when(sharedPreference.getFavorites(activity)).thenReturn(favorites);
        assertTrue(favorites.contains(neighbourToFav));
    }
}
