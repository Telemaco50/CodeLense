package com.example.codelense.pantallas;

import static com.example.codelense.perfil.miUsuario;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codelense.MainActivity;
import com.example.codelense.R;
import com.example.codelense.db.ComentariosAdapter;
import com.example.codelense.db.DBComment;
import com.example.codelense.db.DBManager;
import com.example.codelense.db.DBPosts;
import com.example.codelense.db.DBUsuario;
import com.example.codelense.db.PublicacionesAdapter;
import com.example.codelense.perfil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDetalleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetalleFragment extends Fragment {

    private ImageView likeImageView;
    private ListView commentListView;
    private Button btnComentar;
    private TextView newComent, titleTextView;

    private List<DBComment> commentList;
    private ComentariosAdapter commentAdapter;
    private static int postId;
    protected static boolean textcoment = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostDetalleFragment() {
        // Required empty public constructor
    }
    //Fragmento que se despliega tras pulsar el item de un post
    //Indicandonos con mas detalle ese post y mostrando una lista de sus comentarios

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostDetalleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostDetalleFragment newInstance(String param1, String param2) {
        PostDetalleFragment fragment = new PostDetalleFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_detalle, container, false);

        // Obtener el Bundle enviado desde el Fragment anterior
        Bundle bundle = getArguments();

        // Obtener el objeto serializable enviado
        DBPosts post = (DBPosts) bundle.getSerializable("post");

        // Utilizar el objeto
        TextView titleTextView = rootView.findViewById(R.id.post_title);
        TextView contentTextView = rootView.findViewById(R.id.post_content);

        TextView descriptionTextView = rootView.findViewById(R.id.post_description);
        newComent = rootView.findViewById(R.id.new_comment_edittext);
        btnComentar = rootView.findViewById(R.id.new_comment_button);
        Prettify highlighter = new Prettify();
        String highlighted = highlighter.highlight("java", post.getContenido());

        titleTextView.setText(post.getTitulo());
        contentTextView.setText(Html.fromHtml(highlighted));
        descriptionTextView.setText(post.getDescripcion());


        commentListView = rootView.findViewById(R.id.comment_list);
        postId= post.getId();

        // btn que nos permite insertar un comentario mediante la clase async

        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    textcoment = true;
                    new Async().execute();
            }
        });
        new Async().execute();
        return rootView;
    }


    class Async extends AsyncTask<Void, Void, Void> {
        String error = "";
        List<DBComment> commentList = new ArrayList<>();
        String query="";
        @Override

        protected Void doInBackground(Void... voids) {
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {
                DBManager db = new DBManager();
                // Crear la consulta SQL para obtener los comentarios del post o insertarlos

                if(textcoment == false){
                    query = "SELECT * FROM comentarios INNER JOIN usuarios ON(comentarios.usuario_id = usuarios.id) WHERE comentarios.post_id = ?";
                    statement = db.conectar().prepareStatement(query);
                    statement.setInt(1, postId);
                    resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        int id = resultSet.getInt("comentarios.id");
                        String username = resultSet.getString("usuarios.nombre");
                        String commentText = resultSet.getString("comentarios.contenido");

                        DBComment comment = new DBComment(username, commentText);
                        commentList.add(comment);
                    }

                }else {
                    query = "INSERT INTO comentarios ( post_id,usuario_id,contenido) VALUES (?, ? ,?)";
                    statement = db.conectar().prepareStatement(query);
                    statement.setInt(1, postId);
                    statement.setInt(2, miUsuario.getId());
                    statement.setString(3, newComent.getText().toString());
                    statement.executeUpdate();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                error = e.toString();
                System.out.println("mi error"+error);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("mi error2"+error);
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            /*
        Metodo que nos indica una vez ejecutada la consulta si hay algun error o se ha insertado
        correctamente
        */

            if(textcoment == false) {
                if (error != "") {
                    System.out.println(error);
                } else {
                    System.out.println("todo correcto");

                    commentAdapter = new ComentariosAdapter(getActivity(), R.layout.item_comentario, commentList);
                    commentListView.setAdapter(commentAdapter);
                }
            }else {
                textcoment = false;
            }
             super.onPostExecute(aVoid);

        }
    }
}