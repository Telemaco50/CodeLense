package com.example.codelense;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.codelense.db.DBManager;
import com.example.codelense.db.DBUsuario;
import com.example.codelense.pantallas.CreatePost;
import com.example.codelense.pantallas.Modificar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import com.example.codelense.databinding.ActivityPerfilBinding;
import com.squareup.picasso.Picasso;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class perfil extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPerfilBinding binding;
    private TextView name;
    private ImageView profileImageView;
    public int cod_user = 0;
    public static DBUsuario miUsuario = new DBUsuario();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // RECUPERO EL CODIGO DE USUARIO DESDE EL LOGIN
        miUsuario = (DBUsuario) getIntent().getSerializableExtra("usuario");
        cod_user = miUsuario.getId();

        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarPerfil.toolbar);
        binding.appBarPerfil.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(perfil.this, CreatePost.class);
                intent.putExtra("cod_user", cod_user);
                startActivity(intent);

            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.publicacionesXcategoria, R.id.nav_perfil, R.id.nav_mispublicaciones)
                .setOpenableLayout(drawer)
                .build();
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.nombreNavigation);
        name.setText("Bienvenido " + miUsuario.getNombre());
        profileImageView = headerView.findViewById(R.id.imagenNavigation);

        if(miUsuario.getImage_perfil()!="") {
            Glide.with(this)
                    .load(miUsuario.getImage_perfil()) // Reemplaza con el ID de tu imagen en res/drawable
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(profileImageView);
        }else {

            Glide.with(this)
                    .load(miUsuario.getImage_perfil()) // Reemplaza con el ID de tu imagen en res/drawable
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(profileImageView);

        }


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_perfil);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_perfil);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public DBUsuario getUser(){
        return miUsuario;
    }
}
