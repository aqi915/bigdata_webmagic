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
 * 分页（加入网站正则表达式）的网站抽取
 * @author hyq
 *
 */
public class SinaBlogProcessor2 implements PageProcessor {
	
    public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";
    public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";
    
	private Site site = Site
			.me()
//			.setDomain("blog.sina.com.cn")
			.setRetryTimes(3)
			.setSleepTime(0);
//			.setUserAgent(
//                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

	@Override
	public void process(Page page) {
		
		 //列表页
        if (page.getUrl().regex(URL_LIST).match()) {
        	//div class articleList 里的所有的地址  （下面两行在这个网站上性质一样）
//            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());
            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"articleList\"]").links().regex(URL_POST).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            System.out.println("列表页");
        //文章页
        } else {
        	System.out.println("列表页1");
        	String title1= page.getHtml().xpath("//div[@class='articalTitle']/h2/text()").toString();
        	System.out.println("文章页");
        	String title= page.getHtml().xpath("//div[@class='articalTitle']/h2/text()").toString();
//        	page.putField("content", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']"));
            String content= page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']").toString();
        	String date= page.getHtml().xpath("//div[@id='articlebody']//span[@class='time SG_txtc']").regex("\\((.*)\\)").toString();
            	
        	System.out.println(title+date+content);
        	
//            	Blog blog = new Blog();
//            	blog.setTitle(title);
//            	blog.setDesc(date);
//            	blog.setBlog_name(content);
//            	DBObject object = (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(blog));
//            	MongoDaoImpl.getInstance().insert("blog_article1", object);
            }
            
        }


	
	@Override
	public Site getSite() {
        return site;
	}

	public static void main(String[] args) {
        Spider.create(new SinaBlogProcessor2())
    	.addUrl("http://blog.sina.com.cn/s/articlelist_1487828712_0_1.html")
//    	.addUrl("http://old.yihu.com/hospital/hospitallist.aspx")
    	.thread(5)
    	.run();
    }
	
	
	
}
