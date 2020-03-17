package com.appleto.findteacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.listener.OnItemClickListener;
import com.appleto.findteacher.model.TeacherListResponse;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder>{

    private Context context;
    OnItemClickListener listener;
    List<TeacherListResponse.Details> teacherList;

    public DirectoryAdapter(Context context, List<TeacherListResponse.Details> teacherList, OnItemClickListener listener){
        this.context = context;
        this.listener = listener;
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View c = LayoutInflater.from(context).inflate(R.layout.raw_directory_item, parent, false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        int position = holder.getAdapterPosition();
        holder.tvName.setText(teacherList.get(position).getName());
        holder.tvLocation.setText(teacherList.get(position).getCity());

        if(teacherList.get(position).getProfilePic().equals("")){
            holder.ivProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_user));
        } else {
            Glide.with(context).asBitmap().load(teacherList.get(position).getProfilePic()).into(holder.ivProfile);
        }

        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position, "View");
            }
        });

        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position, "Select");
            }
        });
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivProfile;
        Button btnViewMore, btnSelect;
        TextView tvName, tvLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.raw_iv_teacher_profile);
            tvName = itemView.findViewById(R.id.raw_tv_teacher_name);
            tvLocation = itemView.findViewById(R.id.raw_tv_teacher_location);

            btnViewMore = itemView.findViewById(R.id.raw_btn_view_details);
            btnSelect = itemView.findViewById(R.id.raw_btn_select);
        }
    }
}
