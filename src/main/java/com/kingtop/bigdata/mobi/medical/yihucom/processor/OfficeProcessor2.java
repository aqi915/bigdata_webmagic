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
 * 对mongodb表里的网站进行抓取
 * 
 * @author hyq
 *
 */
public class OfficeProcessor2 implements PageProcessor {

	private String hospital;
	private String hospital_url;

	public OfficeProcessor2(String hospital, String hospital_url) {
		this.hospital = hospital;
		this.hospital_url = hospital_url;
	}

	private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

	@Override
	public void process(Page page) {
		// System.out.println("1111111111111111111111111");

		/*
		 * List<Selectable> officeTypes = page.getHtml().xpath(
		 * "//table[@class='kslist-tb cr-999 link-007']").nodes();
		 * for(Selectable officeType : officeTypes){ String officeTypeStr =
		 * officeType.xpath("//td[@class='w100']/text()").toString();
		 * List<Selectable> offices =
		 * officeType.xpath("//div[@class='mt20']/ul/li").nodes();
		 * for(Selectable office : offices){ String name =
		 * office.xpath("//span/a/text()").toString(); String url =
		 * office.xpath("//span/a/@href").toString(); //
		 * System.out.println(hospital + " " + officeTypeStr + " " + name + " "
		 * + url );
		 * 
		 * Office officeRepo = new Office();
		 * officeRepo.setSource("医护网(www.yihu.com)");
		 * officeRepo.setHospital(hospital);
		 * officeRepo.setHospital_url(hospital_url);
		 * officeRepo.setOffic_type(officeTypeStr); officeRepo.setName(name);
		 * officeRepo.setUrl(url);
		 */
		Selectable provinceBlock = page.getHtml().xpath("//div[@class=' mt5 mb40 hidden']");
		List<Selectable> officeTypes = page.getHtml()
				.xpath("//div[@class='fl ys_box mr15']//div[@class='ml10 fl ys_js mt20 cr-666']").nodes();
		System.out.println("outputofficeTypes:");

		// for(Selectable officeType : officeTypes){
		// System.out.println("outputofficeType:");
		//// String officeTypeStr = officeType.xpath("//td[@class=' link-007
		// mr10']/text()").toString();
		// List<Selectable> offices = officeType.xpath("//div[@class='fl ys_box
		// mr15']//div[@class='ml10 fl ys_js mt20 cr-666']").nodes();
		for (Selectable office : officeTypes) {
			System.out.println("office:");
			String name = office.xpath("//span/a/text()").toString();

			List<Selectable> offices = office.xpath("//span[@class='mr10']").nodes();
			for (Selectable office1 : offices) {

				List<Selectable> spans = office1.xpath("//span").nodes();
	        	if(spans.size() == 1){
	        		
//	        	正则表达式	
	        		String name1 = spans.get(0).regex(">(.*)<").toString();
	        		System.out.println("name1"+name1);
	        	}
				
				
//				String name1 = office1.xpath("//span[@class='link-007']/a/text()").toString();
//				String name2 = office1.xpath("//span[@class='mr10']/text()").toString();
				// System.out.println("name1:" + name1 + "， name2：" + name2);
//				if (name1 != null & name1 != "") {
//					System.out.println("name1:" + name1);
//				}
//
//				if (name2 != null & name2 != "") {
//					System.out.println("name2：" + name2);
//				}
			}
			
			
//			List<Selectable> spans = office.xpath("//div[@class='lh18']").nodes();
//        	if(spans.size() == 2){
//        		
////        	正则表达式	
////        		title = spans.get(1).regex(">(.*)<").toString();
//        		String name3 = spans.get(1).regex(">(.*)<").toString();
//				System.out.println("name3:" + name3);
//        	}
			
			List<Selectable> offices1 = office.xpath("//div[@class='lh18']").nodes();

			for (Selectable office1 : offices1) {
				// System.out.println("name3");
				// String name3 =
				// office.xpath("//div[@class='lh18']/text()").toString();
				String name3 = office.xpath("//div[@class='cr-999']/text()").toString();
//				String name3 = office.xpath("//div").toString();
				System.out.println("name3:" + name3);
			}

			
			
			// String url = office.xpath("//span/a/@href").toString();
			// System.out.println(hospital + " " + officeTypeStr + " " + name +
			// " " + url );

			// Office officeRepo = new Office();
			// officeRepo.setSource("医护网(www.yihu.com)");
			// officeRepo.setHospital(hospital);
			// officeRepo.setHospital_url(hospital_url);
			//// officeRepo.setOffic_type(officeTypeStr);
			// officeRepo.setName(name);
			// officeRepo.setUrl(url);

			// System.out.println("outputname:"+name+","+name1+","+name2);
			// DBObject object =
			// (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(officeRepo));
			// MongoDaoImpl.getInstance().insert("medical_office_findall",
			// object);
		}

	}

	// Selectable nextBlock = page.getHtml().xpath("//li[@class='next']");
	// if(null != nextBlock){
	// page.addTargetRequest(nextBlock.xpath("//a/@href").toString());
	// }

	// }

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		/*
		 * List<DBObject> records =
		 * MongoDaoImpl.getInstance().findAll("medical_office");
		 * 
		 * for(DBObject record : records){ Spider.create(new
		 * OfficeProcessor2((String)record.get("name"),(String)record.get("url")
		 * )) .addUrl((String)record.get("url")) .thread(5).run(); //
		 * System.out.println("name:"+(String)record.get("name")+(String)record.
		 * get("url")); }
		 */

		Spider.create(
				new OfficeProcessor2("漳州市医院", "http://old.yihu.com/dept/0307/A276EFB910654F13890C95D79C3F4F61.shtml"))
				.addUrl("http://old.yihu.com/dept/0307/A276EFB910654F13890C95D79C3F4F61.shtml").thread(5).run();

	}
}
