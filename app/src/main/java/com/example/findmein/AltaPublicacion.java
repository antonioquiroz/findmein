package com.example.findmein;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class AltaPublicacion extends AppCompatActivity implements OnMapReadyCallback {

    String latitud,longitud;
    ImageView imageView;
    TextView titulo,descripcion,recompensa,ubicacion;
    Button add;
    Spinner spinner;
    String tit,des,rec,ubi,cat = "";
    int bandera = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_publicacion);
        //Ubicacion ejemplo
        latitud = "20.68646004155626";
        longitud ="-103.35073956581985";

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.id_map_add);
        mapFragment.getMapAsync(this);

        titulo = findViewById(R.id.id_txt_tit);
        descripcion = findViewById(R.id.id_txt_descrip);
        recompensa = findViewById(R.id.id_txt_recom);
        ubicacion = findViewById(R.id.id_txt_ubicacion);
        imageView = findViewById(R.id.iv_add);
        spinner = findViewById(R.id.spinner);

        String[] valores = {"Vehiculos","Vestimenta","Electronicos","Ocio","Salud","Joyeria", "Mascotas", "Identificaciones","Comunes","Otros"};

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id)
            {
                switch(i)
                {
                    case 0:
                        cat = "Vehiculos";
                        break;
                    case 1:
                        cat = "Vestimenta";
                        break;
                    case 2:
                        cat = "Electronicos";
                        break;
                    case 3:
                        cat = "Ocio";
                        break;
                    case 4:
                        cat = "Salud";
                        break;
                    case 5:
                        cat = "Joyeria";
                        break;
                    case 6:
                        cat = "Mascotas";
                        break;
                    case 7:
                        cat = "Identificaciones";
                        break;
                    case 8:
                        cat = "Comunes";
                        break;
                    case 9:
                        cat = "Otros";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        imagen();
        agregar();
    }
    public void imagen(){
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/");
            startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),10);
        });
    }
    public void agregar(){
        add = findViewById(R.id.bt_add);
        add.setOnClickListener(v -> {
            validaTV(tit,titulo);
            validaTV(des,descripcion);
            validaTV(rec,recompensa);
            validaTV(ubi,ubicacion);
            if(bandera >= 5){
                Toast.makeText(getApplicationContext(), "Datos completos", Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(getApplicationContext(), "Datos incompletos", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void validaTV (String s,TextView textView){
        textView.getText().toString();
        if (!Objects.equals(textView.getText().toString(),""))
        {
          s = textView.getText().toString();
          bandera += 1;
        } else{
            textView.setError("El campo es requerido");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK){
            Uri path = data.getData();
            imageView.setImageURI(path);
            bandera += 1;
        }
    }
    public void onMapReady(GoogleMap map){
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