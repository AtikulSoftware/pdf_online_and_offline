package com.example.pdfdownloader.model;

public class PdfItem {
    String title, subTitle, pdfUrl;

    public PdfItem(String title, String subTitle, String pdfUrl) {
        this.title = title;
        this.subTitle = subTitle;
        this.pdfUrl = pdfUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

} // PdfItem end here ===========
