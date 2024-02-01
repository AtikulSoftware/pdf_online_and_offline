package com.example.pdfdownloader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdfdownloader.helper.MyHelper;
import com.example.pdfdownloader.model.PdfItem;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<PdfItem> pdfItemList;
    Activity activity;


    public MyAdapter(List<PdfItem> pdfItemList, Activity activity) {
        this.pdfItemList = pdfItemList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.pdf_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        PdfItem pdfItem = pdfItemList.get(position);

        holder.tvTitle.setText(pdfItem.getTitle());
        holder.tvSubtitle.setText(pdfItem.getSubTitle());
        int count = position + 1;
        holder.tvCount.setText(String.valueOf(count));

        holder.itemView.setOnClickListener(v -> {
            MyHelper myHelper = new MyHelper();
            myHelper.ShowDialogBox(activity,pdfItem.getPdfUrl(), pdfItem.getTitle());
        });

    }

    @Override
    public int getItemCount() {
        return pdfItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvSubtitle, tvCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialize
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            tvCount = itemView.findViewById(R.id.tvCount);

        }

    } // MyViewHolder end here ======

} // MyAdapter end here =========
