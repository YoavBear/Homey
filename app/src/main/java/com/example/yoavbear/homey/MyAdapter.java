package com.example.yoavbear.homey;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Chore> data;
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public interface OnEditClickListener {
        void onEditClick(Chore chore);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Chore chore);
    }

    // Provide a suitable constructor (depends on the kind of data set)
    public MyAdapter(Context context, @Nullable ArrayList<Chore> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.onEditClickListener = ((OnEditClickListener) context);
        this.onDeleteClickListener = ((OnDeleteClickListener) context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  inflater.inflate(R.layout.chore_row_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(position<data.size()) {
            final Chore current = data.get(position);
            holder.title.setText("Title: "+current.getTitle());
            holder.description.setText("Description: "+current.getDescription());
            holder.creator.setText("Creator: "+current.getCreator().toString());
            holder.assignee.setText("Assignee: "+current.getAssignee().toString());
            holder.category.setText("Category: "+current.getCategory());
            holder.priority.setText("Priority: "+current.getPriority().toString());
            holder.editChoreBtn.setText("EDIT CHORE");
            holder.deleteChoreBtn.setText("DELETE CHORE");

            holder.deleteChoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference dRaffChores = database.getReference().child("Chores").child(current.getCreator().toString());
                    dRaffChores.child(current.getTitle()).removeValue();
                    Toast.makeText(v.getContext(), "The chore was successfully removed", Toast.LENGTH_LONG).show();
                }
            });

            holder.editChoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ChoreUpdater.class);
                    i.putExtra("user", current.getCreator().toString());
                    i.putExtra("choreTitle" ,current.getTitle());
                    v.getContext().startActivity(i);
                }
            });

        }
    }


    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView assignee;
        public TextView category;
        public TextView creator;
        public TextView priority;

        public Button editChoreBtn;
        public Button deleteChoreBtn;


        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.eventRow_title);
            description = v.findViewById(R.id.eventRow_description);
            creator = v.findViewById(R.id.eventRow_creator);
            assignee = v.findViewById(R.id.eventRow_assignee);
            category = v.findViewById(R.id.eventRow_category);
            priority = v.findViewById(R.id.eventRow_priority);

            editChoreBtn = v.findViewById(R.id.choreButton_edit);

            deleteChoreBtn = v.findViewById(R.id.choreButton_delete);

        }
    }
}
