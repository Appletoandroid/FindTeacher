package com.appleto.findteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityByStateListResponse implements Parcelable {

    @SerializedName("details")
    @Expose
    private List<Detail> details = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<CityByStateListResponse> CREATOR = new Creator<CityByStateListResponse>() {

        @SuppressWarnings({"unchecked"})
        public CityByStateListResponse createFromParcel(Parcel in) {
            return new CityByStateListResponse(in);
        }

        public CityByStateListResponse[] newArray(int size) {
            return (new CityByStateListResponse[size]);
        }
    };

    protected CityByStateListResponse(Parcel in) {
        in.readList(this.details, (Detail.class.getClassLoader()));
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CityByStateListResponse() {
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

        @SerializedName("C_ID")
        @Expose
        private String cID;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("State_Name")
        @Expose
        private String stateName;
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
            this.cID = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.stateName = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Detail() {
        }

        public String getCID() {
            return cID;
        }

        public void setCID(String cID) {
            this.cID = cID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(cID);
            dest.writeValue(name);
            dest.writeValue(stateName);
        }

        public int describeContents() {
            return 0;
        }

    }
}
