package com.example.codelense.pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;
import org.mindrot.jbcrypt.BCrypt;

import com.example.codelense.MainActivity;
import com.example.codelense.R;
import com.example.codelense.db.DBManager;

import java.sql.PreparedStatement;

public class Register extends AppCompatActivity {
    private VideoView mVideoView;
    private EditText editTextNombre;
    private EditText editTextEmail;
    private EditText editTextContrasena;
    private Button botonRegistrar;
    private DBManager db ;
    private int insert =0;

    /*
    * Activity de registro, con el que podemos insertar en la bbdd un nuevo usuario
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.bg_raw;
        Uri uri = Uri.parse(uriPath);

        mVideoView.setVideoURI(uri);
        mVideoView.start();

        editTextNombre = findViewById(R.id.txt_user);
        editTextEmail = findViewById(R.id.txt_user2);
        editTextContrasena = findViewById(R.id.password);
        botonRegistrar = findViewById(R.id.btn_login);
        //Boton que nos permite ejecutar la inserccion
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async().execute();
            }
        });
    }

    class Async extends AsyncTask<Void, Void, Void> {
        String error = "";
        DBManager db;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                db = new DBManager();
                String sql = "INSERT INTO usuarios (nombre, email, password) VALUES (?, ?, ?)";
                try (PreparedStatement statement = db.conectar().prepareStatement(sql)) {
                    String nombre = editTextNombre.getText().toString();
                    String email = editTextEmail.getText().toString();
                    String contrasena = editTextContrasena.getText().toString();

                    // Validar que el campo de correo sea un correo válido
                    if (!isValidEmail(email)) {
                        error = "El correo electrónico no es válido";
                        return null;
                    }

                    // Generar el hash de la contraseña utilizando BCrypt
                    String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());

                    statement.setString(1, nombre);
                    statement.setString(2, email);
                    statement.setString(3, hashedPassword);

                    int insert = statement.executeUpdate();
                    if (insert <= 0) {
                        error = "Error al insertar el usuario";
                    }
                }
            } catch (Exception e) {
                error = e.toString();
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
                editTextContrasena.setText(error);
                Toast.makeText(Register.this, "Error", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(Register.this, "Registrado correctamente. Inicie Sesión", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(aVoid);
        }

        // Metodo Implementa la lógica para validar que el campo de correo sea un correo válido
        private boolean isValidEmail(String email) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            return email.matches(emailRegex);
        }
    }


    // Al hacer onclick en (Inicio de Sesion) te lleva a la pantalla de inicio de Sesion
    public void onTextViewClick(View view) {
        Intent intent;
        intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
    }
}