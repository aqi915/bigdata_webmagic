package com.kingtop.bigdata.mobi.medical.yihucom.processor;

import java.util.*;

import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Doctor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class DoctorProcessor implements PageProcessor {

	private String hospital;
	private String hospital_url;
	private String office_type;
	private String office;
	private String office_url;

	public DoctorProcessor(String hospital, String hospital_url,
			String office_type, String office, String office_url) {
		this.hospital = hospital;
		this.hospital_url = hospital_url;
		this.office_type = office_type;
		this.office = office;
		this.office_url = office_url;
	}

	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

	@Override
    public void process(Page page) {
    	
       	List<Selectable> doctorBlocks = page.getHtml().xpath("//div[@class='ys_js']").nodes();
       	for(Selectable doctorBlock : doctorBlocks){
        	String name = doctorBlock.xpath("//span/a/text()").toString();
        	String url = doctorBlock.xpath("//span/a/@href").toString();
        	
        	String title = "";
        	List<Selectable> spans = doctorBlock.xpath("//span").nodes();
        	if(spans.size() == 2){
        		
//        	正则表达式	
        		title = spans.get(1).regex(">(.*)<").toString();
        	}
        	
//        	System.out.println( name + " " + url + " " + title);
            	
        	Doctor doctor = new Doctor();
        	doctor.setSource("医护网(www.yihu.com)");
        	doctor.setHospital(hospital);
        	doctor.setHospital_url(hospital_url);
        	doctor.setOffice_type(office_type);
        	doctor.setOffice(office);
        	doctor.setOffice_url(office_url);
        	doctor.setName(name);
        	doctor.setUrl(url);
        	doctor.setTitle(title);
            	
        	DBObject object = (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(doctor));
        	MongoDaoImpl.getInstance().insert("medical_doctor", object);
       		
       	}
        
       
    }

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
//		 List<DBObject> records =
//		 MongoDaoImpl.getInstance().findAll("medical_office");
//		 for(DBObject record : records){
//		 Spider.create(new DoctorProcessor((String)record.get("hospital"),(String)record.get("hospital_url"),
//				 (String)record.get("offic_type"),(String)record.get("name"),(String)record.get("url")))
//			 .addUrl((String)record.get("url"))
//			 .thread(5).run();
//		 }

		Spider.create(
				new DoctorProcessor(
						"南京军区福州总医院",
						"http://old.yihu.com/hospital/fj/2DDE6ADD434F4E5B8A43B7BDDD775472.shtml",
						"内科", "肾内科",
						"http://old.yihu.com/dept/0306/2F4B4C436ACA47FBAC9B292C8D8AF70F.shtml"))
				.addUrl("http://old.yihu.com/dept/0306/2F4B4C436ACA47FBAC9B292C8D8AF70F.shtml")
				.thread(5).run();

	}
}
