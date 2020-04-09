package com.example.aplikasiuntukuts.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.aplikasiuntukuts.model.Cheese;
import com.example.aplikasiuntukuts.model.CheeseRepository;

import java.util.List;

public class CheeseViewModel extends AndroidViewModel {

    private CheeseRepository mRepository;
    private LiveData<List<Cheese>> mAllCheese;

    public CheeseViewModel(Application application) {
        super(application);
        mRepository = new CheeseRepository(application);
        mAllCheese = mRepository.getAllCheese();

    }

    public LiveData<List<Cheese>> getAllCheese(){
        return mAllCheese;
    }
}