package com.example.project.FragmentRecommend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import cn.jzvd.Jzvd;


public class FragmentRecommend extends Fragment {
    private RecyclerView recyclerView;
    private List<videos> list;
    private  myadapter myadapter;
    private PagerSnapHelper snapHelper;
    private LinearLayoutManager manager;
    private int currentPosition;
    private Button refresh;
    public FragmentRecommend(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recommend,container,false);


        recyclerView=view.findViewById(R.id.recyclerview);
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        refresh = view.findViewById(R.id.refresh);
        list=new ArrayList<videos>();
        initData();
         manager=new LinearLayoutManager(getContext());
       // manager.setOrientation(LinearLayoutManager.VERTICAL);

        myadapter = new myadapter(list,getContext());

        recyclerView.setAdapter(myadapter);
        recyclerView.setLayoutManager(manager);

        addListener();
//        getData(null);

        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }



    private void addListener() {

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(null);
                recyclerView.scrollToPosition(0);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        View view = snapHelper.findSnapView(manager);
                        if(view == null){
                            return;
                        }

                        //当前固定后的item position
                        int position = recyclerView.getChildAdapterPosition(view);
                        if (position != currentPosition) {
                            //如果当前position 和 上一次固定后的position 相同, 说明是同一个, 只不过滑动了一点点, 然后又释放了
                            Jzvd.releaseAllVideos();
                            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                            if (viewHolder != null && viewHolder instanceof com.example.project.FragmentRecommend.myadapter.MyViewHolder) {
//                                ((com.example.project.FragmentRecommend.myadapter.MyViewHolder) viewHolder).videoView.startVideo();
                            }


//                            ((com.example.project.FragmentRecommend.myadapter.MyViewHolder) viewHolder).videoView.startVideo();
                        }
                            currentPosition = position;
                            break;

                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }

            }
        });
    }


    private void initData(){
        getData(null);
//        list.add("Video1");
//        list.add("Video2");
//        list.add("Video3");
//        list.add("Video4");
//        list.add("Video5");
//        list.add("Video6");
//        list.add("Video7");
//        list.add("Video8");
//        list.add("Video9");

    }

    private videoGetResponse baseGetMessagesFromRemote (String studentId,String token){
        String urlStr;
        if(studentId != null){
            urlStr = String.format("https://api-android-camp.bytedance.com/zju/invoke/video?student_id=%s",studentId);
        }
        else{
            urlStr = String.format("https://api-android-camp.bytedance.com/zju/invoke/video");
        }
        videoGetResponse result = null;
        try{
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("token",token);
            if(conn.getResponseCode() == 200){
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                result = new Gson().fromJson(reader,new TypeToken<videoGetResponse>(){}.getType());
                reader.close();
                in.close();
            }
            else{
//                Toast.makeText(this,"获取失败",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(this,"网络异常"+e.toString(),Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private void getData(String studentId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                videoGetResponse text = baseGetMessagesFromRemote(studentId, "WkpVLWJ5dGVkYW5jZS1hbmRyb2lk");
                if (text != null && text.success == true && !text.feeds.isEmpty()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            myadapter.setData(text.feeds);
                            myadapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }



}
