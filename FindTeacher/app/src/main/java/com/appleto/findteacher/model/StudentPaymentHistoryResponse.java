package com.appleto.findteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentPaymentHistoryResponse implements Parcelable {

    @SerializedName("details")
    @Expose
    private List<Detail> details = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<StudentPaymentHistoryResponse> CREATOR = new Creator<StudentPaymentHistoryResponse>() {

        @SuppressWarnings({"unchecked"})
        public StudentPaymentHistoryResponse createFromParcel(Parcel in) {
            return new StudentPaymentHistoryResponse(in);
        }

        public StudentPaymentHistoryResponse[] newArray(int size) {
            return (new StudentPaymentHistoryResponse[size]);
        }
    };

    protected StudentPaymentHistoryResponse(Parcel in) {
        in.readList(this.details, (Detail.class.getClassLoader()));
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StudentPaymentHistoryResponse() {
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

        @SerializedName("P_ID")
        @Expose
        private String pID;
        @SerializedName("Payment_Date")
        @Expose
        private String paymentDate;
        @SerializedName("Amount")
        @Expose
        private String amount;
        @SerializedName("Payment_Status")
        @Expose
        private String paymentStatus;
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
            this.pID = ((String) in.readValue((String.class.getClassLoader())));
            this.paymentDate = ((String) in.readValue((String.class.getClassLoader())));
            this.amount = ((String) in.readValue((String.class.getClassLoader())));
            this.paymentStatus = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Detail() {
        }

        public String getPID() {
            return pID;
        }

        public void setPID(String pID) {
            this.pID = pID;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(pID);
            dest.writeValue(paymentDate);
            dest.writeValue(amount);
            dest.writeValue(paymentStatus);
        }

        public int describeContents() {
            return 0;
        }
    }
}