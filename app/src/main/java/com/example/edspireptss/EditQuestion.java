package com.example.edspireptss;

import android.app.AlertDialog;
import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EditQuestion extends AppCompatActivity {
    EditText question;
    Button addBtn;
    String selectedSubject;
    FirebaseAuth mAuth;
    String currentUserID;
    DatabaseReference UsersReference;
    DatabaseReference Questionref;
    DatabaseReference UsersRef;
    String selectedDept, editPostId, questionss, editQuestion;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        mAuth = FirebaseAuth.getInstance();
        if (this != null) {

        }
        Intent intent = this.getIntent();
        selectedDept = intent.getStringExtra("Dept");
        selectedSubject = intent.getStringExtra("selectedSub");
        editPostId = intent.getStringExtra("editPostId");
        questionss = intent.getStringExtra("questionss");
        loadPostData(editPostId);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserID = mAuth.getCurrentUser().getUid();
        //loadPostData(editPostId);
        question = findViewById(R.id.add_question_txt);
        addBtn = findViewById(R.id.add_question_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditQuestion.this)
                        .setMessage("Are you sure you want to Update this Question?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateQuestion(selectedSubject, selectedDept, editPostId);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });


    }

    public void addNewQuestion(final String selectedSubject, final String selectedDept) {

        final String userQuestion = question.getText().toString();

        if (TextUtils.isEmpty(userQuestion)) {
            Toast.makeText(this, "Please write your question.", Toast.LENGTH_LONG).show();
            return;
        }

        UsersRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("Username").getValue().toString();
                    String userprofileImg = dataSnapshot.child("profileImage").getValue().toString();
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yy");
                    final String saveCurrentDate = currentDate.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                    final String saveCurrentTime = currentTime.format(calForDate.getTime());


                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    UsersReference = database.getReference().child("Departments").child(selectedDept).child(selectedSubject);
                    String pushID = UsersReference.push().getKey();
                    HashMap userMap = new HashMap();
                    userMap.put("uid", currentUserID);
                    userMap.put("Postkey", pushID);
                    userMap.put("username", username);
                    userMap.put("profileImage", userprofileImg);
                    userMap.put("question", userQuestion);
                    userMap.put("date", saveCurrentDate);
                    userMap.put("time", saveCurrentTime);
                    UsersReference.child(pushID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Question added successfully", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                Toast.makeText(context, task.getException().toString(), Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadPostData(String editPostId) {
        Query fquery = FirebaseDatabase.getInstance().getReference().child("Departments").child(selectedDept).child(selectedSubject).orderByChild("Postkey").equalTo(editPostId);
        fquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    editQuestion = "" + ds.child("question").getValue();

                    //set data to views
                    question.setText(editQuestion);

                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void updateQuestion(final String selectedSubject, final String selectedDept, final String editPostId) {

        final String userQuestion = question.getText().toString();

        if (TextUtils.isEmpty(userQuestion)) {
            Toast.makeText(this, "Please write your question.", Toast.LENGTH_LONG).show();
            return;
        }

        String tempNewQuestion=question.getText().toString();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Questionref = database.getReference().child("Departments").child(selectedDept).child(selectedSubject);
        HashMap UserMap=new HashMap();
        UserMap.put("question",tempNewQuestion);
        Questionref.child(editPostId).updateChildren(UserMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditQuestion.this, "Post Updated", Toast.LENGTH_LONG).show();
                }
                else {
                }

            }
        });
    }
}
