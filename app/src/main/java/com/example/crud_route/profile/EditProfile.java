package com.example.crud_route.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import com.example.crud_route.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {
    ImageView userPhoto;
    TextView userName, userDescription, userMotorcycle;
    Button btnTakePhoto, btnDeletePhoto, btnUpdateProfile;
    StorageReference storageReference;
    String storagePaht = "user/*";
    String photo = "photo";
    String idd;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    ProgressDialog progressDialog;

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private Bitmap imgUrl;
    private String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_dialog);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mfirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        // Edit Profile
        userPhoto = findViewById(R.id.profileImage);
        btnTakePhoto = findViewById(R.id.btnTakeProfilePhoto);
        btnDeletePhoto = findViewById(R.id.btnDeleteProfilePhoto);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });
        btnDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            choosePhotoOption();
        }
    }

    private void choosePhotoOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Photo Option");
        builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Take photo
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent, COD_SEL_IMAGE);
                        break;
                    case 1: // Choose from galery
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType("image/*");
                        startActivityForResult(i, COD_SEL_IMAGE);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "" + data, Toast.LENGTH_LONG).show();
        if (requestCode == COD_SEL_IMAGE && resultCode == RESULT_OK) {
            if (imgUrl != null) {
                //imgUrl = data.getData();
                Bundle extras = data.getExtras();
                Bitmap imgBitmap = (Bitmap) extras.get("data");
                imgUrl = imgBitmap;
                sendPhoto(imgUrl);
                userPhoto.setImageBitmap(imgBitmap);
            } else {
                Toast.makeText(this, "Error: URI of selected image is null", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void sendPhoto(Bitmap bitmap) {
        progressDialog.setMessage("Saving photo");
        progressDialog.show();
        //Save bitmap on a temporary file
        File tempFile = saveBitmapToFile(bitmap);
        // Obtain URI of temporary file
        if (tempFile != null) {
            Uri tempUri = FileProvider.getUriForFile(this, "com.example.crud_route.EditProfile", tempFile);
            String routeStoragePhoto = storagePaht + "" + photo + "" + mAuth.getUid() + "" + idd;
            StorageReference reference = storageReference.child(routeStoragePhoto);
            // Upload file to firebase storage
            reference.putFile(tempUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", downloadUri);
                            mfirestore.collection("user").document(idd).update(map);
                            Toast.makeText(getApplication(), "Updated photo", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplication(), "Error:" + e.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private File saveBitmapToFile(Bitmap bitmap) {
        try {
            File tempFile = File.createTempFile("temp_image", ".jpg", getExternalCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void getUser(FirebaseUser currentUser) {
        mfirestore.collection("user").document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String photo = documentSnapshot.getString("photo");
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String motorcycle = documentSnapshot.getString("motorcycle");

                userName.setText(name);
                userDescription.setText(description);
                userMotorcycle.setText(motorcycle);
                try {
                    if (!photo.equals("")) {
                        Toast toast = Toast.makeText(getApplication(), "Loading Photo", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 200);
                        toast.show();
                        Picasso.with(EditProfile.this)
                                .load(photo)
                                .resize(150, 150)
                                .into(userPhoto);

                    }
                } catch (Exception e) {
                    Toast.makeText(getApplication(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

}