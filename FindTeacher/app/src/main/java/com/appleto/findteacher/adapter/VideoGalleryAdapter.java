package com.appleto.findteacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.activity.YoutubeActivity;
import com.appleto.findteacher.model.VideoListResponse;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ViewHolder>{

    private Context context;
    List<VideoListResponse.Detail> videoList;

    public VideoGalleryAdapter(Context context, List<VideoListResponse.Detail> details){
        this.context = context;
        this.videoList = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View c = LayoutInflater.from(context).inflate(R.layout.raw_video_item, parent, false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Glide.with(context).asBitmap().load(videoList.get(position).getVideoURL()).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.ivVideoThumb);
        Glide.with(context).load(getThumbnailFromVideoUrl(videoList.get(position).getVideoURL())).into(holder.ivVideoThumb);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, YoutubeActivity.class);
                intent.putExtra("video_url", videoList.get(position).getVideoURL());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivVideoThumb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVideoThumb = itemView.findViewById(R.id.raw_iv_video_thumb);
        }
    }

    public static String getThumbnailFromVideoUrl(String videoUrl) {
        return "http://img.youtube.com/vi/"+getYoutubeVideoIdFromUrl(videoUrl) + "/0.jpg";
    }

    public static String getYoutubeVideoIdFromUrl(String inUrl) {
        inUrl = inUrl.replace("&feature=youtu.be", "");
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
