package com.example.edspireptss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnswersActivity extends AppCompatActivity {

    private RecyclerView AnsList;
    private ImageButton PostAnsButton;
    private EditText AnswerInputText;
    String selectedSubject;
    String selectedDept;
    String hisUid;

    private String Postkey,current_user_id;
    private FirebaseAuth mAuth;

    private DatabaseReference Userref,Questionref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        Postkey = getIntent().getExtras().get("Postkey").toString();
        mAuth = FirebaseAuth.getInstance();
        current_user_id =  mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");
        //Toast.makeText(AnswersActivity.this, ""+hisUid, Toast.LENGTH_SHORT).show();


            selectedSubject =  getIntent().getExtras().get("selectedSub").toString();
            selectedDept= getIntent().getExtras().get("Dept").toString();


        Userref = FirebaseDatabase.getInstance().getReference().child("Users");
        Questionref = FirebaseDatabase.getInstance().getReference().child("Departments").child(selectedDept).child(selectedSubject).child(Postkey).child("answers");

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AnsList = (RecyclerView) findViewById(R.id.ans_list);
        AnsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        AnsList.setLayoutManager(linearLayoutManager);

        AnswerInputText = (EditText) findViewById(R.id.answersInput);
        PostAnsButton = (ImageButton) findViewById(R.id.post_ans_button);

        PostAnsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Userref.child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String userName = dataSnapshot.child("Username").getValue().toString();
                            String profileImg = dataSnapshot.child("profileImage").getValue().toString();
                            String department = dataSnapshot.child("Department").getValue().toString();
                            ValidateAnswer(userName,profileImg, department);

                            AnswerInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Answers, AnswersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Answers, AnswersViewHolder>(Answers.class,R.layout.all_answers_layout,AnswersViewHolder.class,Questionref) {
            @Override
            protected void populateViewHolder(AnswersViewHolder answersViewHolder, Answers answers, int i) {
                answersViewHolder.setpUid(answers.getpUid());
                answersViewHolder.setUsername(answers.getUsername());
                answersViewHolder.setDepartment(answers.getDepartment());
                answersViewHolder.setProfileimage(getApplicationContext(),answers.getProfileimage());
                answersViewHolder.setAnswer(answers.getAnswer());
                answersViewHolder.setDate(answers.getDate());
                answersViewHolder.setTime(answers.getTime());

            }
        };
        AnsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AnswersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public AnswersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setpUid(String pUid){
        }
        public void setUsername(String username){
            TextView myuserName = (TextView) mView.findViewById(R.id.ans_username);
            myuserName.setText(username);
        }
        public void setDepartment(String department){
            TextView mydepartment = (TextView) mView.findViewById(R.id.ans_department);
            mydepartment.setText(department);
        }
        public void setAnswer(String answer){
            TextView myanswer = (TextView) mView.findViewById(R.id.Answer_text);
            myanswer.setText(answer);
        }
        public void setDate(String date){
            TextView mydate = (TextView) mView.findViewById(R.id.ans_date);
            mydate.setText(date);

        }
        public void setTime(String time) {
            TextView mytime = (TextView) mView.findViewById(R.id.ans_time);
            mytime.setText(time);
        }
        public void setProfileimage(Context ctx, String profileImg){
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_ans_img);
            Picasso.with(ctx).load(profileImg).into(image);
        }
    }

    private void ValidateAnswer(String userName,String profileImg, String department) {
        String answerText = AnswerInputText.getText().toString();
        if (TextUtils.isEmpty(answerText)){
            Toast.makeText(this,"Please write an answer",Toast.LENGTH_SHORT).show();
        }
        else{
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yy");
             final String saveCurrentDate = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String  saveCurrentTime = currentTime.format(calForDate.getTime());

            final String RandomKey = current_user_id + saveCurrentDate + saveCurrentTime;

            HashMap answersMap = new HashMap();
            answersMap.put("uid",current_user_id);
            answersMap.put("answer",answerText);
            answersMap.put("date",saveCurrentDate);
            answersMap.put("time",saveCurrentTime);
            answersMap.put("username",userName);
            answersMap.put("department",department);
            answersMap.put("profileimage",profileImg);

            Questionref.child(RandomKey).updateChildren(answersMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        addToHisNotification("" + hisUid,"" + Postkey, "Answered your question at " + selectedDept +" " + selectedSubject);

                        Toast.makeText(AnswersActivity.this, "Your answer was submitted successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AnswersActivity.this, "Error occurred, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void addToHisNotification(String hisUid,String Postkey,String notification){
        String timestamp = ""+System.currentTimeMillis();

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("timestamp",timestamp);
        hashMap.put("Postkey",Postkey);
        hashMap.put("pUid",hisUid);
        hashMap.put("notification",notification);
        hashMap.put("sUid",current_user_id);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.child(hisUid).child("Notifications").child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}