package com.krishna.fingerdance.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.krishna.fingerdance.R;
import com.krishna.fingerdance.fragment.FragmentMain;
import com.krishna.fingerdance.fragment.UserInputDialog;

/**
 * Created by krishna on 06/02/17.
 */

public class ActivityMain extends AppCompatActivity implements UserInputDialog.Continue, FragmentMain.DataCommunicator {
    private static final String TAG = "ActivityMain";
    private UserInputDialog userInputDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
            showUserInputDialog();
    }

    private void showUserInputDialog() {
        if (userInputDialog == null)
            userInputDialog = new UserInputDialog();
        userInputDialog.show(getSupportFragmentManager(), "inputDialog");
    }

    @Override
    public void onContinue(int n) {
        if (userInputDialog != null)
            userInputDialog.dismiss();
        /* start main fragment */
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, FragmentMain.newInstance(n, n)).commit();
    }

    @Override
    public void onGameOver() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Do you want to play again?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showUserInputDialog();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).show();
    }
}
