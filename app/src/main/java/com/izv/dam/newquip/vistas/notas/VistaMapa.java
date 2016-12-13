package com.izv.dam.newquip.vistas.notas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.NotaMapa;

import java.util.ArrayList;

public class VistaMapa extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private ArrayList<NotaMapa> localizaciones;
    private GoogleMap mMapa;
    private GoogleApiClient mApiClient;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        Bundle b = getIntent().getExtras();
        localizaciones = b.getParcelableArrayList("googleLocalizacion");

        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    protected void onStart() {
        mApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        for (NotaMapa localizacion : localizaciones){
            int i = 1;
            LatLng punto = new LatLng(localizacion.getLatitude(),localizacion.getLongitude());
            mMapa.addMarker(new MarkerOptions().position(punto).title(localizacion.getDate()));
            mMapa.moveCamera(CameraUpdateFactory.newLatLng(punto));
            i++;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapa = googleMap;
    }
}
