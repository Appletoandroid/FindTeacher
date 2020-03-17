package com.appleto.findteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentSessionListResponse implements Parcelable {

    @SerializedName("details")
    @Expose
    private List<Detail> details = null;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<StudentSessionListResponse> CREATOR = new Creator<StudentSessionListResponse>() {

        @SuppressWarnings({"unchecked"})
        public StudentSessionListResponse createFromParcel(Parcel in) {
            return new StudentSessionListResponse(in);
        }

        public StudentSessionListResponse[] newArray(int size) {
            return (new StudentSessionListResponse[size]);
        }
    };

    protected StudentSessionListResponse(Parcel in) {
        in.readList(this.details, (Detail.class.getClassLoader()));
        this.success = ((int) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StudentSessionListResponse() {
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
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

        @SerializedName("ST_ID")
        @Expose
        private String sTID;
        @SerializedName("Session_ID")
        @Expose
        private String sessionID;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("Session_Status")
        @Expose
        private String sessionStatus;
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
            this.sTID = ((String) in.readValue((String.class.getClassLoader())));
            this.sessionID = ((String) in.readValue((String.class.getClassLoader())));
            this.title = ((String) in.readValue((String.class.getClassLoader())));
            this.description = ((String) in.readValue((String.class.getClassLoader())));
            this.sessionStatus = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Detail() {
        }

        public String getSTID() {
            return sTID;
        }

        public void setSTID(String sTID) {
            this.sTID = sTID;
        }

        public String getSessionID() {
            return sessionID;
        }

        public void setSessionID(String sessionID) {
            this.sessionID = sessionID;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSessionStatus() {
            return sessionStatus;
        }

        public void setSessionStatus(String sessionStatus) {
            this.sessionStatus = sessionStatus;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(sTID);
            dest.writeValue(sessionID);
            dest.writeValue(title);
            dest.writeValue(description);
            dest.writeValue(sessionStatus);
        }

        public int describeContents() {
            return 0;
        }

    }
}