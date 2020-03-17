package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appleto.findteacher.R;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFeesFragment extends Fragment implements OnClickListener {

    private Context context;

    TextView tvDate;
    LinearLayout llOtpView;
    EditText edtCode1, edtCode2, edtCode3, edtCode4, tvTotalAmt;
    Button btnAddFees, btnViewPaid, btnSubmit;

    private Storage storage;
    private String P_Id = "";

    public AddFeesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_fees, container, false);

        storage = new Storage(context);

        tvDate = view.findViewById(R.id.tv_fees_date);
        tvTotalAmt = view.findViewById(R.id.tv_fees_total_amt);

        tvDate.setText(storage.getEnrollmentDate());
        tvTotalAmt.setText(storage.getTotalRemaining());

        llOtpView = view.findViewById(R.id.ll_otp_view);
        edtCode1 = view.findViewById(R.id.code_one);
        edtCode2 = view.findViewById(R.id.code_two);
        edtCode3 = view.findViewById(R.id.code_three);
        edtCode4 = view.findViewById(R.id.code_four);

        btnAddFees = view.findViewById(R.id.btn_add_fees);
        btnViewPaid = view.findViewById(R.id.btn_view_paid);
        btnSubmit = view.findViewById(R.id.btn_fees_submit);

        llOtpView.setVisibility(View.GONE);
        btnAddFees.setOnClickListener(this);
        btnViewPaid.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == btnAddFees) {
//            sendPaymentRequest(tvTotalAmt.getText().toString().substring(2, tvTotalAmt.length()));
            sendPaymentRequest(tvTotalAmt.getText().toString().trim());

        } else if (view == btnViewPaid) {
            loadFragment(new FeesDetailsFragment());

        } else if (view == btnSubmit) {
            String otp = edtCode1.getText().toString() + edtCode2.getText().toString() + edtCode3.getText().toString() + edtCode4.getText().toString();
            verifyPaymentRequest(otp);
        }
    }

    private void sendPaymentRequest(String fees) {
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
                        showError(response.get("message").getAsString());
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        llOtpView.setVisibility(View.GONE);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void verifyPaymentRequest(String otp) {
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
                        if (response.get("success").getAsInt() == 1) {
                            llOtpView.setVisibility(View.GONE);
                            P_Id = String.valueOf(response.get("P_ID").getAsInt());
                        } else {
                            llOtpView.setVisibility(View.VISIBLE);
                        }
                        showError(response.get("message").getAsString());
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        llOtpView.setVisibility(View.VISIBLE);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }


    private void showError(String message) {
        Snackbar snackbar = Snackbar.make(btnAddFees, message, Snackbar.LENGTH_LONG);
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
