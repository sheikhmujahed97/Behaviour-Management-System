package com.example.rramesh.emotiveperformance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login, register;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        email = findViewById(R.id.logmail);
        password = findViewById(R.id.logpass);
        login = findViewById(R.id.loglogin);
        register = findViewById(R.id.logregister);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.googlesign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                         String mail=email.getText().toString();
                                         String pass=password.getText().toString();

                                         if(mail.isEmpty()){
                                             Toast.makeText(LoginActivity.this,"Enter name",Toast.LENGTH_LONG).show();
                                         }
                                         else  if(pass.isEmpty()){
                                             Toast.makeText(LoginActivity.this,"Enter password",Toast.LENGTH_LONG).show();
                                         }

                                         else if (!(mail.isEmpty() && pass.isEmpty())){
                                             mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                                     if(task.isSuccessful()){

                                                         Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                                                         Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                                         in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                         startActivity(in);
                                                         finish();
                                                     }
                                                     else if(task.isCanceled()){
                                                         Toast.makeText(LoginActivity.this,"Invalid mail and password",Toast.LENGTH_LONG).show();
                                                     }
                                                 }
                                             });

                                         }
                                         else {
                                             Toast.makeText(LoginActivity.this,"Enter all the fields",Toast.LENGTH_LONG).show();
                                         }

                                     }
                                 }
        );


    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
   protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this,"error", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);


        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();

                    Toast.makeText(LoginActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);

                }

                else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


}
