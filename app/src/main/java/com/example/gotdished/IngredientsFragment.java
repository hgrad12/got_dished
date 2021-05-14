package com.example.gotdished;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gotdished.adapter.IngredientRecyclerAdapter;

import java.util.List;

public class IngredientsFragment extends Fragment implements View.OnClickListener {
    private List<String> listOfIngredients;

    public IngredientsFragment(){}

    public IngredientsFragment(List<String> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    public static IngredientsFragment newInstance(List<String> listOfIngredients) {
        return new IngredientsFragment(listOfIngredients);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        view.findViewById(R.id.ingredientsFloatingActionButton).setOnClickListener(this);
        RecyclerView ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientsRecyclerView.setAdapter(new IngredientRecyclerAdapter(listOfIngredients));

        return view;
    }

    @Override
    public void onClick(View v) {
        if (!listOfIngredients.isEmpty()
                && !listOfIngredients.get(listOfIngredients.size() - 1).isEmpty()) {
            return;
        }
        listOfIngredients.add("");
    }
}