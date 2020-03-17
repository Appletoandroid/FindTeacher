package com.appleto.findteacher.fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFeesInfoFragment extends Fragment implements OnClickListener {

    private Context context;

    TextView tvDate, tvTotalFees, tvNoOfSession;
    Button btnAddFees, btnViewPaid;

    private Storage storage;
    private String P_Id = "";

    public StudentFeesInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_fees_info, container, false);

        storage = new Storage(context);

        tvDate = view.findViewById(R.id.tv_enrollment_date);
        tvTotalFees = view.findViewById(R.id.tv_total_fees);
        tvNoOfSession = view.findViewById(R.id.tv_no_of_session);

        btnAddFees = view.findViewById(R.id.btn_add_fees);
        btnViewPaid = view.findViewById(R.id.btn_view_paid);

        tvDate.setText(storage.getEnrollmentDate());
        tvTotalFees.setText(storage.getTotalFees());
        tvNoOfSession.setText(storage.getTotalSession());

        btnAddFees.setOnClickListener(this);
        btnViewPaid.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == btnAddFees) {
//            loadFragment(new AddFeesFragment());
            showAddFeesPopup();
        } else if (view == btnViewPaid) {
            loadFragment(new FeesDetailsFragment());
        }
    }

    private void showAddFeesPopup() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_add_fees);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvDate = dialog.findViewById(R.id.tv_fees_date);
        EditText tvTotalAmt = dialog.findViewById(R.id.tv_fees_total_amt);

        tvDate.setText(storage.getEnrollmentDate());
        tvTotalAmt.setText(storage.getTotalRemaining());

        LinearLayout llOtpView = dialog.findViewById(R.id.ll_otp_view);
        EditText edtCode1 = dialog.findViewById(R.id.code_one);
        EditText edtCode2 = dialog.findViewById(R.id.code_two);
        EditText edtCode3 = dialog.findViewById(R.id.code_three);
        EditText edtCode4 = dialog.findViewById(R.id.code_four);

        Button btnAddFees = dialog.findViewById(R.id.btn_add_fees);
        Button btnViewPaid = dialog.findViewById(R.id.btn_view_paid);
        Button btnSubmit = dialog.findViewById(R.id.btn_fees_submit);

        edtCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    edtCode2.requestFocus();
                }
            }
        });
        edtCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    edtCode3.requestFocus();
                }
            }
        });
        edtCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    edtCode4.requestFocus();
                }
            }
        });

        btnAddFees.setOnClickListener(v -> {
            if(!tvTotalAmt.getText().toString().trim().equalsIgnoreCase("")){
                double total = Double.parseDouble(tvTotalAmt.getText().toString().trim());
                double remaining = Double.parseDouble(storage.getTotalRemaining());
                if(total > 0 && total <= remaining){
                    sendPaymentRequest(tvTotalAmt.getText().toString().trim(), llOtpView);
                } else {
                    showError("Cannot add more amount than remaining amount",llOtpView);
                }
            }
        });

        btnViewPaid.setOnClickListener(v -> {
            dialog.dismiss();
            loadFragment(new FeesDetailsFragment());
        });

        btnSubmit.setOnClickListener(v -> {
            String otp = edtCode1.getText().toString() + edtCode2.getText().toString() + edtCode3.getText().toString() + edtCode4.getText().toString();
            verifyPaymentRequest(otp, llOtpView, dialog);
        });

        llOtpView.setVisibility(View.GONE);

        dialog.show();
    }

    private void sendPaymentRequest(String fees, LinearLayout llOtpView) {
        Utils.progress_show(context);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .paymentInitiate(storage.getStudentId(), fees)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        if (response.get("success").getAsInt() == 1) {
                            llOtpView.setVisibility(View.VISIBLE);
                            P_Id = String.valueOf(response.get("P_ID").getAsInt());
                        } else {
                            llOtpView.setVisibility(View.GONE);
                        }
                        showError(response.get("message").getAsString(), llOtpView);
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        llOtpView.setVisibility(View.GONE);
                        showError(getResources().getString(R.string.internet_problem), llOtpView);
                    }
                });
    }

    private void verifyPaymentRequest(String otp, LinearLayout llOtpView, Dialog dialog) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .paymentVerify(P_Id, otp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        showError(response.get("message").getAsString(), llOtpView);
                        if (response.get("success").getAsInt() == 1) {
                            dialog.dismiss();
//                            P_Id = String.valueOf(response.get("P_ID").getAsInt());
                        } else {
                            llOtpView.setVisibility(View.VISIBLE);
                        }
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        llOtpView.setVisibility(View.VISIBLE);
                        showError(getResources().getString(R.string.internet_problem), llOtpView);
                    }
                });
    }

    private void showError(String message, LinearLayout llOtpView) {
        Snackbar snackbar = Snackbar.make(llOtpView, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }
}
