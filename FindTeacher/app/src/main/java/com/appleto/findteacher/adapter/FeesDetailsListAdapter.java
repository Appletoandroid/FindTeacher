package com.appleto.findteacher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.model.StudentPaymentHistoryResponse;

import java.util.List;

public class FeesDetailsListAdapter extends RecyclerView.Adapter<FeesDetailsListAdapter.ViewHolder> {

    private Context context;
    List<StudentPaymentHistoryResponse.Detail> paymentList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void itemClick(StudentPaymentHistoryResponse.Detail data);
    }

    public FeesDetailsListAdapter(Context context, List<StudentPaymentHistoryResponse.Detail> details,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.paymentList = details;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View c = LayoutInflater.from(context).inflate(R.layout.raw_fees_details_list_item, parent, false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        int position = holder.getAdapterPosition();
        holder.tvSrNo.setText(String.valueOf(position + 1));
        holder.tvDate.setText(paymentList.get(position).getPaymentDate().substring(0, 10));
        holder.tvAmount.setText(paymentList.get(position).getAmount());
        if (paymentList.get(position).getPaymentStatus().equalsIgnoreCase("OTP Sent")) {
            holder.btnVerifyOtp.setText("Verify");
            holder.btnVerifyOtp.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
        } else {
            holder.btnVerifyOtp.setText("Completed");
            holder.btnVerifyOtp.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.btnVerifyOtp.setOnClickListener(v -> {
            if(paymentList.get(position).getPaymentStatus().equalsIgnoreCase("OTP Sent")){
                onItemClickListener.itemClick(paymentList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSrNo, tvDate, tvAmount;
        TextView btnVerifyOtp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.raw_tv_pay_srNo);
            tvDate = itemView.findViewById(R.id.raw_tv_pay_date);
            tvAmount = itemView.findViewById(R.id.raw_tv_pay_amount);
            btnVerifyOtp = itemView.findViewById(R.id.btn_verify_otp);
        }
    }
}
