package com.example.codelense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import org.mindrot.jbcrypt.BCrypt;

import com.example.codelense.db.DBManager;
import com.example.codelense.db.DBUsuario;
import com.example.codelense.pantallas.Register;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {

    TextView email, password,errorText;
    Button show;
    private VideoView mVideoView;
    public static int cod =0;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        String  uriPath="android.resource://" + getPackageName() + "/" + R.raw.bg_raw;
        Uri uri = Uri.parse(uriPath);
        mVideoView.setVideoURI(uri);
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // El video actual ha finalizado, reiniciar la reproducción desde el principio
                mVideoView.start();
            }
        });
        mVideoView.start();

        email = (TextView) findViewById(R.id.txt_user);
        password = (TextView) findViewById(R.id.password);
        errorText = (TextView) findViewById(R.id.numPosthtml) ;
        show = (Button) findViewById(R.id.btn_login);
        show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Async().execute();

            }

        });

    }


    class Async extends AsyncTask<Void, Void, Void> {
        String error = "";
        Boolean existe = false;
        DBManager db;
        DBUsuario dbuser = new DBUsuario();

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                db = new DBManager();
                String sql = "SELECT * FROM usuarios WHERE email = ?";
                try (PreparedStatement statement = db.conectar().prepareStatement(sql)) {
                    statement.setString(1, email.getText().toString());
                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        String inputPassword = password.getText().toString();

                        if (BCrypt.checkpw(inputPassword, hashedPassword)) {
                            dbuser.setId(rs.getInt("id"));
                            dbuser.setNombre(rs.getString("nombre"));
                            dbuser.setEmail(rs.getString("email"));
                            dbuser.setPassword(rs.getString("password"));
                            dbuser.setImage_perfil(rs.getString("foto_perfil"));
                            existe = true;
                        }
                    }
                }
            } catch (Exception e) {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (error != "") {
                errorText.setText(error);
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
            } else if (existe) {
                Intent intent = new Intent(MainActivity.this, perfil.class);
                intent.putExtra("usuario", dbuser);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "El usuario no existe. Pruebe a registrarse", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(aVoid);
        }

    }

    // Al hacer onclick en el ¿No tienes cuenta?
    public void onTextViewClick(View view) {
        Intent intent;
        intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

}