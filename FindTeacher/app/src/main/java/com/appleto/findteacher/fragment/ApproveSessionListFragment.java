package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appleto.findteacher.R;
import com.appleto.findteacher.adapter.SessionListAdapter;
import com.appleto.findteacher.model.StudentSessionListResponse;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApproveSessionListFragment extends Fragment {

    private Context context;
    private RecyclerView rvSessionList;
    private LinearLayout llSessionView;
    private TextView tvNoSession;

    private Storage storage;

    public ApproveSessionListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_approve_session_list, container, false);

        storage = new Storage(context);

        llSessionView = view.findViewById(R.id.ll_session_details);
        tvNoSession = view.findViewById(R.id.tv_no_session);

        rvSessionList = view.findViewById(R.id.rv_session_list);
        rvSessionList.setLayoutManager(new LinearLayoutManager(context));

        getSessionList();

        return view;
    }

    private void getSessionList() {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getSessionsByStudentId(storage.getStudentId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<StudentSessionListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(StudentSessionListResponse response) {
                        if (response.getSuccess() == 1) {
                            List<StudentSessionListResponse.Detail> approveSessionList = new ArrayList<>();
                            if (response.getDetails().size() > 0) {
                                /*for (int i = 0; i < response.getDetails().size(); i++) {
//                                    if (response.getDetails().get(i).getSessionStatus().equalsIgnoreCase("Complete")) {
                                        approveSessionList.add(response.getDetails().get(i));
//                                    }
                                }*/
                                approveSessionList.addAll(response.getDetails());
                                SessionListAdapter adapter = new SessionListAdapter(context, approveSessionList, "student", new SessionListAdapter.OnItemClickListener() {
                                    @Override
                                    public void itemClick(StudentSessionListResponse.Detail detail) {
                                        ViewSessionFragment viewSessionFragment = new ViewSessionFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("Session",detail);
                                        viewSessionFragment.setArguments(bundle);
                                        loadFragment(viewSessionFragment);
                                    }
                                });
                                rvSessionList.setAdapter(adapter);
                                if (approveSessionList.size() == 0) {
                                    tvNoSession.setVisibility(View.VISIBLE);
                                    llSessionView.setVisibility(View.GONE);
                                } else {
                                    tvNoSession.setVisibility(View.GONE);
                                    llSessionView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                tvNoSession.setVisibility(View.VISIBLE);
                                llSessionView.setVisibility(View.GONE);
                            }
                        } else {
                            Log.d("response", response.getMessage());
                            showError(getResources().getString(R.string.no_data_found));
                            tvNoSession.setVisibility(View.VISIBLE);
                            llSessionView.setVisibility(View.GONE);
                        }
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        tvNoSession.setVisibility(View.VISIBLE);
                        llSessionView.setVisibility(View.GONE);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void showError(String message) {
        Snackbar snackbar = Snackbar.make(rvSessionList, message, Snackbar.LENGTH_LONG);
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
