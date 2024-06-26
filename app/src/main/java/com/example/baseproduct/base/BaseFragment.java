package com.example.baseproduct.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.baseproduct.R;
import com.example.baseproduct.util.SystemUtil;

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {

    public VB binding;

    public abstract VB setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState);

    public abstract void initView();

    public abstract void bindView();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = setBinding(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SystemUtil.setLocale(getContext());

        initView();
        bindView();
    }


    public void startNextActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent(requireActivity(), activity.getClass());
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.in_right, R.anim.out_left);
    }


}
