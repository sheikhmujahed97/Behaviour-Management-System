package com.example.rramesh.emotiveperformance;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Piechart extends AppCompatActivity {
    PieChartView pieChartView;
    ImageView addimage;
    private Uri filePath;

    private final int  PICK_FROM_GALLERY = 1;

    FirebaseStorage storage;
    StorageReference storageReference;

    private int RECORD_REQUEST_CODE = 101;
    TextView name,roll,phone,dept;
    String TAG=this.getClass().getSimpleName();
    String phoneNoTocall="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

        pieChartView = findViewById(R.id.chart);
        name=findViewById(R.id.stuname);
        roll=findViewById(R.id.sturoll);
        phone=findViewById(R.id.stuphone);
        dept=findViewById(R.id.studdept);
        addimage=findViewById(R.id.addiamge);
        storageReference=FirebaseStorage.getInstance().getReference();

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AskgalleryPermission();


            }
        });


        Student student = (Student) getIntent().getSerializableExtra("student");
        if(student!=null){
            Log.d(TAG,student.getName());
            name.setText(student.getName());
            roll.setText(student.getRollno());
            phone.setText(student.getPhone());
            dept.setText(student.getSection());

        }


        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNoTocall=phone.getText().toString();
                askpermission(phoneNoTocall);
            }
        });

        Random rand = new Random();
        int anger = rand.nextInt(50);
        anger += 1;
        int love = rand.nextInt(50);
        love += 1;
        int sad = rand.nextInt(50);
        sad += 1;
        int happy = rand.nextInt(50);
        happy += 1;



        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(anger, Color.BLUE).setLabel("anger"));
        pieData.add(new SliceValue(happy, Color.GRAY).setLabel("happy"));
        pieData.add(new SliceValue(love, Color.RED).setLabel("love"));
        pieData.add(new SliceValue(sad, Color.MAGENTA).setLabel("sad"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1(student.getName()).setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData); }

    private void AskgalleryPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(Piechart.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Piechart.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }*/

    private void askpermission(String phoneNoTocall) {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to phone denied");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    RECORD_REQUEST_CODE);
        }else{
            callPhoneNo(phoneNoTocall);
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

      switch (requestCode){
          case 101:
              if( (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                  Log.i(TAG, "Permission has been granted by user");
                  callPhoneNo(phoneNoTocall);
              }else{
                  Toast.makeText(Piechart.this,"Permission denied to make call",Toast.LENGTH_SHORT).show();
              }

              break;
          case 1:
              // If request is cancelled, the result arrays are empty.
              if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  startActivityForResult(galleryIntent, PICK_FROM_GALLERY);



              } else {
                  //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
              }
              break;


      }

    }

    private void callPhoneNo(String phoneNo) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phoneNo));
        startActivity(callIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FROM_GALLERY && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            filePath=data.getData();
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                uploadImage(bitmap);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Bitmap bitmap) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Piechart.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Piechart.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
            progressDialog.setMessage("failed ");
            progressDialog.dismiss();
        }

    }
}
