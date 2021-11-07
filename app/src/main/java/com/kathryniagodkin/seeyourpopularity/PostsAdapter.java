package com.kathryniagodkin.seeyourpopularity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private ArrayList<FBPost> fbPosts;

    public PostsAdapter(ArrayList<FBPost> fbPosts) {
        this.fbPosts = fbPosts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(fbPosts.get(position).getFull_picture()).into(holder.imageViewPost);
        holder.textViewPost.setText(fbPosts.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return fbPosts.size();
    }

    public void clear() {
        fbPosts.clear();
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewPost;
        TextView textViewPost, textViewNamePage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPost = itemView.findViewById(R.id.post_image);
            textViewPost = itemView.findViewById(R.id.post_text);
            textViewNamePage = itemView.findViewById(R.id.name_page);
        }
    }
}
