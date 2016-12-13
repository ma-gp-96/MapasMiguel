package com.izv.dam.newquip.vistas.notas;

import android.os.Bundle;
import com.izv.dam.newquip.basedatos.AyudanteMapa;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.pojo.NotaMapa;

import android.support.v7.app.AppCompatActivity;
import com.izv.dam.newquip.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import java.sql.SQLException;
import java.util.Date;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private EditText         meditTextTitulo;
    private EditText         meditTextNota;
    private Nota nota      = new Nota();
    private PresentadorNota  presentador;
    private Button           mbtnLocalizacion;
    private Context c      = this;
    private GoogleApiClient mapiClient;

    private AyudanteMapa a = new AyudanteMapa(c);
    RuntimeExceptionDao<NotaMapa,Integer> Dao = a.getSimpleRunTimeDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        presentador = new PresentadorNota(this);

        meditTextTitulo  = (EditText) findViewById(R.id.etxtTitulo);
        meditTextNota    = (EditText) findViewById(R.id.etxtNota);
        mbtnLocalizacion = (Button)   findViewById(R.id.btnLocalizacion);

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
        } else {
            Bundle b = getIntent().getExtras();
            if(b != null ) {
                nota = b.getParcelable("nota");
            }
        }
        mostrarNota(nota);

        if (mapiClient == null) {
            mapiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mbtnLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NotaMapa> loc;
                try{
                    QueryBuilder<NotaMapa,Integer> qb = Dao.queryBuilder();
                    qb.setWhere(qb.where().eq("id",nota.getId()));
                    loc = (ArrayList<NotaMapa>) Dao.query(qb.prepare());
                    Intent i = new Intent(c, VistaMapa.class);
                    Bundle b = new Bundle();
                    b.putParcelableArrayList("googleLocalizacion", loc);
                    i.putExtras(b);
                    startActivity(i);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        saveNota();
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);
    }

    @Override
    public void mostrarNota(Nota n) {
        meditTextTitulo.setText(nota.getTitulo());
        meditTextNota.setText(nota.getNota());
    }

    private void saveNota() {
        nota.setTitulo(meditTextTitulo.getText().toString());
        nota.setNota(meditTextNota.getText().toString());
        long r = presentador.onSaveNota(nota);
        if(r > 0 & nota.getId() == 0){
            nota.setId(r);
        }
    }
    protected void onStart() {
        mapiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mapiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("On connected");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "NO FINE LOCATION", Toast.LENGTH_SHORT);
            System.out.println("NO FINE LOCATION");
            return;
        }

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "NO COARSED LOCATION", Toast.LENGTH_SHORT);
            System.out.println("NO COARSED LOCATION");
            return;

        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mapiClient);
        if (mLastLocation != null) {
            Toast.makeText(this, mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude(), Toast.LENGTH_SHORT);
        }
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mapiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Conexión suspendida",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Fallo de conexión",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location localizacion) {
        saveNota();
        float latitude = (float) localizacion.getLatitude();
        float longitude = (float) localizacion.getLongitude();
        String date = new Date().toString();
        NotaMapa loc = new NotaMapa(nota.getId(),latitude,longitude,date);
        Dao.create(loc);
    }
}
