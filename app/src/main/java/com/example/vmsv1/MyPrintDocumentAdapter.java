package com.example.vmsv1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.vmsv1.dataitems.VisitorSearchResult;

import java.io.FileOutputStream;
import java.io.IOException;

public class MyPrintDocumentAdapter extends PrintDocumentAdapter {

    private VisitorSearchResult v;
    private Context context;
    private PrintedPdfDocument pdfDocument;
    private int pageHeight;
    private int pageWidth;

    public MyPrintDocumentAdapter(Context context,VisitorSearchResult v) {
        this.context = context;
        this.v=v;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        // Retrieve the media size from the new attributes
        PrintAttributes.MediaSize mediaSize = newAttributes.getMediaSize();
        pageWidth = mediaSize.getWidthMils() / 1000 * 72;
        pageHeight = mediaSize.getHeightMils() / 1000 * 72;

        // Create a new PrintedPdfDocument with the new attributes
        pdfDocument = new PrintedPdfDocument(context, newAttributes);

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        PrintDocumentInfo info = new PrintDocumentInfo.Builder("NDA_Document")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN) // Use UNKNOWN to allow for dynamic page count
                .build();

        callback.onLayoutFinished(info, true);
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        PdfDocument.Page page = pdfDocument.startPage(0);

        if (cancellationSignal.isCanceled()) {
            callback.onWriteCancelled();
            pdfDocument.close();
            pdfDocument = null;
            return;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_print_label, null);

        TextView visitorIdView = layout.findViewById(R.id.visitorIdLabel);
        TextView visitorNameView = layout.findViewById(R.id.visitorNameLabel);
        TextView visitorCompanyView = layout.findViewById(R.id.visitorCompanyLabel);
        TextView visitorPlaceView = layout.findViewById(R.id.visitorPlaceLabel);
        TextView visitorContactNumView = layout.findViewById(R.id.visitorContactNumLabel);
        TextView visitingStaffView = layout.findViewById(R.id.visitingStaffLabel);
        TextView entryDateTimeView = layout.findViewById(R.id.entryDateTimeLabel);
        TextView exitDateTimeView = layout.findViewById(R.id.exitDateTimeLabel);

        // Populate the data
        visitorIdView.setText(v.getVisitorId());
        visitorNameView.setText(v.getVisitorName());
        visitorCompanyView.setText(v.getVisitorCompany());
        visitorPlaceView.setText(v.getVisitorPlace());
        visitorContactNumView.setText(v.getMobileNo());
        visitingStaffView.setText(v.getVisitingFaculty());
        entryDateTimeView.setText(v.getEntryDatetime());
        exitDateTimeView.setText(v.getExitDatetime() != null ? v.getExitDatetime() : "_");

        // Adjust layout parameters dynamically
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(pageWidth, pageHeight);
        } else {
            params.width = pageWidth;
            params.height = pageHeight;
        }
        layout.setLayoutParams(params);

        // Measure and layout the view
        layout.measure(View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY));
        layout.layout(0, 0, layout.getMeasuredWidth(), layout.getMeasuredHeight());

        // Draw the view on the PDF page
        Canvas canvas = page.getCanvas();
        canvas.clipRect(0, 0, pageWidth, pageHeight);
        float scale = Math.min((float) pageWidth / layout.getWidth(), (float) pageHeight / layout.getHeight());
        canvas.scale(scale, scale);
        layout.draw(canvas);

        pdfDocument.finishPage(page);

        try (FileOutputStream out = new FileOutputStream(destination.getFileDescriptor())) {
            pdfDocument.writeTo(out);
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            pdfDocument.close();
            pdfDocument = null;
        }

        callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
    }
}
