package com.example.pdfdownloader.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pdfdownloader.R;
import com.example.pdfdownloader.ViewPDF;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyHelper {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public void ShowDialogBox (Activity context, String pdfUrl, String title){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = context.getLayoutInflater().inflate(R.layout.pdf_dialog, null);
        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        TextView tvOffline = mView.findViewById(R.id.tvOffline);

        final String appPackageName = context.getPackageName();
        final String filePath = "/storage/emulated/0/Android/data/"+appPackageName+"/files/PDf Downloader/"+getFileNameFromUrl(pdfUrl);
        if (doesFileExist(filePath)){
            tvOffline.setText("View Offline");
        }

        mView.findViewById(R.id.layoutOnline).setOnClickListener(v -> {
            Intent myIntent = new Intent(context, ViewPDF.class);
            myIntent.putExtra("isOnline", true);
            myIntent.putExtra("title", title);
            myIntent.putExtra("pdf", pdfUrl);
            context.startActivity(myIntent);
        });

        mView.findViewById(R.id.layoutOffline).setOnClickListener(v -> {
            if (doesFileExist(filePath)){
                alertDialog.dismiss();
                Intent myIntent = new Intent(context, ViewPDF.class);
                myIntent.putExtra("isOnline", false);
                myIntent.putExtra("title", title);
                myIntent.putExtra("pdf", filePath);
                context.startActivity(myIntent);
            } else {
               if (isStoragePermissionGranted(context)){
                   new FileDownloader(context, pdfUrl, getFileNameFromUrl(pdfUrl), new DownloadListener() {
                       @Override
                       public void onDownloadComplete() {
                           tvOffline.setText("View Offline");
                           Toast.makeText(context, "PDF Downloaded and saved", Toast.LENGTH_SHORT).show();
                       }
                   });
               }
            }
        });

        mView.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    public static boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public String getFileNameFromUrl(String url) {
        String fileName = "";

        if (url != null && !url.isEmpty()) {
            int lastIndex = url.lastIndexOf('/');

            if (lastIndex != -1 && lastIndex < url.length() - 1) {
                fileName = url.substring(lastIndex + 1);
            }
        }

        return fileName;
    }

    private boolean isStoragePermissionGranted(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            int READ_MEDIA_IMAGES = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_MEDIA_IMAGES);
            int READ_MEDIA_VIDEO = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_MEDIA_VIDEO);
            int READ_MEDIA_AUDIO = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.	READ_MEDIA_AUDIO);

            List<String> listPermissionsNeeded = new ArrayList<>();

            // image permission
            if (READ_MEDIA_IMAGES != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_MEDIA_IMAGES);
            }

            // video permission
            if (READ_MEDIA_VIDEO != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_MEDIA_VIDEO);
            }

            // audio file permission
            if (READ_MEDIA_AUDIO != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_MEDIA_AUDIO);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        }

        return true;

    } // isStoragePermissionGranted end here ==================



} // MyHelper end here ============
