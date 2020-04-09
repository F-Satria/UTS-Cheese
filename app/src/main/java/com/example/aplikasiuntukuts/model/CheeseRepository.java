package com.example.aplikasiuntukuts.model;

import androidx.lifecycle.LiveData;
import android.app.Application;

import java.util.List;

public class CheeseRepository {

    private CheeseDao mCheeseDao;
    private LiveData<List<Cheese>> mAllCheese;

    public CheeseRepository(Application application) {
        CheeseDatabase db = CheeseDatabase.getInstance(application);
        mCheeseDao = db.cheese();
        mAllCheese = mCheeseDao.getAllCheese();
    }

    public LiveData<List<Cheese>> getAllCheese(){
        return mAllCheese;
    }
}