package com.example.findmein;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findmein.Login.Login;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import com.example.findmein.adapter.CategoryAdapter;
import com.example.findmein.adapter.DiscountedProductAdapter;
import com.example.findmein.adapter.RecentlyViewedAdapter;
import com.example.findmein.model.Category;
import com.example.findmein.model.DiscountedProducts;
import com.example.findmein.model.RecentlyViewed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inicio extends AppCompatActivity implements View.OnClickListener {
    GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    RecyclerView discountRecyclerView, categoryRecyclerView, recentlyViewedRecycler;
    DiscountedProductAdapter discountedProductAdapter;
    List<DiscountedProducts> discountedProductsList;

    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    RecentlyViewedAdapter recentlyViewedAdapter;
    List<RecentlyViewed> recentlyViewedList = new ArrayList<>();

    TextView allCategory;
    DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if(extras != null){gso = (GoogleSignInOptions) extras.get("gso");}
        valida_ses();

        discountRecyclerView = findViewById(R.id.discountedRecycler);
        categoryRecyclerView = findViewById(R.id.categoryRecycler);
        allCategory = findViewById(R.id.allCategoryImage);
        recentlyViewedRecycler = findViewById(R.id.recently_item);

        allCategory.setOnClickListener(view -> {
            Intent i = new Intent(Inicio.this, AllCategory.class);
            startActivity(i);
        });

        // adding data to model
        discountedProductsList = new ArrayList<>();
        discountedProductsList.add(new DiscountedProducts(1, R.drawable.discountberry));
        discountedProductsList.add(new DiscountedProducts(2, R.drawable.discountbrocoli));
        discountedProductsList.add(new DiscountedProducts(3, R.drawable.discountmeat));
        discountedProductsList.add(new DiscountedProducts(4, R.drawable.discountberry));
        discountedProductsList.add(new DiscountedProducts(5, R.drawable.discountbrocoli));
        discountedProductsList.add(new DiscountedProducts(6, R.drawable.discountmeat));

        // adding data to model
        categoryList = new ArrayList<>();
        categoryList.add(new Category(1, R.drawable.cat_vehiculos,"Vehiculos"));
        categoryList.add(new Category(2, R.drawable.cat_vestimenta,"Vestimenta"));
        categoryList.add(new Category(3, R.drawable.cat_electronicos,"Electronicos"));
        categoryList.add(new Category(4, R.drawable.cat_ocio,"Ocio"));
        categoryList.add(new Category(5, R.drawable.cat_salud,"Salud"));
        categoryList.add(new Category(6, R.drawable.cat_joyeria,"Joyeria"));
        categoryList.add(new Category(7, R.drawable.cat_mascotas,"Mascotas"));
        categoryList.add(new Category(8, R.drawable.cat_identificaciones,"Identificaciones"));
        categoryList.add(new Category(6, R.drawable.cat_comunes,"Comunes"));
        categoryList.add(new Category(7, R.drawable.cat_otros,"Otros"));

        // adding data to model
        recentlyViewedList= new ArrayList<>();
        lista_publicaciones();

        setDiscountedRecycler(discountedProductsList);
        setCategoryRecycler(categoryList);
        setRecentlyViewedRecycler((recentlyViewedList));

        Button salir = findViewById(R.id.logout_button);
        salir.setOnClickListener(this::logout);

        ImageView add = findViewById(R.id.id_add);
        add.setOnClickListener(v -> addPublication());
    }
    private void goLoginScreen(){
        Intent login = new Intent(Inicio.this, Login.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(login);
    }
    private void logout(View view){
        if(AccessToken.getCurrentAccessToken() != null)
        {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            goLoginScreen();
        }
        if(GoogleSignIn.getLastSignedInAccount(getApplicationContext()) != null){
            FirebaseAuth.getInstance().signOut();
            googleSignInClient.signOut();
            goLoginScreen();
        }
    }
    private  void valida_ses(){
        if(AccessToken.getCurrentAccessToken() == null && GoogleSignIn.getLastSignedInAccount(getApplicationContext()) == null) {
            goLoginScreen();
        } else{
            if(GoogleSignIn.getLastSignedInAccount(getApplicationContext()) != null){
                googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);
                init_fireface();
            }
        }
    }
    private void init_fireface(){
        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    private void lista_publicaciones(){
        init_fireface();
        databaseReference.child("Publicaciones");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    recentlyViewedList.add(new RecentlyViewed(
                            Objects.requireNonNull(snapshot.child("titulo").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("descripcion").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("recompensa").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("categoria").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("fecha").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("latitud").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("longitud").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("imagen").getValue()).toString()));
                    setRecentlyViewedRecycler((recentlyViewedList));
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No se obtuvo informacion", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "cancelado error", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
    private void setDiscountedRecycler(List<DiscountedProducts> dataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        discountRecyclerView.setLayoutManager(layoutManager);
        discountedProductAdapter = new DiscountedProductAdapter(this,dataList);
        discountRecyclerView.setAdapter(discountedProductAdapter);
    }
    private void setCategoryRecycler(List<Category> categoryDataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(this,categoryDataList);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }
    private void setRecentlyViewedRecycler(List<RecentlyViewed> recentlyViewedDataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recentlyViewedRecycler.setLayoutManager(layoutManager);
        recentlyViewedAdapter = new RecentlyViewedAdapter(this,recentlyViewedDataList);
        recentlyViewedRecycler.setAdapter(recentlyViewedAdapter);
    }
    private void addPublication(){
        Intent p = new Intent(Inicio.this, AltaPublicacion.class);
        p.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(p);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout_button) {logout(v);}
    }
}