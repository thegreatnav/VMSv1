package com.example.vmsv1.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> userId = new MutableLiveData<>();
    private final MutableLiveData<String> sbuId=new MutableLiveData<>();
    private final MutableLiveData<String> defaultGateId=new MutableLiveData<>();


    public void setUserId(String id) {
        userId.setValue(id);
    }

    public LiveData<String> getUserId() {
        return userId;
    }

    public void setSbuId(String id) {
        sbuId.setValue(id);
    }

    public LiveData<String> getSbuId() {
        return sbuId;
    }

    public void setDefaultGateId(String id) {
        defaultGateId.setValue(id);
    }

    public LiveData<String> getDefaultGateId() {
        return defaultGateId;
    }
}
