package com.example.gotdished.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotdished.adapter.RecipeItemAdapter;
import com.example.gotdished.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecipeItemAdapter adapter;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListOfRecipeItems();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.fragmentHomeRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new RecipeItemAdapter(getActivity(), false);
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void getListOfRecipeItems() {
        homeViewModel.getRecipeItems().observe(getViewLifecycleOwner(), recipeItems ->
                adapter.setListOfRecipeItems(recipeItems));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}