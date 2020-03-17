package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appleto.findteacher.R;
import com.appleto.findteacher.adapter.DirectoryAdapter;
import com.appleto.findteacher.listener.OnItemClickListener;
import com.appleto.findteacher.model.TeacherListResponse;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectoryFragment extends Fragment {

    private Context context;
    private RecyclerView rvDirectoryList;
    private TextView tvCity;
    private Storage storage;

    String city, state;

    public DirectoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_directory, container, false);

        storage = new Storage(context);

        tvCity = view.findViewById(R.id.tv_city_name);
        rvDirectoryList = view.findViewById(R.id.rv_directory_list);
        rvDirectoryList.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null){
            city = getArguments().getString("student_city");
            state = getArguments().getString("student_state");
            tvCity.setText(city);

            getTeacherByStateCity();
        }
    }

    private void getTeacherByStateCity(){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getTeacherByStateCity(state, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TeacherListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TeacherListResponse response) {
                        if (response.getSuccess() == 1) {
                            DirectoryAdapter adapter = new DirectoryAdapter(context, response.getDetails(), new OnItemClickListener() {
                                @Override
                                public void onItemClicked(int pos, String type) {
                                    if(type.equalsIgnoreCase("View")){
                                        TeacherInfoFragment fragment = new TeacherInfoFragment();
                                        Bundle bundle  = new Bundle();
                                        bundle.putString("teacher_id", response.getDetails().get(pos).getTID());
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                    } else {
                                        sendTeacherRequest(storage.getStudentId(), response.getDetails().get(pos).getTID());
                                    }
                                }
                            });
                            rvDirectoryList.setAdapter(adapter);
                        } else {
                            showError(getResources().getString(R.string.no_data_found));
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

    private void sendTeacherRequest(String studentId, String teacherId) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .requestTeacher(studentId, teacherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        Utils.progress_dismiss(context);
                        if(response.get("success").getAsInt() == 1){
                            loadFragment(new TeacherContactDetailsFragment());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void showError(String message){
        Snackbar snackbar = Snackbar.make(rvDirectoryList, message, Snackbar.LENGTH_LONG);
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
