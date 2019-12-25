package com.example.rramesh.emotiveperformance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText emails, passwords,names;
    TextView logins;
    FirebaseAuth mAuth;
    Button  registers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emails = findViewById(R.id.regmail);
        passwords = findViewById(R.id.regpass);
        mAuth=FirebaseAuth.getInstance();
        logins = findViewById(R.id.reglogin);
        names=findViewById(R.id.regname);
        registers = findViewById(R.id.regregister);
        logins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        registers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=emails.getText().toString();
                String password=passwords.getText().toString();
                String name=names.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Enter mail",Toast.LENGTH_LONG).show();
                }
                else  if(password.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Enter password",Toast.LENGTH_LONG).show();
                }
                 else if(email.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Enter name",Toast.LENGTH_LONG).show();
                }
                else if(name.length()<3){
                    Toast.makeText(RegisterActivity.this,"name should be greater than 3 characters",Toast.LENGTH_LONG).show();

                }
            else if(password.length()<6){
                    Toast.makeText(RegisterActivity.this,"password should be greater than 6 characters",Toast.LENGTH_LONG).show();
                }
         else if (!(name.isEmpty() && email.isEmpty() && password.isEmpty())){

                  UserRegistered(email,password);
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Enter all the fields",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void UserRegistered(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Registered Succesfully",Toast.LENGTH_LONG).show();
                    Intent in = new Intent(RegisterActivity.this, MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    finish();

                }else if(task.isCanceled()){
                    Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
