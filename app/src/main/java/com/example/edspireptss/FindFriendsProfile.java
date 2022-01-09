package com.example.edspireptss;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class FindFriendsProfile extends Fragment {

    private ImageView ProfileImg;
    private StorageReference ProfileImgRef;
    private FirebaseAuth mAuth;
    String currentUserID;
    FirebaseUser user;
    private TextView enrollment;
    private TextView username;
    private TextView email;
    private TextView department;
    private TextView description;
    private TextView contact;
    private ProgressDialog loadingBar;
    DatabaseReference UsersReference;
    DatabaseReference UsersRef;
    private String visitorID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.find_friends_profile, container, false);

        ProfileImgRef= FirebaseStorage.getInstance().getReference().child("Profile Images");

        mAuth= FirebaseAuth.getInstance();

        Bundle bundle=this.getArguments();
        visitorID=bundle.getString("visit_user_id");

        currentUserID=mAuth.getCurrentUser().getUid();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersReference=database.getReference().child("Users");
        UsersRef=database.getReference().child("Users");
        loadingBar=new ProgressDialog(rootview.getContext());
        username=(TextView)rootview.findViewById(R.id.profile_username) ;
        enrollment=(TextView)rootview.findViewById(R.id.profile_enrollment) ;
        email=(TextView)rootview.findViewById(R.id.profile_email) ;
        department=(TextView)rootview.findViewById(R.id.profile_department) ;
        description=(TextView)rootview.findViewById(R.id.profile_description) ;
        contact=(TextView)rootview.findViewById(R.id.profile_contact) ;

        ProfileImg=(ImageView)rootview.findViewById(R.id.profile_pic);


        UsersReference.child(visitorID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild("profileImage")) {
                        String image = dataSnapshot.child("profileImage").getValue().toString();
                        loadingBar.dismiss();
                        Picasso.with(getActivity()).load(image).placeholder(R.drawable.profile_img).into(ProfileImg);
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
        UsersRef.child(visitorID).addValueEventListener(new ValueEventListener() {
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
                        String number = dataSnapshot.child("Phone Number").getValue().toString();
                        contact.setText(number);
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
                        Toast.makeText(getActivity(),"Profile was not set up",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return rootview;
    }


}
