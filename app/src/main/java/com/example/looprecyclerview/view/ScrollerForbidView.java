package com.example.looprecyclerview.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.looprecyclerview.R;
import com.example.looprecyclerview.adapter.LoopAdapter;
import com.example.looprecyclerview.manager.VerticalLoopLayoutManager;
import java.util.ArrayList;
import java.util.List;
import static android.os.Looper.getMainLooper;

public class ScrollerForbidView extends LinearLayout {

    private final static int LOOP_RECYCLER_VIEW_MSG = 100;
    private final static int LOOP_INTERVAL = 5000;
    private Handler myHandler;
    private VerticalLoopLayoutManager verticalLoopLayoutManager;

    public ScrollerForbidView(Context context) {
        this(context, null);
    }

    public ScrollerForbidView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.scroller_forbid_view,this);
    }

    public void test(){
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        List<String> dataList = new ArrayList<>();
        for(int i = 0; i < 10; i ++){
            dataList.add("测试数据**-->" + i);
        }
        LoopAdapter loopAdapter = new LoopAdapter(dataList);
        recyclerView.setAdapter(loopAdapter);
        verticalLoopLayoutManager = new VerticalLoopLayoutManager();
        recyclerView.setLayoutManager(verticalLoopLayoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        loopAdapter.notifyDataSetChanged();



        final int size = dataList.size();
        myHandler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == LOOP_RECYCLER_VIEW_MSG){
                    int firstItem = verticalLoopLayoutManager.findFirstVisibleItemPosition();
                    if(firstItem != -1 && size > 2){
                        int targetItemPosition = (firstItem + 1) % size;
                        recyclerView.smoothScrollToPosition(targetItemPosition);
                    }
                }
                myHandler.sendEmptyMessageDelayed(LOOP_RECYCLER_VIEW_MSG, LOOP_INTERVAL);
            }
        };
        myHandler.sendEmptyMessageDelayed(LOOP_RECYCLER_VIEW_MSG, LOOP_INTERVAL);

        recyclerView.requestDisallowInterceptTouchEvent(true);
    }

    public void release(){
        if(myHandler != null){
            myHandler.removeCallbacksAndMessages(null);
        }
    }

    //打开事件拦截RecyclerView禁止滚动

    float downX = 0;
    float downY = 0;
    boolean performClick = false;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                performClick = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                int differenceX = Math.abs((int)(ev.getX() - downX));
                int differenceY = Math.abs((int)(ev.getY() - downY));
                if(differenceX >= touchSlop || differenceY >= touchSlop){
                    downX = 0;
                    downY = 0;
                    performClick = false;
                }else {
                    performClick = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(performClick){
                    Toast toast = Toast.makeText(getContext(),"点击事件",Toast.LENGTH_SHORT);
                    toast.show();
                }
        }
        return true;
    }
}
