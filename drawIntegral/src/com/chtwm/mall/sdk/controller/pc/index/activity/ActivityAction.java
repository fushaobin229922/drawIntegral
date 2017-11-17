package com.chtwm.mall.sdk.controller.pc.index.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import basic.arch.component.cache.redis.impl.RedisCacheService;
import basic.arch.component.logger.annotation.AuditLog;

import com.chtwm.mall.core.service.activity.ActivityAwardService;
import com.chtwm.mall.pojo.UserInfo;
import com.chtwm.mall.pojo.activity.ActivityRecordlist;
import com.chtwm.mall.pojo.activity.ActivityUser;
import com.chtwm.mall.sdk.controller.common.convert.RequestJsonToBean;
import com.chtwm.mall.sdk.controller.pc.common.CommonBaseAction;
import com.chtwm.mall.sdk.controller.pc.index.params.TokenParams;
import com.chtwm.mall.sdk.utils.UserSessionManagerUtils;
import com.hengtianjf.basic.business.tools.common.CommonAppResponseBody;
import com.hengtianjf.basic.business.tools.exception.ValidateParamsException;
/**
 * @Description:荣耀月抽奖活动时间 2017-12-01 10:00——2017-12-8 18:00
 * @author:wangyj
 * @version V1.0
 */
@Controller
@RequestMapping("/apis/pc/activity")
public class ActivityAction extends CommonBaseAction{
	private static final Logger logger = LoggerFactory.getLogger(ActivityAction.class);
	@Resource
	private ActivityAwardService activityAwardService;
	@Autowired
	private RedisCacheService redisCacheService;
	
	@RequestMapping("/gettoken.action")
	@AuditLog(description = "获取token")
	public @ResponseBody CommonAppResponseBody<Object> getToken(@RequestBody String json, HttpServletRequest httpServletRequest) {
		CommonAppResponseBody<Object> appReBody = new CommonAppResponseBody<Object>();
		UserInfo userInfo = UserSessionManagerUtils.getUserSession(httpServletRequest);
		String token = UUID.randomUUID().toString().replace("-", "");
		Map<String,Object> res = new HashMap<String,Object>();
		res.put("token", token);
		redisCacheService.saveOrUpdateTimeOut(userInfo.getCustomerNo()+"_activity_token", token, 1, TimeUnit.HOURS);
		appReBody.setData(res);
		return appReBody;
	}
	@RequestMapping("/drawintegral.action")
	@AuditLog(description = "开始抽奖")
	public @ResponseBody CommonAppResponseBody<Object> startActivity(@RequestBody String json, HttpServletRequest httpServletRequest) {
		CommonAppResponseBody<Object> appReBody = new CommonAppResponseBody<Object>();
		UserInfo userInfo = UserSessionManagerUtils.getUserSession(httpServletRequest);
		String token = redisCacheService.get(userInfo.getCustomerNo()+"_activity_token", String.class, true);
		if(!StringUtils.isEmpty(token)){
			redisCacheService.deleteByKeys(new String[]{"TO:"+userInfo.getCustomerNo()+"_activity_token"});
		}else{
			logger.info("CustomerNo用户： {} 开始抽奖,出现问题：redis缓存中找不到用户的token记录,原因有可能重复提交，或者token超时",userInfo.getCustomerNo());
			return defaultFail("请刷新页面,重新抽奖", "HD0002");
		}
		TokenParams reqParams = RequestJsonToBean.requestJsonToBean(json, TokenParams.class);
		if(reqParams==null||StringUtils.isEmpty(reqParams.getToken())){
			logger.info("请求抽奖接口的入参 token参数的数据为空");
			throw new ValidateParamsException("请求参数出现问题", "HD0003");
		}
		if(!reqParams.getToken().equals(token)){
			logger.info("入参 token参数：{}; redis缓存中数据：{}, 二者不相等 ",reqParams.getToken(),token);
			throw new ValidateParamsException("请求参数出现问题", "HD0004");
		}
		if(0!=compareTime()){
			return defaultFail(compareTime()==1?"活动未开始":"活动已结束", "HD0001");
		}
		
		logger.info("CustomerNo用户： {} 开始抽奖",userInfo.getCustomerNo());
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("award", "");
		result.put("record", "");
		try {
			Map<String,Object> res = activityAwardService.updateDrawRecord(userInfo);
			if(res!=null&&res.get("award")!=null){
				result.put("award", res.get("award"));
				result.put("record", getRecord((String)res.get("award")));
			}
		} catch (Exception e) {
			logger.info("CustomerNo用户： {} 开始抽奖,出现问题：{}",userInfo.getCustomerNo(),e.getMessage());
			return defaultFail(e.getMessage(), "HD0002");
		}
		appReBody.setData(result);
		return appReBody;
	}
	
	/**
	 * 查询用户中奖纪录
	 * @author wangyj
	 * @param json
	 * @return
	 */
	@RequestMapping("/queryrecord.action")
	@AuditLog(description = "查询用户中奖纪录")
	public @ResponseBody CommonAppResponseBody<Object> getUserAward(@RequestBody String json,HttpServletRequest httpServletRequest){
		CommonAppResponseBody<Object> appReBody = new CommonAppResponseBody<Object>();
		UserInfo userInfo = UserSessionManagerUtils.getUserSession(httpServletRequest);
		ActivityRecordlist activityRecordlist = new ActivityRecordlist();
		activityRecordlist.setCustomerNo(userInfo.getCustomerNo());
		List<ActivityRecordlist> list = activityAwardService.queryRecordlist(activityRecordlist);
		appReBody.setData(list);
		return appReBody;
	}
	/**
	 * 查询所有用户中奖纪录 不需要登录
	 * @author wangyj
	 * @param json
	 * @return
	 */
	@RequestMapping("/queryallrecordlist.action")
	@AuditLog(description = "查询所有用户中奖纪录")
	public @ResponseBody CommonAppResponseBody<Object> getAllAward(@RequestBody String json){
		CommonAppResponseBody<Object> appReBody = new CommonAppResponseBody<Object>();
		List<ActivityRecordlist> list = activityAwardService.queryRecordlist(null);
		appReBody.setData(list);
		return appReBody;
	}
	/**
	 * 查询用户可抽奖的次数
	 * @author wangyj
	 * @param json
	 * @return
	 */
	@RequestMapping("/querytimes.action")
	@AuditLog(description = "查询用户可抽奖的次数")
	public @ResponseBody CommonAppResponseBody<Object> queryTimes(HttpServletRequest httpServletRequest){
		CommonAppResponseBody<Object> appReBody = new CommonAppResponseBody<Object>();
	     UserInfo userInfo = UserSessionManagerUtils.getUserSession(httpServletRequest);
		ActivityUser activityUser = new ActivityUser();
		activityUser.setCustomerNo(userInfo.getCustomerNo());
		Map<String,Object> map = new HashMap<String,Object>();
		int times = activityAwardService.queryTimes(activityUser);
		map.put("times", times);
		map.put("startflag", compareTime());
		appReBody.setData(map);
		return appReBody;
	}
	/**
	 * 比较时间  
	 * @author wangyj
	 * @return 0-活动中，1-活动未开始，2-活动已结束
	 */
	private int compareTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String now = df.format(new Date());
		if("201712011000000".compareTo(now)>0){
			return 1;
		}
		if("201712081800000".compareTo(now)<0){
			return 2;
		}
		return 0;
	}
	/**
	 * 根据奖项等级获取对应的积分数 
	 * @author wangyj
	 * @param award
	 * @return
	 */
	private String getRecord(String award) {
		String result = "";
		switch (award) {
		case "1":
			result = "6666";
			break;
		case "2":
			result = "666";
			break;
		case "3":
			result = "266";
			break;
		case "4":
			result = "166";
			break;
		case "5":
			result = "66";
			break;
		default:
			result = "";
			break;
		}

		return result;
	}
	
}
