package com.appleto.findteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentListByTIdResponse implements Parcelable {

    @SerializedName("details")
    @Expose
    private List<Detail> details = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<StudentListByTIdResponse> CREATOR = new Creator<StudentListByTIdResponse>() {

        @SuppressWarnings({"unchecked"})
        public StudentListByTIdResponse createFromParcel(Parcel in) {
            return new StudentListByTIdResponse(in);
        }

        public StudentListByTIdResponse[] newArray(int size) {
            return (new StudentListByTIdResponse[size]);
        }
    };

    protected StudentListByTIdResponse(Parcel in) {
        in.readList(this.details, (Detail.class.getClassLoader()));
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StudentListByTIdResponse() {
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

        @SerializedName("S_ID")
        @Expose
        private String sID;
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
        @SerializedName("Teacher_Id")
        @Expose
        private String teacherId;
        @SerializedName("Teacher_Request_Status")
        @Expose
        private String teacherRequestStatus;
        @SerializedName("Enrollment_Date")
        @Expose
        private String enrollmentDate;
        @SerializedName("Total_Sessions")
        @Expose
        private String totalSessions;
        @SerializedName("Total_Fees")
        @Expose
        private String totalFees;
        @SerializedName("Total_Paid")
        @Expose
        private String totalPaid;
        @SerializedName("Total_Remaining")
        @Expose
        private String totalRemaining;
        @SerializedName("Last_Visit")
        @Expose
        private String lastVisit;
        @SerializedName("U_ID")
        @Expose
        private String uID;
        @SerializedName("Profile_Pic")
        @Expose
        private String profilePic;
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
            this.sID = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.mobile = ((String) in.readValue((String.class.getClassLoader())));
            this.email = ((String) in.readValue((String.class.getClassLoader())));
            this.city = ((String) in.readValue((String.class.getClassLoader())));
            this.state = ((String) in.readValue((String.class.getClassLoader())));
            this.age = ((String) in.readValue((String.class.getClassLoader())));
            this.gender = ((String) in.readValue((String.class.getClassLoader())));
            this.teacherId = ((String) in.readValue((String.class.getClassLoader())));
            this.teacherRequestStatus = ((String) in.readValue((String.class.getClassLoader())));
            this.enrollmentDate = ((String) in.readValue((String.class.getClassLoader())));
            this.totalSessions = ((String) in.readValue((String.class.getClassLoader())));
            this.totalFees = ((String) in.readValue((String.class.getClassLoader())));
            this.totalPaid = ((String) in.readValue((String.class.getClassLoader())));
            this.totalRemaining = ((String) in.readValue((String.class.getClassLoader())));
            this.lastVisit = ((String) in.readValue((String.class.getClassLoader())));
            this.uID = ((String) in.readValue((String.class.getClassLoader())));
            this.profilePic = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Detail() {
        }

        public String getSID() {
            return sID;
        }

        public void setSID(String sID) {
            this.sID = sID;
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

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public String getTeacherRequestStatus() {
            return teacherRequestStatus;
        }

        public void setTeacherRequestStatus(String teacherRequestStatus) {
            this.teacherRequestStatus = teacherRequestStatus;
        }

        public String getEnrollmentDate() {
            return enrollmentDate;
        }

        public void setEnrollmentDate(String enrollmentDate) {
            this.enrollmentDate = enrollmentDate;
        }

        public String getTotalSessions() {
            return totalSessions;
        }

        public void setTotalSessions(String totalSessions) {
            this.totalSessions = totalSessions;
        }

        public String getTotalFees() {
            return totalFees;
        }

        public void setTotalFees(String totalFees) {
            this.totalFees = totalFees;
        }

        public String getTotalPaid() {
            return totalPaid;
        }

        public void setTotalPaid(String totalPaid) {
            this.totalPaid = totalPaid;
        }

        public String getTotalRemaining() {
            return totalRemaining;
        }

        public void setTotalRemaining(String totalRemaining) {
            this.totalRemaining = totalRemaining;
        }

        public String getLastVisit() {
            return lastVisit;
        }

        public void setLastVisit(String lastVisit) {
            this.lastVisit = lastVisit;
        }

        public String getUID() {
            return uID;
        }

        public void setUID(String uID) {
            this.uID = uID;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(sID);
            dest.writeValue(name);
            dest.writeValue(mobile);
            dest.writeValue(email);
            dest.writeValue(city);
            dest.writeValue(state);
            dest.writeValue(age);
            dest.writeValue(gender);
            dest.writeValue(teacherId);
            dest.writeValue(teacherRequestStatus);
            dest.writeValue(enrollmentDate);
            dest.writeValue(totalSessions);
            dest.writeValue(totalFees);
            dest.writeValue(totalPaid);
            dest.writeValue(totalRemaining);
            dest.writeValue(lastVisit);
            dest.writeValue(uID);
            dest.writeValue(profilePic);
        }

        public int describeContents() {
            return 0;
        }

    }
}
