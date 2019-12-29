package com.openclassrooms.entrevoisins.ui.neighbour_details;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.ToggleNeighbourFavoriteStateEvent;
import com.openclassrooms.entrevoisins.intents.IntentName;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.utils.SharedPreference;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NeighbourDetailsActivity extends AppCompatActivity {


    private NeighbourApiService apiService = DI.getNeighbourApiService();

    private Neighbour selectedNeighbour;

    //FOR UI

    @BindView(R.id.details_content)
    public CoordinatorLayout coordinatorLayout;

    @BindView(R.id.backgroundImageView)
    public ImageView header;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.ctoolbar)
    public CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.fabfav)
    public FloatingActionButton fabFav;

    @BindView(R.id.title_cardview)
    public TextView titleCardview1;

    @BindView(R.id.neighbour_place)
    public TextView neighbourPlace;

    @BindView(R.id.neighbour_phone_number)
    public TextView neighbourPhoneNumber;

    @BindView(R.id.neighbour_url_website)
    public TextView neighbourUrlWebsite;

    @BindView(R.id.neigbour_description)
    public TextView neighbourDesc;

    private Integer selectedId;
    private String selectedName;
    private String selectedAvatar;

    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
            Log.i("DEBUG", "backPressed");
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            selectedNeighbour = apiService.getNeighbour(extras.getInt(IntentName.INFORMATION_ACTIVITY_INTENT_NAME));
            initSelectedNeighbourDetails();
        }
    }

    @OnClick(R.id.fabfav)
    public void toggleNeighbourFavoriteState(){
        if (selectedNeighbour.isFavorite()){
            fabFav.hide();
            fabFav.setImageResource(R.drawable.ic_star_border_white_24dp);
            fabFav.show();
            EventBus.getDefault().post(new ToggleNeighbourFavoriteStateEvent(selectedNeighbour));
            selectedNeighbour.setFavorite(false);
        } else {
            fabFav.hide();
            fabFav.setImageResource(R.drawable.ic_star_white_24dp);
            fabFav.show();
            EventBus.getDefault().post(new ToggleNeighbourFavoriteStateEvent(selectedNeighbour));
            selectedNeighbour.setFavorite(true);
        }
    }

    private void initSelectedNeighbourDetails() {
        selectedId = selectedNeighbour.getId();
        selectedName = selectedNeighbour.getName();
        selectedAvatar = selectedNeighbour.getAvatarUrl();

        sharedPreference = new SharedPreference();

        if (selectedId != Integer.MAX_VALUE){
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(selectedAvatar)
                    .into(header);
            collapsingToolbarLayout.setTitle(selectedName);
            titleCardview1.setText(selectedName);
            neighbourPlace.setText(selectedName + "'s home");
            neighbourPhoneNumber.setText(selectedName + "'s phone number");
            neighbourUrlWebsite.setText(selectedName + "'s url website");
            neighbourDesc.setText(selectedName + "'s description");
        }
        if (apiService.getFavoritesNeighbours().contains(selectedNeighbour)){
            fabFav.setImageResource(R.drawable.ic_star_white_24dp);
            selectedNeighbour.setFavorite(true);
        } else {
            fabFav.setImageResource(R.drawable.ic_star_border_white_24dp);
            selectedNeighbour.setFavorite(false);
        }
    }
}
