package com.kingtop.bigdata.mobi.medical.yihucom.processor;

import java.util.List;

import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Office;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
/**
 * 对mongodb表里的网站进行抓取,table表的抓取
 * @author hyq
 *
 */
public class OfficeProcessor implements PageProcessor {
	
	private String hospital;
	private String hospital_url;
	
	public OfficeProcessor(String hospital,String hospital_url){
		this.hospital = hospital;
		this.hospital_url = hospital_url;
	}

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

    @Override
    public void process(Page page) {
    	
       	List<Selectable> officeTypes = page.getHtml().xpath("//table[@class='kslist-tb cr-999 link-007']").nodes();
       	for(Selectable officeType : officeTypes){
       		String officeTypeStr = officeType.xpath("//td[@class='w100']/text()").toString();
       		List<Selectable> offices = officeType.xpath("//div[@class='mt20']/ul/li").nodes();
       		for(Selectable office : offices){
            	String name = office.xpath("//span/a/text()").toString();
            	String url = office.xpath("//span/a/@href").toString();
//            	System.out.println(hospital + " " + officeTypeStr + " " + name + " " + url );
            	
            	Office officeRepo = new Office();
            	officeRepo.setSource("医护网(www.yihu.com)");
            	officeRepo.setHospital(hospital);
            	officeRepo.setHospital_url(hospital_url);
            	officeRepo.setOffic_type(officeTypeStr);
            	officeRepo.setName(name);
            	officeRepo.setUrl(url);
            	
            	DBObject object = (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(officeRepo));
            	MongoDaoImpl.getInstance().insert("medical_office_findall", object);
       		}
       		
       	}
        
//        Selectable nextBlock = page.getHtml().xpath("//li[@class='next']");
//        if(null != nextBlock){
//        	page.addTargetRequest(nextBlock.xpath("//a/@href").toString());
//        }
       
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
    	List<DBObject> records = MongoDaoImpl.getInstance().findAll("medical_office");
    	
    	for(DBObject record : records){
    		  Spider.create(new OfficeProcessor((String)record.get("name"),(String)record.get("url")))
					.addUrl((String)record.get("url"))
					.thread(5).run();
//    		  System.out.println("name:"+(String)record.get("name")+(String)record.get("url"));
    	}
    	
//		  Spider.create(new OfficeProcessor("漳州市医院","http://old.yihu.com/hospital/fj/1FD024241F8F4D4E9989D9215A1B4F09.shtml"))
//				.addUrl("http://old.yihu.com/hospital/fj/1FD024241F8F4D4E9989D9215A1B4F09.shtml")
//				.thread(5).run();
    		  
    }
}
