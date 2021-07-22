package com.example.project.FragmentUpload;

import com.google.gson.annotations.SerializedName;
import com.example.project.FragmentRecommend.*;

public class UploadResponse {
    @SerializedName("result")
    public videos videos;
    @SerializedName("success")
    public boolean success;

    @SerializedName("error")
    public String error;
}
