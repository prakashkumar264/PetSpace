package edu.neu.madcourse.petspace.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.petspace.PostDetailActivity;
import edu.neu.madcourse.petspace.R;
import edu.neu.madcourse.petspace.data.model.ModelComment;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.myHolder> {

    Context context;
    List<ModelComment> commentList;

    public AdapterComments(Context context, List<ModelComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, viewGroup, false);

        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        String uid = commentList.get(i).getUid();
        String comment = commentList.get(i).getComment();
        String cid = commentList.get(i).getcId();
        String time = commentList.get(i).getTime();

        Calendar calendar  = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(time));
        String newTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        myHolder.timeTv.setText(newTime);
        myHolder.commentTv.setText(comment);

        //set user image and name
        DatabaseReference Users_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        Users_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    myHolder.nameTv.setText(dataSnapshot.child("username").getValue(String.class));
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
                                    .into(myHolder.avatarIv);
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

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class myHolder extends RecyclerView.ViewHolder{

        ImageView avatarIv;
        TextView nameTv,commentTv,timeTv;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            avatarIv = itemView.findViewById(R.id.avatarIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            commentTv = itemView.findViewById(R.id.commentTv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }
}
