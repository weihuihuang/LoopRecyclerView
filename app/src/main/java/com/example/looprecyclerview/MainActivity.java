package com.example.looprecyclerview;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.looprecyclerview.view.ScrollerForbidView;

public class MainActivity extends AppCompatActivity {

    private ScrollerForbidView scrollerForbidView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollerForbidView = findViewById(R.id.test_view);
        scrollerForbidView.test();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(scrollerForbidView != null){
            scrollerForbidView.release();
        }
    }
}