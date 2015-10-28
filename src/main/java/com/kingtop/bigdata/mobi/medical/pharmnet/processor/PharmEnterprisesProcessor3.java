package com.kingtop.bigdata.mobi.medical.pharmnet.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingtop.bigdata.mobi.medical.pharmnet.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.pharmnet.repo.PharmEnterprises;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 读取公司名称，网站地址
 * 
 * @author hyq
 *
 */
public class PharmEnterprisesProcessor3 implements PageProcessor {
	// http://www.pharmnet.com.cn/company/111211/111411/1.html
	public static final String URL_POST = "http://www\\.pharmnet\\.com\\.cn/company/\\d+\\/\\d+\\/\\d+\\.html";
	private String hospital;
	private String hospital_url;
	public Map<String, String> key = new HashMap<String, String>();
	PharmEnterprises peRepo = new PharmEnterprises();

	public PharmEnterprisesProcessor3(String hospital, String hospital_url) {
		this.hospital = hospital;
		this.hospital_url = hospital_url;
	}

	private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

	@Override
	public void process(Page page) {
		// div class articleList 里的所有的地址 （下面两行在这个网站上性质一样）
		page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());
		// page.addTargetRequests(page.getHtml().xpath("//div[@class=\"articleList\"]").links().regex(URL_POST).all());
		List<Selectable> peTypes = page.getHtml()
				.xpath("//div[@id='list_left']/dl/dt|//div[@id='list_left']/dl/dd/ul/li/h1/").nodes();
		System.out.println("1111:" + peTypes.size());
		for (Selectable pe : peTypes) {
			String provinces3 = pe.xpath("/dt/a[3]/text()").toString();
			String sourceUrl3 = pe.xpath("/dt/a[3]/@href").toString();
			String provinces4 = pe.xpath("/dt/a[4]/text()").toString();
			String sourceUrl4 = pe.xpath("/dt/a[4]/@href").toString();
			String provinces5 = pe.xpath("/dt/a[5]/text()").toString();
			String sourceUrl5 = pe.xpath("/dt/a[5]/@href").toString();
			String provinces6 = pe.xpath("/dt/a[6]/text()").toString();
			String sourceUrl6 = pe.xpath("/dt/a[6]/@href").toString();
			String provinces7 = pe.xpath("/dt/a[7]/text()").toString();//城市
			String sourceUrl7 = pe.xpath("/dt/a[7]/@href").toString();
			String company = pe.xpath("/a/text()").toString();
			String companyurl = pe.xpath("/a/@href").toString();
			// System.out.println("company111:" + company);
			if (provinces7 == null && sourceUrl7 == null && company == null) {
				break;
			} else {
				if (provinces7 != null) {
					key.put("provinces3", provinces3);
					key.put("provinces4", provinces4);
					key.put("provinces5", provinces5);
					key.put("provinces6", provinces6);
					key.put("provinces7", provinces7);
					key.put("sourceUrl7", sourceUrl7);
				} else if (company != null) {
					peRepo.setDrugType1(key.get("provinces3"));
					peRepo.setDrugType2(key.get("provinces4"));
					peRepo.setDrugType3(key.get("provinces5"));
					peRepo.setProvinces(key.get("provinces6"));
					peRepo.setCitys(key.get("provinces7"));
					peRepo.setCitysUrl(key.get("sourceUrl7"));
					peRepo.setCompanyUrl(companyurl);
					peRepo.setCompany(company);

					DBObject object = (DBObject) JSON.parse(com.alibaba.fastjson.JSON.toJSONString(peRepo));
					MongoDaoImpl.getInstance().insert("pharmEnterprisesTest6", object);
					System.out.println(
							peRepo.getDrugType1() + ", " + peRepo.getProvinces() + "， 城市：" + peRepo.getCitys());
				}
			}
		}

	}

	// }

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

		Spider.create(new PharmEnterprisesProcessor3("医药企业", "http://www.pharmnet.com.cn/company/111211/112218/1.html"))
				.addUrl("http://www.pharmnet.com.cn/company/111211/112218/1.html").thread(5).run();

	}
}
