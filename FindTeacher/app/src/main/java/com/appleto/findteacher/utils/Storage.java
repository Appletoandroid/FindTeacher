package com.appleto.findteacher.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    private static final boolean IS_APP_FIRST_TIME = false;
    private static final boolean LOGIN_STATUS = false;

    private static final String  STUDENT_ID = null;
    private static final String  TEACHER_ID = null;
    private static final String  USER_TYPE = null;
    private static final String  REQUEST_STATUS = null;

    private static final String  ENROLLMENT_DATE = null;
    private static final String  TOTAL_PAID = null;
    private static final String  TOTAL_SESSION = null;
    private static final String  TOTAL_FEES = null;
    private static final String  TOTAL_REMAINING = null;

    private static final String  LOGIN_U_ID = null;
    private static final String  LOGIN_U_NAME = null;
    private static final String  LOGIN_U_EMAIL = null;
    private static final String  LOGIN_U_PROFILE = null;
    private static final String  LOGIN_U_MOBILE = null;
    private static final String  LOGIN_U_AGE = null;
    private static final String  LOGIN_U_GENDER = null;
    private static final String  LOGIN_U_PASSWORD = null;
    private static final String  STUDENT_CITY = null;
    private static final String  STUDENT_STATE = null;
    private Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public SharedPreferences.Editor getPreferencesEditor() {
        return getsharedPreferences().edit();
    }

    private SharedPreferences getsharedPreferences() {
        return context.getSharedPreferences("FindTeacher", Context.MODE_PRIVATE);
    }

    public void saveIsFirstTime(boolean p) {
        getPreferencesEditor().putBoolean("first", p).commit();
    }

    public boolean getIsFirstTime() {
        return getsharedPreferences().getBoolean("first", IS_APP_FIRST_TIME);
    }

    public void saveLogInState(boolean p) {
        getPreferencesEditor().putBoolean("logged_in", p).commit();
    }

    public boolean getLogInState() {
        return getsharedPreferences().getBoolean("logged_in", LOGIN_STATUS);
    }

    public void saveStudentId(String id) {
        getPreferencesEditor().putString("s_id", id).commit();
    }

    public String getStudentId() {
        return getsharedPreferences().getString("s_id", STUDENT_ID);
    }

    public void saveTeacherId(String id) {
        getPreferencesEditor().putString("t_id", id).commit();
    }

    public String getTeacherId() {
        return getsharedPreferences().getString("t_id", TEACHER_ID);
    }

    public void saveUserId(String id) {
        getPreferencesEditor().putString("u_id", id).commit();
    }

    public String getUserId() {
        return getsharedPreferences().getString("u_id", LOGIN_U_ID);
    }

    public void saveUserName(String name) {
        getPreferencesEditor().putString("u_name", name).commit();
    }

    public String getUserName() {
        return getsharedPreferences().getString("u_name", LOGIN_U_NAME);
    }

    public void saveUserEmail(String name) {
        getPreferencesEditor().putString("u_email", name).commit();
    }

    public String getUserEmail() {
        return getsharedPreferences().getString("u_email", LOGIN_U_EMAIL);
    }

    public void saveUserProfile(String profile) {
        getPreferencesEditor().putString("u_profile", profile).commit();
    }

    public String getUserProfile() {
        return getsharedPreferences().getString("u_profile", LOGIN_U_PROFILE);
    }

    public void saveUserContact(String contact) {
        getPreferencesEditor().putString("u_contact", contact).commit();
    }

    public String getUserContact() {
        return getsharedPreferences().getString("u_contact", LOGIN_U_MOBILE);
    }

    public void saveUserAge(String age) {
        getPreferencesEditor().putString("u_age", age).commit();
    }

    public String getUserAge() {
        return getsharedPreferences().getString("u_age", LOGIN_U_AGE);
    }

    public void saveUserGender(String gender) {
        getPreferencesEditor().putString("u_gender", gender).commit();
    }

    public String getUserGender() {
        return getsharedPreferences().getString("u_gender", LOGIN_U_GENDER);
    }

    public void saveStudentCity(String city) {
        getPreferencesEditor().putString("std_city", city).commit();
    }

    public String getStudentCity() {
        return getsharedPreferences().getString("std_city", STUDENT_CITY);
    }

    public void saveStudentState(String state) {
        getPreferencesEditor().putString("std_state", state).commit();
    }

    public String getStudentState() {
        return getsharedPreferences().getString("std_state", STUDENT_STATE);
    }

    public void saveUserType(String type) {
        getPreferencesEditor().putString("user_type", type).commit();
    }

    public String getUserType() {
        return getsharedPreferences().getString("user_type", USER_TYPE);
    }

    public void saveRequestStatus(String status) {
        getPreferencesEditor().putString("rq_status", status).commit();
    }

    public String getRequestStatus() {
        return getsharedPreferences().getString("rq_status", REQUEST_STATUS);
    }

    public void saveEnrollmentDate(String date) {
        getPreferencesEditor().putString("en_date", date).commit();
    }

    public String getEnrollmentDate() {
        return getsharedPreferences().getString("en_date", ENROLLMENT_DATE);
    }

    public void saveTotalSession(String session) {
        getPreferencesEditor().putString("t_session", session).commit();
    }

    public String getTotalSession() {
        return getsharedPreferences().getString("t_session", TOTAL_SESSION);
    }

    public void saveTotalFees(String fees) {
        getPreferencesEditor().putString("total_fees", fees).commit();
    }

    public String getTotalFees() {
        return getsharedPreferences().getString("total_fees", TOTAL_FEES);
    }

    public void saveTotalPaid(String paid) {
        getPreferencesEditor().putString("total_paid", paid).commit();
    }

    public String getTotalPaid() {
        return getsharedPreferences().getString("total_paid", TOTAL_PAID);
    }

    public void saveTotalRemaining(String remain) {
        getPreferencesEditor().putString("total_remain", remain).commit();
    }

    public String getTotalRemaining() {
        return getsharedPreferences().getString("total_remain", TOTAL_REMAINING);
    }
}
