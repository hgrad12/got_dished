package com.example.gotdished;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.adapter.StepRecyclerAdapter;

import java.util.List;

public class StepsFragment extends Fragment implements View.OnClickListener{
    private List<String> listOfSteps;

    public StepsFragment(List<String> listOfSteps) {
        super(R.layout.fragment_steps);
        this.listOfSteps = listOfSteps;
    }

    public StepsFragment(){
        super(R.layout.fragment_steps);
    }

    public static StepsFragment newInstance(List<String> listOfSteps) {
        return new StepsFragment(listOfSteps);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        view.findViewById(R.id.stepsFloatingActionButton).setOnClickListener(this);
        RecyclerView stepsRecyclerView = view.findViewById(R.id.stepsRecyclerView);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsRecyclerView.setAdapter(new StepRecyclerAdapter(listOfSteps));

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stepsFloatingActionButton) {
            if (!listOfSteps.isEmpty()
                    && !listOfSteps.get(listOfSteps.size() - 1).isEmpty())
                return;

            listOfSteps.add("");
        }
    }
}