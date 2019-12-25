package com.example.rramesh.emotiveperformance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
      FloatingActionButton fabadd,fablog;
      FirebaseAuth mAuth;
    private ProgressDialog progress;
    DatabaseReference studentdatabase;
    DatabaseReference myRef;
    FirebaseUser mCurrentUser;
    private ListView listView;
    String TAG=this.getClass().getSimpleName();
    List<Student> students=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        String current_uid=mCurrentUser.getUid();
        progress=new ProgressDialog(this);
        progress.setMessage("Fecthing Data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progress.setIndeterminate(true);
        progress.setProgress(0);



        listView = (ListView) findViewById(R.id.listview);
        studentdatabase = FirebaseDatabase.getInstance().getReference("student").child(current_uid);

        progress.show();
        studentdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                students=new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, child.getKey());
                    Log.i(TAG, child.getKey()+" ---> "+child.child("name").getValue(String.class));
                    students.add(new Student(child.child("name").getValue(String.class), child.child("roll").getValue(String.class), child.child("section").getValue(String.class),  child.child("phone").getValue(String.class)));
                }


                int jumpTime = 0;
                jumpTime += 5;

                final Adatperlist adapter = new Adatperlist(MainActivity.this, students);



                listView.setAdapter(adapter);



                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                        Intent in=new Intent(MainActivity.this,Piechart.class);
                      in.putExtra("student",students.get(position));
                       startActivity(in);
                    }
                });
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fabadd=findViewById(R.id.fab_add_button);
        fablog=findViewById(R.id.fab_log_button);
        fablog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
int i;
                if(v.getId() == R.id.fab_log_button){
                    try {
                        mAuth.signOut();
                        i=1;
                    } catch (Exception e) {
                        i=0;
                        e.printStackTrace();
                    }
                    if(i==1){
                        Intent in=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(in);
                        finish();
                    }

                }


         /*       mAuth.signOut();
                Intent in=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(in);
                finish();*/
            }
        });
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent in=new Intent(MainActivity.this,AddData.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(in);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
