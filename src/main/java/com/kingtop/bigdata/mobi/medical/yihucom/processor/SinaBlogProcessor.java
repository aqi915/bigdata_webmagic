package com.kingtop.bigdata.mobi.medical.yihucom.processor;

import java.util.List;

import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Area;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Blog;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
/**
 * 知道固定网站，爬虫固定网页格式数据
 * @author hyq
 *
 */
public class SinaBlogProcessor implements PageProcessor {
	
//    public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";
//    public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";
    
	private Site site = Site
			.me()
//			.setDomain("blog.sina.com.cn")
			.setRetryTimes(3)
			.setSleepTime(0);
//			.setUserAgent(
//                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

	@Override
	public void process(Page page) {
//		Selectable provinceBlock = page.getHtml().xpath("//div[@class='conditions-query fl']");
		Selectable provinceBlock = page.getHtml().xpath("//div[@class='articleList']");
//		List<Selectable> provinces = provinceBlock.xpath("//div[@class='right-con fr cr-666 lk-069 li-fl hidden']/ul/li").nodes();
		List<Selectable> provinces = provinceBlock.xpath("//div[@class='articleCell SG_j_linedot1']/p/span/").nodes();

		for(Selectable province : provinces){
        	Blog blog = new Blog();
        	blog.setBlog_name(province.xpath("//a/text()").toString());
        	blog.setBlog_url(province.xpath("//a/@href").toString());
        	blog.setDesc(province.xpath("//a/@target").toString());
//        	System.out.println(area.getName() + " 好 " + area.getLevel() + " 好 " + area.getUrl());
        	System.out.println(blog.getBlog_name()+blog.getBlog_url()+"  你好："+blog.getDesc());
        	DBObject object = (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(blog));
        	MongoDaoImpl.getInstance().insert("blog_article", object);
        }
	}
	
	@Override
	public Site getSite() {
        return site;
	}

	public static void main(String[] args) {
		Spider.create(new SinaBlogProcessor())
        	.addUrl("http://blog.sina.com.cn/s/articlelist_1487828712_0_1.html")
//        	.addUrl("http://old.yihu.com/hospital/hospitallist.aspx")
        	.thread(5)
        	.run();
    }
	
	
	
}
