package com.example.edspireptss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Forum extends Fragment {
    String selectedSubject;
    String selectedDept;
    FirebaseAuth mAuth;
    private RecyclerView queList;
    private DatabaseReference Questionref,Likesref;
    Boolean LikeChecker = false;
    String currentuserID;
    DatabaseReference UsersRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_forum, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentuserID = mAuth.getCurrentUser().getUid();
        if(getActivity()!=null) {
            Intent intent = getActivity().getIntent();
            selectedSubject = intent.getStringExtra("SelectedSubject");
            selectedDept=intent.getStringExtra("dept");
        }
        queList = (RecyclerView) rootview.findViewById(R.id.all_que_list);
        queList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        queList.setLayoutManager(linearLayoutManager);
        Questionref = FirebaseDatabase.getInstance().getReference().child("Departments").child(selectedDept).child(selectedSubject);
        Likesref = FirebaseDatabase.getInstance().getReference().child("Likes");
        DisplayAllQuestion();

        return rootview;
    }

    private void DisplayAllQuestion() {
        FirebaseRecyclerAdapter<Questionss, QuestionViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Questionss, QuestionViewHolder>(Questionss.class,R.layout.all_ques_ans,QuestionViewHolder.class,Questionref) {
                    @Override
                    protected void populateViewHolder(QuestionViewHolder questionViewHolder, Questionss questionss, int i) {

                        final String  PostKey = getRef(i).getKey();

                        questionViewHolder.setQuestion(questionss.getQuestion());
                        questionViewHolder.setProfileImage(getContext(),questionss.getProfileImage());
                        questionViewHolder.setUsername(questionss.getUsername());
                        questionViewHolder.setTime(questionss.getTime());
                        questionViewHolder.setDate(questionss.getDate());

                        questionViewHolder.setLikeButtonStatus(PostKey);

                        questionViewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent commentsIntent = new Intent(getActivity(),AnswersActivity.class);
                                // commentsIntent.putExtra("Que",)
                                commentsIntent.putExtra("Postkey",PostKey);
                                commentsIntent.putExtra("selectedSub",selectedSubject);
                                commentsIntent.putExtra("Dept",selectedDept);
                                commentsIntent.putExtra("hisUid",questionss.getUid());
                                startActivity(commentsIntent);

                            }
                        });

                        questionViewHolder.LikePostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LikeChecker = true;
                                Likesref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(LikeChecker.equals(true)){
                                            if(dataSnapshot.child(PostKey).hasChild(currentuserID))
                                            {
                                                Likesref.child(PostKey).child(currentuserID).removeValue();
                                                LikeChecker = false;
                                            }
                                            else {
                                                Likesref.child(PostKey).child(currentuserID).setValue(true);
                                                LikeChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });

                        //Admin Delete
                        Query query = FirebaseDatabase.getInstance().getReference().child("Departments").child(selectedDept).child(selectedSubject).orderByChild("Postkey").equalTo(PostKey);
                        final FirebaseDatabase database=FirebaseDatabase.getInstance();
                        UsersRef= database.getReference().child("Users");
                        questionViewHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
                                                                          @Override
                                                                          public void onClick(View view) {
                                                                              UsersRef.child(currentuserID).addValueEventListener(new ValueEventListener() {
                                                                                  @Override
                                                                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                      if (snapshot.exists()) {
                                                                                          if (snapshot.hasChild("Admin")) {
                                                                                              query.addValueEventListener(new ValueEventListener() {
                                                                                                  @Override
                                                                                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                      for (DataSnapshot queSnap : snapshot.getChildren()) {
                                                                                                          queSnap.getRef().removeValue();
                                                                                                      }
                                                                                                  }

                                                                                                  @Override
                                                                                                  public void onCancelled(@NonNull DatabaseError error) {

                                                                                                  }
                                                                                              });

                                                                                          }
                                                                                      }
                                                                                  }

                                                                                  @Override
                                                                                  public void onCancelled(@NonNull DatabaseError error) {

                                                                                  }
                                                                              });
                                                                          }
                                                                      });


                        //Delete Question
                        final String  uid = questionss.getUid();
                        if (uid.equals(currentuserID)){
                            Query dquery = FirebaseDatabase.getInstance().getReference().child("Departments").child(selectedDept).child(selectedSubject).orderByChild("Postkey").equalTo(PostKey);
                            questionViewHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dquery.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot queSnap: snapshot.getChildren()){
                                                queSnap.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }


                        //Edit Question
                        if (uid.equals(currentuserID)){
                            questionViewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(),EditQuestion.class);
                                    // commentsIntent.putExtra("Que",)
                                    intent.putExtra("Dept",selectedDept);
                                    intent.putExtra("selectedSub",selectedSubject);
                                    intent.putExtra("editPostId",PostKey);
                                    intent.putExtra("questionss",questionss.getQuestion());
                                    intent.putExtra("hisUid",questionss.getUid());
                                    startActivity(intent);

                                }
                            });
                        }



                        /*questionViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                                builder.setTitle("Delete");
                                builder.setMessage("Are you sure you want to delete this post?");
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final String  PostKey = getRef(i).getKey();
                                        questionViewHolder.deletePost(PostKey);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                            }
                        });*/
                    }
                };
        queList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder{
        View mView;
        String selectedSubject;
        String selectedDept;
        ImageButton LikePostButton,CommentPostButton, moreBtn, editBtn;
        TextView DisplaynoOfLikes;
        int countLikes;
        String currentUserId;
        DatabaseReference Likesref,Questionref;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            LikePostButton = (ImageButton) mView.findViewById(R.id.like_button);
            moreBtn = (ImageButton) mView.findViewById(R.id.moreBtn);
            editBtn = (ImageButton) mView.findViewById(R.id.editBtn);
            CommentPostButton = (ImageButton) mView.findViewById(R.id.comment_button);
            DisplaynoOfLikes = (TextView) mView.findViewById(R.id.no_likes);

            Likesref = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        /*public void deletePost(final String PostKey){
            Questionref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (snapshot.child(PostKey).hasChild(currentUserId)){
                        snapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
         */

        public void setLikeButtonStatus(final String PostKey){
            Likesref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId)){
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.upvoted);

                        DisplaynoOfLikes.setText((Integer.toString(countLikes)+(" Upvotes")));
                    }
                    else {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.upvote);

                        DisplaynoOfLikes.setText((Integer.toString(countLikes)+(" Upvotes")));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        /*
        public void deletePost(final String PostKey){

            Questionref = FirebaseDatabase.getInstance().getReference().child("Departments").child(selectedDept).child(selectedSubject);
            Questionref.child("Departments").child(selectedDept).child(selectedSubject).child(PostKey).removeValue();
        }*/

        public void setUsername(String username){
            TextView u = (TextView) mView.findViewById(R.id.post_que_user_name);
            u.setText(username);
        }
        public void setProfileImage(Context ctx, String profileImage) {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_que_user_image);
            Picasso.with(ctx).load(profileImage).into(image);
        }
        public void setQuestion(String question) {
            TextView userq = (TextView) mView.findViewById(R.id.user_que);
            userq.setText(question);
        }
        public void setTime(String time){
            TextView ptime = (TextView) mView.findViewById(R.id.post_time);
            ptime.setText("  "+time);
        }
        public void setDate(String date){
            TextView pdate = (TextView) mView.findViewById(R.id.post_date);
            pdate.setText("  "+date);
        }
    }
}
