package com.appleto.findteacher.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appleto.findteacher.R;
import com.appleto.findteacher.model.LoginResponse;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.appleto.findteacher.utils.Validator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements OnClickListener{

    private Context context;

    EditText edtUserName, edtPassowrd;
    Button btnLogin, btnSignUp;
    TextView tvForgetPwd;

    private Storage storage;
    private String clickOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;
        storage = new Storage(context);

        edtUserName = findViewById(R.id.edt_user_name);
        edtUserName.setTag("Email");
        edtPassowrd = findViewById(R.id.edt_password);
        edtPassowrd.setTag("Passowrd");

        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signup);
        tvForgetPwd = findViewById(R.id.tv_forget_pwd);

        edtUserName.setText("sagarmaher2@gmail.com");
        edtPassowrd.setText("123456");

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin){
            clickOn="Login";
            String email = edtUserName.getText().toString().trim();
            String password = edtPassowrd.getText().toString().trim();
            if (Validator.validateInputField(new EditText[]{edtUserName, edtPassowrd}, context)) {
                if(Utils.isInternetAvaiable(context)){
                    userLogin(email, password);
                } else {
                    Utils.alertDialog(context, getResources().getString(R.string.internet_problem));
                }
            }
        } else if(view == btnSignUp){
            Utils.navigateTo(LoginActivity.this, SignUpActivity.class);

        } else if(view == tvForgetPwd){
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_forget_password);

            int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.18);
            int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.95);

            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setCanceledOnTouchOutside(true);

            final EditText edtEmail = dialog.findViewById(R.id.dialog_edt_email);
            if(storage.getUserEmail() != null){
                edtEmail.setText(storage.getUserEmail());
            }

            Button btnSend = dialog.findViewById(R.id.dialog_btn_send);
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String strEmail = edtEmail.getText().toString().trim();
                    if(isValidEmail(strEmail)) {
                        clickOn="Forget";
                        forgetPassword(strEmail, dialog);
                    } else {
                        Utils.showToast(context, "Invalid Email Id");
                    }
                }
            });
            Button btnCancel = dialog.findViewById(R.id.dialog_btn_cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void forgetPassword(String strEmail, Dialog dialog) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .forgetPassword(strEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        if(response.get("success").getAsInt() == 1){
                            dialog.dismiss();
                        }
                        showError(response.get("message").getAsString());
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void userLogin(String email, String password) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .userLogin(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        if (response.get("success").getAsInt() == 1) {
                            storage.saveLogInState(true);
                            JsonObject object = response.getAsJsonObject("details");
                            storage.saveUserType(object.get("U_Type").getAsString());
                            storage.saveUserId(object.get("U_Id").getAsString());
                            storage.saveUserName(object.get("Name").getAsString());
                            storage.saveUserEmail(object.get("Email").getAsString());
                            storage.saveUserContact(object.get("Mobile").getAsString());
                            storage.saveUserProfile(object.get("Profile_Pic").getAsString());
                            storage.saveUserGender(object.get("Gender").getAsString());
                            storage.saveUserAge(object.get("Age").getAsString());

                            if(object.has("S_ID")){
                                storage.saveStudentId(object.get("S_ID").getAsString());
                                storage.saveRequestStatus(object.get("Teacher_Request_Status").getAsString());
                                storage.saveStudentCity(object.get("City").getAsString());
                                storage.saveStudentState(object.get("State").getAsString());
                                storage.saveTeacherId(object.get("Teacher_Id").getAsString());
                                storage.saveEnrollmentDate(object.get("Enrollment_Date").getAsString());
                                storage.saveTotalSession(object.get("Total_Sessions").getAsString());
                                storage.saveTotalFees(object.get("Total_Fees").getAsString());
                                storage.saveTotalPaid(object.get("Total_Paid ").getAsString());
                                storage.saveTotalRemaining(object.get("Total_Remaining").getAsString());

                            } else if(object.has("T_ID")) {
                                storage.saveTeacherId(object.get("T_ID").getAsString());
                            }

                            Utils.navigateTo(LoginActivity.this, MainActivity.class);
                        } else {
                            showError(getResources().getString(R.string.login_failed));
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
        Snackbar snackbar;
        if(clickOn.equals("Login")) {
            snackbar = Snackbar
                    .make(btnLogin, message, Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String email = edtUserName.getText().toString().trim();
                            String password = edtPassowrd.getText().toString().trim();
                            userLogin(email, password);
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
        } else {
            snackbar = Snackbar.make(btnLogin, message, Snackbar.LENGTH_LONG);
        }
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();

        snackbar.show();
    }
}
