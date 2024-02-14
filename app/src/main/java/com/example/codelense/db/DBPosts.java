package com.example.codelense.db;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.codelense.R;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBPosts implements Serializable {

    // Clase Posts que incorpora el constructor con sus respectivos setter y getter

    private int id;
    private String titulo;
    private String descripcion;
    private int cod_categoria;
    private String contenido;
    private String cod_usuario;
    private String nombreUser;
    private String nombreCategor;
    private int numComents;

    public DBPosts() {
    }

    public DBPosts( String titulo, String descripcion, String contenido) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.contenido = contenido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCod_categoria() {
        return cod_categoria;
    }

    public void setCod_categoria(int cod_categoria) {
        this.cod_categoria = cod_categoria;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(String cod_usuario) {
        this.cod_usuario = cod_usuario;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getNombreCategor() {return nombreCategor;}

    public void setNombreCategor(String nombreCategor) {this.nombreCategor = nombreCategor;}

    public int getNumComents() {
        return numComents;
    }

    public void setNumComents(int numComents) {
        this.numComents = numComents;
    }

    @Override
    public String toString() {
        return "DBPosts{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cod_categoria=" + cod_categoria +
                ", contenido='" + contenido + '\'' +
                ", cod_usuario='" + cod_usuario + '\'' +
                ", nombreUser='" + nombreUser + '\'' +
                ", nombreCategor='" + nombreCategor + '\'' +
                ", numComents=" + numComents +
                '}';
    }
}







