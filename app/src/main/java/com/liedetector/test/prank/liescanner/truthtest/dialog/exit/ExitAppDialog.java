package com.liedetector.test.prank.liescanner.truthtest.dialog.exit;

import android.content.Context;

import com.liedetector.test.prank.liescanner.truthtest.base.BaseDialog;
import com.liedetector.test.prank.liescanner.truthtest.databinding.DialogExitAppBinding;

public class ExitAppDialog extends BaseDialog<DialogExitAppBinding> {
    IClickDialogExit iBaseListener;
    Context context;

    public ExitAppDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogExitAppBinding setBinding() {
        return DialogExitAppBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.btnCancelQuitApp.setOnClickListener(view -> dismiss());

        binding.btnQuitApp.setOnClickListener(view -> {
            dismiss();
            iBaseListener.quit();
        });

    }

    public void init(IClickDialogExit iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
