package com.example.project.FragmentMine;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.MyVideo;
import com.example.project.R;
import com.example.project.SurfaceActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FragmentMine extends Fragment {
//    public ImageView head;
//    public ImageView circle_head;

    private static final int REQUEST_CODE_COVER_IMAGE = 101;
    private static final String COVER_IMAGE_TYPE = "image/*";
    private Uri coverImageUri;
    private SimpleDraweeView coverSD;

    private final String SP = "user";
    private String mFileName = null;
    public static EditText user_name;


    public FragmentMine(String filename){
        mFileName = filename;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        Button btn1 = view.findViewById(R.id.button1);
        Drawable drawable1=getResources().getDrawable(R.drawable.user);
        drawable1.setBounds(0,0,100,100);
        drawable1.setAlpha(100);
        btn1.setCompoundDrawables(drawable1,null,null,null);

        Button btn2 = view.findViewById(R.id.button2);
        Drawable drawable2=getResources().getDrawable(R.drawable.like);
        drawable2.setBounds(0,0,100,100);
        drawable2.setAlpha(100);
        btn2.setCompoundDrawables(drawable2,null,null,null);

        Button btn3 = view.findViewById(R.id.button3);
        Drawable drawable3=getResources().getDrawable(R.drawable.hot);
        drawable3.setBounds(0,0,100,100);
        drawable3.setAlpha(100);
        btn3.setCompoundDrawables(drawable3,null,null,null);

        Button btn4 = view.findViewById(R.id.button4);
        Drawable drawable4=getResources().getDrawable(R.drawable.friends);
        drawable4.setBounds(0,0,100,100);
        drawable4.setAlpha(100);
        btn4.setCompoundDrawables(drawable4,null,null,null);

        Button btn5 = view.findViewById(R.id.button5);
        Drawable drawable5=getResources().getDrawable(R.drawable.advise);
        drawable5.setBounds(0,0,100,100);
        drawable5.setAlpha(100);
        btn5.setCompoundDrawables(drawable5,null,null,null);

        Button btn6 = view.findViewById(R.id.button6);
        Drawable drawable6=getResources().getDrawable(R.drawable.about);
        drawable6.setBounds(0,0,100,100);
        drawable6.setAlpha(100);
        btn6.setCompoundDrawables(drawable6,null,null,null);


        Button btn8 = view.findViewById(R.id.button8);
        Drawable drawable8=getResources().getDrawable(R.drawable.setting);
        drawable8.setBounds(0,0,100,100);
        drawable8.setAlpha(100);
        btn8.setCompoundDrawables(drawable8,null,null,null);



        coverSD = view.findViewById(R.id.sd_cover);
        TextView username_change = view.findViewById(R.id.username_change);
        user_name = view.findViewById(R.id.username);
        readFromFile();



        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){

                Intent intent = new Intent( getActivity(),MyVideo.class);
                startActivity( intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){

                Toast.makeText(getActivity(),"该功能全速开发中，敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){

                Toast.makeText(getActivity(),"该功能全速开发中，敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){

                Toast.makeText(getActivity(),"该功能全速开发中，敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                Toast.makeText(getActivity(),"该功能全速开发中，敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });

        btn6.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                Toast.makeText(getActivity(),"该功能全速开发中，敬请期待！",Toast.LENGTH_SHORT).show();

            }
        });

        btn8.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){

                Toast.makeText(getActivity(),"该功能全速开发中，敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });










        username_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_name.isEnabled()){
                    user_name.setEnabled(false);
                    username_change.setText("修改");
                    SharedPreferences sp = getActivity().getSharedPreferences(SP, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", user_name.getText().toString());
                    editor.apply();
                }
                else{
                    user_name.setEnabled(true);
                    user_name.requestFocus();
                    username_change.setText("提交");
                }
            }
        });

        coverSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(REQUEST_CODE_COVER_IMAGE, COVER_IMAGE_TYPE, "选择图片");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_COVER_IMAGE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverImageUri = data.getData();
                coverSD.setImageURI(coverImageUri);
            }
            saveToFile(readDataFromUri(coverImageUri));
        }
    }

    private void readFromFile() {
        File file = new File(mFileName);
        SharedPreferences sp = getActivity().getSharedPreferences(SP, Context.MODE_PRIVATE);
        String value = sp.getString("username", "username");
        user_name.setText(value);
        if(!file.exists()){
            return;
        }
        else{
            coverImageUri = Uri.fromFile(file);
            coverSD.setImageURI(coverImageUri);
        }
    }

    private void saveToFile(final byte[] content) {
        // hello world
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建文件
                File file = new File(mFileName);
                if (!file.exists()) {
                    try {
                        boolean isSuccess = file.createNewFile();
                        if (!isSuccess) {
                            throw new IOException("create exception error.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(content);
                }  catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != outputStream) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private byte[] readDataFromUri(Uri uri) {
        byte[] data = null;
        try {
            InputStream is = getActivity().getContentResolver().openInputStream(uri);
            data = Util.inputStream2bytes(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


}
