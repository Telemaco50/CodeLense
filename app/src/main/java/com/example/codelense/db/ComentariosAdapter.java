package com.example.codelense.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codelense.R;

import java.util.List;

public class ComentariosAdapter  extends ArrayAdapter<DBComment> {
    private Context mContext;
    private int mResource;

    //Clase Adaptador que permite generar items de tipo comentario

    public ComentariosAdapter(Context context, int resource, List<DBComment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        DBComment comment = getItem(position);

        TextView usernameTextView = convertView.findViewById(R.id.comment_username);
        TextView commentTextView = convertView.findViewById(R.id.comment_content);

        usernameTextView.setText(comment.getUsername());
        commentTextView.setText(comment.getCommentText());

        return convertView;
    }
}
