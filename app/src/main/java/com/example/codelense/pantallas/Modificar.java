package com.example.codelense.pantallas;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.codelense.MainActivity;
import com.example.codelense.R;
import com.example.codelense.db.DBManager;
import com.example.codelense.db.DBUsuario;
import com.example.codelense.perfil;
import com.squareup.picasso.Picasso;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Modificar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Modificar extends Fragment {
    private ImageView imgPerfil;
    public EditText etNombreUsuario, etCorreoElectronico, etContrasena, etfotoPerfil;
    private Button btnguardar;
    private DBUsuario miUsuario;
    private perfil dbperfil;
    //Activity que nos permite la edicion de un usuario realizando un update a la bbdd


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Modificar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Modificar.
     */
    // TODO: Rename and change types and number of parameters
    public static Modificar newInstance(String param1, String param2) {
        Modificar fragment = new Modificar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View rootView = inflater.inflate(R.layout.fragment_modificar, container, false);
        // Obtener referencias a las vistas del layout
        imgPerfil = rootView.findViewById(R.id.imgPerfil);

        etNombreUsuario = rootView.findViewById(R.id.etNombreUsuario);
        etCorreoElectronico = rootView.findViewById(R.id.etCorreoElectronico);
        etContrasena = rootView.findViewById(R.id.etContrasena);
        btnguardar = rootView.findViewById(R.id.btnguardar);
        etfotoPerfil = rootView.findViewById(R.id.etfotoPerfil);

        //Recupero el Objeto Usuario del perfil

        dbperfil = new perfil();
        miUsuario = dbperfil.getUser();

        if(miUsuario.getNombre() != "") {
            etNombreUsuario.setHint(miUsuario.getNombre());
            etCorreoElectronico.setHint(miUsuario.getEmail());

            etfotoPerfil.setHint(miUsuario.getImage_perfil());
            if(miUsuario.getImage_perfil() !="") {
                Glide.with(this)
                        .load(miUsuario.getImage_perfil()) // Reemplaza con el ID de tu imagen en res/drawable
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(imgPerfil);
            }
        }
        // Onclick del btnCrear que llama a la clase AsyncTask para realizar el update en
        // la bbdd

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Async().execute();
            }
        });

        return rootView;
    }
    class Async extends AsyncTask<Void, Void, Void> {
        String error="";
        DBManager db;


        @Override

        protected Void doInBackground(Void... voids) {
            try {
                db = new DBManager();
                //Metodo de la Clase async que nos permite realizar el update a la bbdd mediante
                // la siguiente consulta con sus respectivos parametros
                String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ? ,foto_perfil = ? WHERE (id = ?)";
                try (PreparedStatement statement = db.conectar().prepareStatement(sql)) {
                    String hashedPassword = BCrypt.hashpw(etContrasena.getText().toString(), BCrypt.gensalt());
                    statement.setString(1, etNombreUsuario.getText().toString());
                    statement.setString(2, etCorreoElectronico.getText().toString());
                    statement.setString(3, hashedPassword);
                    statement.setString(4, etfotoPerfil.getText().toString());
                    statement.setString(5, ""+miUsuario.getId());
                    statement.executeUpdate();

                }
            }catch(Exception e)
            {
                error = e.toString();
            }
            return null;

        }
        @Override

        protected void onPostExecute(Void aVoid) {
             /*
        Metodo que nos indica una vez ejecutada la consulta si hay algun error o se ha insertado
        correctamente
        */
            if(error != "") {
                System.out.println(error);
                etNombreUsuario.setText(error);
            }else {
                Context context = getContext();
                Toast.makeText(context, "Registro guardado, cambios actualizados al iniciar sesión", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(aVoid);

        }


    }
}


