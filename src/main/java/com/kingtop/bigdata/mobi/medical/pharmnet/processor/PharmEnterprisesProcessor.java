package com.kingtop.bigdata.mobi.medical.pharmnet.processor;

import java.util.List;

import com.kingtop.bigdata.mobi.medical.pharmnet.repo.PharmEnterprises;
import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.yihucom.processor.OfficeProcessor;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Office;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 读取省份
 * 
 * @author hyq
 *
 */
public class PharmEnterprisesProcessor implements PageProcessor {

	private String hospital;
	private String hospital_url;

	public PharmEnterprisesProcessor(String hospital, String hospital_url) {
		this.hospital = hospital;
		this.hospital_url = hospital_url;
	}

	private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

	@Override
	public void process(Page page) {

		List<Selectable> peTypes = page.getHtml().xpath("//dd[@class='zs-cont']").nodes();
		for (Selectable pe : peTypes) {
			for (int i = 1; i < 36; i++) {
				String provinces = pe.xpath("/dd/a[" + i + "]/text()").toString();
				String sourceUrl = pe.xpath("/dd/a[" + i + "]/@href").toString();

				PharmEnterprises peRepo = new PharmEnterprises();
				peRepo.setProvinces(provinces);
				peRepo.setSourceUrl(sourceUrl);
				System.out.println(hospital + hospital_url + " provinces:" + provinces + " url:" + sourceUrl);

				DBObject object = (DBObject) JSON.parse(com.alibaba.fastjson.JSON.toJSONString(peRepo));
				MongoDaoImpl.getInstance().insert("pharmEnterprisesTest", object);
			}

		}

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		// List<DBObject> records =
		// MongoDaoImpl.getInstance().findAll("medical_office");
		//
		// for(DBObject record : records){
		// Spider.create(new
		// OfficeProcessor((String)record.get("name"),(String)record.get("url")))
		// .addUrl((String)record.get("url"))
		// .thread(5).run();
		//// System.out.println("name:"+(String)record.get("name")+(String)record.get("url"));
		// }

		Spider.create(new PharmEnterprisesProcessor("医药企业", "http://www.pharmnet.com.cn/company/"))
				.addUrl("http://www.pharmnet.com.cn/company/").thread(5).run();

	}
}
