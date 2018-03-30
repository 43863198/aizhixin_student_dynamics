package com.aizhixin.cloud.dataanalysis.permissions;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.method.HandlerMethod;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-03-28
 */
public class OmsCoreManager
{
    private static Logger log = Logger.getLogger(OmsCoreManager.class);
    //全局权限映射
    public static  final Map<String,PrivilegeBean> PRIVIlEG_MAPPING= new HashMap<String,PrivilegeBean>();

    private static void loadXml()
    {
        ClassLoader cl = OmsCoreManager.class.getClassLoader();
        SAXReader sr = new SAXReader();
        try {
            Document doc = sr.read(cl.getResourceAsStream("operator.xml"));
            Element root =  doc.getRootElement();
            List<Element> privileges = root.elements();
            BeanInfo beanInfo = Introspector.getBeanInfo(PrivilegeBean.class);
            PropertyDescriptor pds[] = beanInfo.getPropertyDescriptors();
            for(Element privilege : privileges)
            {
                PrivilegeBean bean = new PrivilegeBean();
                for(Element attr : (List<Element>)privilege.elements())
                {
                    for(PropertyDescriptor pd : pds)
                    {
                        if(attr.getName().equals(pd.getName()) && !attr.getName().equals("include"))
                        {
                            if(StringUtils.isNotBlank(attr.getTextTrim()))
                                pd.getWriteMethod().invoke(bean,attr.getTextTrim());
                            break;
                        }
                        if(attr.getName().equals("include") && attr.getName().equals(pd.getName()))
                        {
                            List<String> opids = new ArrayList<>();
                            for(Element opid :(List<Element>)attr.elements() )
                            {
                                if(StringUtils.isNotBlank(opid.getTextTrim()))
                                {
                                    opids.add(opid.getTextTrim());
                                }
                            }
                            pd.getWriteMethod().invoke(bean,opids);
                        }
                    }
                }
                PRIVIlEG_MAPPING.put(bean.getOpid(),bean);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    //类初始化
    static
    {
        loadXml();
    }

    //接收多个角色的权限(用分号隔开)，然后进行权限合并
    public static List<String> margePrivilege(List<String> privileges)
    {
        List<String> opidList = new ArrayList<>();
        for(String line : privileges)
        {
            String[] opidArray = line.split(";");
            for(String opid : opidArray)
            {
                opidList.add(opid);
            }
        }
        HashSet<String> opidFilter = new HashSet<String>();
        for(String opid : opidList)
        {
            searchChildOpid(opid,opidFilter);
//            opidFilter.add(opid);
//            PrivilegeBean bean = PRIVIlEG_MAPPING.get(opid);
//            if(bean != null && bean.getInclude() != null && bean.getInclude().size() > 0)
//            {
//                for(String opItem : bean.getInclude())
//                {
//                    opidFilter.add(opItem);
//                }
//            }
        }
        return Arrays.asList(opidFilter.toArray(new String[opidFilter.size()]));
    }

    //递归收集子权限
    private static void searchChildOpid(String opid, Set<String> filter)
    {
            filter.add(opid);
            PrivilegeBean bean = PRIVIlEG_MAPPING.get(opid);
            if(bean != null && bean.getInclude() != null && bean.getInclude().size() > 0)
            {
                for(String opItem : bean.getInclude())
                {
                    //filter.add(opItem);
                    searchChildOpid(opItem,filter);
                }
            }
    }

    //检查用户权限
    //拥有的权限，后端控制器方法
    public static boolean CheckPrivilege(List<String> opids,HandlerMethod method)
    {

        //获取后端控制器方法的权限注解
        HashSet<String> target = null;
        Privilege privilege = null;
        try
        {
            privilege  = method.getMethod().getAnnotation(Privilege.class);
        }
        catch (Exception e)
        {
            log.error(e.toString());
            return false;
        }
        //没有配置权限
        if(privilege == null)
        {
            return true;
        } else {
            if(privilege.value().length == 0) {
                return true;
            }
            String[] opItems = privilege.value();
            target = new HashSet<>(Arrays.asList(opItems));
        }

        if(opids != null && target != null && opids.size()>0 && target.size() > 0) {
           return  opids.containsAll(target);
        }
        return false;
    }

    //权限Bean
    public static class PrivilegeBean
    {
        private  String opid;
        private  String name;
        private  String des;

        public String getOpid() {
            return opid;
        }

        public String getName() {
            return name;
        }

        public String getDes() {
            return des;
        }

        public List<String> getInclude() {
            return include;
        }

        private  List<String> include;

        public void setOpid(String opid) {
            this.opid = opid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public void setInclude(List<String> includeOpids) {
            this.include = includeOpids;
        }
    }

}
