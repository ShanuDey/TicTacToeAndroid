package com.example.tictactoe;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

class DownloadFileFromURL extends AsyncTask<String, String, String> {
    //ProgressDialog pd;
    String pathFolder = "";
    String pathFile = "";
    Context context;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder notificationBuilder;
    final int progressMax = 100;



    public void setContext(Context context){
        this.context = context;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.v("shanu","on execute");

        notificationManager = NotificationManagerCompat.from(context);

        notificationBuilder = new NotificationCompat.Builder(context,App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_file_download)
                .setContentTitle("Download")
                .setContentText("Downloading in progress")
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setProgress(progressMax,0,false);

        notificationManager.notify(1, notificationBuilder.build());
    }

    @Override
    protected String doInBackground(String... f_url) {
        int count;

        try {
            pathFolder = context.getExternalFilesDir(null).toString();
            pathFile = pathFolder + "/Equinox_Manager.apk";
            Log.v("shanu",pathFile);
            File futureStudioIconFile = new File(pathFolder);
            if(!futureStudioIconFile.exists()){
                futureStudioIconFile.mkdirs();
            }

            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lengthOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            FileOutputStream output = new FileOutputStream(pathFile);

            byte data[] = new byte[1024]; //anybody know what 1024 means ?
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lengthOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();


        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return pathFile;
    }

    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        Log.v("shanu","progress = "+progress[0]);
        notificationBuilder.setProgress(progressMax,Integer.parseInt(progress[0]),false);
        notificationManager.notify(1,notificationBuilder.build());
    }

    @Override
    protected void onPostExecute(String file_url) {
        Log.v("shanu","on post execute");
        notificationBuilder.setContentText("Download Finished")
                .setProgress(0,0,false)
                .setOngoing(false);
        notificationManager.notify(1,notificationBuilder.build());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent i = new Intent(Intent.ACTION_VIEW);

        i.setDataAndType(Uri.fromFile(new File(file_url)), "application/vnd.android.package-archive" );
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        context.startActivity(i);
    }

}
