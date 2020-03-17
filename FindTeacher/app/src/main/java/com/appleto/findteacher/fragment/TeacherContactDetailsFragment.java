package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.model.TeacherDetailsResponse;
import com.appleto.findteacher.model.TeacherListResponse;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
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
public class TeacherContactDetailsFragment extends Fragment {

    private Context context;

    CircleImageView ivProfile;
    TextView tvName, tvCity, tvAge, tvExperience;

    String teacherEmail, teacherContact;

    public TeacherContactDetailsFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_teacher_contact, container, false);

        ivProfile = view.findViewById(R.id.iv_teacher_profile);
        tvName = view.findViewById(R.id.tv_teacher_name);
        tvCity = view.findViewById(R.id.tv_teacher_city);
        tvAge = view.findViewById(R.id.tv_teacher_age);
        tvExperience = view.findViewById(R.id.tv_teacher_experience);

        if(getArguments() != null){
            getTeacherDetails(getArguments().getString("teacher_id"));
        }
        return view;
    }

    private void getTeacherDetails(String teacher_id){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getTeacherById(teacher_id)
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
                                    tvName.setText(innerJson.get("Name").getAsString());
                                    tvCity.setText(innerJson.get("City").getAsString());
                                    tvAge.setText("Age : "+innerJson.get("Age").getAsString());
                                    teacherContact = innerJson.get("Mobile").getAsString();
                                    teacherEmail = innerJson.get("Email").getAsString();
                                    tvExperience.setText(innerJson.get("Experience").getAsString() +" years experience");

                                    String teacherProfile = innerJson.get("Profile_Pic").getAsString();
                                    if(teacherProfile.equals("")){
                                        ivProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_user));
                                    } else {
                                        Glide.with(context).asBitmap().load("http://zamb.codingvisions.in/assets/images/"+teacherProfile).into(ivProfile);
                                    }
                                } else {
                                    showError(jsonObject.get("message").getAsString());
                                }
                            }
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

    private void showError(String message){
        Snackbar snackbar = Snackbar.make(tvName, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();
    }
}
