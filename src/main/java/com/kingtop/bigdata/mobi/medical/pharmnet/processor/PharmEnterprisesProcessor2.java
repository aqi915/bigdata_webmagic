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
 * 读取地市
 * 
 * @author hyq
 *
 */
public class PharmEnterprisesProcessor2 implements PageProcessor {

	private String hospital;
	private String hospital_url;

	public PharmEnterprisesProcessor2(String hospital, String hospital_url) {
		this.hospital = hospital;
		this.hospital_url = hospital_url;
	}

	private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

	@Override
	public void process(Page page) {

		List<Selectable> peTypes = page.getHtml().xpath("//div[@class='smenu_dq']").nodes();
		for (Selectable pe : peTypes) {
			for (int i = 1; i < 30; i++) {
				String provinces = pe.xpath("/div/a[" + i + "]/text()").toString();
				String sourceUrl = pe.xpath("/div/a[" + i + "]/@href").toString();
				
				if (provinces == null && sourceUrl == null) {
					break;
				} else {
					PharmEnterprises peRepo = new PharmEnterprises();
					peRepo.setProvinces(hospital);
					peRepo.setCitys(provinces);
					peRepo.setSourceUrl(sourceUrl);
					System.out.println(hospital + hospital_url + " provinces:" + provinces + " url:" + sourceUrl);

					DBObject object = (DBObject) JSON.parse(com.alibaba.fastjson.JSON.toJSONString(peRepo));
					MongoDaoImpl.getInstance().insert("pharmEnterprisesTest1", object);
				}
			}

		}

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		List<DBObject> records = MongoDaoImpl.getInstance().findAll("pharmEnterprisesTest");

		for (DBObject record : records) {
			Spider.create(
					new PharmEnterprisesProcessor2((String) record.get("provinces"), (String) record.get("sourceUrl")))
					.addUrl((String) record.get("sourceUrl")).thread(5).run();
			// System.out.println("name:"+(String)record.get("name")+(String)record.get("url"));
		}

		// Spider.create(new PharmEnterprisesProcessor2("医药企业",
		// "http://www.pharmnet.com.cn/company/"))
		// .addUrl("http://www.pharmnet.com.cn/company/").thread(5).run();

	}
}
