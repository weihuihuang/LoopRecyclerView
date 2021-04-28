package com.example.looprecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.looprecyclerview.adapter.LoopAdapter;
import com.example.looprecyclerview.manager.VerticalLoopLayoutManager;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        List<String> dataList = new ArrayList<>();
        for(int i = 0; i < 10; i ++){
            dataList.add("测试数据**-->" + i);
        }
        LoopAdapter loopAdapter = new LoopAdapter(dataList);
        recyclerView.setAdapter(loopAdapter);
        recyclerView.setLayoutManager(new VerticalLoopLayoutManager());
        loopAdapter.notifyDataSetChanged();
    }
}