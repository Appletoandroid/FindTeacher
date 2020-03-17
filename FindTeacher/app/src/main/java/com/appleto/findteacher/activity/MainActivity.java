package com.appleto.findteacher.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appleto.findteacher.R;
import com.appleto.findteacher.adapter.NavigationManuAdapter;
import com.appleto.findteacher.fragment.AcceptStudentListFragment;
import com.appleto.findteacher.fragment.AddFeesFragment;
import com.appleto.findteacher.fragment.ApproveStudentInfoFragment;
import com.appleto.findteacher.fragment.DirectoryFragment;
import com.appleto.findteacher.fragment.FeesDetailsFragment;
import com.appleto.findteacher.fragment.PendingStudentListFragment;
import com.appleto.findteacher.fragment.StudentProfileFragment;
import com.appleto.findteacher.fragment.TeacherContactDetailsFragment;
import com.appleto.findteacher.fragment.TeacherProfileFragment;
import com.appleto.findteacher.fragment.EnrollmentDetailsFragment;
import com.appleto.findteacher.fragment.SearchLocationFragment;
import com.appleto.findteacher.fragment.ApproveSessionListFragment;
import com.appleto.findteacher.fragment.TeacherInfoFragment;
import com.appleto.findteacher.fragment.VideoGalleryFragment;
import com.appleto.findteacher.listener.OnItemClickListener;
import com.appleto.findteacher.model.NavigationMenuList;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private DrawerLayout drawer;
    private RecyclerView rvNavMenu;
    private Toolbar toolbar;

    CircleImageView ivProfile;
    TextView tvName, tvEmail;

    private Storage storage;
    List<NavigationMenuList> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        storage = new Storage(context);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        rvNavMenu = findViewById(R.id.rv_menu_item_list);
        ivProfile = findViewById(R.id.iv_profile);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);

        tvName.setText(storage.getUserName());
        tvEmail.setText(storage.getUserEmail());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();



        if(storage.getUserType().equalsIgnoreCase("Teacher")) {
            menuList.add(new NavigationMenuList("My Students", R.drawable.ic_email));
            menuList.add(new NavigationMenuList("Student Requests", R.drawable.ic_email));
            menuList.add(new NavigationMenuList("My Profile", R.drawable.ic_email));
            menuList.add(new NavigationMenuList("Logout", R.drawable.ic_email));

            TeacherProfileFragment fragment = new TeacherProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("teacher_id", storage.getTeacherId());
            fragment.setArguments(bundle);
            loadFragment(fragment);

            LinearLayoutManager linearLayout = new LinearLayoutManager(context);
            rvNavMenu.setLayoutManager(linearLayout);
            NavigationManuAdapter customeMainAdapter = new NavigationManuAdapter(context, menuList, new OnItemClickListener() {
                @Override
                public void onItemClicked(int pos, String type) {
                    getStartFragment(pos);
                    drawer.closeDrawers();
                }
            });
            rvNavMenu.setAdapter(customeMainAdapter);
        } else {


            getStudentById(storage.getStudentId());
        }

        if(storage.getUserProfile().equals("")){
            ivProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_user));
        } else {
            Glide.with(context).asBitmap().load("http://zamb.codingvisions.in/assets/images/"+storage.getUserProfile()).into(ivProfile);
        }

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openGallery(context);
            }
        });
    }

    private void getStudentById(String student_id) {
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
                                    JsonObject object = jsonObject.getAsJsonObject("details");

                                    storage.saveStudentId(object.get("S_ID").getAsString());
                                    storage.saveUserName(object.get("Name").getAsString());
                                    storage.saveUserContact(object.get("Mobile").getAsString());
                                    storage.saveUserEmail(object.get("Email").getAsString());
                                    storage.saveStudentCity(object.get("City").getAsString());
                                    storage.saveStudentState(object.get("State").getAsString());
                                    storage.saveUserAge(object.get("Age").getAsString());
                                    storage.saveUserGender(object.get("Gender").getAsString());
                                    storage.saveTeacherId(object.get("Teacher_Id").getAsString());
                                    storage.saveRequestStatus(object.get("Teacher_Request_Status").getAsString());
                                    storage.saveEnrollmentDate(object.get("Enrollment_Date").getAsString());
                                    storage.saveTotalSession(object.get("Total_Sessions").getAsString());
                                    storage.saveTotalFees(object.get("Total_Fees").getAsString());
                                    storage.saveTotalPaid(object.get("Total_Paid").getAsString());
                                    storage.saveTotalRemaining(object.get("Total_Remaining").getAsString());
                                    storage.saveUserProfile(object.get("Profile_Pic").getAsString());

                                    if(storage.getStudentCity().equals("") && storage.getStudentState().equals("")){
                                        loadFragment(new SearchLocationFragment());
                                    } else if(storage.getRequestStatus().equalsIgnoreCase("Pending")){
                                        menuList.add(new NavigationMenuList("My Profile", R.drawable.ic_email));
                                        menuList.add(new NavigationMenuList("My Teacher`s Profile", R.drawable.ic_email));
                                        menuList.add(new NavigationMenuList("Logout", R.drawable.ic_email));
                                        TeacherInfoFragment fragment = new TeacherInfoFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("teacher_id", storage.getTeacherId());
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                    } else if(storage.getRequestStatus().equalsIgnoreCase("Accepted")){
                                        menuList.add(new NavigationMenuList("My Sessions", R.drawable.ic_email));
                                        menuList.add(new NavigationMenuList("Video Gallery", R.drawable.ic_email));
                                        menuList.add(new NavigationMenuList("Enrollment Details", R.drawable.ic_email));
                                        menuList.add(new NavigationMenuList("My Profile", R.drawable.ic_email));
                                        menuList.add(new NavigationMenuList("My Teacher`s Profile", R.drawable.ic_email));
                                        menuList.add(new NavigationMenuList("Logout", R.drawable.ic_email));
                                        TeacherContactDetailsFragment fragment = new TeacherContactDetailsFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("teacher_id", storage.getTeacherId());
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                    } else if(storage.getRequestStatus().equals("") && storage.getTeacherId().equals("0")){
                                        DirectoryFragment fragment = new DirectoryFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("student_state", storage.getStudentState());
                                        bundle.putString("student_city", storage.getStudentCity());
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                    } else {
                                        ApproveSessionListFragment fragment = new ApproveSessionListFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("student_id", storage.getStudentId());
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                    }
                                    LinearLayoutManager linearLayout = new LinearLayoutManager(context);
                                    rvNavMenu.setLayoutManager(linearLayout);
                                    NavigationManuAdapter customeMainAdapter = new NavigationManuAdapter(context, menuList, new OnItemClickListener() {
                                        @Override
                                        public void onItemClicked(int pos, String type) {
                                            getStartFragment(pos);
                                            drawer.closeDrawers();
                                        }
                                    });
                                    rvNavMenu.setAdapter(customeMainAdapter);
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

    private void getStartFragment(int pos) {
        switch (pos) {
            case 0:
                if(storage.getUserType().equalsIgnoreCase("Teacher")) {
                    AcceptStudentListFragment fragment = new AcceptStudentListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("teacher_id", storage.getTeacherId());
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                } else {
                    if(storage.getRequestStatus().equalsIgnoreCase("Accepted")) {
                        loadFragment(new ApproveSessionListFragment());
                    } else {
                        loadFragment(new StudentProfileFragment());
                    }
                }
                break;
            case 1:
                if(storage.getUserType().equalsIgnoreCase("Teacher")) {
                    loadFragment(new PendingStudentListFragment());
                } else {
                    if(storage.getRequestStatus().equalsIgnoreCase("Accepted")) {
                        loadFragment(new VideoGalleryFragment());
                    } else {
                        TeacherInfoFragment fragment = new TeacherInfoFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("teacher_id", storage.getTeacherId());
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                    }
                }
                break;
            case 2:
                if(storage.getUserType().equalsIgnoreCase("Teacher")) {
                    TeacherProfileFragment fragment = new TeacherProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("teacher_id", storage.getTeacherId());
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                } else {
                    if(storage.getRequestStatus().equalsIgnoreCase("Accepted")) {
                        loadFragment(new EnrollmentDetailsFragment());
                    } else {
                        storage.saveLogInState(false);
                        storage.saveStudentId("");
                        storage.saveTeacherId("");
                        Utils.navigateTo(MainActivity.this, LoginActivity.class);
                    }
                }
                break;
            case 3:
                if(storage.getUserType().equalsIgnoreCase("Teacher")) {
                    storage.saveLogInState(false);
                    storage.saveTeacherId("");
                    Utils.navigateTo(MainActivity.this, LoginActivity.class);
                } else {
                    loadFragment(new StudentProfileFragment());
                }
                break;
            case 4:
                if(storage.getUserType().equalsIgnoreCase("Student")) {
                    if(storage.getRequestStatus().equalsIgnoreCase("Accepted")) {
                        TeacherContactDetailsFragment fragment = new TeacherContactDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("teacher_id", storage.getTeacherId());
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                    } else {
                        TeacherInfoFragment fragment = new TeacherInfoFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("teacher_id", storage.getTeacherId());
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                    }
                }
                break;

            case 5:
                if(storage.getUserType().equalsIgnoreCase("Student")) {
                    storage.saveLogInState(false);
                    storage.saveStudentId("");
                    storage.saveTeacherId("");
                    Utils.navigateTo(MainActivity.this, LoginActivity.class);
                }
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                ivProfile.setImageURI(result.getUri());
                upoadProfile(result.getUri());
            }
        }
    }

    private void upoadProfile(Uri uri) {
        Utils.progress_show(context);
        RequestBody userid = Utils.createPart(context, storage.getUserId());
        MultipartBody.Part profilePic = Utils.createMultipart(context, uri, "ProfilePic");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .changeProfile(userid, profilePic)
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
                        //Utils.showToast(context, getResources().getString(R.string.connection_timeout));
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if(storage.getUserType().equals("Student")) {
            if(storage.getStudentCity().equals("") && storage.getStudentState().equals("")){
                if (fragment instanceof SearchLocationFragment) {
                    super.onBackPressed();
                } else {
                    loadFragment(new SearchLocationFragment());
                }

            } else if(storage.getRequestStatus().equals("") && storage.getTeacherId().equals("0")){
                if (fragment instanceof DirectoryFragment) {
                    super.onBackPressed();
                } else {
                    loadFragment(new DirectoryFragment());
                }

            } else if(storage.getRequestStatus().equalsIgnoreCase("Pending")){
                if (fragment instanceof TeacherInfoFragment) {
                    super.onBackPressed();
                } else {
                    TeacherInfoFragment teacherInfoFragment = new TeacherInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("teacher_id", storage.getTeacherId());
                    teacherInfoFragment.setArguments(bundle);
                    loadFragment(teacherInfoFragment);
//                    loadFragment(new TeacherContactDetailsFragment());
                }

            } else if(storage.getRequestStatus().equalsIgnoreCase("Accepted")){
                if (fragment instanceof TeacherContactDetailsFragment) {
                    super.onBackPressed();
                } else {
                    TeacherContactDetailsFragment teacherContactDetailsFragment = new TeacherContactDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("teacher_id", storage.getTeacherId());
                    teacherContactDetailsFragment.setArguments(bundle);
                    loadFragment(teacherContactDetailsFragment);
//                    loadFragment(new TeacherContactDetailsFragment());
                }
            } else {
                if (fragment instanceof ApproveSessionListFragment) {
                    super.onBackPressed();
                } else {
                    loadFragment(new ApproveSessionListFragment());
                }
            }
        } else {
            if (fragment instanceof TeacherProfileFragment) {
                super.onBackPressed();
            } else {
                loadFragment(new TeacherProfileFragment());
            }
        }
    }
}
