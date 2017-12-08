package com.pentilku.distance;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button button;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.normal : mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);break;
            case R.id.hibrid : mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);break;
            case R.id.none : mMap.setMapType(GoogleMap.MAP_TYPE_NONE);break;
            case R.id.terain : mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);break;
            case R.id.satellite : mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button = (Button)findViewById(R.id.buttonku);
        button.setOnClickListener(op);
        Button btn = (Button)findViewById(R.id.cariku);
        btn.setOnClickListener(op);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ITS = new LatLng(-7.28,112.79);
        mMap.addMarker(new MarkerOptions().position(ITS).
                title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS,8));
    }

    public View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.buttonku:sembunyikan(view);
                    gotoLokasi();break;
                case R.id.cariku:gocari();break;
            }
        }
    };

    private void gocari() {
        EditText tempat = (EditText)findViewById(R.id.caritext);
        Geocoder geocoder = new Geocoder(getBaseContext());
        try {
            List<Address>daftar = geocoder.getFromLocationName(tempat.getText().toString(),1);
            Address address = daftar.get(0);
            String namaAlamat = address.getAddressLine(0);
            Double lintang = address.getLatitude();
            Double bujur = address.getLongitude();
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            EditText zoom = (EditText) findViewById(R.id.besar);
            Float dbzoom = Float.parseFloat(zoom.getText().toString());
            Toast.makeText(this, "pindah ", Toast.LENGTH_SHORT).show();
            gotopeta(lintang,bujur,dbzoom);

            EditText lat = (EditText)findViewById(R.id.lat);
            EditText lng = (EditText)findViewById(R.id.lng);

            lat.setText(lintang.toString());
            lng.setText(bujur.toString());

            Double tujulat = Double.parseDouble(lat.getText().toString());
            Double tujulng = Double.parseDouble(lng.getText().toString());

            Double asallat = -7.28;
            Double asallng = 112.79;

            hitungjarak(tujulat,tujulng,asallat,asallng);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hitungjarak(Double lat, Double lng, Double asallat, Double asallng) {
        Location asal = new Location("asal");
        Location tujuan = new Location("tujuan");
        asal.setLongitude(asallng);
        asal.setLatitude(asallat);
        tujuan.setLatitude(lat);
        tujuan.setLongitude(lng);
        float jarak = (Float) asal.distanceTo(tujuan)/1000;
        String jaraknya = String.valueOf(jarak);
        Toast.makeText(this, jaraknya, Toast.LENGTH_SHORT).show();
    }

    private void sembunyikan(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public void gotoLokasi()
    {

        EditText lat = (EditText)findViewById(R.id.lat);
        EditText lg = (EditText)findViewById(R.id.lng);
        EditText zoom = (EditText)findViewById(R.id.besar);

        Double dblat = Double.parseDouble(lat.getText().toString());
        Double dblong = Double.parseDouble(lg.getText().toString());
        Float dbzoom = Float.parseFloat(zoom.getText().toString());

        Toast.makeText(this, "Move to lat"+dblat+ "Long" + dblong, Toast.LENGTH_SHORT).show();
        gotopeta(dblat,dblong,dbzoom);

    }

    private void gotopeta(Double dblat, Double dblong, Float dbzoom) {
        LatLng Lokasibaru = new LatLng(dblat,dblong);
        mMap.addMarker(new MarkerOptions().position(Lokasibaru).title("Marker in" + dblat+dblong));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Lokasibaru,dbzoom));
    }
}
