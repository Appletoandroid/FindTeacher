package com.appleto.findteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoListResponse implements Parcelable {

    @SerializedName("details")
    @Expose
    private List<Detail> details = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<VideoListResponse> CREATOR = new Creator<VideoListResponse>() {

        @SuppressWarnings({"unchecked"})
        public VideoListResponse createFromParcel(Parcel in) {
            return new VideoListResponse(in);
        }

        public VideoListResponse[] newArray(int size) {
            return (new VideoListResponse[size]);
        }
    };

    protected VideoListResponse(Parcel in) {
        in.readList(this.details, (Detail.class.getClassLoader()));
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public VideoListResponse() {
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(details);
        dest.writeValue(success);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }

    public static class Detail implements Parcelable {

        @SerializedName("V_ID")
        @Expose
        private String vID;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Video_URL")
        @Expose
        private String videoURL;
        public final static Parcelable.Creator<Detail> CREATOR = new Creator<Detail>() {

            @SuppressWarnings({"unchecked"})
            public Detail createFromParcel(Parcel in) {
                return new Detail(in);
            }

            public Detail[] newArray(int size) {
                return (new Detail[size]);
            }
        };

        protected Detail(Parcel in) {
            this.vID = ((String) in.readValue((String.class.getClassLoader())));
            this.title = ((String) in.readValue((String.class.getClassLoader())));
            this.videoURL = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Detail() {
        }

        public String getVID() {
            return vID;
        }

        public void setVID(String vID) {
            this.vID = vID;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public void setVideoURL(String videoURL) {
            this.videoURL = videoURL;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(vID);
            dest.writeValue(title);
            dest.writeValue(videoURL);
        }

        public int describeContents() {
            return 0;
        }

    }

}