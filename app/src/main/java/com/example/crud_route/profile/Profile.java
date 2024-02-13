package com.example.crud_route.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crud_route.R;
import com.example.crud_route.more.ExplorePage;
import com.example.crud_route.route.CreateRoute;
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

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    ImageView userPhoto;
    TextView userName, userDescription, userMotorcycle;
    Button btnMore, btnCreateRoute, btnTakePhoto, btnDeletePhoto, btnUpdateProfile;
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

    private Uri imgUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_dialog);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mfirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Profile
        /*userPhoto = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userDescription = findViewById(R.id.userDescription);
        userMotorcycle = findViewById(R.id.userMotorcycle);*/
        // Edit Profile
        btnTakePhoto = findViewById(R.id.btnTakeProfilePhoto);
        btnDeletePhoto = findViewById(R.id.btnDeleteProfilePhoto);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        //BottomMenu buttons
        /*btnMore = findViewById(R.id.btnMore);
        btnCreateRoute = findViewById(R.id.btnCreateRoute);*/

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });


        /*btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), ExplorePage.class));
            }
        });
        btnCreateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), CreateRoute.class));
            }
        });*/
    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == COD_SEL_IMAGE) {
                imgUrl = data.getData();
                sendPhoto(imgUrl);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendPhoto(Uri imgUrl) {
        progressDialog.setMessage("Loading photo");
        progressDialog.show();
        String routeStoragePhoto = storagePaht + "" + photo + "" + mAuth.getUid() + "" + idd;
        StorageReference reference = storageReference.child(routeStoragePhoto);
        reference.putFile(imgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                    if (uriTask.isSuccessful()) {
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), "Error:" + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
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
                        Picasso.with(Profile.this)
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