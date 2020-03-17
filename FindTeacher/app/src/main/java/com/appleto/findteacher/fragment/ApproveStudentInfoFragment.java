package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
public class ApproveStudentInfoFragment extends Fragment implements OnClickListener{

    private Context context;
    private Storage storage;

    TextView tvHeader, tvName, tvAge, tvContact, tvCity;
    CircleImageView ivStudentPic;
    Button btnSession, btnFees;
    LinearLayout llStudentDetail;
    TextView tvNoDetails;
    private String student_id;
    JsonObject studentDetails;



    public ApproveStudentInfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_approve_student_info, container, false);

        storage = new Storage(context);

        tvHeader = view.findViewById(R.id.tv_city_name);
        tvName = view.findViewById(R.id.tv_student_name);
        tvCity = view.findViewById(R.id.tv_student_city);
        tvAge = view.findViewById(R.id.tv_student_age);
        tvContact = view.findViewById(R.id.tv_student_contact);
        ivStudentPic = view.findViewById(R.id.iv_student_profile);
        llStudentDetail = view.findViewById(R.id.ll_student_details);
        tvNoDetails = view.findViewById(R.id.tv_no_details);

        btnSession = view.findViewById(R.id.btn_student_session);
        btnFees = view.findViewById(R.id.btn_student_fees);

        btnSession.setOnClickListener(this);
        btnFees.setOnClickListener(this);

        if(storage.getUserType().equalsIgnoreCase("Teacher")){
            if(getArguments() != null){
                getStudentById(getArguments().getString("student_id"));
            }
        } else {
            getStudentById(storage.getStudentId());
        }
        return view;
    }

    private void getStudentById(String studentId){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getStudentById(studentId)
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
                                    studentDetails = jsonObject.get("details").getAsJsonObject();
                                    student_id = studentDetails.get("S_ID").getAsString();
                                    tvName.setText(studentDetails.get("Name").getAsString());
                                    tvCity.setText(studentDetails.get("City").getAsString());
                                    tvHeader.setText(studentDetails.get("City").getAsString());
                                    tvAge.setText("Age : "+studentDetails.get("Age").getAsString());
                                    tvContact.setText(studentDetails.get("Mobile").getAsString());

                                    storage.saveEnrollmentDate(studentDetails.get("Enrollment_Date").getAsString());
                                    storage.saveTotalSession(studentDetails.get("Total_Sessions").getAsString());
                                    storage.saveTotalFees(studentDetails.get("Total_Fees").getAsString());
                                    storage.saveTotalPaid(studentDetails.get("Total_Paid").getAsString());
                                    storage.saveTotalRemaining(studentDetails.get("Total_Remaining").getAsString());
                                    storage.saveStudentId(student_id);

                                    if(studentDetails.get("Profile_Pic").getAsString().equals("")){
                                        ivStudentPic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_user));
                                    } else {
                                        Glide.with(context).asBitmap().load("http://zamb.codingvisions.in/assets/images/"+studentDetails.get("Profile_Pic").getAsString()).into(ivStudentPic);
                                    }

                                    tvNoDetails.setVisibility(View.GONE);
                                    llStudentDetail.setVisibility(View.VISIBLE);
                                } else {
                                    showError(jsonObject.get("message").getAsString());
                                    tvNoDetails.setVisibility(View.VISIBLE);
                                    llStudentDetail.setVisibility(View.GONE);
                                }
                            } /*else {
                                JsonArray jsonArray = (JsonArray) obj;
                                JsonObject object = jsonArray.get(0).getAsJsonObject();
                                showError(object.get("message").getAsString());
                            }*/
                        } catch (IOException e) {
                            e.printStackTrace();
                            tvNoDetails.setVisibility(View.VISIBLE);
                            llStudentDetail.setVisibility(View.GONE);
                        }
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        tvNoDetails.setVisibility(View.VISIBLE);
                        llStudentDetail.setVisibility(View.GONE);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == btnSession){
            SessionListFragment fragment = new SessionListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("student_id", student_id);
            fragment.setArguments(bundle);
            loadFragment(fragment);
        } else if(view == btnFees){
            loadFragment(new StudentFeesInfoFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void showError(String message){
        Snackbar snackbar = Snackbar.make(btnSession, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();
    }
}
