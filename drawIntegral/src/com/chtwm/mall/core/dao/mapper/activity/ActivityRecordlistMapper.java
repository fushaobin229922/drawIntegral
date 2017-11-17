package com.chtwm.mall.core.dao.mapper.activity;

import java.util.List;

import com.chtwm.mall.pojo.activity.ActivityRecordlist;

public interface ActivityRecordlistMapper {
	public int insert(ActivityRecordlist activityRecordlist);
	public int update(ActivityRecordlist activityRecordlist);
	public List<ActivityRecordlist> query(ActivityRecordlist activityRecordlist);
}
