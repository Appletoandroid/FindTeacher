package com.appleto.findteacher.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.adapter.VideoGalleryAdapter;
import com.appleto.findteacher.model.StateListResponse;
import com.appleto.findteacher.model.VideoListResponse;
import com.appleto.findteacher.retrofit2.ApiClient;
import com.appleto.findteacher.retrofit2.ApiInterface;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoGalleryFragment extends Fragment {

    private Context context;
    RecyclerView rvVideoList;
    private Storage storage;

    public VideoGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context =context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_gallery, container, false);

        storage = new Storage(context);

        rvVideoList = view.findViewById(R.id.rv_video_list);
        rvVideoList.setLayoutManager(new GridLayoutManager(context, 2));

        getVideoList();

        return view;
    }

    private void getVideoList(){
        Utils.progress_show(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService
                .getVideos(storage.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<VideoListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(VideoListResponse response) {
                        if (response.getSuccess() == 1) {
                            if(response.getDetails().size() > 0){
                                VideoGalleryAdapter adapter = new VideoGalleryAdapter(context, response.getDetails());
                                rvVideoList.setAdapter(adapter);

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

    private void showError(String message){
        Snackbar snackbar = Snackbar.make(rvVideoList, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15f);
        snackbar.show();
    }
}
