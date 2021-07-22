package com.example.project.FragmentRecommend;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class videoGetResponse {
    @SerializedName("feeds")
    public List<videos> feeds;
    @SerializedName("success")
    public boolean success;
}
