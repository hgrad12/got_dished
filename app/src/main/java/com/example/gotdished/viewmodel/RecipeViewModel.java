package com.example.gotdished.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gotdished.model.Step;

import java.util.List;

public class RecipeViewModel extends ViewModel {
    private MutableLiveData<String> name;
    private MutableLiveData<String> category;
    private MutableLiveData<String> ttc;
    private MutableLiveData<String> imageUri;
    private MutableLiveData<List<String>> listOfEquipment;
    private MutableLiveData<List<Step>> listOfSteps;

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public MutableLiveData<String> getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category.setValue(category);
    }

    public MutableLiveData<String> getTtc() {
        return ttc;
    }

    public void setTtc(String ttc) {
        this.ttc.setValue(ttc);
    }

    public MutableLiveData<String> getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri.setValue(imageUri);
    }

    public MutableLiveData<List<String>> getListOfEquipment() {
        return listOfEquipment;
    }

    public void setListOfEquipment(List<String> listOfEquipment) {
        this.listOfEquipment.setValue(listOfEquipment);
    }

    public MutableLiveData<List<Step>> getListOfSteps() {
        return listOfSteps;
    }

    public void setListOfSteps(List<Step> listOfSteps) {
        this.listOfSteps.setValue(listOfSteps);
    }
}
