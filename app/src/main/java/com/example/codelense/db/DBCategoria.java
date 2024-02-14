package com.example.codelense.db;

public class DBCategoria {

    // Clase Categoria que incorpora el constructor con sus respectivos setter y getter

    private int cod_categoria;
    private String nom_categoria;
    private int numPosts;

    public DBCategoria(int cod_categoria, int numPosts) {
        this.cod_categoria = cod_categoria;
        this.numPosts = numPosts;
    }

    public int getCod_categoria() {
        return cod_categoria;
    }

    public void setCod_categoria(int cod_categoria) {
        this.cod_categoria = cod_categoria;
    }

    public String getNom_categoria() {
        return nom_categoria;
    }

    public void setNom_categoria(String nom_categoria) {
        this.nom_categoria = nom_categoria;
    }

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    @Override
    public String toString() {
        return "DBCategoria{" +
                "cod_categoria=" + cod_categoria +
                ", nom_categoria='" + nom_categoria + '\'' +
                ", numPosts=" + numPosts +
                '}';
    }
}
