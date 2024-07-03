package com.liedetector.test.prank.liescanner.truthtest.ui.main;

import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityMainBinding;
import com.liedetector.test.prank.liescanner.truthtest.dialog.exit.ExitAppDialog;
import com.liedetector.test.prank.liescanner.truthtest.ui.scanner.ScannerActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.SoundsActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    public ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.layoutScanner.setOnClickListener(view -> startNextActivity(ScannerActivity.class, null));

        binding.layoutSounds.setOnClickListener(view -> startNextActivity(SoundsActivity.class, null));

    }


    private void showDialogExit() {
        ExitAppDialog dialog = new ExitAppDialog(this, false);
        dialog.init(this::finishAffinity);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showDialogExit();
    }
}