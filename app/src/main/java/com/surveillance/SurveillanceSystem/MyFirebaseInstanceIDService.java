package com.surveillance.SurveillanceSystem;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("!!!!!!!TOKEN!!!!!!!!", "Refreshed token: " + refreshedToken);
        Log.e("!!!!!!!TOKEN!!!!!!!!", "Refreshed token: " + refreshedToken);
        Log.e("!!!!!!!TOKEN!!!!!!!!", "Refreshed token: " + refreshedToken);
    }
}
