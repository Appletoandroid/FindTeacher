package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
public class TeacherInfoFragment extends Fragment {

    private Context context;
    private Storage storage;

    CircleImageView ivTeacherProfile;
    TextView tvName, tvCity, tvAge, tvContact, tvEmail, tvExperience;
    LinearLayout llteacherDetails;
    TextView tvNoTeacher;
    Button btnSelect;
    String teacherId;

    public TeacherInfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_teacher_info, container, false);

        storage = new Storage(context);

        ivTeacherProfile = view.findViewById(R.id.iv_teacher_profile);
        tvName = view.findViewById(R.id.tv_teacher_name);
        tvCity = view.findViewById(R.id.tv_teacher_city);
        tvAge = view.findViewById(R.id.tv_teacher_age);
        tvContact = view.findViewById(R.id.tv_teacher_contact);
        tvEmail = view.findViewById(R.id.tv_teacher_email);
        tvExperience = view.findViewById(R.id.tv_teacher_experience);
        tvNoTeacher = view.findViewById(R.id.tv_no_details);
        llteacherDetails = view.findViewById(R.id.ll_teacher_details);

        btnSelect = view.findViewById(R.id.btn_select_teacher);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*TeacherContactDetailsFragment fragment = new TeacherContactDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("teacher_id", teacherId);
                fragment.setArguments(bundle);
                loadFragment(fragment);*/
                sendTeacherRequest(storage.getStudentId(), teacherId);
            }
        });

        if (getArguments() != null) {
            getTeacherDetails(getArguments().getString("teacher_id"));
        }
        return view;
    }

    private void getTeacherDetails(String teacher_id) {
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
                                    tvAge.setText("Age : " + innerJson.get("Age").getAsString());
                                    tvContact.setText(innerJson.get("Mobile").getAsString());
                                    tvEmail.setText(innerJson.get("Email").getAsString());
                                    tvExperience.setText(innerJson.get("Experience").getAsString() + " years experience");
                                    teacherId = innerJson.get("T_ID").getAsString();

                                    String teacherProfile = innerJson.get("Profile_Pic").getAsString();
                                    if(teacherProfile.equals("")){
                                        ivTeacherProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_user));
                                    } else {
                                        Glide.with(context).asBitmap().load("http://zamb.codingvisions.in/assets/images/"+teacherProfile).into(ivTeacherProfile);
                                    }
                                    tvNoTeacher.setVisibility(View.GONE);
                                    llteacherDetails.setVisibility(View.VISIBLE);
                                } else {
                                    showError(jsonObject.get("message").getAsString());
                                    tvNoTeacher.setVisibility(View.VISIBLE);
                                    llteacherDetails.setVisibility(View.GONE);
                                }
                            } /*else {
                                JsonArray jsonArray = (JsonArray) obj;
                                JsonObject object = jsonArray.get(0).getAsJsonObject();
                                showError(object.get("message").getAsString());
                            }*/
                        } catch (IOException e) {
                            e.printStackTrace();
                            tvNoTeacher.setVisibility(View.VISIBLE);
                            llteacherDetails.setVisibility(View.GONE);
                        }
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        tvNoTeacher.setVisibility(View.VISIBLE);
                        llteacherDetails.setVisibility(View.GONE);
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

    private void showError(String message) {
        Snackbar snackbar = Snackbar.make(tvName, message, Snackbar.LENGTH_LONG);
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
