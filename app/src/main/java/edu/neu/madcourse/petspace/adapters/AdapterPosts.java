package edu.neu.madcourse.petspace.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.petspace.R;
import edu.neu.madcourse.petspace.data.model.ModelPost;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {

    Context context;
    List<ModelPost> postList;

    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
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

        Calendar calendar  = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(ptimestamp));
        String ptime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.pContentTv.setText(pcontent);
        holder.uTimeTv.setText(ptime);
        holder.uNameTv.setText(uid);

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
                Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show();
            }
        });
        holder.commentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

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
