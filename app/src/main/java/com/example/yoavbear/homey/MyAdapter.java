package com.example.yoavbear.homey;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Chore> data;
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private boolean isCompletedChore;
    private String currentLoggedInUser;

    // Provide a suitable constructor (depends on the kind of data set)
    public MyAdapter(Context context, @Nullable ArrayList<Chore> data, String currentLoggedInUser, boolean isCompletedChore) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.isCompletedChore = isCompletedChore;
        this.currentLoggedInUser = currentLoggedInUser;
    }

    public void updateList(ArrayList<Chore> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chore_row_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (position < data.size()) {
            final Chore current = data.get(position);
            holder.title.setText("Title: " + current.getTitle());
            holder.description.setText("Description: " + current.getDescription());
            holder.creator.setText("Creator: " + current.getCreator());
            holder.assignee.setText("Assignee: " + current.getAssignee());
            holder.category.setText("Category: " + current.getCategory());
            holder.priority.setText("Priority: " + current.getPriority().toString());

//            holder.completeChoreBtn.setText("COMPLETED");
//            holder.editChoreBtn.setText("EDIT CHORE");
//            holder.deleteChoreBtn.setText("DELETE CHORE");

            if (isCompletedChore) {
                holder.completeChoreBtn.setVisibility(View.GONE);
                holder.editChoreBtn.setVisibility(View.GONE);
                holder.deleteChoreBtn.setVisibility(View.GONE);
            } else {
                if (!current.getAssignee().equals(currentLoggedInUser))
                    holder.completeChoreBtn.setVisibility(View.GONE);

                if (!current.getCreator().equals(currentLoggedInUser)) {
                    holder.editChoreBtn.setVisibility(View.GONE);
                    holder.deleteChoreBtn.setVisibility(View.GONE);
                }

            }


            // handel buttons
            holder.completeChoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Grete job!", Toast.LENGTH_LONG).show();

                    final DatabaseReference dRaffChores = FirebaseDatabase.getInstance().getReference().child("Households").child(user_id).child("Chores").child(current.getCreator()).child(current.getTitle());
                    dRaffChores.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            DatabaseReference dRafCompletedChores = FirebaseDatabase.getInstance().getReference().child("Households").child(user_id).child("Completed Chores").child(dataSnapshot.getKey());
                            dRafCompletedChores.setValue(dataSnapshot.getValue());
                            dRaffChores.removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });

            holder.editChoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ChoreUpdater.class);
                    i.putExtra("user", current.getCreator());
                    i.putExtra("choreTitle", current.getTitle());
                    v.getContext().startActivity(i);
                }
            });

            holder.deleteChoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference dRaffChores = FirebaseDatabase.getInstance().getReference().child("Households").child(user_id).child("Chores").child(current.getCreator());
                    dRaffChores.child(current.getTitle()).removeValue();
                    Toast.makeText(v.getContext(), "The chore was removed successfully", Toast.LENGTH_LONG).show();
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

        public ImageButton completeChoreBtn;
        public ImageButton editChoreBtn;
        public ImageButton deleteChoreBtn;


        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.eventRow_title);
            description = v.findViewById(R.id.eventRow_description);
            creator = v.findViewById(R.id.eventRow_creator);
            assignee = v.findViewById(R.id.eventRow_assignee);
            category = v.findViewById(R.id.eventRow_category);
            priority = v.findViewById(R.id.eventRow_priority);

            completeChoreBtn = v.findViewById(R.id.choreButton_completed);
            editChoreBtn = v.findViewById(R.id.choreButton_edit);
            deleteChoreBtn = v.findViewById(R.id.choreButton_delete);
        }
    }
}

