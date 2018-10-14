package com.sourcedev.kryptoquiz.UI;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;

import com.sourcedev.kryptoquiz.R;

public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {
    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), getTheme());
    }
}