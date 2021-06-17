package com.example.gotdished.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.R;
import com.example.gotdished.model.Step;

import java.util.List;

public class StepRecyclerAdapter extends RecyclerView.Adapter<StepRecyclerAdapter.ViewHolder> {
    private static final String TAG = "StepRecyclerAdapter.class";
    private final List<Step> listOfSteps;

    public StepRecyclerAdapter(List<Step> listOfSteps) {
        this.listOfSteps = listOfSteps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.step_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step step = listOfSteps.get(position);
        holder.rowNumber.setText(String.valueOf(position + 1));
        holder.description.setText(step.getDetails());
    }

    @Override
    public int getItemCount() {
        return listOfSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView rowNumber;
        public TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rowNumber = itemView.findViewById(R.id.step_row_number);
            description = itemView.findViewById(R.id.step_row_description);
            itemView.findViewById(R.id.step_row_remove).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            removeAt(getAdapterPosition());
        }
    }

    public void removeAt(int position) {
        Log.d(TAG, "Removing Step " + (position + 1));
        listOfSteps.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listOfSteps.size());
        notifyDataSetChanged();
    }
}
