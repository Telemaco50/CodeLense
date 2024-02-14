package com.example.codelense.db;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.codelense.R;

import org.w3c.dom.Text;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

public class PublicacionesAdapter extends ArrayAdapter<DBPosts> {
    private Context context;
    private List<DBPosts> publicaciones;
    int resource;
    DBPosts publicacion;
    TextView numComents;


    //Clase Adaptador que permite generar items de tipo DBPosts

    public PublicacionesAdapter(Context context,int resource, List<DBPosts> publicaciones) {
        super(context, 0, publicaciones);
        this.context = context;
        this.resource = resource;
        this.publicaciones = publicaciones;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);

        publicacion = publicaciones.get(position);

        TextView tituloTextView = (TextView) convertView.findViewById(R.id.tituloTextView);
        TextView contenidoTextView = (TextView) convertView.findViewById(R.id.numPosthtml);
        TextView nomUsuarioTextViewTextView = (TextView) convertView.findViewById(R.id.nomUsuarioTextView);
        TextView nomCategoTextView = (TextView) convertView.findViewById(R.id.nomCategoTextView);
        numComents = (TextView) convertView.findViewById(R.id.numPostcss);

        tituloTextView.setText(publicacion.getTitulo());
        contenidoTextView.setText(publicacion.getDescripcion());
        nomUsuarioTextViewTextView.setText(publicacion.getNombreUser());
        nomCategoTextView.setText(publicacion.getNombreCategor());
        numComents.setText(publicacion.getNumComents()+ " Comentarios");
        return convertView;
    }
}
