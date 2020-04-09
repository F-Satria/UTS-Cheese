
/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.aplikasiuntukuts.model;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.VisibleForTesting;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room database.
 */
@Database(entities = {Cheese.class}, version = 1)
public abstract class CheeseDatabase extends RoomDatabase {

    /**
     * @return The DAO for the Cheese table.
     */
    @SuppressWarnings("WeakerAccess")

    public abstract CheeseDao cheese();
    private static final int NUMBER_OF_THREAD = 4;
    static final ExecutorService databaseWriterExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREAD);

    /** The only instance */
    private static CheeseDatabase sInstance;

    private static RoomDatabase.Callback populateDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriterExecutor.execute(() -> {
                CheeseDao dao = sInstance.cheese();
                for (int i = 0; i < Cheese.CHEESES.length; i++){
                    Cheese data = new Cheese(Cheese.CHEESES[i]);
                    dao.insert(data);
                }
            });
        }
    };

    /**
     * Gets the singleton instance of CheeseDatabase.
     *
     * @param context The context.
     * @return The singleton instance of CheeseDatabase.
     */

    public static synchronized CheeseDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), CheeseDatabase.class, "ex")
                    .addCallback(populateDatabaseCallback)
                    .build();
//            sInstance.populateInitialData();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                CheeseDatabase.class).build();
    }

    /**
     * Inserts the dummy data into the database if it is currently empty.
     */
    private void populateInitialData() {
        CheeseDao cheeseDao = sInstance.cheese();
        Cheese cheese = new Cheese();

        for (String name: Cheese.CHEESES){
            cheese.setName(name);
            cheeseDao.insert(cheese);
        }
    }



}
