package edu.neu.madcourse.petspace.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.petspace.MainActivity;
import edu.neu.madcourse.petspace.ProfileSetupActivity;
import edu.neu.madcourse.petspace.R;
import edu.neu.madcourse.petspace.data.model.ModelPost;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {

    Context context;
    List<ModelPost> postList;

    String myUid;
    private DatabaseReference likesRef;
    private DatabaseReference postsRef;

    boolean mProcessLike = false;

    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        String uid = postList.get(i).getUid();
        String pcontent = postList.get(i).getPcontent();
        String ptimestamp = postList.get(i).getPtime();
        String pimage = postList.get(i).getPimage();
        String  pLikes = postList.get(i).getpLikes();
        String pId = postList.get(i).getPtime();

        Calendar calendar  = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(ptimestamp));
        String ptime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        DatabaseReference Users_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        Users_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    holder.uNameTv.setText(dataSnapshot.child("username").getValue(String.class));
                    String url = dataSnapshot.child("profileImage").getValue().toString();
                    String url2 = url.replaceAll("gs://petspace-2c47c.appspot.com/", "");
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference storageReference = firebaseStorage.getReference();
                    StorageReference reference = storageReference.child(url2);
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context)
                                    .load(uri)
                                    .into(holder.uPictureTv);
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
        holder.pContentTv.setText(pcontent);
        holder.uTimeTv.setText(ptime);
        if(pLikes == null){
            pLikes = "0";
        }
        holder.pLikesTv.setText(pLikes + " Likes");
        setLikes(holder, pId);
        if(pimage.equals("noImage")){
            holder.pImageTv.setVisibility(View.GONE);
        }else{
            try{
                Picasso.get().load(pimage).into(holder.pImageTv);

            }catch (Exception e){

            }
        }


        holder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pLikes = Integer.parseInt(postList.get(i).getpLikes());
                mProcessLike = true;
                final String postIde = postList.get(i).getPtime();
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mProcessLike){
                            if(snapshot.child(postIde).hasChild(myUid)){
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes-1));
                                likesRef.child(postIde).child(myUid).removeValue();
                                mProcessLike = false;
                            }else{
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes+1));
                                likesRef.child(postIde).child(myUid).setValue("Liked");
                                mProcessLike = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.commentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Commented", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setLikes(final MyHolder holder,final String pId) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pId).hasChild(myUid)){
                    holder.likebtn.setText("Liked");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                holder.likebtn.setText("Like");
            }
        });
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{

        ImageView uPictureTv, pImageTv;
        TextView uNameTv, uTimeTv, pContentTv, pLikesTv;
        Button likebtn, commentbtn;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uPictureTv = itemView.findViewById(R.id.uPictureTv);
            pImageTv = itemView.findViewById(R.id.pImageTv);
            uNameTv = itemView.findViewById(R.id.uNameTv);
            uTimeTv = itemView.findViewById(R.id.uTimeTv);
            pContentTv = itemView.findViewById(R.id.pContentTv);
            pLikesTv = itemView.findViewById(R.id.pLikesTv);
            likebtn = itemView.findViewById(R.id.likebtn);
            commentbtn = itemView.findViewById(R.id.commentbtn);

        }
    }

}
