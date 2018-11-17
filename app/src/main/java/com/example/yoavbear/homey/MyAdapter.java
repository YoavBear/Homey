package com.example.yoavbear.homey;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public  TextView description;
        public  TextView date;
        public  TextView time;
        public TextView type;
        public TextView creator;

        public Button joinBtn;
        public Button moreFromCreatorBtn;


        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.eventRow_title);
            description = v.findViewById(R.id.eventRow_description);
            date = v.findViewById(R.id.eventRow_date);
            time = v.findViewById(R.id.eventRow_time);
            type = v.findViewById(R.id.eventRow_type);
            creator = v.findViewById(R.id.eventRow_creator);

            joinBtn = v.findViewById(R.id.eventRow_join);
            moreFromCreatorBtn = v.findViewById(R.id.eventRow_more);
        }
    }
}
