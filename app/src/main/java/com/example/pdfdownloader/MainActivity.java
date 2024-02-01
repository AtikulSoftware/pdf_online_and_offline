package com.example.pdfdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.example.pdfdownloader.model.PdfItem;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<PdfItem> pdfItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize
        recyclerView = findViewById(R.id.recyclerView);

        pdfItemList.add(new PdfItem("Bangla Book","Class 1", "https://file-examples.com/storage/fe9f92926365baacd9d565f/2017/10/file-sample_150kB.pdf"));
        pdfItemList.add(new PdfItem("English Book","Class 10", "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"));
        pdfItemList.add(new PdfItem("Math Book","Class 9", "https://css4.pub/2015/icelandic/dictionary.pdf"));

        MyAdapter myAdapter = new MyAdapter(pdfItemList, MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(myAdapter);

    } // onCreate method end here ================


} // public class end here =======================