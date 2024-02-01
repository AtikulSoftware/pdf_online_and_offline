package com.example.pdfdownloader.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.pdfdownloader.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {

    private Context context;
    private String fileURL;
    private String fileName;
    private ProgressDialog progressDialog;
    private DownloadListener downloadListener;

    public FileDownloader(Context context, String fileURL, String fileName, DownloadListener listener) {
        this.context = context;
        this.fileURL = fileURL;
        this.fileName = fileName;
        this.downloadListener = listener;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        new DownloadTask().execute();
    }

    private class DownloadTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File directory = new File(context.getExternalFilesDir(null), context.getString(R.string.app_name));

            // If directory does not exist, create it
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Initialize a File object for the output PDF file
            File pdfFile = new File(directory, fileName);

            try {
                // Create a URL object
                URL url = new URL(fileURL);

                // Open a connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Stream for reading the data
                InputStream inputStream = connection.getInputStream();

                // Stream for writing the data to the file
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

                // Read bytes and write to the file
                byte[] buffer = new byte[4096];
                long total = 0;
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    total += len;
                    fileOutputStream.write(buffer, 0, len);
                    // Publish the progress
                    publishProgress((int) (total * 100 / connection.getContentLength()));
                }

                // Close the streams
                fileOutputStream.close();
                inputStream.close();

                Log.d("DownloadManager", "PDF Downloaded and saved to " + pdfFile.getPath());
            } catch (IOException e) {
                Log.d("DownloadManager", "Error: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            downloadListener.onDownloadComplete();
        }
    }
}
