package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.activity.LoginActivity;
import com.appleto.findteacher.activity.MainActivity;
import com.appleto.findteacher.model.CityByStateListResponse;
import com.appleto.findteacher.model.LoginResponse;
import com.appleto.findteacher.model.StateListResponse;
import com.appleto.findteacher.model.TeacherListResponse;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.LocationTracker;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchLocationFragment extends Fragment {

    private Context context;
    private Button btnNext;

    private ImageView ivUpdateLoc;
    private TextView tvCurrentLoc;
    private Spinner spStateList, spCityList;
    private String selectedState, selectedCity;

    private Storage storage;
    private LocationTracker tracker;


    public SearchLocationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search_location, container, false);

        storage = new Storage(context);
        tracker = new LocationTracker(context);

        spStateList = view.findViewById(R.id.sp_state_list);
        spCityList = view.findViewById(R.id.sp_city_list);

        tvCurrentLoc = view.findViewById(R.id.tv_current_location);
        tvCurrentLoc.setText(Utils.getAddress(context, tracker.getLatitude(), tracker.getLongitude()));

        btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTeacherByStateCity();
            }
        });

        ivUpdateLoc = view.findViewById(R.id.iv_update_location);
        ivUpdateLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCurrentLoc.setText(Utils.getAddress(context, tracker.getLatitude(), tracker.getLongitude()));
            }
        });

        spStateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedState = spStateList.getSelectedItem().toString();
                getCityByStateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spCityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCity = spCityList.getSelectedItem().toString();
                if(!selectedCity.equals("Select City")) {
                    updateStudentLocation();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getStateList();
        return view;
    }

    private void updateStudentLocation() {
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .updateStudentLocation(selectedCity, selectedState, storage.getStudentId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(JsonObject response) {
                        Utils.progress_dismiss(context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.progress_dismiss(context);
                        showError(getResources().getString(R.string.internet_problem));
                    }
                });
    }

    private void getStateList(){
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
                            spStateList.setAdapter(itemsAdapter);

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

    private void getCityByStateList(){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getCityByState(selectedState)
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
                            spCityList.setAdapter(itemsAdapter);

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

    private void getTeacherByStateCity(){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getTeacherByStateCity(selectedState, selectedCity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TeacherListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TeacherListResponse response) {
                        if (response.getSuccess() == 1) {
                            DirectoryFragment fragment = new DirectoryFragment();
                            Bundle bundle = new Bundle();
                            storage.saveStudentCity(selectedCity);
                            storage.saveStudentState(selectedState);
                            bundle.putString("student_state", selectedState);
                            bundle.putString("student_city", selectedCity);
                            fragment.setArguments(bundle);
                            loadFragment(fragment);
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

    private void showError(String message){
        Snackbar snackbar = Snackbar.make(spStateList, message, Snackbar.LENGTH_LONG);
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
