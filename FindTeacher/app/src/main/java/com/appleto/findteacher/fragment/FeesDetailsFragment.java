package com.appleto.findteacher.fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appleto.findteacher.R;
import com.appleto.findteacher.adapter.FeesDetailsListAdapter;
import com.appleto.findteacher.model.StudentPaymentHistoryResponse;
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
public class FeesDetailsFragment extends Fragment {

    private Context context;
    RecyclerView rvFeesDetails;
    TextView tvTotal, tvPaid, tvRemain;

    private Storage storage;
    double totalPaid = 0;


    public FeesDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_fees_details, container, false);

        storage = new Storage(context);

        tvTotal = view.findViewById(R.id.tv_total_fees);
        tvPaid = view.findViewById(R.id.tv_total_paid);
        tvRemain = view.findViewById(R.id.tv_total_remain);

        tvPaid.setText(storage.getTotalPaid());
        tvTotal.setText(storage.getTotalFees());
        tvRemain.setText(storage.getTotalRemaining());


        rvFeesDetails = view.findViewById(R.id.rv_fees_details_list);
        rvFeesDetails.setLayoutManager(new LinearLayoutManager(context));

        getStudentPaymentHistory();
        return view;
    }

    private void getStudentPaymentHistory() {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getPaymentsByStudentId(storage.getStudentId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<StudentPaymentHistoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(StudentPaymentHistoryResponse response) {
                        if (response.getSuccess() == 1) {
                            if (response.getDetails().size() > 0) {
                                totalPaid = 0;
                                FeesDetailsListAdapter adapter = new FeesDetailsListAdapter(context, response.getDetails(), data -> customDialog(data));
                                rvFeesDetails.setAdapter(adapter);
                                for (int i = 0; i < response.getDetails().size(); i++) {
                                    totalPaid += Double.parseDouble(response.getDetails().get(i).getAmount());
                                }
                            }
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

    private void customDialog(StudentPaymentHistoryResponse.Detail data) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText codeOne = dialog.findViewById(R.id.code_one);
        EditText codeTwo = dialog.findViewById(R.id.code_two);
        EditText codeThree = dialog.findViewById(R.id.code_three);
        EditText codeFour = dialog.findViewById(R.id.code_four);
        Button btnSubmit = dialog.findViewById(R.id.btn_fees_submit);

        codeOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    codeTwo.requestFocus();
                }
            }
        });
        codeTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    codeThree.requestFocus();
                }
            }
        });
        codeThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    codeFour.requestFocus();
                }
            }
        });

        btnSubmit.setOnClickListener(v -> {
            String otp = codeOne.getText().toString() + codeTwo.getText().toString() + codeThree.getText().toString() + codeFour.getText().toString();
            verifyPaymentRequest(otp, data.getPID());
            dialog.dismiss();
        });


        dialog.show();
    }

    private void verifyPaymentRequest(String otp, String P_Id) {
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
                        Utils.progress_dismiss(context);
                        showError(response.get("message").getAsString());
                        if (response.get("success").getAsInt() == 1) {
                            getStudentPaymentHistory();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void showError(String message) {
        Snackbar snackbar = Snackbar.make(rvFeesDetails, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();
    }

}
