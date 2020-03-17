package com.appleto.findteacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.listener.OnItemClickListener;
import com.appleto.findteacher.model.NavigationMenuList;

import java.util.List;


public class NavigationManuAdapter extends RecyclerView.Adapter<NavigationManuAdapter.ViewHolder> {

    Context context;
    List<NavigationMenuList> list;
    OnItemClickListener listener;

    public NavigationManuAdapter(Context context, List<NavigationMenuList> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;

    }

    @NonNull
    @Override
    public NavigationManuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.raw_menu_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {

        final int position = holder.getAdapterPosition();

        holder.tv_menuname.setText(list.get(position).getName());
        holder.iv_menuimg.setImageResource(list.get(position).getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position, "View");
            }
        });
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_menuimg;
        TextView tv_menuname;


        public ViewHolder(View itemview) {
            super(itemview);
            iv_menuimg = itemview.findViewById(R.id.iv_menuimg);
            tv_menuname = itemview.findViewById(R.id.tv_menuname);

        }
    }
}


