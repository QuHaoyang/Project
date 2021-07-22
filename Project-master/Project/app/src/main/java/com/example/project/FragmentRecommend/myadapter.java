package com.example.project.FragmentRecommend;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.VideoView;
import android.net.Uri;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.SurfaceActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.net.URL;
import java.util.List;

public class myadapter extends RecyclerView.Adapter <myadapter.MyViewHolder> {
    List<videos> list;
    Context context;

    public myadapter(List<videos> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setData(List<videos> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public myadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

 /*   public int getVideoTime(String video){
        MediaMetadataRetriever media = new MediaMetadataRetriever();

        media.setDataSource(video);
        String duration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int time = Integer.parseInt(duration);
        return time/1000;
    }*/


    @Override
    public void onBindViewHolder(@NonNull myadapter.MyViewHolder holder, int position) {
        videos video = list.get(position);

        holder.time.setText(video.getUpdatedAt()+"");
        holder.author.setText(video.getUser_name());
     //   holder.cover.setImageURI(Uri.parse( video.getImageUrl()));
     //   holder.videoView.setVideoURI(Uri.parse( video.getVideoUrl()));
        holder.videoView.setUp(video.getVideoUrl(),null);
        holder.videoView.posterImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Glide.with(context).asDrawable().load(video.getImageUrl()).into(holder.videoView.posterImageView);
        holder.videoView.startVideo();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public     TextView author;
        public     TextView time;
        public     JzvdStd videoView;
     //   public     ImageView cover;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.author);
            time = itemView.findViewById(R.id.time);
          //  cover = itemView.findViewById(R.id.cover);
            videoView = itemView.findViewById(R.id.video );

        }

    }

}