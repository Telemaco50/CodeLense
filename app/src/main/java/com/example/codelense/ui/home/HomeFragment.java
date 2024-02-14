package com.example.codelense.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.codelense.R;
import com.example.codelense.databinding.FragmentHomeBinding;
import com.example.codelense.db.DBCategoria;
import com.example.codelense.db.DBManager;
import com.example.codelense.pantallas.publicacionesXcategoria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button btncss,btnhtml,btncmas,btnjava;
    TextView numhtml,numcss,numjava,numcmas;
    private List<DBCategoria> listcategorias = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        numhtml = (TextView) root.findViewById(R.id.numPosthtml);
        numcss = (TextView) root.findViewById(R.id.numPostcss);
        numjava = (TextView) root.findViewById(R.id.numPostjava);
        numcmas = (TextView) root.findViewById(R.id.numPostcmas);
        new Async().execute();

        btncss = (Button) root.findViewById(R.id.css);
        btnhtml = (Button) root.findViewById(R.id.html);
        btncmas = (Button) root.findViewById(R.id.cmas);
        btnjava = (Button) root.findViewById(R.id.java);

        btncss.setOnClickListener(this::onClick);
        btnhtml.setOnClickListener(this::onClick);
        btncmas.setOnClickListener(this::onClick);
        btnjava.setOnClickListener(this::onClick);

        return root;
    }


    public void onClick(View view) {
        publicacionesXcategoria publicacionesFragment = new publicacionesXcategoria();
        Bundle args = new Bundle();
        switch (view.getId()) {
            case R.id.html:
                args.putString("categoria", "1");
                break;
            case R.id.css:
                args.putString("categoria", "2");
                break;
            case R.id.cmas:
                args.putString("categoria", "3");
                break;
            case R.id.java:
                args.putString("categoria", "4");
                break;
        }

        publicacionesFragment.setArguments(args);
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_perfil, publicacionesFragment).addToBackStack(null).commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class Async extends AsyncTask<Void, Void, Void> {


        @Override

        protected Void doInBackground(Void... voids) {
            DBCategoria categoria;
            DBManager db = null;

            try {
                db = new DBManager();
                Statement stmt = db.conectar().createStatement();
                String sql = "SELECT id_categoria, COUNT(*) as num_posts\n" +
                        "FROM posts\n" +
                        "GROUP BY id_categoria;\n";
                ResultSet rs = stmt.executeQuery(sql);

                // 3. Procesar los resultados de la consulta
                while (rs.next()) {
                    int cod_cat = rs.getInt("id_categoria");
                    int numPost = rs.getInt("num_posts");

                    categoria = new DBCategoria(cod_cat, numPost);
                    listcategorias.add(categoria);
                    System.out.println(cod_cat + " " + numPost);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override

        protected void onPostExecute(Void aVoid) {
            for (DBCategoria cate : listcategorias) {
                switch (cate.getCod_categoria()) {
                    case 1:
                        numhtml.setText(cate.getNumPosts() + " Posts");
                        break;
                    case 2:
                        numcss.setText(cate.getNumPosts() + " Posts");
                        break;
                    case 3:
                        numcmas.setText(cate.getNumPosts() + " Posts");
                        break;
                    case 4:
                        numjava.setText(cate.getNumPosts() + " Posts");
                        break;

                }
            }
            super.onPostExecute(aVoid);

        }

    }}




