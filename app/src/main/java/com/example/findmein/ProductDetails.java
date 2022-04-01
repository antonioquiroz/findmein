package com.example.findmein;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity implements OnMapReadyCallback {

    ImageView img, back;
    TextView ttitulo,trecompensa,tdescripcion,tcategoria,tfecha;

    String titulo,recompensa,descripcion,categoria,image,fecha,latitud,longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();

         titulo = i.getStringExtra("titulo");
         recompensa = i.getStringExtra("recompensa");
         descripcion = i.getStringExtra("descripcion");
         image = i.getStringExtra("image");
         categoria = i.getStringExtra("categoria");
         fecha = i.getStringExtra("fecha");
         latitud = i.getStringExtra("latitud");
         longitud = i.getStringExtra("longitud");

         ttitulo = findViewById(R.id.ttitulo);
         trecompensa = findViewById(R.id.trecompensa);
         tdescripcion = findViewById(R.id.tdescripcion);
         img = findViewById(R.id.big_image);
         back = findViewById(R.id.back2);
         tcategoria= findViewById(R.id.tcategoria);
         tfecha = findViewById(R.id.tv_fecha);

         ttitulo.setText(titulo);
         trecompensa.setText(recompensa);
         tdescripcion.setText(descripcion);
         tcategoria.setText(categoria);
         tfecha.setText(fecha);
         Picasso.get().load(image).into(img);

        back.setOnClickListener(view -> {
            Intent i1 = new Intent(ProductDetails.this, Inicio.class);
            startActivity(i1);
            finish();
        });
    }
    public void onMapReady(GoogleMap map)
    {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        //map.clear();
        //Se crea el marcador de la ubicacion del dispositivo
        LatLng dispositivo = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        map.addMarker(new MarkerOptions().position(dispositivo).title("Aqui se extravió el objeto"));
        map.animateCamera(CameraUpdateFactory.newLatLng(dispositivo));
        //Se crea la animación
        CameraPosition position = new CameraPosition.Builder()
                .target(dispositivo) // Sets the new camera position
                .zoom(18) // Sets the zoom
                .bearing(0) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder
        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}
