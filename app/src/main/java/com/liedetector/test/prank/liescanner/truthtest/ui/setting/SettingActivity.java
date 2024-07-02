package com.liedetector.test.prank.liescanner.truthtest.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivitySettingBinding;
import com.liedetector.test.prank.liescanner.truthtest.dialog.rate.IClickDialogRate;
import com.liedetector.test.prank.liescanner.truthtest.dialog.rate.RatingDialog;
import com.liedetector.test.prank.liescanner.truthtest.ui.about.AboutActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.language.LanguageActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.SharePrefUtils;
import com.liedetector.test.prank.liescanner.truthtest.util.SystemUtil;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    @Override
    public ActivitySettingBinding getBinding() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.headerSetting.tvTitle.setText(getString(R.string.setting));
        binding.headerSetting.imgTick.setVisibility(View.INVISIBLE);

        binding.tvLanguageCurrent.setText(SystemUtil.getLanguageName(this));

        if (SharePrefUtils.isRated(this)) {
            binding.layoutRate.setVisibility(View.GONE);
            binding.lineRate.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindView() {
        binding.headerSetting.imgLeft.setOnClickListener(view -> onBackPressed());

        binding.layoutLanguage.setOnClickListener(view -> startNextActivity(LanguageActivity.class, null));

        binding.layoutAbout.setOnClickListener(view -> startNextActivity(AboutActivity.class, null));

        binding.layoutRate.setOnClickListener(view -> onRate());

        binding.layoutShare.setOnClickListener(view -> onShare());
    }

    private void onRate() {
        RatingDialog ratingDialog = new RatingDialog(SettingActivity.this, true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                binding.layoutRate.setVisibility(View.GONE);
                binding.lineRate.setVisibility(View.GONE);
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    finishAffinity();
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    SharePrefUtils.forceRated(SettingActivity.this);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SettingActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                ReviewManager manager = ReviewManagerFactory.create(SettingActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(SettingActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            binding.layoutRate.setVisibility(View.GONE);
                            binding.lineRate.setVisibility(View.GONE);
                            SharePrefUtils.forceRated(SettingActivity.this);
                            ratingDialog.dismiss();
                            finishAffinity();
                        });
                    } else {
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                ratingDialog.dismiss();
                finishAffinity();
            }

        });
        ratingDialog.show();
    }

    private void onShare() {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intentShare.putExtra(Intent.EXTRA_TEXT, "Download application :https://play.google.com/store/apps/details?id=${activity.packageName}");
        startActivity(Intent.createChooser(intentShare, "Share with"));

    }


}