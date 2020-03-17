package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingStudentInfoFragment extends Fragment implements OnClickListener{

    private Context context;
    private Storage storage;

    TextView tvName, tvAge, tvContact, tvCity;
    CircleImageView ivProfile;
    EditText edtFees;
    Button btnAccept, btnReject;
    String T_Id, S_Id;

    public PendingStudentInfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pending_student_info, container, false);

        storage = new Storage(context);

        tvName = view.findViewById(R.id.tv_student_name);
        tvCity = view.findViewById(R.id.tv_student_city);
        tvAge = view.findViewById(R.id.tv_student_age);
        tvContact = view.findViewById(R.id.tv_student_contact);
        edtFees = view.findViewById(R.id.edt_student_fees);
        ivProfile = view.findViewById(R.id.iv_student_profile);

        edtFees.setText(storage.getTotalFees());
        btnAccept = view.findViewById(R.id.btn_accept_request);
        btnReject = view.findViewById(R.id.btn_reject_request);

        if(getArguments() != null){
            getStudentById(getArguments().getString("student_id"));

            btnAccept.setOnClickListener(this::onClick);
            btnReject.setOnClickListener(this::onClick);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == btnAccept){
            sendRequest("Accepted");
        } else if(view == btnReject){
            sendRequest("Rejected");
        }
    }

    private void getStudentById(String student_id){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getStudentById(student_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ResponseBody body) {

                        try {
                            String s = body.string();
                            Object obj = new JsonParser().parse(s);
                            if (obj instanceof JsonObject) {
                                JsonObject jsonObject = (JsonObject) obj;
                                if (jsonObject.get("success").getAsInt() == 1) {
                                    JsonObject innerJson = jsonObject.get("details").getAsJsonObject();
                                    S_Id = innerJson.get("S_ID").getAsString();
                                    T_Id = innerJson.get("Teacher_Id").getAsString();
                                    tvName.setText(innerJson.get("Name").getAsString());
                                    tvCity.setText(innerJson.get("City").getAsString());
                                    tvAge.setText("Age : "+innerJson.get("Age").getAsString());
                                    tvContact.setText(innerJson.get("Mobile").getAsString());
                                    edtFees.setText(innerJson.get("Total_Fees").getAsString());

                                    String profilePic = innerJson.get("Profile_Pic").getAsString();
                                    if(profilePic.equals("")){
                                        ivProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_user));
                                    } else {
                                        Glide.with(context).asBitmap().load("http://zamb.codingvisions.in/assets/images/"+profilePic).into(ivProfile);
                                    }
                                } else {
                                    showError(jsonObject.get("message").getAsString());
                                }
                            } /*else {
                                JsonArray jsonArray = (JsonArray) obj;
                                JsonObject object = jsonArray.get(0).getAsJsonObject();
                                showError(object.get("message").getAsString());
                            }*/
                        } catch (IOException e) {
                            e.printStackTrace();
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

    private void sendRequest(String status){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .updateStudentRequest(S_Id, T_Id, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        if(response.get("success").getAsInt() == 1){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadFragment(new TeacherProfileFragment());
                                }
                            }, 1800);
                        }
                        showError(response.get("message").getAsString());
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void showError(String message){
        Snackbar snackbar = Snackbar.make(btnAccept, message, Snackbar.LENGTH_LONG);
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
