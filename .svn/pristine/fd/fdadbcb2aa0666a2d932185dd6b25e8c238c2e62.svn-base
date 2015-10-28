package com.kingtop.bigdata.mobi.medical.yihucom.processor;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Doctor;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class DoctorIntroductionProcessor implements PageProcessor {

	// private String hospital;
	// private String hospital_url;
	// private String office_type;
	// private String office;
	// private String office_url;

//	private DBObject dbObject;
	private String intro_url;

	public DoctorIntroductionProcessor(String intro_url) {
		this.intro_url = intro_url;
	}

	private Site site = Site.me().setRetryTimes(1).setSleepTime(100);

	@Override
	public void process(Page page) {

		List<Selectable> blocks = page.getHtml()
				.xpath("//td[@class='p15']").nodes();
		if(blocks.size() == 2){
		
			String skill = blocks.get(0).xpath("//td/text()").toString();
			String desc = blocks.get(1).xpath("//td/text()").toString();
//			System.out.println(skill + " " + desc);
			DBObject query = new BasicDBObject();
			query.put("intro_url", intro_url);
			
			DBObject updateValue = new BasicDBObject();
			updateValue.put("skill", skill);
			updateValue.put("desc", desc);
			DBObject set = new BasicDBObject("$set",updateValue);
			MongoDaoImpl.getInstance().update("medical_doctor", query, set);
		}
		
//		if (urlBlocks.size() > 0) {
//			Selectable urlBlock = urlBlocks.get(0);
//			String url = urlBlock.xpath("//a/@href").toString();
//			
//			DBObject query = new BasicDBObject();
//			query.put("url", dbObject.get("url"));
//			
//			DBObject updateValue = new BasicDBObject();
//			updateValue.put("intro_url", url);
//			DBObject set = new BasicDBObject("$set",updateValue);
//			MongoDaoImpl.getInstance().update("medical_doctor", query, set);
//		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Map<String, Object> key = new HashMap<String, Object>();
//		key.put("intro_url",new BasicDBObject("$exists",true));
		key.put("skill",new BasicDBObject("$exists",false));
//		key.put("desc",new BasicDBObject("$exists",false));
		
		List<DBObject> records = MongoDaoImpl.getInstance().find(
				"medical_doctor", key, -1);
		
		int numThread = 10;
		int size = records.size();
		int subSize = size/numThread;
		ExecutorService executorService = Executors.newFixedThreadPool(numThread);
		for(int i = 0; i != numThread; i++){
			final List<DBObject> subList;
			if(i != numThread - 1){
				subList = records.subList(subSize * i,subSize * (i+1));
			}else{
				subList = records.subList(subSize * i,size);
			}
			
			executorService.execute(new SpiderRunnable(subList));
		}
		executorService.shutdown();
		

//		 Spider.create(new DoctorIntroductionProcessor())
//		 	.addUrl("http://old.yihu.com/doctor/fj/1F52BE7CDA7343D29607C1F0FD9896B9_introduction.shtml")
//		 	.thread(1).run();
	}
}

class SpiderRunnable implements Runnable{
	
	final List<DBObject> list;
	
	public SpiderRunnable(List<DBObject> list){
		this.list = list;
	}
	
	@Override
	public void run() {
//		int sum = 0;
		for (DBObject record : list) {
			String url = (String) record.get("intro_url");
			if(null != url && !"".equals(url)){
				Spider.create(new DoctorIntroductionProcessor(url))
				.addUrl(url).thread(1).run();
			}
//			sum++;
			
		}
//		System.out.println(this.toString() + " " + sum);
	}
	
}
