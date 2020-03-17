package com.appleto.findteacher.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appleto.findteacher.R;
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

public class ChangePasswordActivity extends AppCompatActivity {

    private Context context;

    TextView tvUname;
    EditText edtOldPwd, edtConfPwd;
    Button btnSubmit;

    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        context = ChangePasswordActivity.this;
        storage = new Storage(context);

        tvUname = findViewById(R.id.tv_username);
        edtOldPwd = findViewById(R.id.edt_old_password);
        edtOldPwd.setTag("Old Password");
        edtConfPwd = findViewById(R.id.edt_confirm_password);
        edtConfPwd.setTag("Confirm Password");
        btnSubmit = findViewById(R.id.btn_submit);

        tvUname.setText(storage.getUserEmail());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = edtOldPwd.getText().toString().trim();
                String confPwd = edtConfPwd.getText().toString().trim();
                if (Validator.validateInputField(new EditText[]{edtOldPwd, edtConfPwd}, context)) {
                    if(password.equals(confPwd)){
                        updatePassword(password, confPwd);
                    } else {
                        Utils.alertDialog(context, "Password not matched. Enter valid password");
                    }
                }
            }
        });

    }

    private void updatePassword(String password, String confPwd) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .changePassword(storage.getUserId(), password, confPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        if(response.get("success").getAsInt() == 1){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.navigateTo(ChangePasswordActivity.this, LoginActivity.class);
                                }
                            }, 1800);
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

    private void showError(String message){
        Snackbar snackbar = Snackbar.make(btnSubmit, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();
    }
}
