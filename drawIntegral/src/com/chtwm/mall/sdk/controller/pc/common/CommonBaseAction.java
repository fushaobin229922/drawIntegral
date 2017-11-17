package com.chtwm.mall.sdk.controller.pc.common;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chtwm.mall.sdk.controller.common.convert.RequestJsonToBean;
import com.chtwm.mall.sdk.controller.common.exception.TransformJsonParamsException;
import com.hengtianjf.basic.business.tools.common.CommonAppResponseBody;
import com.hengtianjf.basic.business.tools.common.constant.GlobalResultCode;
import com.hengtianjf.basic.business.tools.exception.ValidateParamsException;
import com.hengtianjf.basic.business.tools.validate.NumberValidationUtils;

/**
 * 通用的action
 * @author jannal
 */
@Controller
public class CommonBaseAction {
    
    private static final Logger logger = LoggerFactory.getLogger(CommonBaseAction.class);
    
    /**
     * 通用返回成功或失败参数拼接
     * @param flag
     * @return
     */
    public <T>  CommonAppResponseBody<T> defaultResultBack(Boolean flag) {
      if (flag) {
          return defaultSuccess();
      } else {
          return defaultFail();
      }        
  }
    
    public <T>  CommonAppResponseBody<T> defaultFail() {
        return CommonAppResponseBody.defaultFail();
    }



    public <T> CommonAppResponseBody<T> defaultSuccess() {
        return CommonAppResponseBody.defaultSuccess();
    }
    
    
    public <T> CommonAppResponseBody<T> defaultFail(String msg,String code){
        return new CommonAppResponseBody<T>(CommonAppResponseBody.FAILED_STATUS,msg,code);   
    }
    
    public <T> CommonAppResponseBody<T> defaultFail(String msg,String code,T data){
        return new CommonAppResponseBody<T>(CommonAppResponseBody.FAILED_STATUS,msg,code,data);   
    }
    /**初始化AppResponseBody<T>对象*/
    public <T> CommonAppResponseBody<T> getAppResponseBody(){
    	return new CommonAppResponseBody<T>();
    }
    /**json串转bean*/
    public <T> T jsonConvertToBean(String json, Class<T> clazz) throws TransformJsonParamsException{
    	return RequestJsonToBean.requestJsonToBean(json, clazz);
    }
    public Long[] stringsToLongs(String idsStr,String msg) throws ValidateParamsException {
        String[] ids = idsStr.split(",");
        Long[] idsLong = new Long[ids.length];
        for(int i=0;i<ids.length;i++){
            if(NumberValidationUtils.isLongNumeric(ids[i])){
                idsLong[i] =  Long.valueOf(ids[i]);
            }else{
                idsLong = null;
                throw new ValidateParamsException(msg, GlobalResultCode.CommonResultCode.PARAMS_ILLEGAL);
            }
        }
        return idsLong;
    }
    
    public String[] stringsToStrs(String idsStr,String msg) throws ValidateParamsException {
    	String[] ids = StringUtils.split(idsStr, ",");
		return ids;
    }
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
        binder.registerCustomEditor(int.class, new MyEditor());
    }
    
    class MyEditor extends PropertyEditorSupport  {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if(text == null ||text.equals(""))
				text = "0";
			if ( !StringUtils.hasText(text)) {
			
				setValue(null);
			}
			else {
				setValue(Integer.parseInt(text));//这句话是最重要的，他的目的是通过传入参数的类型来匹配相应的databind
			}
		}
		@Override
		public String getAsText() {
			
			return getValue().toString();
		}
}
    
    
    /**
     * 所有ActionMap 统一从这里获取
     * 
     * @return
     */
    public Map<String, Object> getRootMap() {
    
        Map<String, Object> rootMap = new HashMap<String, Object>();
        // 添加url到 Map中
        // rootMap.putAll(URLUtils.getUrlMap());
        return rootMap;
    }
    
    public ModelAndView forword(String viewName, Map<String,?> context) {
    
        return new ModelAndView(viewName, context);
    }
    
    public ModelAndView forword(String viewName) {
        
        return new ModelAndView(viewName);
    }
    
    public ModelAndView error(String errMsg) {
    
        return new ModelAndView("error");
    }
    
    
    
}
