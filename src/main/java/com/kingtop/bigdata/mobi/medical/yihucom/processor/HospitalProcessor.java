package com.kingtop.bigdata.mobi.medical.yihucom.processor;

import java.util.*;

import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Hospital;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
/**
 * 查询表里的url(可以限制条数和字段内容选择)，进行webmagic 抓取
 * @author hyq
 *
 */
public class HospitalProcessor implements PageProcessor {
	
	private String area;
	
	public HospitalProcessor(String area){
		this.area = area;
	}

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

    @Override
    public void process(Page page) {
    	
       	Selectable hospitalListBlock = page.getHtml().xpath("//div[@class='left-box fl']");
        List<Selectable> hospitalNodes = hospitalListBlock.xpath("//div/ul/li").nodes();
        for(Selectable hospitalNode : hospitalNodes){
        	String name = hospitalNode.xpath("//div[@class='lh24 fl']/h2/a/text()").toString();
        	String url = hospitalNode.xpath("//div[@class='lh24 fl']/h2/a/@href").toString();
        	String level = hospitalNode.xpath("//div[@class='lh24 fl']/span[@class='pl10 fl']").regex("\\[(.*)\\]").toString();
        	String address = hospitalNode.xpath("//div[@class='cr-666']/span/text()").toString();
        	String [] parts = address.split("：");
        	if(parts.length > 1){
        		address = address.split("：")[1];
        	}else{
        		address = "";
        	}
        	
//        	System.out.println(name + " " + url + " " + level );
//        	System.out.println(address);
        	Hospital hospital = new Hospital();
        	hospital.setSource("医护网(www.yihu.com)");
        	hospital.setArea(area);
        	hospital.setUrl(url);
        	hospital.setName(name);
        	hospital.setLevel(level);
        	hospital.setAddress(address);
        	
        	DBObject object = (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(hospital));
        	MongoDaoImpl.getInstance().insert("medical_hospital", object);
        	
        }
        
        Selectable nextBlock = page.getHtml().xpath("//li[@class='next']");
        if(null != nextBlock){
        	page.addTargetRequest(nextBlock.xpath("//a/@href").toString());
        }
       
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("level", 1);
    	List<DBObject> records = MongoDaoImpl.getInstance().find("medical_area", map, -1);
    	for(DBObject record : records){
    		  Spider.create(new HospitalProcessor((String)record.get("name")))
					.addUrl((String)record.get("url"))
					.thread(5).run();
    		  System.out.println((String)record.get("url"));
    	}
    	
//    	 Spider.create(new HospitalListProcessor(""))
//			.addUrl("http://old.yihu.com/hospital/hospitallist.aspx?pid=13&cid=119")
//			.thread(5).run();
    	
    		  
    }
}
