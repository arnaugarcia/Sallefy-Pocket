package com.sallefy.custom_views;

import android.content.Context;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LogoutDialog extends MaterialAlertDialogBuilder {

    public LogoutDialog(Context context) {
        super(context);

        setTitle("Logout");
        setMessage("Are you sure you want to logout?");
        setNegativeButton("No, take me back", (dialog, i) -> Toast.makeText(context, "Not logging out", Toast.LENGTH_SHORT).show());
    }

    public LogoutDialog(Context context, int overrideThemeResId) {
        super(context, overrideThemeResId);
    }
}
