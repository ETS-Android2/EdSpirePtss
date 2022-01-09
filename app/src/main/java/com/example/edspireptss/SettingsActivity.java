package com.example.edspireptss;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class SettingsActivity extends AppCompatActivity {

    DatabaseReference UsersRef;
    DatabaseReference SettingUserRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    String currentUserID;
    String currentUsername;
    String currentPassword;
    EditText newphone,enteredPassword,newUsername,newPassword,newDesc;
    Button save;
    Button deleteAccount;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        mAuth= FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        currentUserID=mAuth.getCurrentUser().getUid();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersRef=database.getReference().child("Users");
        SettingUserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        enteredPassword=findViewById(R.id.settings_current_password);
        newUsername=findViewById(R.id.settings_new_username);
        newphone=findViewById(R.id.settings_new_phone);
        loadingBar=new ProgressDialog(this);
        newDesc=findViewById(R.id.settings_new_description);
        save=findViewById(R.id.settings_save_button);
        deleteAccount=findViewById(R.id.delete_account);

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your " +
                        "account from the app and you won't able to access the app!");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserData(currentUserID);
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SettingsActivity.this, "Account Deleted", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    SettingsActivity.this.finish();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();;
            }
        });

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("Username")) {
                       currentUsername = dataSnapshot.child("Username").getValue().toString();
                    }
                    if (dataSnapshot.hasChild("Password")) {
                        currentPassword = dataSnapshot.child("Password").getValue().toString();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });

    }

    private void deleteUserData(String currentUserID) {
        DatabaseReference deUsers = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        deUsers.removeValue();
    }

    private void saveDetails() {
        loadingBar.setMessage("Updating.");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);

        String tempNewUsername=newUsername.getText().toString();
        String temptNewPhone=newphone.getText().toString();
        String temptNewDescription=newDesc.getText().toString();

        if (!TextUtils.isEmpty(tempNewUsername)) {
            HashMap UserMap=new HashMap();
            final Drawable updateIcon=getResources().getDrawable(R.drawable.checked_icon);
            updateIcon.setBounds(0,0,updateIcon.getIntrinsicWidth(),updateIcon.getIntrinsicHeight());
            UserMap.put("Username",tempNewUsername);
            SettingUserRef.updateChildren(UserMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        newUsername.setError("Updated",updateIcon);
                        Toast.makeText(SettingsActivity.this, "Account Updated", Toast.LENGTH_LONG).show();
                    }
                    else {
                        newUsername.setError("Update Failed");
                    }

                }
            });
        }

        if (!TextUtils.isEmpty(temptNewPhone)) {
            HashMap UserMap=new HashMap();
            final Drawable updateIcon=getResources().getDrawable(R.drawable.checked_icon);
            updateIcon.setBounds(0,0,updateIcon.getIntrinsicWidth(),updateIcon.getIntrinsicHeight());
            UserMap.put("Phone Number",temptNewPhone);
            SettingUserRef.updateChildren(UserMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        newUsername.setError("Updated",updateIcon);
                        Toast.makeText(SettingsActivity.this, "Account Updated", Toast.LENGTH_LONG).show();
                    }
                    else {
                        newUsername.setError("Update Failed");
                    }

                }
            });
        }

        if (!TextUtils.isEmpty(temptNewDescription)) {
            HashMap UserMap=new HashMap();
            final Drawable updateIcon=getResources().getDrawable(R.drawable.checked_icon);
            updateIcon.setBounds(0,0,updateIcon.getIntrinsicWidth(),updateIcon.getIntrinsicHeight());
            UserMap.put("Description",temptNewDescription);
            SettingUserRef.updateChildren(UserMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        newUsername.setError("Updated",updateIcon);
                        Toast.makeText(SettingsActivity.this, "Account Updated", Toast.LENGTH_LONG).show();
                    }
                    else {
                        newUsername.setError("Update Failed");
                    }

                }
            });
        }

        loadingBar.dismiss();

    }

}
