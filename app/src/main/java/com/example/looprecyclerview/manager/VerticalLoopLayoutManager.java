package com.example.looprecyclerview.manager;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalLoopLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        if(state.isPreLayout() || getItemCount() <= 0){
            return;
        }
        int heightOffset = 0;
        for(int i = 0; i < getItemCount(); i++){
            View childView = recycler.getViewForPosition(i);
            addView(childView);
            measureChildWithMargins(childView,0,0);
            int viewWidth = getDecoratedMeasuredWidth(childView);
            int viewHeight = getDecoratedMeasuredHeight(childView);
            int left = getPaddingLeft();
            int top = heightOffset;
            int right = left + viewWidth;
            int bottom = heightOffset + viewHeight;
            layoutDecoratedWithMargins(childView, left, top, right, bottom);
            heightOffset += viewHeight;
            //超出高度不再布局
            if(heightOffset + getPaddingTop() + getPaddingBottom() > getHeight()){
                break;
            }
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //填充item view
        fillChild(dy, recycler);
        //上下滑动的偏移动画
        offsetChildrenVertical(-dy);
        //回收view
        recyclerViews(dy, recycler);
        return dy;
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    private void fillChild(int dy, RecyclerView.Recycler recycler){
        if(dy > 0){
            scrollUp(recycler);
        }else {
            scrollDown(recycler);
        }
    }

    //上滑
    private void scrollUp(RecyclerView.Recycler recycler){
        View anchorView = getChildAt(getChildCount() - 1);
        while (true){
            //获取最后一个可见item
            if(anchorView != null){
                //最后一个可见item还没完全进入
                if(anchorView.getBottom() + getPaddingBottom() > getHeight()){
                    break;
                }
                //最后一个可见item完全进入，需要填充下一个item
                int position = getPosition(anchorView);
                View nextView;
                if(position == getItemCount() - 1){
                    nextView = recycler.getViewForPosition(0);
                }else {
                    nextView = recycler.getViewForPosition(position + 1);
                }
                if(nextView == null){
                    break;
                }
                addView(nextView);
                measureChildWithMargins(nextView, 0,0);
                int viewWidth = getDecoratedMeasuredWidth(nextView);
                int viewHeight = getDecoratedMeasuredHeight(nextView);
                int left = getPaddingLeft();
                int right = getPaddingLeft() + viewWidth - getPaddingRight();
                int top = anchorView.getBottom();
                int bottom = top + viewHeight;
                layoutDecoratedWithMargins(nextView, left, top, right, bottom);
                anchorView = nextView;
            }else {
                break;
            }
        }
    }

    //下滑
    private void scrollDown(RecyclerView.Recycler recycler){
        View anchorView = getChildAt(0);
        while (true){
            if(anchorView.getTop() - getPaddingTop() < 0){
                break;
            }
            int position = getPosition(anchorView);
            //要填充的view
            View preView;
            if(position == 0){
                preView = recycler.getViewForPosition(getItemCount() - 1);
            }else {
                preView = recycler.getViewForPosition(position - 1);
            }
            if(preView == null){
                break;
            }
            addView(preView, 0);
            measureChildWithMargins(preView, 0,0);
            int viewWidth = getDecoratedMeasuredWidth(preView);
            int viewHeight = getDecoratedMeasuredHeight(preView);
            int left = getPaddingLeft();
            int right = getPaddingLeft() + viewWidth;
            int top = anchorView.getTop() - viewHeight;
            int bottom = anchorView.getTop();
            layoutDecoratedWithMargins(preView, left, top, right, bottom);
            anchorView = preView;
        }
    }

    private void recyclerViews(int dy, RecyclerView.Recycler recycler){
        int itemCount = getItemCount();
        for(int i = 0; i < itemCount; i++){
            View childView = getChildAt(i);
            if(childView == null){
                continue;
            }
            //上滑
            if(dy > 0){
                if(childView.getTop() >= getHeight()){
                    removeAndRecycleView(childView,recycler);
                }
            }else {
                if(childView.getBottom() <= 0){
                    removeAndRecycleView(childView, recycler);
                }
            }
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
