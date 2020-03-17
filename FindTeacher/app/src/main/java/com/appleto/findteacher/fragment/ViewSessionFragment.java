package com.appleto.findteacher.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.model.StudentSessionListResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSessionFragment extends Fragment {

    private Context context;
    TextView tvDesc,tvTitle;
    private StudentSessionListResponse.Detail detail;

    public ViewSessionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_view_session, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            detail = getArguments().getParcelable("Session");
        }

        tvDesc = view.findViewById(R.id.tvDesc);
        tvTitle = view.findViewById(R.id.tvTitle);


        if (detail != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvDesc.setText(Html.fromHtml(detail.getDescription(),Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvDesc.setText(Html.fromHtml(detail.getDescription()));
            }
            tvTitle.setText(detail.getTitle());
        }
    }
}
