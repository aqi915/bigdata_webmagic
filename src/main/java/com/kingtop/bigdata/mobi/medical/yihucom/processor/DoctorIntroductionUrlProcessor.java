package com.kingtop.bigdata.mobi.medical.yihucom.processor;

import java.util.*;

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

public class DoctorIntroductionUrlProcessor implements PageProcessor {

	// private String hospital;
	// private String hospital_url;
	// private String office_type;
	// private String office;
	// private String office_url;

	private DBObject dbObject;

	public DoctorIntroductionUrlProcessor(DBObject dbObject) {
		this.dbObject = dbObject;
		// hospital_url,
		// String office_type, String office, String office_url) {
		// this.hospital = hospital;
		// this.hospital_url = hospital_url;
		// this.office_type = office_type;
		// this.office = office;
		// this.office_url = office_url;
	}

	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

	@Override
	public void process(Page page) {

		List<Selectable> urlBlocks = page.getHtml()
				.xpath("//span[@class='link-007']").nodes();
		if (urlBlocks.size() > 0) {
			Selectable urlBlock = urlBlocks.get(0);
			String url = urlBlock.xpath("//a/@href").toString();
			
			DBObject query = new BasicDBObject();
			query.put("url", dbObject.get("url"));
			
			DBObject updateValue = new BasicDBObject();
			updateValue.put("intro_url", url);
			DBObject set = new BasicDBObject("$set",updateValue);
			MongoDaoImpl.getInstance().update("medical_doctor", query, set);
//			Iterator<String> it = dbObject.keySet().iterator();
//			while(it.hasNext()){
//				updateValue.
//			}
//			System.out.println(url);
		}

		// for(Selectable doctorBlock : doctorBlocks){
		// String name = doctorBlock.xpath("//span/a/text()").toString();
		// String url = doctorBlock.xpath("//span/a/@href").toString();
		//
		// String title = "";
		// List<Selectable> spans = doctorBlock.xpath("//span").nodes();
		// if(spans.size() == 2){
		// title = spans.get(1).regex(">(.*)<").toString();
		// }

		// System.out.println( name + " " + url + " " + title);

		// Doctor doctor = new Doctor();
		// doctor.setSource("医护网(www.yihu.com)");
		// doctor.setHospital(hospital);
		// doctor.setHospital_url(hospital_url);
		// doctor.setOffice_type(office_type);
		// doctor.setOffice(office);
		// doctor.setOffice_url(office_url);
		// doctor.setName(name);
		// doctor.setUrl(url);
		// doctor.setTitle(title);
		//
		// DBObject object =
		// (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(doctor));
		// MongoDaoImpl.getInstance().insert("medical_doctor", object);

		// }

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
//		Map<String, Object> key = new HashMap<String, Object>();
//		key.put("url",
//				"http://old.yihu.com/doctor/fj/590FD2FF9C8C4908BFBF4DDC352CCC7D.shtml?sn=6642");
//		List<DBObject> records = MongoDaoImpl.getInstance().find(
//				"medical_doctor", key, -1);
		List<DBObject> records = MongoDaoImpl.getInstance().findAll("medical_doctor");

		for (DBObject record : records) {
			Spider.create(new DoctorIntroductionUrlProcessor(record))
					.addUrl((String) record.get("url")).thread(1).run();
		}

		// Spider.create(new DoctorIntroductionUrlProcessor())
		// .addUrl("http://old.yihu.com/doctor/fj/590FD2FF9C8C4908BFBF4DDC352CCC7D.shtml?sn=6642")
		// .thread(5).run();

	}
}
