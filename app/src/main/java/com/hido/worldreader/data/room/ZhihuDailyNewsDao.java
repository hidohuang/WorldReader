package com.hido.worldreader.data.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/24.
 */
@Dao
public interface ZhihuDailyNewsDao {
    @Query("SELECT * FROM zhihu_daily_news WHERE timestamp BETWEEN (:timestamp - 24*60*60*1000 + 1) AND :timestamp ORDER BY timestamp ASC")
    List<ZhihuDailyNewsQuestion> queryAllByDate(long timestamp);
    
    @Query("SELECT * FROM zhihu_daily_news WHERE id = :id")
    ZhihuDailyNewsQuestion queryItemById(int id);
    
    @Query("SELECT * FROM zhihu_daily_news WHERE favorite = 1")
    List<ZhihuDailyNewsQuestion> queryAllFavorites();
    
    @Query("SELECT * FROM zhihu_daily_news WHERE (timestamp < :timestamp) AND (favorite = 0)")
    List<ZhihuDailyNewsQuestion> queryAllTimeoutItems(long timestamp);
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<ZhihuDailyNewsQuestion> items);
    
    @Update
    void update(ZhihuDailyNewsQuestion item);
    
    @Delete
    void delete(ZhihuDailyNewsQuestion item);
}
