package com.appleto.findteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse implements Parcelable {

    @SerializedName("details")
    @Expose
    private Details details;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {

        @SuppressWarnings({"unchecked"})
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        public LoginResponse[] newArray(int size) {
            return (new LoginResponse[size]);
        }
    };

    protected LoginResponse(Parcel in) {
        this.details = ((Details) in.readValue((Details.class.getClassLoader())));
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LoginResponse() {
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

        @SerializedName("U_Id")
        @Expose
        private String uId;
        @SerializedName("U_Type")
        @Expose
        private String uType;
        @SerializedName("Profile_Pic")
        @Expose
        private String profilePic;
        @SerializedName("T_ID")
        @Expose
        private String tID;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Mobile")
        @Expose
        private String mobile;
        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("City")
        @Expose
        private String city;
        @SerializedName("State")
        @Expose
        private String state;
        @SerializedName("Age")
        @Expose
        private String age;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("Experience")
        @Expose
        private String experience;
        @SerializedName("Last_Visit")
        @Expose
        private String lastVisit;
        @SerializedName("Teacher_Fees")
        @Expose
        private Object teacherFees;
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
            this.uId = ((String) in.readValue((String.class.getClassLoader())));
            this.uType = ((String) in.readValue((String.class.getClassLoader())));
            this.profilePic = ((String) in.readValue((String.class.getClassLoader())));
            this.tID = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.mobile = ((String) in.readValue((String.class.getClassLoader())));
            this.email = ((String) in.readValue((String.class.getClassLoader())));
            this.city = ((String) in.readValue((String.class.getClassLoader())));
            this.state = ((String) in.readValue((String.class.getClassLoader())));
            this.age = ((String) in.readValue((String.class.getClassLoader())));
            this.gender = ((String) in.readValue((String.class.getClassLoader())));
            this.experience = ((String) in.readValue((String.class.getClassLoader())));
            this.lastVisit = ((String) in.readValue((String.class.getClassLoader())));
            this.teacherFees = ((Object) in.readValue((Object.class.getClassLoader())));
        }

        public Details() {
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getUType() {
            return uType;
        }

        public void setUType(String uType) {
            this.uType = uType;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getTID() {
            return tID;
        }

        public void setTID(String tID) {
            this.tID = tID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getLastVisit() {
            return lastVisit;
        }

        public void setLastVisit(String lastVisit) {
            this.lastVisit = lastVisit;
        }

        public Object getTeacherFees() {
            return teacherFees;
        }

        public void setTeacherFees(Object teacherFees) {
            this.teacherFees = teacherFees;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(uId);
            dest.writeValue(uType);
            dest.writeValue(profilePic);
            dest.writeValue(tID);
            dest.writeValue(name);
            dest.writeValue(mobile);
            dest.writeValue(email);
            dest.writeValue(city);
            dest.writeValue(state);
            dest.writeValue(age);
            dest.writeValue(gender);
            dest.writeValue(experience);
            dest.writeValue(lastVisit);
            dest.writeValue(teacherFees);
        }

        public int describeContents() {
            return 0;
        }

    }
}
