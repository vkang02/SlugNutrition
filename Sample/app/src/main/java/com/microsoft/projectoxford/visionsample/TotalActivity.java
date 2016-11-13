package com.microsoft.projectoxford.visionsample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;

import com.microsoft.projectoxford.vision.VisionServiceRestClient;

/**
 * Created by micha on 11/13/2016.
 */
public class TotalActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe);
    }
}
