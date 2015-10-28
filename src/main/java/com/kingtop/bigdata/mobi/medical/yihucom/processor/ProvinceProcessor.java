package com.kingtop.bigdata.mobi.medical.yihucom.processor;

import java.util.List;

import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.kingtop.bigdata.mobi.medical.yihucom.repo.Area;
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
public class ProvinceProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

    @Override
    public void process(Page page) {
       
    	Selectable provinceBlock = page.getHtml().xpath("//div[@class='conditions-query fl']");
        List<Selectable> provinces = provinceBlock.xpath("//div[@class='right-con fr cr-666 lk-069 li-fl hidden']/ul/li").nodes();
        for(Selectable province : provinces){
        	Area area = new Area();
        	area.setLevel(1);
        	area.setName(province.xpath("//a/text()").toString());
        	area.setUrl(province.xpath("//a/@href").toString());
        	
        	System.out.println(area.getName() + " 好 " + area.getLevel() + " 好 " + area.getUrl());
        	
//        	DBObject object = (DBObject)JSON.parse(com.alibaba.fastjson.JSON.toJSONString(area));
//        	MongoDaoImpl.getInstance().insert("medical_area", object);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new ProvinceProcessor())
        	.addUrl("http://old.yihu.com/hospital/hospitallist.aspx")
        	.thread(5)
        	.run();
    }
}
