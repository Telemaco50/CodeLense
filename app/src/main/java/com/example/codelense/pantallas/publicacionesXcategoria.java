package com.example.codelense.pantallas;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.codelense.R;
import com.example.codelense.db.DBManager;
import com.example.codelense.db.DBPosts;
import com.example.codelense.db.DBUsuario;
import com.example.codelense.db.PublicacionesAdapter;
import com.example.codelense.perfil;
import com.example.codelense.ui.home.HomeFragment;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link publicacionesXcategoria#newInstance} factory method to
 * create an instance of this fragment.
 */
public class publicacionesXcategoria extends Fragment {

    private ListView listViewPublicaciones;
    private PublicacionesAdapter publicacionesAdapter;
    private static List<DBPosts> publicaciones;
    private DBUsuario miUsuario;
    private perfil dbperfil;
    private String aux;
    TextView titulo;

    //Fragmento que despliega una lista con todos los item de tipo post

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String categoria;
    private String sql;

    public publicacionesXcategoria() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment publicacionesXcategoria.
     */
    // TODO: Rename and change types and number of parameters
    public static publicacionesXcategoria newInstance(String param1, String param2) {
        publicacionesXcategoria fragment = new publicacionesXcategoria();
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



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publicaciones_xcategoria, container, false);
        listViewPublicaciones = (ListView) view.findViewById(R.id.publicaciones_list_view);
        titulo = (TextView) view.findViewById(R.id.tituloGrandre);
        // Cuando se seleccione un post
        listViewPublicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Crear el nuevo fragment
                PostDetalleFragment fragment = new PostDetalleFragment();
                publicacionesXcategoria pxc = new publicacionesXcategoria();

                // Agregar el ID del post seleccionado al Bundle de argumentos
                DBPosts post = publicaciones.get(position);// obtener el ID del post desde el adaptador o la lista
                Bundle args = new Bundle();
                args.putSerializable("post",post);

                System.out.println(post);
                fragment.setArguments(args);

                getParentFragmentManager().beginTransaction().replace(R.id.publicacionesXcategoria, fragment).addToBackStack(null).setReorderingAllowed(true).commit();

            }
        });

        // En funcion si el item post se pulsa desde mis publicaciones o desde una categoria
        // la consulta sql cambia fildrando o por el usuario_id o por la categoria_id , pudiendo reutilizar
        // este fragment en distintos puntos de la aplicacion

        Bundle args = getArguments();
        if (getArguments() != null) {
            categoria = args.getString("categoria");
            sql = "SELECT posts.contenido, posts.id, posts.titulo, posts.descripcion, posts.usuario_id, usuarios.nombre, categorias.nombre_categoria,IFNULL(count(comentarios.id), 0) AS numcoments FROM posts INNER JOIN usuarios ON (posts.usuario_id = usuarios.id)INNER JOIN categorias ON (posts.id_categoria = categorias.id_categoria)LEFT JOIN comentarios ON (posts.id = comentarios.post_id) WHERE posts.id_categoria = ? GROUP BY posts.id ORDER BY posts.id DESC ";
            aux = categoria;
            String nom_Categoria="";
            switch (categoria) {
                case "1":
                    nom_Categoria="HTML";
                    break;
                case "2":
                    nom_Categoria="CSS";
                    break;
                case "3":
                    nom_Categoria="C++";
                    break;
                case "4":
                    nom_Categoria="JAVA";
                    break;

            }
            titulo.setText(nom_Categoria);

        } else {
            dbperfil = new perfil();
            miUsuario = dbperfil.getUser();
            sql = "SELECT posts.contenido, posts.id, posts.titulo, posts.descripcion, posts.usuario_id, usuarios.nombre, categorias.nombre_categoria,IFNULL(count(comentarios.id), 0) AS numcoments FROM posts INNER JOIN usuarios ON (posts.usuario_id = usuarios.id)INNER JOIN categorias ON (posts.id_categoria = categorias.id_categoria)LEFT JOIN comentarios ON (posts.id = comentarios.post_id) WHERE usuarios.id = ? GROUP BY posts.id ORDER BY posts.id DESC";
            aux = ""+miUsuario.getId();
            titulo.setText("MIS PUBLICACIONES");
        }

        new Async().execute();
        return view;
    }




    class Async extends AsyncTask<Void, Void, Void> {
        String error = "";
        Boolean existe = false;
        DBManager db;


        @Override

        protected Void doInBackground(Void... voids) {
            publicaciones = new ArrayList<>();

            System.out.println("categoria"+categoria);

            try {
                db = new DBManager();
                // Crear la consulta SQL para obtener los item post
                PreparedStatement statement = db.conectar().prepareStatement(sql);
                statement.setString(1, aux);
                ResultSet rs = statement.executeQuery();
                DBPosts publicacion;
                while (rs.next()) {
                    publicacion = new DBPosts();
                    publicacion.setContenido(rs.getString("contenido"));
                    publicacion.setId(rs.getInt("id"));
                    publicacion.setTitulo(rs.getString("titulo"));
                    publicacion.setDescripcion(rs.getString("descripcion"));
                    publicacion.setCod_usuario(rs.getString("usuario_id"));
                    publicacion.setNombreUser(rs.getString("nombre"));
                    publicacion.setNombreCategor(rs.getString("nombre_categoria"));
                    publicacion.setNumComents(rs.getInt("numcoments"));
                    System.out.println(publicacion);
                    publicaciones.add(publicacion);
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(error);
                error = e.toString();
                System.out.println("Error entro catch");
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
                /*
        Método que nos indica una vez ejecutada la consulta si hay algun error o se ha insertado
        correctamente
        */
            if (error != "") {
                System.out.println(error);
            } else {
                System.out.println("todo correcto");

                publicacionesAdapter = new PublicacionesAdapter(getActivity(),R.layout.item_publicacion, publicaciones);
                listViewPublicaciones.setAdapter(publicacionesAdapter);

            }

            super.onPostExecute(aVoid);

        }
    }
}