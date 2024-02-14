package com.example.codelense.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
//Clase que nos permite parametrizar la conexion a la bbdd mediante JDBC
    private Connection connection;
    private static final String DATABASE_URL = "jdbc:mysql://192.168.1.39:3333/codelens";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "admin";
    private List<DBCategoria> listcategorias = new ArrayList<>();


    public DBManager() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
    }

   public Connection conectar (){
        return connection;
   }

   public void desconectar() throws SQLException {connection.close();};






}
