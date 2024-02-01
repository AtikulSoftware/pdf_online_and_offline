package com.example.pdfdownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ViewPDF extends AppCompatActivity {

    PDFView pdfView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);

        //======================================
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //======================================

        Bundle bundle = getIntent().getExtras();
        Boolean isOnline = null;
        String title = "";
        String pdf = "";
        if (bundle != null) {
            isOnline = bundle.getBoolean("isOnline");
            title = bundle.getString("title");
            pdf = bundle.getString("pdf");
            toolbar.setTitle(title);
        }

        if (isOnline != null && isOnline){
            new RetrivePDFfromUrl().execute(pdf);
        } else {
            pdfView.fromFile(new File(pdf))
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true)
                    .spacing(0)
                    .pageFitPolicy(FitPolicy.WIDTH)

                    .pageSnap(true) // snap pages to screen boundaries
                    .pageFling(true) // make a fling change only a single page like ViewPager

                    .onLoad(nbPages -> {
                         progressBar.setVisibility(View.GONE);
                    })
                    .load();
        }


    } // onCreate method end here ===================

    private class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {

            pdfView.useBestQuality(true);
            pdfView.fromStream(inputStream)
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true)
                    .spacing(0)
                    .pageFitPolicy(FitPolicy.WIDTH)

                    .pageSnap(true) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager

                    // page change
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            // Toast.makeText(PdfDetails.this, "Change page: "+page, Toast.LENGTH_SHORT).show();
                        }
                    })

                    //.scrollHandle(new DefaultScrollHandle(PdfDetails.this))

                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            progressBar.setVisibility(View.GONE);

                        }
                    })
                    .load();
        }

    } // RetrivePDFfromUrl end here ===========


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}