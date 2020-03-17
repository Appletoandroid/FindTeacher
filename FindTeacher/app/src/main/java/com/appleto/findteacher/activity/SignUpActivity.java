package com.appleto.findteacher.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appleto.findteacher.R;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.appleto.findteacher.utils.Validator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class SignUpActivity extends AppCompatActivity implements OnClickListener{

    private Context context;

    EditText edtFullName, edtPassowrd, edtEmail, edtMobile, edtAge;
    RadioGroup rgGender;
    Button btnSignUp;
    TextView tvLogin;
    String gender="Male";

    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context = SignUpActivity.this;
        storage = new Storage(context);

        edtFullName = findViewById(R.id.edt_full_name);
        edtFullName.setTag("Full name");
        edtEmail = findViewById(R.id.edt_email_id);
        edtEmail.setTag("Email");
        edtPassowrd = findViewById(R.id.edt_password);
        edtPassowrd.setTag("Password");
        edtMobile = findViewById(R.id.edt_mobile_no);
        edtMobile.setTag("Mobile");
        edtAge = findViewById(R.id.edt_age);
        edtAge.setTag("Age");

        rgGender = findViewById(R.id.rg_gender);
        ((RadioButton)rgGender.getChildAt(0)).setChecked(true);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radiobtn = findViewById(selectedId);
                gender = radiobtn.getText().toString();
            }
        });

        btnSignUp = findViewById(R.id.btn_sign_up);
        tvLogin = findViewById(R.id.tv_goto_login);

        btnSignUp.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnSignUp){
            String name = edtFullName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String contact = edtMobile.getText().toString().trim();
            String password = edtPassowrd.getText().toString().trim();
            String age = edtAge.getText().toString().trim();

            if (Validator.validateInputField(new EditText[]{edtFullName, edtEmail, edtPassowrd, edtMobile, edtAge}, context)) {
                if(Utils.isInternetAvaiable(context)){
                    registerUser(name, email, contact, password, age);
                } else {
                    Utils.alertDialog(context, getResources().getString(R.string.internet_problem));
                }
            }
        } else if(view == tvLogin){
            Utils.navigateTo(SignUpActivity.this, LoginActivity.class);
        }
    }

    private void registerUser(String name, final String email, String contact, String password, String age) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .addStudent(name, email, password, contact, gender, age)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ResponseBody body) {
                        try {
                            String s = body.string();
                            Object obj = new JsonParser().parse(s);
                            if (obj instanceof JsonObject) {
                                JsonObject jsonObject = (JsonObject) obj;
                                if (jsonObject.get("success").getAsInt() == 1) {
                                    JsonObject object = jsonObject.getAsJsonObject("details");
                                    storage.saveLogInState(true);
                                    storage.saveUserName(object.get("Name").getAsString());
                                    storage.saveUserContact(object.get("Mobile").getAsString());
                                    storage.saveUserEmail(object.get("Email").getAsString());
                                    storage.saveUserAge(object.get("Age").getAsString());
                                    storage.saveUserGender(object.get("Gender").getAsString());

                                    storage.saveUserId(object.get("U_ID").getAsString());
                                    storage.saveUserProfile(object.get("Profile_Pic").getAsString());

                                    if(object.has("S_ID")){
                                        storage.saveStudentId(object.get("S_ID").getAsString());
                                        storage.saveRequestStatus(object.get("Teacher_Request_Status").getAsString());
                                        storage.saveTeacherId(object.get("Teacher_Id").getAsString());
                                        storage.saveStudentCity(object.get("City").getAsString());
                                        storage.saveStudentState(object.get("State").getAsString());
                                        storage.saveEnrollmentDate(object.get("Enrollment_Date").getAsString());
                                        storage.saveTotalSession(object.get("Total_Sessions").getAsString());
                                        storage.saveTotalFees(object.get("Total_Fees").getAsString());
                                        storage.saveTotalPaid(object.get("Total_Paid").getAsString());
                                        storage.saveTotalRemaining(object.get("Total_Remaining").getAsString());
                                        storage.saveUserType("Student");
                                    } else if(object.has("T_ID")) {
                                        storage.saveTeacherId(object.get("T_ID").getAsString());
                                    }
                                    Utils.navigateTo(SignUpActivity.this, MainActivity.class);
                                } else {
                                    showError(getResources().getString(R.string.registar_failed));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void showError(String message){
        Snackbar snackbar = Snackbar.make(btnSignUp, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();
    }
}
