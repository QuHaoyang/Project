package com.example.project.FragmentUpload;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.project.FragmentMine.FragmentMine;
import com.example.project.FragmentRecommend.myadapter;
import com.example.project.FragmentRecommend.videos;
import com.example.project.MainActivity;
import com.example.project.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.project.FragmentMine.Util;
import com.example.project.SurfaceActivity;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


public class FragmentUpload extends Fragment {
    public FragmentUpload(){

    }

    private Uri videoUri = null;
    private Uri coverUri = null;
    private static final int REQUEST_CODE_VIDEO = 101;
    private static final int REQUEST_CODE_COVER = 102;
    private static final int GET_IMAGE_BY_CAMERA = 103;
    private static final int GET_VIDEO_BY_CAMERA = 104;
    private static final int PERMISSION_IMAGE_BY_CAMERA = 105;
    private static final int PERMISSION_VIDEO_BY_CAMERA = 106;
    private String takeImagePath;
    private String takeVideoPath;

    File coverSaved;
    File videoSaved;
    Button selectVideo;
    Button selectCover;
    Button getVideo;
    Button getCover;
    Button upload;
    ImageView cover;
    Bitmap coverBitmap = null;
    VideoView video;
    SeekBar pos;
    TextView loadingText;
    float videoPos;
    private IApi api;
    private MediaMetadataRetriever mMetadataRetriever;
    private boolean fromVideo = false;
    public boolean loading = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_upload,container,false);
        selectVideo = view.findViewById(R.id.selectVideo);
        selectCover = view.findViewById(R.id.selectCover);
        getVideo = view.findViewById(R.id.getVideo);
        getCover = view.findViewById(R.id.getCover);
        upload = view.findViewById(R.id.upload);
        cover = view.findViewById(R.id.previewCover);
        video = view.findViewById(R.id.previewVideo);
        pos = view.findViewById(R.id.videoPosition);
        loadingText = view.findViewById(R.id.loadingText);
        initNetwork();


        selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loading == true){
                    Toast.makeText(getActivity(),"正在上传请稍等",Toast.LENGTH_SHORT).show();
                    return;
                }
                selectVideo();
            }
        });
        selectCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loading == true){
                    Toast.makeText(getActivity(),"正在上传请稍等",Toast.LENGTH_SHORT).show();
                    return;
                }
                SelectCover(REQUEST_CODE_COVER,"image/*","选择图片");
            }
        });
        getVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loading == true){
                    Toast.makeText(getActivity(),"正在上传请稍等",Toast.LENGTH_SHORT).show();
                    return;
                }
                record();
            }
        });
        getCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loading == true){
                    Toast.makeText(getActivity(),"正在上传请稍等",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    takePhoto();
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loading == true){
                    Toast.makeText(getActivity(),"正在上传请稍等",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(videoUri == null){
                    Toast.makeText(getActivity(),"请选择视频",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(coverUri == null && coverBitmap == null){
                    Toast.makeText(getActivity(),"请选择封面",Toast.LENGTH_SHORT).show();
                    return;
                }
                loading = true;
                pos.setEnabled(false);
                video.pause();
                loadingText.setText("正在上传");
                if(fromVideo){
                    getOutputMediaCoverPath();
                    saveBitmapAsPng();
                    saveImageToGallery(getActivity());
                }
                submit();
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loading == true){
                    Toast.makeText(getActivity(),"正在上传请稍等",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(videoUri != null){
                    if(video.isPlaying()){
                        video.pause();
                    }
                    else{
                        video.start();
                    }
                }
            }
        });
        video.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(loading == true){
                    Toast.makeText(getActivity(),"正在上传请稍等",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(videoUri != null){
                    video.pause();
                    video.seekTo(0);
                    video.start();
                }
                return false;
            }
        });
        pos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(videoUri != null){
                    float a =seekBar.getProgress();
                    float b =seekBar.getMax();
                    videoPos = a/b;
                    getCoverFromVideo();
                    cover.setImageBitmap(coverBitmap);
                    video.pause();
                }
                else{
                    Toast.makeText(getActivity(),"请先选择视频",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }


    private void initNetwork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-android-camp.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IApi.class);
    }

    private void submit(){
        byte[] coverImageData = readDataFromUri(coverUri);
        byte[] coverVideoData = readDataFromUri(videoUri);

        MultipartBody.Part coverPart = MultipartBody.Part.createFormData("cover_image",
                "cover.png",
                RequestBody.create(MediaType.parse("multipart/form-data"),
                        coverImageData));

        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video",
                "video.mp4",
                RequestBody.create(MediaType.parse("multipart/form-data"),
                        coverVideoData));

        Call<UploadResponse> message = api.submitMessage("3190101095", FragmentMine.user_name.getText().toString()
                ,"",coverPart,videoPart
                ," WkpVLWJ5dGVkYW5jZS1hbmRyb2lk");
        message.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(),"提交成功",Toast.LENGTH_SHORT).show();
                    loadingText.setText("上传成功");
                }
                else{
                    Toast.makeText(getActivity(),"提交失败",Toast.LENGTH_SHORT).show();
                    loadingText.setText("上传失败");
                }
                loading = false;
                pos.setEnabled(true);
                return;
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(),"提交失败",Toast.LENGTH_SHORT).show();
                loadingText.setText("上传失败");
                loading=false;
                pos.setEnabled(true);
                return;
            }
        });
    }

    private void selectVideo() {
        if (android.os.Build.BRAND.equals("Huawei")) {
            Intent intentPic = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentPic, REQUEST_CODE_VIDEO);
        }
        if (android.os.Build.BRAND.equals("Xiaomi")) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
            startActivityForResult(Intent.createChooser(intent, "选择要导入的视频"), REQUEST_CODE_VIDEO);
        } else {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT < 19) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
            } else {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("video/*");
            }
            startActivityForResult(Intent.createChooser(intent, "选择要导入的视频"), REQUEST_CODE_VIDEO);
        }
    }

    private void SelectCover(int requestCode, String type, String title) {
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
        if (REQUEST_CODE_VIDEO == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                videoUri = data.getData();
                video.setVideoURI(videoUri);
//                video.start();
            }
        }
        else if (REQUEST_CODE_COVER == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverUri = data.getData();
                Glide.with(this).load(coverUri).into(cover);
                fromVideo = false;
            }
        }
        else if((requestCode == GET_IMAGE_BY_CAMERA) && (resultCode == RESULT_OK))
        {

            int targetWidth = cover.getWidth();
            int targetHeight = cover.getHeight();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(takeImagePath, options);
            int photoWidth = options.outWidth;
            int photoHeight = options.outHeight;

            int scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;
            coverBitmap = BitmapFactory.decodeFile(takeImagePath, options);
            cover.setImageBitmap(coverBitmap);
            saveImageToGallery(getActivity());
            fromVideo = false;

        }
        else if((requestCode == GET_VIDEO_BY_CAMERA) && (resultCode == RESULT_OK)){
            video.setVideoURI(videoUri);
            video.start();
        }
        if(videoUri != null){
            video.start();
        }
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
    public void getCoverFromVideo(){
        mMetadataRetriever = new MediaMetadataRetriever();
        //mPath本地视频地址
        String path = videoUri.getPath();
        mMetadataRetriever.setDataSource(getActivity(),videoUri);
        //这个时候就可以通过mMetadataRetriever来获取这个视频的一些视频信息了
        String duration = mMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);

        //下面这行代码才是关键，用来获取当前视频某一时刻(毫秒*1000)的一帧
        coverBitmap = mMetadataRetriever.getFrameAtTime((long)(videoPos*Integer.valueOf(duration)*1000), MediaMetadataRetriever.OPTION_CLOSEST);
        fromVideo = true;
        //这时就可以获取这个视频的某一帧的bitmap了
    }

    private byte[] getBitFromBitmap(){
        int bytes = coverBitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        coverBitmap.copyPixelsToBuffer(buffer);

        byte[] data = buffer.array();
        return data;
    }

    public void saveBitmapAsPng() {
        try {
            FileOutputStream out = new FileOutputStream(coverSaved);
            coverBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getOutputMediaCoverPath() {
        File mediaStorageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "IMG_" + timeStamp + ".png");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        try {
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    mediaFile.getAbsolutePath(), "IMG_" + timeStamp + ".png", null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        coverSaved = mediaFile;
        coverUri = Uri.fromFile(mediaFile);
        return mediaFile.getAbsolutePath();
    }

    private String getOutputMediaVideoPath() {
        File mediaStorageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "VIDEO_" + timeStamp + ".mp4");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        try {
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    mediaFile.getAbsolutePath(), "VIDEO_" + timeStamp + ".mp4", null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        videoSaved = mediaFile;
        videoUri = Uri.fromFile(mediaFile);
        return mediaFile.getAbsolutePath();
    }

    public void saveImageToGallery(Context context) {
        //保存封面图到相册
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "IMG_" + timeStamp + ".png";
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    coverSaved.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] paths = new String[]{coverSaved.getAbsolutePath()};
            MediaScannerConnection.scanFile(context, paths, null, null);
        } else {
            final Intent intent;
            if (coverSaved.isDirectory()) {
                intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
            } else {
                intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(coverSaved));
            }
            context.sendBroadcast(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == PERMISSION_IMAGE_BY_CAMERA) {
                boolean hasPermission = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        hasPermission = false;
                        break;
                    }
                }
                if (hasPermission) {
                    takePhotoUsePathHasPermission();
                } else {
                    Toast.makeText(getActivity(), "权限获取失败", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == PERMISSION_VIDEO_BY_CAMERA){
                boolean hasPermission = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        hasPermission = false;
                        break;
                    }
                }
                if (hasPermission) {
                    recordVideo();
                } else {
                    Toast.makeText(getActivity(), "权限获取失败", Toast.LENGTH_SHORT).show();
                }
            }
    }

    private void requestCameraAndSDCardPermission() {
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        if (hasCameraPermission) {
            takePhotoUsePathHasPermission();
        } else {
            String[] permissions = new String[]{Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(getActivity(), permissions, PERMISSION_IMAGE_BY_CAMERA);
        }
    }

    public void takePhoto() {
        requestCameraAndSDCardPermission();
    }

    private void takePhotoUsePathHasPermission() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeImagePath = getOutputMediaCoverPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PathUtils.getUriForFile(getActivity(), takeImagePath));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, GET_IMAGE_BY_CAMERA);
        }
    }

    public void record(){
        requestVideoPermission();
    }

    public void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoPath = getOutputMediaVideoPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,PathUtils.getUriForFile(getActivity(),takeVideoPath));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent,GET_VIDEO_BY_CAMERA);
        }
    }

    private void requestVideoPermission() {
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasAudioPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if (hasCameraPermission && hasAudioPermission) {
            recordVideo();
        } else {
            List<String> permission = new ArrayList<String>();
            if (!hasCameraPermission) {
                permission.add(Manifest.permission.CAMERA);
            }
            if (!hasAudioPermission) {
                permission.add(Manifest.permission.RECORD_AUDIO);
            }
            ActivityCompat.requestPermissions(getActivity(), permission.toArray(new String[permission.size()]), PERMISSION_VIDEO_BY_CAMERA);
        }

    }
}
