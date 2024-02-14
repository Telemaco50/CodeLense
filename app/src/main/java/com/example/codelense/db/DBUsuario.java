package com.example.codelense.db;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.codelense.R;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBUsuario implements Serializable {
    // Clase Usuario que incorpora el constructor con sus respectivos setter y getter

        private int id;
        private String nombre;
        private String email;
        private String password;
        private String image_perfil;



        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getImage_perfil() {
            return image_perfil;
        }

        public void setImage_perfil(String image_perfil) {
            this.image_perfil = image_perfil;
        }


}


