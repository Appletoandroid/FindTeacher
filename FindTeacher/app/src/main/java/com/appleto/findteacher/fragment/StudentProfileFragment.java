package com.appleto.findteacher.fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appleto.findteacher.R;
import com.appleto.findteacher.model.CityByStateListResponse;
import com.appleto.findteacher.model.StateListResponse;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {

    private Context context;
    private Storage storage;

    TextView tvHeader, tvName, tvAge, tvContact, tvCity, tvEmail;
    CircleImageView ivProfile;
    Button btnEditProfile;
    LinearLayout llStudentDetail;
    TextView tvNoDetails;
    JsonObject studentDetails;

    public StudentProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

        storage = new Storage(context);

        tvHeader = view.findViewById(R.id.tv_location);
        tvName = view.findViewById(R.id.tv_student_name);
        tvCity = view.findViewById(R.id.tv_student_city);
        tvAge = view.findViewById(R.id.tv_student_age);
        tvEmail = view.findViewById(R.id.tv_student_email);
        tvContact = view.findViewById(R.id.tv_student_contact);
        ivProfile = view.findViewById(R.id.iv_student_profile);
        llStudentDetail = view.findViewById(R.id.ll_student_details);
        tvNoDetails = view.findViewById(R.id.tv_no_details);
        btnEditProfile = view.findViewById(R.id.btn_edit_student);

        getStudentById();
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileDialog();
            }
        });

        return view;
    }

    private void getStudentById(){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getStudentById(storage.getStudentId())
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
                                    tvName.setText(studentDetails.get("Name").getAsString());
                                    tvCity.setText(studentDetails.get("City").getAsString());
                                    tvHeader.setText(studentDetails.get("City").getAsString());
                                    tvAge.setText("Age : "+studentDetails.get("Age").getAsString());
                                    tvContact.setText(studentDetails.get("Mobile").getAsString());
                                    tvEmail.setText(studentDetails.get("Email").getAsString());

                                    storage.saveEnrollmentDate(studentDetails.get("Enrollment_Date").getAsString());
                                    storage.saveTotalSession(studentDetails.get("Total_Sessions").getAsString());
                                    storage.saveTotalFees(studentDetails.get("Total_Fees").getAsString());
                                    storage.saveTotalPaid(studentDetails.get("Total_Paid").getAsString());
                                    storage.saveTotalRemaining(studentDetails.get("Total_Remaining").getAsString());

                                    String profilePic = studentDetails.get("Profile_Pic").getAsString();
                                    if(profilePic.equals("")){
                                        ivProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_user));
                                    } else {
                                        Glide.with(context).asBitmap().load("http://zamb.codingvisions.in/assets/images/"+profilePic).into(ivProfile);
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

    private void updateProfileDialog() {
        final Dialog dialog=new Dialog(context,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_profile);

        EditText edtName = dialog.findViewById(R.id.dialog_edt_full_name);
        EditText edtEmail = dialog.findViewById(R.id.dialog_edt_email);
        EditText edtMobile = dialog.findViewById(R.id.dialog_edt_mobile);
        EditText edtAge = dialog.findViewById(R.id.dialog_edt_age);
        EditText edtGender = dialog.findViewById(R.id.dialog_edt_gender);

        Spinner spCity =dialog.findViewById(R.id.dialog_sp_city_list);
        Spinner spState =dialog.findViewById(R.id.dialog_sp_state_list);

        getStateList(spState);

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getCityByStateList(spState.getSelectedItem().toString(), spCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        LinearLayout llFees = dialog.findViewById(R.id.ll_fees_info);
        llFees.setVisibility(View.GONE);

        edtName.setText(studentDetails.get("Name").getAsString());
        edtEmail.setText(studentDetails.get("Email").getAsString());
        edtMobile.setText(studentDetails.get("Mobile").getAsString());
        edtAge.setText(studentDetails.get("Age").getAsString());
        edtGender.setText(studentDetails.get("Gender").getAsString());

        Button btnSave = dialog.findViewById(R.id.dialog_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String mobile = edtMobile.getText().toString().trim();
                String age = edtAge.getText().toString().trim();
                String gender = edtGender.getText().toString().trim();
                String city = spCity.getSelectedItem().toString();
                String state = spState.getSelectedItem().toString();

                if(!spCity.getSelectedItem().toString().equals("Select State") && !spCity.getSelectedItem().toString().equals("Select City")){
                    updateProfile(name, email, mobile, age, gender, city, state, dialog);
                } else {
                    Toast.makeText(context, "State or city is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void getStateList(Spinner spState){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getStates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<StateListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(StateListResponse response) {
                        if (response.getSuccess() == 1) {
                            ArrayList<String> nameList = new ArrayList<>();
                            nameList.add("Select State");
                            for (int i = 0; i < response.getDetails().size(); i++) {
                                nameList.add(response.getDetails().get(i).getName());
                            }
                            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(context, R.layout.raw_spinner_item, R.id.raw_tv_name, nameList);
                            itemsAdapter.setDropDownViewResource(R.layout.raw_spinner_item);
                            spState.setAdapter(itemsAdapter);

                            for (int i = 0; i < nameList.size(); i++) {
                                if(studentDetails.get("State").getAsString().equalsIgnoreCase(nameList.get(i))){
                                    spState.setSelection(i);
                                    break;
                                }
                            }

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

    private void getCityByStateList(String state, Spinner spCity){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getCityByState(state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CityByStateListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(CityByStateListResponse response) {
                        if (response.getSuccess() == 1) {

                            ArrayList<String> nameList = new ArrayList<>();
                            nameList.add("Select City");
                            for (int i = 0; i < response.getDetails().size(); i++) {
                                nameList.add(response.getDetails().get(i).getName());
                            }
                            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(context, R.layout.raw_spinner_item, R.id.raw_tv_name, nameList);
                            itemsAdapter.setDropDownViewResource(R.layout.raw_spinner_item);
                            spCity.setAdapter(itemsAdapter);
                            for (int i = 0; i < nameList.size(); i++) {
                                if(studentDetails.get("City").getAsString().equalsIgnoreCase(nameList.get(i))){
                                    spCity.setSelection(i);
                                    break;
                                }
                            }

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

    private void updateProfile(String name, String email, String mobile, String age, String gender, String city, String state, Dialog dialog) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .updateStudent(name, email, mobile, age, gender, city, state, storage.getStudentId(), storage.getUserEmail())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        if(response.get("success").getAsInt() == 1){
                            dialog.dismiss();
                            getStudentById();
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
        Snackbar snackbar = Snackbar.make(btnEditProfile, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();
    }
}
