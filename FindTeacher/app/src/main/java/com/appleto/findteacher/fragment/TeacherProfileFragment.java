package com.appleto.findteacher.fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

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
import com.appleto.findteacher.model.TeacherDetailsResponse;
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
public class TeacherProfileFragment extends Fragment {

    private Context context;
    private Storage storage;

    CircleImageView ivTeacherProfile;
    TextView tvName, tvCity, tvAge, tvContact, tvEmail, tvExperience;
    LinearLayout llTeacherDetails;
    TextView tvNoDetails;
    Button btnEdit;
    String teacherId = "";

    JsonObject teacherDetails;


    public TeacherProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        storage = new Storage(context);

        ivTeacherProfile = view.findViewById(R.id.iv_teacher_profile);
        tvName = view.findViewById(R.id.tv_teacher_name);
        tvCity = view.findViewById(R.id.tv_teacher_city);
        tvAge = view.findViewById(R.id.tv_teacher_age);
        tvContact = view.findViewById(R.id.tv_teacher_contact);
        tvEmail = view.findViewById(R.id.tv_teacher_email);
        tvExperience = view.findViewById(R.id.tv_teacher_experience);
        llTeacherDetails = view.findViewById(R.id.ll_teacher_details);
        tvNoDetails = view.findViewById(R.id.tv_no_details);

        btnEdit = view.findViewById(R.id.btn_edit_teacher);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!teacherId.equals("") && storage.getUserType().equals("Teacher")) {
                    updateProfileDialog();
                } else {
                    AcceptStudentListFragment fragment = new AcceptStudentListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("teacher_id", storage.getTeacherId());
                    fragment.setArguments(bundle);

                    loadFragment(fragment);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (storage.getUserType().equals("Teacher")) {
            if (getArguments() != null) {
                getTeacherDetails(getArguments().getString("teacher_id"));
            } else {
                getTeacherDetails(storage.getTeacherId());
            }
        } else {
            getTeacherDetails(storage.getTeacherId());
        }
    }

    private void updateProfileDialog() {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_profile);

        EditText edtName = dialog.findViewById(R.id.dialog_edt_full_name);
        EditText edtEmail = dialog.findViewById(R.id.dialog_edt_email);
        EditText edtMobile = dialog.findViewById(R.id.dialog_edt_mobile);
        EditText edtAge = dialog.findViewById(R.id.dialog_edt_age);
        EditText edtGender = dialog.findViewById(R.id.dialog_edt_gender);
        EditText edtFees = dialog.findViewById(R.id.dialog_edt_teacher_fees);
        EditText edtExp = dialog.findViewById(R.id.dialog_edt_teacher_exp);

        Spinner spCity = dialog.findViewById(R.id.dialog_sp_city_list);
        Spinner spState = dialog.findViewById(R.id.dialog_sp_state_list);

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
        llFees.setVisibility(View.VISIBLE);

        edtName.setText(teacherDetails.get("Name").getAsString());
        edtEmail.setText(teacherDetails.get("Email").getAsString());
        edtMobile.setText(teacherDetails.get("Mobile").getAsString());
        edtAge.setText(teacherDetails.get("Age").getAsString());
        edtFees.setText(teacherDetails.get("Teacher_Fees").getAsString());
        edtExp.setText(teacherDetails.get("Experience").getAsString());

        Button btnSave = dialog.findViewById(R.id.dialog_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String mobile = edtMobile.getText().toString().trim();
                String age = edtAge.getText().toString().trim();
                String gender = edtGender.getText().toString().trim();
                String fees = edtFees.getText().toString().trim();
                String experience = edtExp.getText().toString().trim();
                String city = spCity.getSelectedItem().toString();
                String state = spState.getSelectedItem().toString();

                if (!spCity.getSelectedItem().toString().equals("Select State") && !spCity.getSelectedItem().toString().equals("Select City")) {
                    updateProfile(name, email, mobile, age, gender, fees, city, state, experience, dialog);
                } else {
                    Toast.makeText(context, "State or city is invalid", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();
    }

    private void getStateList(Spinner spState) {
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
                                if (teacherDetails.get("State").getAsString().equalsIgnoreCase(nameList.get(i))) {
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

    private void getCityByStateList(String state, Spinner spCity) {
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
                                if (teacherDetails.get("City").getAsString().equalsIgnoreCase(nameList.get(i))) {
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

    private void updateProfile(String name, String email, String mobile, String age, String gender, String fees, String city, String state, String exp, Dialog dialog) {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .updateTeacher(name, email, mobile, age, gender, city, state, teacherId, storage.getUserEmail(), fees, exp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        if (response.get("success").getAsInt() == 1) {
                            dialog.dismiss();
                            storage.saveUserContact(mobile);
                            storage.saveUserEmail(email);
                            storage.saveUserName(name);
                            storage.saveUserAge(age);
                            storage.saveUserGender(gender);
                            storage.saveTotalFees(fees);
                            getTeacherDetails(teacherId);
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
                                    teacherDetails = jsonObject.get("details").getAsJsonObject();
                                    teacherId = teacherDetails.get("T_ID").getAsString();
                                    tvName.setText(teacherDetails.get("Name").getAsString());
                                    tvEmail.setText(teacherDetails.get("Email").getAsString());
                                    tvCity.setText(teacherDetails.get("City").getAsString());
                                    tvAge.setText("Age : " + teacherDetails.get("Age").getAsString());
                                    tvContact.setText(teacherDetails.get("Mobile").getAsString());
                                    tvExperience.setText(teacherDetails.get("Experience").getAsString() + " years experience");

                                    if (teacherDetails.get("Profile_Pic").getAsString().equals("")) {
                                        ivTeacherProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_user));
                                    } else {
                                        Glide.with(context).asBitmap().load("http://zamb.codingvisions.in/assets/images/" + teacherDetails.get("Profile_Pic").getAsString()).into(ivTeacherProfile);
                                    }

                                    tvNoDetails.setVisibility(View.GONE);
                                    llTeacherDetails.setVisibility(View.VISIBLE);
                                } else {
                                    showError(jsonObject.get("message").getAsString());
                                    tvNoDetails.setVisibility(View.VISIBLE);
                                    llTeacherDetails.setVisibility(View.GONE);
                                }
                            } /*else {
                                JsonArray jsonArray = (JsonArray) obj;
                                JsonObject object = jsonArray.get(0).getAsJsonObject();
                                showError(object.get("message").getAsString());
                            }*/
                        } catch (IOException e) {
                            e.printStackTrace();
                            tvNoDetails.setVisibility(View.VISIBLE);
                            llTeacherDetails.setVisibility(View.GONE);
                        }
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        tvNoDetails.setVisibility(View.VISIBLE);
                        llTeacherDetails.setVisibility(View.GONE);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void showError(String message) {
        Snackbar snackbar = Snackbar.make(btnEdit, message, Snackbar.LENGTH_LONG);
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
