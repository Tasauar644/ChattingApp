package com.example.chattingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class userAdapter extends RecyclerView.Adapter<userAdapter.viewholder> {
    MainActivity mainActivity;
    ArrayList<Users> usersArrayList;
    public userAdapter(MainActivity mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity=mainActivity;
        this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public userAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mainActivity).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.viewholder holder, int position) {
        Users users=usersArrayList.get(position);
        holder.userName.setText(users.getUserName());
        holder.userStatus.setText(users.getStatus());
        Picasso.get().load(users.userPicture).into(holder.userImage);



    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView userName,userStatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.userImage);
            userName=itemView.findViewById(R.id.userName);
            userStatus=itemView.findViewById(R.id.userStatus);
        }
    }
}
