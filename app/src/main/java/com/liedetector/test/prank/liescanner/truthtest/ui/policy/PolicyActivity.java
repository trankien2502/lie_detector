package com.liedetector.test.prank.liescanner.truthtest.ui.policy;

import android.annotation.SuppressLint;
import android.view.View;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityPolicyBinding;

public class PolicyActivity extends BaseActivity<ActivityPolicyBinding> {

    String linkPolicy = "https://firebasestorage.googleapis.com/v0/b/lie-detector-test---prank.appspot.com/o/Privacy%20Policy.html?alt=media&token=cfb30b66-0779-4f0a-8d7f-d7f27a4a6bdb";

    @Override
    public ActivityPolicyBinding getBinding() {
        return ActivityPolicyBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        binding.viewTop.imgTick.setVisibility(View.INVISIBLE);
        binding.viewTop.tvTitle.setText(getString(R.string.privacy_policy));

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
        binding.viewTop.imgLeft.setOnClickListener(v -> onBackPressed());
    }

}
