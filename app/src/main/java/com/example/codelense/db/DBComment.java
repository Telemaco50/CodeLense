package com.example.codelense.db;

public class DBComment {
    // Clase Comentario que incorpora el constructor con sus respectivos setter y getter

    private int id;
    private String username;
    private String commentText;
    private int likesCount;

    public DBComment( String username, String commentText) {
        this.username = username;
        this.commentText = commentText;

    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

}
