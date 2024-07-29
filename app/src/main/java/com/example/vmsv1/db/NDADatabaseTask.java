package com.example.vmsv1.db;

import android.os.Handler;
import android.os.Looper;

import com.example.vmsv1.dataitems.NDADetails;
import com.example.vmsv1.dataitems.VisitorSearchResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NDADatabaseTask {

    public interface OnNDADetailsRetrievedListener {
        void onNDADetailsRetrieved(List<NDADetails> ndaDetailsList);
        void onVisitorDetailsRetrieved(VisitorSearchResult visitorDetails);
    }

    private final DatabaseHelperSQL dbsql;
    private final ExecutorService executorService;
    private final Handler handler;

    public NDADatabaseTask(DatabaseHelperSQL dbsql) {
        this.dbsql = dbsql;
        this.executorService = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void retrieveNDADetails(int sbuId, OnNDADetailsRetrievedListener listener) {
        executorService.execute(() -> {
            List<NDADetails> ndaDetailsList = dbsql.getNDADetails(sbuId);
            handler.post(() -> listener.onNDADetailsRetrieved(ndaDetailsList));
        });
    }

    public void retrieveVisitorDetails(long uniqueId, OnNDADetailsRetrievedListener listener) {
        executorService.execute(() -> {
            VisitorSearchResult visitorDetails = dbsql.getVisitorDetails(uniqueId);
            handler.post(() -> listener.onVisitorDetailsRetrieved(visitorDetails));
        });
    }
}
