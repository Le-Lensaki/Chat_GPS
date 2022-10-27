package com.example.chat_gps2.fragment.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_gps2.ChatActivity;
import com.example.chat_gps2.MainActivity;
import com.example.chat_gps2.R;
import com.example.chat_gps2.fragment.Model.Group;
import com.example.chat_gps2.upLoadImgGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//https://xuanthulab.net/su-dung-recyclerview-trong-lap-trinh-android.html
public class    GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    private ArrayList<Group> groupList;
    private Context contextList;
    private String imgURl;
    public GroupListAdapter(ArrayList<Group> groupList, Context contextList) {
        this.groupList = groupList;
        this.contextList = contextList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View grView =
                inflater.inflate(R.layout.groupitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(grView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Group group = groupList.get(position);
        holder.nameGroup.setText(group.getName());
        holder.memberCount.setText("Join Code: "+group.getJoinCode());
        if (group.getImageURL().equals("default")){
            holder.groupImg.setImageResource(R.drawable.your_group);
        }
        else {
            Glide.with(contextList).load(group.getImageURL()).into(holder.groupImg);
        }
        final View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setCancelable(false);//false là không bấm đc ra bên ngoài
                dialog.setContentView(R.layout.view_holder_gr_dialog);
                ImageView imageView = dialog.findViewById(R.id.grImg);
                if (!group.getImageURL().equals("") && group.getImageURL()!=null)
                Glide.with(dialog.getContext()).load(group.getImageURL()).into(imageView);

                //HIện hộp thoại
                dialog.show();
                //Set 2 sự kiện người dùng bấm button
                Button ok = dialog.findViewById(R.id.yes);
                Button cancer = dialog.findViewById(R.id.no);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
                        databaseReference.child("groupOn").setValue(group.getGroupID());
                        databaseReference.child("nameGroupOn").setValue(group.getName());
                        dialog.cancel();


                    }
                });
                cancer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                return false;
            }
        };

        holder.setOnLongClickListener(onLongClickListener);
        holder.groupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), upLoadImgGroup.class);
                intent.putExtra("Key_1", group.getName());
                intent.putExtra("Key_2", group.getGroupID());
                intent.putExtra("Key_3", group.getImageURL());
                view.getContext().startActivity(intent);
            }
        });

    }




    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView nameGroup;
        public ImageView groupImg;
        public TextView memberCount;

        public ViewHolder(View itemView) {
            super(itemView);
            nameGroup = itemView.findViewById(R.id.username);
            groupImg = itemView.findViewById(R.id.profile_image);
            memberCount = itemView.findViewById(R.id.memberCount);
        }

        public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
            itemView.setOnLongClickListener(onLongClickListener);
        }
    }
}