package com.example.baseproduct.ui.policy;

import android.annotation.SuppressLint;
import android.view.View;

import com.example.baseproduct.R;
import com.example.baseproduct.ads.IsNetWork;
import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivityPolicyBinding;

public class PolicyActivity extends BaseActivity<ActivityPolicyBinding> {

    String linkPolicy = "";

    @Override
    public ActivityPolicyBinding getBinding() {
        return ActivityPolicyBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        binding.viewTop.tvToolBar.setText(getString(R.string.privacy_policy));

        binding.viewTop.ivCheck.setVisibility(View.INVISIBLE);

        if (!linkPolicy.isEmpty() && IsNetWork.haveNetworkConnection(this)) {
            binding.webView.setVisibility(View.VISIBLE);
            binding.lnNoInternet.setVisibility(View.GONE);

            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.loadUrl(linkPolicy);
        } else {
            binding.webView.setVisibility(View.GONE);
            binding.lnNoInternet.setVisibility(View.VISIBLE);
        }

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(linkPolicy);
    }

    @Override
    public void bindView() {
        binding.viewTop.ivBack.setOnClickListener(v -> onBackPressed());
    }

}
