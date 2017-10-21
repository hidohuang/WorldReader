package com.hido.worldreader.data.bean;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/22.
 */

public class ZhihuDailyNews {
    

    
    private String date;
    
    private List<ZhihuDailyNewsQuestion> stories;
    
    public String getDate() { return date;}
    
    public void setDate(String date) { this.date = date;}
    
    public List<ZhihuDailyNewsQuestion> getStories() { return stories;}
    
    public void setStories(List<ZhihuDailyNewsQuestion> stories) { this.stories = stories;}
    
    
}
