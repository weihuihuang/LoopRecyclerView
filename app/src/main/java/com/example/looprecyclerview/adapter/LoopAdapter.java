package com.example.looprecyclerview.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.looprecyclerview.R;

import java.util.List;

public class LoopAdapter extends RecyclerView.Adapter<LoopAdapter.MyViewHolder> {

    private List<String> dataList;

    public LoopAdapter(List<String> dataList){
        this.dataList = dataList;
    }

    //统计是否有复用
    private int count;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        count++;
        Log.e("onCreateViewHolder", "count-->" + count);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loop_adapter_item,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
