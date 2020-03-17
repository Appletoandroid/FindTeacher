package com.appleto.findteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class StudentDetailsResponse implements Parcelable {

    private List<Details> details;
    private Integer success;
    private String message;
    public final static Parcelable.Creator<StudentDetailsResponse> CREATOR = new Creator<StudentDetailsResponse>() {

        @SuppressWarnings({"unchecked"})
        public StudentDetailsResponse createFromParcel(Parcel in) {
            return new StudentDetailsResponse(in);
        }

        public StudentDetailsResponse[] newArray(int size) {
            return (new StudentDetailsResponse[size]);
        }
    };

    protected StudentDetailsResponse(Parcel in) {
        in.readList(this.details, (StudentDetailsResponse.Details.class.getClassLoader()));
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StudentDetailsResponse() {
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
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

        private String sID;
        private String name;
        private String mobile;
        private String email;
        private String city;
        private String state;
        private String age;
        private String gender;
        private String teacherId;
        private String teacherRequestStatus;
        private String enrollmentDate;
        private String totalSessions;
        private String totalFees;
        private String totalPaid;
        private String totalRemaining;
        private String lastVisit;
        private String uID;
        private String profilePic;
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

        public Details() {
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