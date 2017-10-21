package com.hido.worldreader.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.hido.worldreader.data.bean.ZhihuDailyContent;
import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;

/**
 * Created by hidohuang on 2017/9/24.
 */

@Database(entities = {
        ZhihuDailyContent.class,
        ZhihuDailyNewsQuestion.class},
        version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    
    public static final String DATABASE_NAME = "world-reader-db";
    
    public abstract ZhihuDailyNewsDao zhihuDailyNewsDao();
    
    public abstract ZhihuDailyContentDao zhihuDailyContentDao();
    
}
