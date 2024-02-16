package com.example.crud_route.profile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.crud_route.R;
import com.example.crud_route.more.ExplorePage;
import com.example.crud_route.more.Inclination;
import com.example.crud_route.route.CreateRoute;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Profile extends AppCompatActivity {
    ImageView userPhoto;
    TextView userName, userDescription, userMotorcycle;
    Button btnTakePhoto, btnDeletePhoto, btnMore, btnCreateRoute, btnEditProfile;
    ProgressDialog progressDialog;

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    private Bitmap imgUrl;
    StorageReference storageReference;
    String storagePaht = "user/*";
    String photo = "photo";
    String idd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressDialog = new ProgressDialog(this);


        //Profile
        userPhoto = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userDescription = findViewById(R.id.userDescription);
        userMotorcycle = findViewById(R.id.userMotorcycle);

        btnTakePhoto = findViewById(R.id.btnTakeProfile);
        btnDeletePhoto = findViewById(R.id.btnDeleteProfile);
        //btnEditProfile = findViewById(R.id.btnEditProfile);
        btnMore = findViewById(R.id.btnMore);
        btnCreateRoute = findViewById(R.id.btnCreateRoute);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });
        btnDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPhoto.setImageDrawable(null); // Eliminar la imagen del ImageView
                // Establecer el ImageView con otro icono o imagen de marcador de posición
                userPhoto.setImageResource(R.drawable.bikerblack);
            }
        });
        /*btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), EditProfile.class));
            }
        });*/
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), Inclination.class));
            }
        });
        btnCreateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), CreateRoute.class));
            }
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
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
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Obtener la imagen capturada como un bitmap
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Mostrar la imagen capturada en el ImageView
            userPhoto.setImageBitmap(imageBitmap);
        }
        /*if (requestCode == COD_SEL_IMAGE && resultCode == RESULT_OK) {
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
        }*/
    }

    private void sendPhoto(Bitmap bitmap) {
        //progressDialog.setMessage("Saving photo");
        //progressDialog.show();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si se otorgan los permisos, iniciar la actividad de captura de imágenes
                choosePhotoOption();
            } else {
                // Si se deniegan los permisos, mostrar un mensaje al usuario
                Toast.makeText(this, "Los permisos de la cámara son necesarios para tomar fotos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
