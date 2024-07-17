package com.example.vmsv1;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vmsv1.MyPrintDocumentAdapter;
import com.example.vmsv1.R;

public class PrintLabel extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_label);
        printDocument();
    }

    private void printDocument() {
        // Get the PrintManager instance
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Create a print adapter instance
        PrintDocumentAdapter printAdapter = new MyPrintDocumentAdapter(this);

        // Create a print job with name and adapter
        PrintJob printJob = printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());

        // Check the status of the print job (optional)
        if (printJob.isCompleted()) {
            Toast.makeText(PrintLabel.this, "Print job completed", Toast.LENGTH_SHORT).show();
        } else if (printJob.isFailed()) {
            Toast.makeText(PrintLabel.this, "Print job failed", Toast.LENGTH_SHORT).show();
        }
    }
}
