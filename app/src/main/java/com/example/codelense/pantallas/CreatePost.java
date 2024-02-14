package com.example.codelense.pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codelense.R;
import com.example.codelense.db.DBManager;
import com.example.codelense.perfil;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class CreatePost extends AppCompatActivity {
TextView titulo, desc, codigo;
Button btnCrear;
public static int cod_user =0;
int cod_categoria =0;
private int insert =0;
private Spinner categoriaSpinner;

//Activity que nos permite la inserccion de un nuevo post en la bbdd


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        cod_user = getIntent().getIntExtra("cod_user",0);

        categoriaSpinner = findViewById(R.id.categoria_spinner);
        titulo = (TextView) findViewById(R.id.titulo_publicacion_edit_text);
        desc = (TextView) findViewById(R.id.desc_texvitw);
        codigo = (TextView) findViewById(R.id.codigo_edit_text);
        btnCrear = (Button) findViewById(R.id.crear_publicacion_button);

        // Onclick del btnCrear que llama a la clase AsyncTask para realizar el insert en
        // la bbdd
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async().execute();

            }
        });

        // Crear una lista de opciones para el Spinner
        List<String> opciones = new ArrayList<>();
        opciones.add("HTML");
        opciones.add("CSS");
        opciones.add("C++");
        opciones.add("JAVA");


        // Crear un adaptador para el Spinner
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opciones
        );
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Establecer el adaptador para el Spinner
        categoriaSpinner.setAdapter(adaptador);
}

class Async extends AsyncTask<Void, Void, Void> {
    String error = "";


    @Override

    protected Void doInBackground(Void... voids) {
//
        DBManager db;

        try {
            db = new DBManager();
            switch (categoriaSpinner.getSelectedItem().toString()){
                case "HTML":
                    cod_categoria =1;
                    break;
                case "CSS":
                    cod_categoria =2;
                    break;
                case "C++":
                    cod_categoria =3;
                    break;
                case "JAVA":
                    cod_categoria =4;
                    break;

            }
            //Metodo de la Clase async que nos permite realizar la inserccion a la bbdd mediante
            // la siguiente consulta con sus respectivos parametros
            String sql = "INSERT INTO posts (usuario_id,id_categoria,titulo,descripcion,contenido) VALUES (?,?,?,?,?)";
            try (PreparedStatement statement = db.conectar().prepareStatement(sql)) {
                statement.setString(1, "" + cod_user);
                statement.setString(2, ""+cod_categoria);
                statement.setString(3, titulo.getText().toString());
                statement.setString(4, desc.getText().toString());
                statement.setString(5, codigo.getText().toString());
                insert = statement.executeUpdate();

            }
        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(CreatePost.this, "Error", Toast.LENGTH_LONG).show();


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
            Toast.makeText(CreatePost.this, "Error", Toast.LENGTH_LONG).show();
        }else if(insert > 0){
            Toast.makeText(CreatePost.this, "Publicado", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CreatePost.this, perfil.class);
            //intent.putExtra("cod_user", cod_user);
            startActivity(intent);
        }else {
            Toast.makeText(CreatePost.this, "No se ha podido publicar", Toast.LENGTH_LONG).show();

        }
        super.onPostExecute(aVoid);

    }

}


}