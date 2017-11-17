package com.chtwm.mall.core.service.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.chtwm.mall.core.dao.mapper.activity.ActivityAwardMapper;
import com.chtwm.mall.core.dao.mapper.activity.ActivityRecordlistMapper;
import com.chtwm.mall.core.dao.mapper.activity.ActivityUserMapper;
import com.chtwm.mall.pojo.UserInfo;
import com.chtwm.mall.pojo.activity.ActivityAward;
import com.chtwm.mall.pojo.activity.ActivityRecordlist;
import com.chtwm.mall.pojo.activity.ActivityUser;

@Service
public class ActivityAwardService {
	private static final Logger logger = LoggerFactory.getLogger(ActivityAwardService.class);
	@Resource
	private ActivityAwardMapper activityAwardMapper;
	@Resource
	private ActivityRecordlistMapper activityRecordlistMapper;
	@Resource
	private ActivityUserMapper activityUserMapper;
	@Resource
	RedisTemplate<String,String> redisTemplate;
	ExecutorService executorService = null;
	/**
	 * 
	 * @author wangyj
	 */
	@PostConstruct
	public void init(){
		//初始化线程池
		executorService = Executors.newFixedThreadPool(50);
		//初始化奖品
		if(redisTemplate.boundListOps("activity")==null||redisTemplate.boundListOps("activity").size()<=0){
			List<ActivityAward> list = queryActivityAwardList("0");
			Collections.shuffle(list);//随机整理两次
			Collections.shuffle(list);
			logger.info("redis队列："+list);
			for(ActivityAward activityAward :list){
				redisTemplate.boundListOps("activity").leftPush(activityAward.getId()+"_"+activityAward.getAward());
			}
		}
		
	}
	/**
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * @author wangyj
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> updateDrawRecord(UserInfo userInfo) throws Exception{
		String customerNo = userInfo.getCustomerNo();
		Map<String,Object> result = new HashMap<String,Object>();
		ActivityUser activityUser =new ActivityUser();
		activityUser.setCustomerNo(customerNo);
		String awardId="";//奖池的id
		String award="";//奖项的等级
		if(minusTimes(activityUser)>0){//使用数据库锁防止用户的抽奖次数出现负数
			String value = redisTemplate.boundListOps("activity").rightPop();
			if(!StringUtils.isEmpty(value)){
				String[] str = value.split("_");
				awardId = str[0];
				award = str[1];
			}else{
				logger.info("用户CustomerNo:{} 抽取奖品时，队列里的奖品已经空了，奖品库存为 0",customerNo);
				throw new Exception("奖品已被抢光！");
			}
			//更新奖池里奖品的状态为已发放
			ActivityAward activityAward = new ActivityAward();
			activityAward.setId(Integer.parseInt(awardId));
			activityAward.setStatu("1");
			//新增流水记录
			ActivityRecordlist recordlist = new ActivityRecordlist();
			recordlist.setAwardid(Integer.parseInt(awardId));
			recordlist.setAward(award);
			recordlist.setCustomerNo(customerNo);
			//异步更新、添加记录
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					logger.info("用户CustomerNo:{} ,抽取到的奖品的编号awardId为：{}, 为 {} 等奖,准备更新奖池的奖品发放状态",customerNo,activityAward.getId(),activityAward.getAward());
					int updterecord = updateAward(activityAward);
					logger.info("用户CustomerNo:{} ,更新奖池的奖品发放状态成功否---{}",customerNo,updterecord==1?"成功":"否");
					int record = addActivityRecordlist(recordlist);
					logger.info("用户CustomerNo:{} ,添加流水记录成功否---{}",customerNo,record==1?"成功":"否");
				}
			});
		}else{
			logger.info("用户CustomerNo:{} 剩余抽奖次数为 0 ",customerNo);
			throw new Exception("您的抽奖次数已用光");
		}
		result.put("award", award);
		return result;
	}
	/**
	 * @Description:根据id 更新奖池的状态
	 * @author wangyj
	 * @param award
	 * @return
	 */
	public int updateAward(ActivityAward award) {
		return activityAwardMapper.update(award);

	}
	/**
	 * @Description:<br/> 
	 * 查询所有未发放的奖池的奖
	 * @author wangyj  0-未发放;1-已发放'
	 * @param statu 
	 * @return
	 */
	public List<ActivityAward> queryActivityAwardList(String statu){
		List<ActivityAward> result = new ArrayList<ActivityAward>(1000);
		ActivityAward activityAward = new ActivityAward();
		if(StringUtils.isEmpty(statu)){
			activityAward.setStatu("0");
		}else{
			activityAward.setStatu(statu);
		}
		result = activityAwardMapper.query(activityAward);
		return result;
	}
	/**
	 * @Description:<br/> 
	 * 添加流水纪录
	 * @author wangyj
	 * @param activityRecordlist
	 * @return
	 */
	public int addActivityRecordlist(ActivityRecordlist activityRecordlist){
		return activityRecordlistMapper.insert(activityRecordlist);
	}
	/**
	 * @Description:查询流水纪录
	 * @author wangyj
	 * @param activityRecordlist
	 * @return
	 */
	public List<ActivityRecordlist> queryRecordlist(ActivityRecordlist activityRecordlist){
		List<ActivityRecordlist> result = activityRecordlistMapper.query(activityRecordlist);
		return result;
	}
	/**
	 * @Description:查询客户的抽奖次数
	 * @author wangyj
	 * @return
	 */
	public int queryTimes(ActivityUser activityUser){
		int result=0;
		activityUser = activityUserMapper.query(activityUser);
		if(activityUser!=null){
			result = activityUser.getCustomertimes();
		}
		return result;
	}
	/**
	 * @Description:<br/> 更新用户的抽奖次数减一
	 * @author wangyj
	 * @param activityUser
	 * @return
	 */
	public int minusTimes(ActivityUser activityUser){
		int result=0;
		if(activityUser!=null&&activityUser.getCustomerNo()!=null){
			result = activityUserMapper.update(activityUser);
		}
		return result;
	}
}
