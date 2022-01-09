package com.example.edspireptss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Profile extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private ImageView ProfileImg;
    final static int Gallery_Pick=1;
    private StorageReference ProfileImgRef;
    private FirebaseAuth mAuth;
    String currentUserID="ZkHajqRNBDaXCViUlmb9dESAqer1";
    FirebaseUser user;
    private TextView enrollment;
    private TextView username;
    private TextView email;
    private TextView phone;
    private TextView department;
    private TextView description;
    private ProgressDialog loadingBar;
    private static final int RESULT_CODE = 100;
    TextView tvProfile;
    ImageView NavProfileImg;
    ImageButton btnSetting;
    DatabaseReference UsersReference;
    DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        ProfileImgRef= FirebaseStorage.getInstance().getReference().child("Profile Images");

        mAuth= FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersReference=database.getReference().child("Users").child(currentUserID);
        UsersRef=database.getReference().child("Users");

        loadingBar = new ProgressDialog(this);
        btnSetting = findViewById(R.id.editBtn) ;
        username = findViewById(R.id.profile_username) ;
        enrollment = findViewById(R.id.profile_enrollment) ;
        email = findViewById(R.id.profile_email) ;
        phone = findViewById(R.id.profile_phone) ;
        department = findViewById(R.id.profile_department) ;
        description = findViewById(R.id.profile_description) ;
        tvProfile = findViewById(R.id.tvProfile);
        NavProfileImg=findViewById(R.id.nav_profileimg);
        
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Profile.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("Email ID")) {
                        String mail = dataSnapshot.child("Email ID").getValue().toString();
                        tvProfile.setText(mail);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your mail was not set up successfully",Toast.LENGTH_LONG).show();
                    }
                    if (dataSnapshot.hasChild("profileImage")) {
                        String profilePic = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.with(Profile.this).load(profilePic).placeholder(R.drawable.profile_img).into(NavProfileImg);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your profile was not set up successfully",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ProfileImg = findViewById(R.id.profile_pic);
        ProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);
            }
        });
        UsersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild("profileImage")) {
                        String image = dataSnapshot.child("profileImage").getValue().toString();
                        loadingBar.dismiss();
                        Picasso.with(Profile.this).load(image).placeholder(R.drawable.profile_img).into(ProfileImg);
                    }
                    else
                    {
                        loadingBar.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("Username")) {
                        String pusername = dataSnapshot.child("Username").getValue().toString();
                        username.setText(pusername);
                    }
                    if (dataSnapshot.hasChild("Email ID")) {
                        String mail = dataSnapshot.child("Email ID").getValue().toString();
                        email.setText(mail);
                    }
                    if (dataSnapshot.hasChild("Phone Number")) {
                        String phonenum = dataSnapshot.child("Phone Number").getValue().toString();
                        phone.setText(phonenum);
                    }
                    if (dataSnapshot.hasChild("Matric Number")) {
                        String enrollment_number = dataSnapshot.child("Matric Number").getValue().toString();
                        enrollment.setText(enrollment_number);
                    }
                    if (dataSnapshot.hasChild("Department")) {
                        String department1 = dataSnapshot.child("Department").getValue().toString();
                        department.setText(department1);
                    }
                    if (dataSnapshot.hasChild("Description")) {
                        String description1 = dataSnapshot.child("Description").getValue().toString();
                        description.setText(description1);
                    }
                    else {
                        Toast.makeText(Profile.this,"Your profile was not set up successfully",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return;
    }

    public void ClickMenu(View view){
        com.example.edspireptss.MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        com.example.edspireptss.MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this,Home.class);
    }

    public void ClickProfile(View view){
        recreate();
    }

    public void ClickReminder(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this,AssignmentList.class);
    }

    public void ClickForum(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this, com.example.edspireptss.Department.class);
    }

    public void ClickNotification(View view){
        com.example.edspireptss.MainActivity.redirectActivity(this, com.example.edspireptss.NotificationFragment.class);
    }

    public void ClickLogout(View view){
        com.example.edspireptss.MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        com.example.edspireptss.MainActivity.closeDrawer(drawerLayout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(Profile.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                loadingBar.setMessage("Please wait, while we update your profile picture.");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(false);
                Uri resultUri = result.getUri();

                final StorageReference filePath = ProfileImgRef.child(currentUserID + ".jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();

                                UsersReference.child("profileImage").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(Profile.this, Profile.class);
                                                    startActivity(intent);
                                                    Profile.this.finish();

                                                } else {
                                                    Toast.makeText(Profile.this, "Error occured..." + task.getException(), Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });
                            }
                        });
                    }
                });
            }
        }
    }

}