package com.example.project.FragmentRecommend;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.util.Date;

public class videos {
    @SerializedName("_id")
    private String Id;
    @SerializedName("student_id")
    private String studentId;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("video_url")
    private String video_url;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("image_w")
    private int imageW;
    @SerializedName("image_h")
    private int imageH;
    @SerializedName("createdAt")
    private Date createdAt;
    @SerializedName("updatedAt")
    private Date updatedAt;

    public void setId(String Id) {
        this.Id = Id;
    }
    public String getId() {
        return Id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getStudentId() {
        return studentId;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_name() {
        return user_name;
    }

    public void setVideoUrl(String video_url) {
        this.video_url = video_url;
    }
    public String getVideoUrl() {
        return video_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }
    public String getImageUrl() {
        return image_url;
    }

    public void setImageW(int imageW) {
        this.imageW = imageW;
    }
    public int getImageW() {
        return imageW;
    }

    public void setImageH(int imageH) {
        this.imageH = imageH;
    }
    public int getImageH() {
        return imageH;
    }

    public void setCreatedAt(Date createdat) {
        this.createdAt = createdat;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAtt(Date updatedat) {
        this.updatedAt = updatedat;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
}
