package com.example.gotdished.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.R;
import com.example.gotdished.model.Step;

import java.util.List;

public class StepRecyclerAdapter extends RecyclerView.Adapter<StepRecyclerAdapter.ViewHolder> {
    private static final String TAG = "StepRecyclerAdapter.class";
    private final List<Step> listOfSteps;
    private final Context context;
    private AlertDialog dialog;

    public StepRecyclerAdapter(List<Step> listOfSteps, Context context) {
        this.listOfSteps = listOfSteps;
        this.context = context;
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
            itemView.findViewById(R.id.step_row_edit).setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.step_row_edit:
                    editAt(getAdapterPosition());
                    break;
                case R.id.step_row_remove:
                    removeAt(getAdapterPosition());
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void editAt(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.steps_popup, null);
        ((EditText)v.findViewById(R.id.steps_popup_description)).setText(listOfSteps.get(position).getDetails());
        Button update = v.findViewById(R.id.steps_popup_save_button);
        update.setText("Update");
        update.setOnClickListener(e -> onEditClicked(v, position));
        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }

    private void onEditClicked(View view, int position) {
        TextView description = view.findViewById(R.id.steps_popup_description);

        if (description.getText().toString().trim().isEmpty()) {
            description.setError("Details required");
            description.requestFocus();
            return;
        }

        Step step = listOfSteps.get(position);
        step.setDetails(description.getText().toString().trim());

        new Handler().postDelayed(() -> {
            notifyItemChanged(position);
            dialog.dismiss();
        }, 120);
    }

    public void removeAt(int position) {
        Log.d(TAG, "Removing Step " + (position + 1));
        listOfSteps.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listOfSteps.size());
        notifyDataSetChanged();
    }
}
