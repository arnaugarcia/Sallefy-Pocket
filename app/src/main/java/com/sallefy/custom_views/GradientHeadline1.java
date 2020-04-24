package com.sallefy.custom_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.google.android.material.textview.MaterialTextView;
import com.sallefy.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class GradientHeadline1 extends MaterialTextView {

    public GradientHeadline1(@NonNull Context context) {
        super(context);
    }

    public GradientHeadline1(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientHeadline1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) getPaint().setShader(new LinearGradient(0, 0, getWidth(), 0,
                ContextCompat.getColor(getContext(), R.color.gradientStart),
                ContextCompat.getColor(getContext(), R.color.gradientEnd),
                Shader.TileMode.MIRROR));
    }
}
