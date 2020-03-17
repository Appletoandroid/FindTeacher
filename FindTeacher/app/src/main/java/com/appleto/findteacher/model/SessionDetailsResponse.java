package com.appleto.findteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SessionDetailsResponse implements Parcelable {

    private Details details;
    private Integer success;
    private String message;
    public final static Parcelable.Creator<SessionDetailsResponse> CREATOR = new Creator<SessionDetailsResponse>() {

        @SuppressWarnings({"unchecked"})
        public SessionDetailsResponse createFromParcel(Parcel in) {
            return new SessionDetailsResponse(in);
        }

        public SessionDetailsResponse[] newArray(int size) {
            return (new SessionDetailsResponse[size]);
        }
    };

    protected SessionDetailsResponse(Parcel in) {
        this.details = ((Details) in.readValue((Details.class.getClassLoader())));
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SessionDetailsResponse() {
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
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
        dest.writeValue(details);
        dest.writeValue(success);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }

    public static class Details implements Parcelable {

        private String sessionID;
        private String title;
        private String description;
        public final static Parcelable.Creator<Details> CREATOR = new Creator<Details>() {

            @SuppressWarnings({"unchecked"})
            public Details createFromParcel(Parcel in) {
                return new Details(in);
            }

            public Details[] newArray(int size) {
                return (new Details[size]);
            }
        };

        protected Details(Parcel in) {
            this.sessionID = ((String) in.readValue((String.class.getClassLoader())));
            this.title = ((String) in.readValue((String.class.getClassLoader())));
            this.description = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Details() {
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

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(sessionID);
            dest.writeValue(title);
            dest.writeValue(description);
        }

        public int describeContents() {
            return 0;
        }

    }
}