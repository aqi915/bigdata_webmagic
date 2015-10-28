package com.kingtop.bigdata.mobi.medical.yihucom.processor;

import java.util.*;

import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Area;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class CityProcessor implements PageProcessor {
	
	private String belong;
	
	public CityProcessor(String belong){
		this.belong = belong;
	}

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

    @Override
    public void process(Page page) {
       
        List<Selectable> citys = page.getHtml().xpath("//ul[@class='two-options fl pt5 pl20']/li").nodes();
        for(Selectable city : citys){
        	
        	Area area = new Area();
        	area.setBelong(belong);
        	area.setLevel(1);
        	area.setName(city.xpath("//a/text()").toString());
        	area.setUrl(city.xpath("//a/@href").toString());
        	
        	if("地方不限" != area.getName()){
//        		System.out.println(area.getBelong() + " " + area.getName() + " " + area.getUrl());
            	DBObject object = (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(area));
            	MongoDaoImpl.getInstance().insert("medical_area", object);
        	}
        	
//        	DBObject object = (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(area));
//        	MongoDaoImpl.getInstance().insert("medical_area", object);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
//    	Map<String,Object> map = new HashMap<String,Object>();
//    	map.put("level", 1);
//    	List<DBObject> records = MongoDaoImpl.getInstance().find("medical_area", map, -1);
//    	for(DBObject record : records){
////    		System.out.println(record.get("name") + " " + record.get("url"));
//    		  Spider.create(new CityProcessor((String)record.get("name")))
//					.addUrl((String)record.get("url"))
//					.thread(5).run();
//    	}
//    	
    	
        Spider.create(new CityProcessor("hei"))
        	.addUrl("http://old.yihu.com/hospital/hospitallist.aspx?pid=13")
        	.thread(5)
        	.run();
    }
}
