package com.appleto.findteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateListResponse implements Parcelable {

    @SerializedName("details")
    @Expose
    private List<Detail> details = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<StateListResponse> CREATOR = new Creator<StateListResponse>() {

        @SuppressWarnings({"unchecked"})
        public StateListResponse createFromParcel(Parcel in) {
            return new StateListResponse(in);
        }

        public StateListResponse[] newArray(int size) {
            return (new StateListResponse[size]);
        }
    };

    protected StateListResponse(Parcel in) {
        in.readList(this.details, (Detail.class.getClassLoader()));
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StateListResponse() {
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

        @SerializedName("ID")
        @Expose
        private String iD;
        @SerializedName("Name")
        @Expose
        private String name;
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
            this.iD = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Detail() {
        }

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(iD);
            dest.writeValue(name);
        }

        public int describeContents() {
            return 0;
        }
    }
}
