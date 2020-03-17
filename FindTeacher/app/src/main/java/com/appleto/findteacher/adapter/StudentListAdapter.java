package com.appleto.findteacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.listener.OnItemClickListener;
import com.appleto.findteacher.model.StudentListByTIdResponse;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    List<StudentListByTIdResponse.Detail> studentList;

    public StudentListAdapter(Context context, List<StudentListByTIdResponse.Detail> details, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.studentList = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View c = LayoutInflater.from(context).inflate(R.layout.raw_my_student_item, parent, false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        final int position = holder.getAdapterPosition();
        if (studentList.get(position).getProfilePic().equals("")) {
            holder.ivProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_user));
        } else {
            Glide.with(context).asBitmap().load("http://zamb.codingvisions.in/assets/images/"+studentList.get(position).getProfilePic()).into(holder.ivProfile);
        }
        holder.tvName.setText(studentList.get(position).getName());
        holder.tvCity.setText(studentList.get(position).getCity());
        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position, "View");
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivProfile;
        TextView tvName, tvCity;
        Button btnViewMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.raw_iv_student_profile);
            tvName = itemView.findViewById(R.id.raw_tv_student_name);
            tvCity = itemView.findViewById(R.id.raw_tv_student_city);
            btnViewMore = itemView.findViewById(R.id.raw_btn_view_more);
        }
    }
}
