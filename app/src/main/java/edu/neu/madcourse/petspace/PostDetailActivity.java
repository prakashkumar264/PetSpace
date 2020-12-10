package edu.neu.madcourse.petspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.petspace.adapters.AdapterComments;
import edu.neu.madcourse.petspace.data.model.ModelComment;

public class PostDetailActivity extends AppCompatActivity {

    //to get detail of user and post
    String myUid, myEmail, myName, myDp, postId, pLikes, hisDp, hisName;


    //comment variables
    EditText comment_data;
    ImageButton post_comment;

    //post variables
    ImageView uPictureTv, pImageTv;
    TextView uNameTv, uTimeTv, pContentTv, pLikesTv, pCommentsTv;
    Button likebtn;
    LinearLayout profileLayout;

    //firebase variables
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    RecyclerView recyclerView;
    List<ModelComment> commentList;
    AdapterComments adapterComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //get id of post using intent
        Intent intent = getIntent();
        postId = intent.getStringExtra("postid");

        //init views
        comment_data = findViewById(R.id.comment_data);
        post_comment = findViewById(R.id.post_comment);
        uPictureTv = findViewById(R.id.uPictureTv);
        pImageTv = findViewById(R.id.pImageTv);
        uNameTv = findViewById(R.id.uNameTv);
        uTimeTv = findViewById(R.id.uTimeTv);
        pContentTv = findViewById(R.id.pContentTv);
        pLikesTv = findViewById(R.id.pLikesTv);
        pCommentsTv = findViewById(R.id.pCommentsTv);
        likebtn = findViewById(R.id.likebtn);
        profileLayout = findViewById(R.id.profileLayout);
        recyclerView = findViewById(R.id.recycler_view_comment);

        loadPostInfo();

        loadComments();

        post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });
    }

    private void loadComments() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        commentList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comment");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                commentList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelComment modelComment = ds.getValue(ModelComment.class);
                    commentList.add(modelComment);
                    adapterComments = new AdapterComments(getApplicationContext(), commentList);
                    recyclerView.setAdapter(adapterComments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void postComment() {
        String comment = comment_data.getText().toString().trim();
        if(TextUtils.isEmpty(comment)){
            Toast.makeText(this, "Cannot Post Empty comment", Toast.LENGTH_SHORT).show();
            return;
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        myUid = mAuth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comment");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cId", timeStamp);
        hashMap.put("comment", comment);
        hashMap.put("uid", myUid);
        hashMap.put("time", timeStamp);

        ref.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PostDetailActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                        comment_data.setText("");
                        updateCommentCount();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        comment_data.setText("");
                    }
                });
    }

    boolean mProcessComment = false;
    private void updateCommentCount() {
        mProcessComment = true;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mProcessComment){
                    String comments = ""+ snapshot.child("pComments").getValue();
                    int newCommentVal = Integer.parseInt(comments) + 1;
                    ref.child("pComments").setValue(""+newCommentVal);
                    mProcessComment = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPostInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = ref.orderByChild("ptime").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String pcontent= ""+ds.child("pcontent").getValue();
                    pLikes = ""+ds.child("pLikes").getValue();
                    String pTimeStamp = ""+ds.child("ptime").getValue();
                    String pImage = ""+ds.child("pimage").getValue();
                    String uid = ""+ds.child("uid").getValue();
                    String pcomments = ""+ds.child("pComments").getValue();

                    Calendar calendar  = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

                    //set data
                    pContentTv.setText(pcontent);
                    uTimeTv.setText(pTime);
                    if(pcomments.equals("null")){
                        pcomments = "0";
                    }
                    if(pLikes == null){
                        pLikes = "0";
                    }
                    pLikesTv.setText(pLikes+ " Likes");
                    pCommentsTv.setText(pcomments+ " Comments");

                    if(pImage.equals("noImage")){
                       pImageTv.setVisibility(View.GONE);
                    }else{
                        try{
                            Picasso.get().load(pImage).into(pImageTv);

                        }catch (Exception e){

                        }
                    }

                    DatabaseReference Users_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    Users_Ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                uNameTv.setText(dataSnapshot.child("username").getValue(String.class));
                                String url = dataSnapshot.child("profileImage").getValue().toString();
                                String url2 = url.replaceAll("gs://petspace-2c47c.appspot.com/", "");
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                StorageReference storageReference = firebaseStorage.getReference();
                                StorageReference reference = storageReference.child(url2);
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(getApplicationContext())
                                                .load(uri)
                                                .into(uPictureTv);
                                    }
                                });
                            }
                            else
                            {

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}