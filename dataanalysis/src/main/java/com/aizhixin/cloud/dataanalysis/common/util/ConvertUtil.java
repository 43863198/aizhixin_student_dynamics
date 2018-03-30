package com.aizhixin.cloud.dataanalysis.common.util;

import org.apache.log4j.Logger;
import org.dom4j.*;

import java.util.*;
/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * 数据结构转换类，如:javaBean转换为xml,xml转换为javaBean
 * @Date: 2018-01-04
 */

public class ConvertUtil
{
    private static Logger log = Logger.getLogger(ConvertUtil.class);

    private static Object xml2map(Element element) {

        Map<String, Object> map = new HashMap<String, Object>();
        List<Element> elements = element.elements();
        if (elements.size() == 0) {
            map.put(element.getName(), element.getText());
            if (!element.isRootElement()) {
                return element.getText();
            }
        } else if (elements.size() == 1) {
            map.put(elements.get(0).getName(), xml2map(elements.get(0)));
        } else if (elements.size() > 1) {
            // 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的
            // 构造一个map用来去重
            Map<String, Element> tempMap = new HashMap<String, Element>();
            for (Element ele : elements) {
                tempMap.put(ele.getName(), ele);
            }
            Set<String> keySet = tempMap.keySet();
            for (String string : keySet) {
                Namespace namespace = tempMap.get(string).getNamespace();
                List<Element> elements2 = element.elements(new QName(string, namespace));
                // 如果同名的数目大于1则表示要构建list
                if (elements2.size() > 1) {
                    List<Object> list = new ArrayList<Object>();
                    for (Element ele : elements2) {
                        list.add(xml2map(ele));
                    }
                    map.put(string, list);
                } else {
                    // 同名的数量不大于1则直接递归去
                    map.put(string, xml2map(elements2.get(0)));
                }
            }
        }

        return map;
    }

    /**
     * xml转为HashMap
     * @param xml
     * @return
     */
    public static Map xml2map(String xml) {
        Document doc = null;
        try
        {
            doc = DocumentHelper.parseText(xml);
        }
        catch (Exception ex)
        {
            log.error(ex.toString());
            return null;
        }

        return (Map)xml2map(doc.getRootElement());
    }

    public static void main(String[] ss)
    {
        String str ="<xml><return_code><![CDATA[SUCCESS]]></return_code>" +
                "<return_msg><![CDATA[OK]]></return_msg>" +
                "<appid><![CDATA[wxec068af78840da2c]]></appid>" +
                "<mch_id><![CDATA[1381896102]]></mch_id>" +
                "<nonce_str><![CDATA[sZFo5X62RvKawwRO]]></nonce_str>" +
                "<sign><![CDATA[1DB3FDB312656766F19DE7347CBD1FEC]]></sign>" +
                "<result_code><![CDATA[SUCCESS]]></result_code>" +
                "<prepay_id><![CDATA[wx2016083109065070a401c39d0066102512]]></prepay_id>" +
                "<trade_type><![CDATA[NATIVE]]></trade_type>" +
                "<code_url><![CDATA[weixin://wxpay/bizpayurl?pr=Bm6M372]]></code_url>" +
                "</xml>";
        System.out.println(xml2map(str));
    }
}
