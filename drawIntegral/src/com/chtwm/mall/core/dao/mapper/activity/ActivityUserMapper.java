package com.chtwm.mall.core.dao.mapper.activity;

import com.chtwm.mall.pojo.activity.ActivityUser;

public interface ActivityUserMapper {
	/**
	 * 查询用户的抽奖次数
	 * @author wangyj
	 * @param activityUser
	 * @return
	 */
	public ActivityUser query(ActivityUser activityUser);
	/**
	 * @Description:更新客户抽奖次数减1
	 * @author wangyj
	 * @param activityUser
	 * @return
	 */
	public int update(ActivityUser activityUser);
}
