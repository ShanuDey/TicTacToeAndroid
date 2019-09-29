package com.example.tictactoe;

import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class HomeActivityDark extends AppCompatActivity implements UpdateHelper.OnUpdateNeededListner {
    private SwitchMaterial s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_dark);

        s = findViewById(R.id.id_switch);

        UpdateHelper.with(this).onUpdateNeeded(this).check();

    }

    public void onClickTheme(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSP(View v) {
        Log.v("shanu", "Single Player Clicked");
        Intent intent = new Intent(this, SinglePlayer.class);
        intent.putExtra("darkTheme", s.isChecked());
        startActivity(intent);
    }

    public void onClickTP(View v) {
        Log.v("shanu", "Two Player Clicked");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("darkTheme", s.isChecked());
        startActivity(intent);
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        Log.v("shanu", "alert dialog");

        final MaterialAlertDialogBuilder materialAlertDialogBuilder
                = new MaterialAlertDialogBuilder(this, R.style.Theme_MaterialComponents_Dialog_Alert);

        final AlertDialog alertDialog = materialAlertDialogBuilder.setTitle("New version available")
                .setMessage("Please, update the app to new version for latest features")
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateApp(updateUrl);
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("shanu", "Later clicked");
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();

    }

    private void downloadNewVersion(String updateUrl) {
        //Toast.makeText(this, updateUrl, Toast.LENGTH_SHORT).show();
        Log.v("shanu", "downloading from " + updateUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(updateUrl));
        startActivity(intent);
    }

    public void updateApp(String updateUrl) {
        Log.v("shanu", "downloading from " + updateUrl);
        try {
            DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL();
            downloadFileFromURL.setContext(getApplicationContext());
            downloadFileFromURL.execute(updateUrl);
            finish();
        } catch (Exception e) {
            Log.v("shanu", e.getMessage());
        }
    }
}
