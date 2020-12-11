package edu.neu.madcourse.petspace.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.petspace.MainActivity;
import edu.neu.madcourse.petspace.PostDetailActivity;
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
        String pcomments = postList.get(i).getpComments();

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
        if(pcomments == null){
            pcomments = "0";
        }
        holder.pCommentTV.setText(pcomments + " Comments");
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
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("postid", pId);
                context.startActivity(intent);
            }
        });
        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.pImageTv.getDrawable();
                if(bitmapDrawable == null){
                    shareTextOnly(pcontent, holder.uNameTv.getText().toString());
                }else{
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    shareTextImageOnly(pcontent, bitmap, holder.uNameTv.getText().toString());
                }
            }
        });

    }

    private void shareTextImageOnly(String pcontent, Bitmap pimage, String name) {
        Uri uri = saveImagetoShare(pimage);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        intent.putExtra(Intent.EXTRA_TEXT, name+" Posted on PetSpace : "+pcontent);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/png");
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    private Uri saveImagetoShare(Bitmap pimage) {
        File imageFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try{
            imageFolder.mkdirs();
            File file = new File(imageFolder, "shared_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            pimage.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(context, "edu.neu.madcourse.petspace", file);

        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return  uri;
    }

    private void shareTextOnly(String pcontent, String name) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        intent.putExtra(Intent.EXTRA_TEXT, name+" Posted on PetSpace : "+pcontent);
        context.startActivity(Intent.createChooser(intent, "Share via"));
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
        TextView uNameTv, uTimeTv, pContentTv, pLikesTv, pCommentTV;
        Button likebtn, commentbtn, sharebtn;


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
            pCommentTV = itemView.findViewById(R.id.pCommentsTv);
            sharebtn = itemView.findViewById(R.id.share_post);

        }
    }

}
