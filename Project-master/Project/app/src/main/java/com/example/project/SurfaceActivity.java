package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import  android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import com.example.project.FragmentMine.*;
import com.example.project.FragmentUpload.*;
import com.example.project.FragmentRecommend.*;

import java.io.File;

public class SurfaceActivity extends AppCompatActivity {
    private FragmentRecommend f1 = null;
    private FragmentUpload f2 = null;
    private FragmentMine f3 = null;
    String mFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFileName = getFilesDir().getAbsolutePath() + File.separator + "cover.png";
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_surface);

        RadioGroup guide = findViewById(R.id.guide);

        RadioButton surface_recommend = findViewById(R.id.surface_recommend);
        Drawable drawable1=getResources().getDrawable(R.drawable.homepage);
        drawable1.setBounds(0,0,100,100);
        drawable1.setAlpha(100);
        surface_recommend.setCompoundDrawables(null,drawable1,null,null);

        RadioButton surface_upload = findViewById(R.id.surface_upload);

        Drawable drawable2=getResources().getDrawable(R.drawable.upload);
        drawable2.setBounds(0,0,100,100);
        drawable2.setAlpha(100);
        surface_upload.setCompoundDrawables(null,drawable2,null,null);

        RadioButton surface_mine = findViewById(R.id.surface_mine);
        Drawable drawable3=getResources().getDrawable(R.drawable.user);
        drawable3.setBounds(0,0,100,100);
        drawable3.setAlpha(100);
        surface_mine.setCompoundDrawables(null,drawable3,null,null);

        init();

        showf1();
        guide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId){
                if(surface_recommend.isChecked()){
                    showf1();
                }
                else if(surface_upload.isChecked()){
                    showf2();
                }
                else if(surface_mine.isChecked()){
                    showf3();
                }
            }
        });
    }

    public void HideAll(FragmentTransaction transaction){
        if(f1 != null){
            transaction.hide(f1);
        }
        if(f2 != null){
            transaction.hide(f2);
        }
        if(f3 != null){
            transaction.hide(f3);
        }
    }

    public void init(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(f1 == null){
            f1 = new FragmentRecommend();
            transaction.add(R.id.surface_fragment,f1);
        }
        if(f2 == null){
            f2 = new FragmentUpload();
            transaction.add(R.id.surface_fragment,f2);
        }
        if(f3 == null){
            f3 = new FragmentMine(mFileName);
            transaction.add(R.id.surface_fragment,f3);
        }
        HideAll(transaction);
        transaction.show(f1);
        transaction.commit();
    }

    public void showf1(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(f1 == null){
            f1 = new FragmentRecommend();
            transaction.add(R.id.surface_fragment,f1);
        }
        HideAll(transaction);
        transaction.show(f1);
        transaction.commit();
    }
    public void showf2(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(f2 == null){
            f2 = new FragmentUpload();
            transaction.add(R.id.surface_fragment,f2);
        }
        HideAll(transaction);
        transaction.show(f2);
        transaction.commit();
    }

    public void showf3(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(f3 == null){
            f3 = new FragmentMine(mFileName);
            transaction.add(R.id.surface_fragment,f3);
        }
        HideAll(transaction);
        transaction.show(f3);
        transaction.commit();
    }

}