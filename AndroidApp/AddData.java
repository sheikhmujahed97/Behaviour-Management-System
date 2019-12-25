package com.example.rramesh.emotiveperformance;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;

public class AddData extends AppCompatActivity {

 FirebaseAuth mAuth;

    TextInputEditText name,roll,section,phone;
    private FirebaseUser mCurrentUser;
    ImageView image;
    private DatabaseReference mDatabase;
    Button adddata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);


        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=mCurrentUser.getUid();
       // String key=mDatabase.push().getKey();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("student").child(current_uid);


        name=findViewById(R.id.studname);
        roll=findViewById(R.id.studroll);
        section=findViewById(R.id.studsection);
        phone=findViewById(R.id.studphone);
        adddata=findViewById(R.id.adddata);
        image=findViewById(R.id.studimage);

        adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentdata();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    private void addStudentdata() {
        String nameadd=name.getText().toString().trim();
        String rolladd=roll.getText().toString().trim();
        String sectionadd=section.getText().toString().trim();
        String phoneadd=phone.getText().toString().trim();

        if(!(nameadd.isEmpty() && rolladd.isEmpty() && sectionadd.isEmpty())){
            String currentMilliSeconds=""+System.currentTimeMillis();
            mDatabase.child(currentMilliSeconds).child("name").setValue(nameadd);
            mDatabase.child(currentMilliSeconds).child("roll").setValue(rolladd);
            mDatabase.child(currentMilliSeconds).child("section").setValue(sectionadd);
            mDatabase.child(currentMilliSeconds).child("phone").setValue(phoneadd);
            Toast.makeText(AddData.this, "Data Saved", Toast.LENGTH_SHORT).show();

            Intent in=new Intent(AddData.this,MainActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
            finish();

        }else {
            Toast.makeText(AddData.this,"Enter all the fields ",Toast.LENGTH_LONG).show();
        }

    }
}
