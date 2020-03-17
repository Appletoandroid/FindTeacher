package com.appleto.findteacher.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appleto.findteacher.R;
import com.appleto.findteacher.utils.Storage;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnrollmentDetailsFragment extends Fragment {

    private Context context;
    private Button btnView;
    private TextView tvDate, tvFees, tvNoOfSession;

    private Storage storage;

    public EnrollmentDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_enrollment_details, container, false);

        storage = new Storage(context);

        tvDate = view.findViewById(R.id.tv_enrollment_date);
        tvFees = view.findViewById(R.id.tv_total_fees);
        tvNoOfSession = view.findViewById(R.id.tv_no_of_session);
        btnView = view.findViewById(R.id.btn_view_enrollment);

        tvDate.setText(storage.getEnrollmentDate());
        tvFees.setText(storage.getTotalFees());
        tvNoOfSession.setText(storage.getTotalSession());

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new FeesDetailsFragment());
            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

}
