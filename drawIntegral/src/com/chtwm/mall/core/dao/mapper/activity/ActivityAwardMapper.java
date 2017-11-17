package com.chtwm.mall.core.dao.mapper.activity;

import java.util.List;
import java.util.Map;

import com.chtwm.mall.pojo.activity.ActivityAward;

public interface ActivityAwardMapper {

    public int insert(ActivityAward activityAward);
    
    public int update(ActivityAward activityAward);
    
    public List<ActivityAward> query(ActivityAward activityAward);
}
