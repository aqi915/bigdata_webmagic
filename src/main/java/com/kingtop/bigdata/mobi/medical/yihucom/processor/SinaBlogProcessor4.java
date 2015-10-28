package com.kingtop.bigdata.mobi.medical.yihucom.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Area;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Blog;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * mongodb 数据库判断 $exists(title 字段是否存在)
 * 
 * @author hyq
 *
 */
public class SinaBlogProcessor4 implements PageProcessor {

	private Site site = Site.me()
			// .setDomain("blog.sina.com.cn")
			.setRetryTimes(3).setSleepTime(0);
	// .setUserAgent(
	// "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31
	// (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

	@Override
	public void process(Page page) {

		System.out.println("文章页");
		String title = page.getHtml().xpath("//div[@class='articalTitle']/h2/text()").toString();
		// page.putField("content",
		// page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']"));
		String content = page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']").toString();
		String date = page.getHtml().xpath("//div[@id='articlebody']//span[@class='time SG_txtc']").regex("\\((.*)\\)")
				.toString();

		System.out.println(title + date + content);

		Blog blog = new Blog();
		blog.setTitle(title);
		blog.setDesc(date);
		blog.setBlog_name(content);
		DBObject object = (DBObject) JSON.parse(com.alibaba.fastjson.JSON.toJSONString(blog));
		MongoDaoImpl.getInstance().insert("blog_article4", object);
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Map<String, Object> key = new HashMap<String, Object>();
		// key.put("intro_url",new BasicDBObject("$exists",true));
		key.put("title", new BasicDBObject("$exists", false));
		List<DBObject> records = MongoDaoImpl.getInstance().find("blog_article1", key, -1);

		for (DBObject record : records) {
			if ((String) record.get("blog_url") != null & (String) record.get("blog_url") != "") {
				Spider.create(new SinaBlogProcessor4()).addUrl((String) record.get("blog_url")).thread(5).run();
				System.out.println("name:" + (String) record.get("blog_name") + (String) record.get("blog_url"));
			}
		}
	}

}
