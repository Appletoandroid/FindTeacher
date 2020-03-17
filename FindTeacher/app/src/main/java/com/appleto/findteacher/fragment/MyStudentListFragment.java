package com.appleto.findteacher.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appleto.findteacher.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyStudentListFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    TabLayout tabLayout;

    public MyStudentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_student_list, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Accept"));

        tabLayout.setSelectedTabIndicator(0);
        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                loadFragment(new PendingStudentListFragment());
                break;

            case 1:
                loadFragment(new AcceptStudentListFragment());
                break;

            default:
                loadFragment(new PendingStudentListFragment());
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                loadFragment(new PendingStudentListFragment());
                break;

            case 1:
                loadFragment(new AcceptStudentListFragment());
                break;

            default:
                loadFragment(new PendingStudentListFragment());
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.tab_frame, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }
}
