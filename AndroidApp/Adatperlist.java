package com.example.rramesh.emotiveperformance;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Adatperlist extends ArrayAdapter<Student> {
    private Activity context;
    private List<Student> studentlist;

    public Adatperlist(Activity context , List<Student> studentList) {
        super(context,R.layout.singlerow,studentList);
        this.context=context;
        this.studentlist = studentList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.singlerow,null,true);

        TextView name=(TextView) listViewItem.findViewById(R.id.rowname);
        TextView roll=(TextView) listViewItem.findViewById(R.id.rowrollno);
        TextView section=(TextView) listViewItem.findViewById(R.id.rowsection);



        Student studentmodel=studentlist.get(position);

        name.setText(studentmodel.getName());

        roll.setText(studentmodel.getRollno());
        section.setText(studentmodel.getSection());




        return listViewItem;

    }
}
