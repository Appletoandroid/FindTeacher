package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appleto.findteacher.R;
import com.appleto.findteacher.adapter.StudentListAdapter;
import com.appleto.findteacher.listener.OnItemClickListener;
import com.appleto.findteacher.model.StudentListByTIdResponse;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcceptStudentListFragment extends Fragment {

    private Context context;
    RecyclerView rvStudentList;
    TextView tvNoRequest;

    private Storage storage;

    public AcceptStudentListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_accept_student_list, container, false);

        storage = new Storage(context);

        tvNoRequest = view.findViewById(R.id.tv_no_request_found);
        rvStudentList = view.findViewById(R.id.rv_accept_student_list);
        rvStudentList.setLayoutManager(new LinearLayoutManager(context));

        if(getArguments() != null){
            getStudentList(getArguments().getString("teacher_id"));
        }
        return view;
    }

    private void getStudentList(String teacher_id){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getStudentByTeacherIdAccepted(teacher_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<StudentListByTIdResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(final StudentListByTIdResponse response) {

                        if(response.getSuccess() == 1){
                            if(response.getDetails().size() > 0){
                                StudentListAdapter adapter = new StudentListAdapter(context, response.getDetails(), new OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(int pos, String type) {
                                        ApproveStudentInfoFragment fragment = new ApproveStudentInfoFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("student_id", response.getDetails().get(pos).getSID());
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                    }
                                });
                                rvStudentList.setAdapter(adapter);
                                tvNoRequest.setVisibility(View.GONE);
                                rvStudentList.setVisibility(View.VISIBLE);
                            } else {
                                showError(getResources().getString(R.string.no_data_found));
                                tvNoRequest.setVisibility(View.VISIBLE);
                                rvStudentList.setVisibility(View.GONE);
                            }
                        } else {
                            showError(getResources().getString(R.string.no_data_found));
                            tvNoRequest.setVisibility(View.VISIBLE);
                            rvStudentList.setVisibility(View.GONE);
                        }

                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        tvNoRequest.setVisibility(View.VISIBLE);
                        rvStudentList.setVisibility(View.GONE);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void showError(String message){
        Snackbar snackbar = Snackbar.make(rvStudentList, message, Snackbar.LENGTH_LONG);
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
