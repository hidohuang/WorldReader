package com.hido.worldreader.data.room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hidohuang on 2017/9/24.
 */

public class DatabaseCreator {
    
    private static final DatabaseCreator ourInstance = new DatabaseCreator();
    
    private final AtomicBoolean mIsDbCreated = new AtomicBoolean(false);
    
    private AppDataBase mAppDataBase;
    
    public static DatabaseCreator getInstance() {
        return ourInstance;
    }
    
    private DatabaseCreator() {
    }
    
    public void createDb(Context context) {
        
        new AsyncTask<Context, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mIsDbCreated.set(true);
            }
            
            @Override
            protected Void doInBackground(Context... contexts) {
                Context ctx = contexts[0].getApplicationContext();
                mAppDataBase = Room.databaseBuilder(ctx, AppDataBase.class, AppDataBase.DATABASE_NAME).build();
                return null;
            }
        }.execute(context.getApplicationContext());
    }
    
    public AppDataBase getDatabase() {
        return mAppDataBase;
    }
    
    public boolean isDatabaseCreated() {
        return mIsDbCreated.get();
    }
    
    
}
