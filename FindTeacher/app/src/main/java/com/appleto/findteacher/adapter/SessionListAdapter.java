package com.appleto.findteacher.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.model.StudentSessionListResponse;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.ViewHolder> {

    private Context context;
    List<StudentSessionListResponse.Detail> sessionList;
    private String from = "";
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void itemClick(StudentSessionListResponse.Detail detail);
    }

    public SessionListAdapter(Context context, List<StudentSessionListResponse.Detail> details,String from) {
        this.context = context;
        this.sessionList = details;
        this.from = from;
    }

    public SessionListAdapter(Context context, List<StudentSessionListResponse.Detail> details,String from,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.sessionList = details;
        this.from = from;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View c = LayoutInflater.from(context).inflate(R.layout.raw_session_list_item, parent, false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tvDesc.setText(sessionList.get(position).getDescription());
        holder.tvName.setText(sessionList.get(position).getTitle());

        if (sessionList.get(position).getSessionStatus().equalsIgnoreCase("Complete")) {
            holder.llOtpView.setVisibility(View.GONE);
            holder.ivStatus.getDrawable().setColorFilter(context.getResources().getColor(R.color.light_green), PorterDuff.Mode.SRC_ATOP);
        } else if (sessionList.get(position).getSessionStatus().equalsIgnoreCase("Pending")) {
            holder.llOtpView.setVisibility(View.GONE);
            holder.ivStatus.getDrawable().setColorFilter(context.getResources().getColor(R.color.dim_red), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.llOtpView.setVisibility(View.VISIBLE);
            holder.ivStatus.getDrawable().setColorFilter(context.getResources().getColor(R.color.dim_white), PorterDuff.Mode.SRC_ATOP);
        }

        holder.code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    holder.code2.requestFocus();
                }
            }
        });
        holder.code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    holder.code3.requestFocus();
                }
            }
        });
        holder.code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    holder.code4.requestFocus();
                }
            }
        });

        holder.itemView.setOnClickListener(view -> {
            if(from.equalsIgnoreCase("Student")) {
                onItemClickListener.itemClick(sessionList.get(position));
            } else {
                if (sessionList.get(position).getSessionStatus().equalsIgnoreCase("Pending")) {
                    showConfirmAlert(sessionList.get(position).getSTID(), holder);
                }
            }
        });

        holder.btnSubmit.setOnClickListener(view -> {
            String otp = holder.code1.getText().toString() + holder.code2.getText().toString() + holder.code3.getText().toString() + holder.code4.getText().toString();
            verifySesssionRequest(sessionList.get(position).getSTID(), otp, holder);
        });
    }

    private void showConfirmAlert(String stid, ViewHolder holder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("Send Verification OTP?");
        alertDialog.setPositiveButton("Send", (dialog, which) -> {
            holder.llOtpView.setVisibility(View.VISIBLE);
            sendSesssionRequest(stid, holder);
            dialog.dismiss();
        });
        alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivStatus;
        TextView tvName, tvDesc, tvMessage, tvStatus;
        EditText code1, code2, code3, code4;
        LinearLayout llOtpView;
        Button btnSubmit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStatus = itemView.findViewById(R.id.raw_iv_session_status);
            tvStatus = itemView.findViewById(R.id.raw_tv_session_status);
            tvName = itemView.findViewById(R.id.raw_tv_session_name);
            tvDesc = itemView.findViewById(R.id.raw_tv_session_desc);
            tvMessage = itemView.findViewById(R.id.raw_tv_wating_msg);

            code1 = itemView.findViewById(R.id.raw_code_one);
            code2 = itemView.findViewById(R.id.raw_code_two);
            code3 = itemView.findViewById(R.id.raw_code_three);
            code4 = itemView.findViewById(R.id.raw_code_four);
            llOtpView = itemView.findViewById(R.id.raw_ll_otp_view);
            btnSubmit = itemView.findViewById(R.id.raw_btn_submit_otp);
        }
    }

    private void sendSesssionRequest(String sessionId, final ViewHolder holder) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .sessionCompleteInitiate(sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        if (response.get("success").getAsInt() == 1) {
                            holder.llOtpView.setVisibility(View.VISIBLE);
                            holder.tvMessage.setText("Pending");
                            holder.tvMessage.setVisibility(View.VISIBLE);
                            holder.tvStatus.setVisibility(View.VISIBLE);
                            holder.ivStatus.getDrawable().setColorFilter(context.getResources().getColor(R.color.dim_red), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            holder.llOtpView.setVisibility(View.GONE);
                            holder.tvMessage.setVisibility(View.GONE);
                            holder.tvStatus.setVisibility(View.GONE);
                        }
                        showError(response.get("message").getAsString(), holder.ivStatus);
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        holder.llOtpView.setVisibility(View.GONE);
                        holder.tvMessage.setVisibility(View.GONE);
                        holder.tvStatus.setVisibility(View.GONE);
                        showError(context.getResources().getString(R.string.internet_problem), holder.ivStatus);
                    }
                });
    }

    private void verifySesssionRequest(String st_id, String code, final ViewHolder holder) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .sessionCompleteVerify(st_id, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        if (response.get("success").getAsInt() == 1) {
                            holder.tvMessage.setText("Complete");
                            holder.tvMessage.setTextColor(context.getResources().getColor(R.color.light_green));
                            holder.tvStatus.setVisibility(View.GONE);
                            holder.ivStatus.getDrawable().setColorFilter(context.getResources().getColor(R.color.light_green), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            holder.tvMessage.setText("Pending");
                            holder.tvStatus.setVisibility(View.VISIBLE);
                            holder.tvMessage.setTextColor(context.getResources().getColor(R.color.dim_red));
                        }
                        showError(response.get("message").getAsString(), holder.ivStatus);
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        holder.tvMessage.setText("Pending");
                        holder.tvStatus.setVisibility(View.VISIBLE);
                        holder.tvMessage.setTextColor(context.getResources().getColor(R.color.dim_red));
                        showError(context.getResources().getString(R.string.internet_problem), holder.ivStatus);
                    }
                });
    }

    private void showError(String message, View imageView) {
        Snackbar snackbar = Snackbar.make(imageView, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();
    }
}
