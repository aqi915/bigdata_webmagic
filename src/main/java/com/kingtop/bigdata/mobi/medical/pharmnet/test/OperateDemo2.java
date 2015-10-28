package com.kingtop.bigdata.mobi.medical.pharmnet.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
 
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
 
public class OperateDemo2 {
 
	/**
	 * @return
	 * @throws Exception
	 */
	public static MongoClient getMongoClient()throws Exception{
		try {
			//===================================================//
			List<ServerAddress> serverList = new ArrayList<ServerAddress>();
			serverList.add(new ServerAddress("192.168.100.141", 27017));
			//===================================================//
			List<MongoCredential> mcList = new ArrayList<MongoCredential>();
			String username = "sa";
			String database = "admin";
			char[] password = "sa".toCharArray();
			mcList.add(MongoCredential.createCredential(username, database,password));
			//===================================================//
			MongoClientOptions.Builder builder = MongoClientOptions.builder();
			// 与目标数据库能够建立的最大connection数量为50 
			builder.connectionsPerHost(50);  
			// 如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待  
			builder.threadsAllowedToBlockForConnectionMultiplier(50); 
			// 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟 
			// 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception 
			// 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败 
			builder.maxWaitTime(1000*60*2);  
			// 与数据库建立连接的timeout设置为1分钟  
			builder.connectTimeout(1000*60*1);   
			//===================================================//
			MongoClientOptions mco = builder.build(); 
			return new MongoClient(serverList, mcList, mco);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * @param dbname
	 * @return
	 * @throws Exception
	 */
	public static DB getDB(String dbname) throws Exception{
		return getMongoClient().getDB(dbname);
	}
	
	/**
	 * @param db
	 */
	public static void collections(DB db){
		Set<String> colls = db.getCollectionNames();
		for (String collName : colls) {
		    System.out.println(collName);
		}
	}
	
	/**
	 * 记录总数查询
	 * 
	 * @param db
	 * @param name
	 */
	public static void count(DB db,String name){
		DBCollection dbColl = db.getCollection(name);
		int count = dbColl.find().count();
	    System.out.println("共有： " + count + "个");
	}
	
	
	/**
	 * 模糊查询
	 * 
	 * @param db
	 * @param name
	 */
	public static void query(DB db,String name){
		DBCollection dbColl = db.getCollection(name);
		//完全匹配
		//Pattern pattern = Pattern.compile("^name$", Pattern.CASE_INSENSITIVE);
		//右匹配
		//Pattern pattern = Pattern.compile("^.*name$", Pattern.CASE_INSENSITIVE);
		//左匹配
		//Pattern pattern = Pattern.compile("^name.*$", Pattern.CASE_INSENSITIVE);
		//模糊匹配
		Pattern pattern = Pattern.compile("^.*name8.*$", Pattern.CASE_INSENSITIVE);
		BasicDBObject query = new BasicDBObject();
		query.put("name",pattern);
		BasicDBObject sort = new BasicDBObject();
		// 1,表示正序； －1,表示倒序
		sort.put("name",1);
		DBCursor cur = dbColl.find(query).sort(sort);
		int count = 0;
	    while (cur.hasNext()) {
	    	DBObject obj = cur.next();
	    	System.out.print("name=" + obj.get("name"));
	    	System.out.print(",email=" + obj.get("email"));
	    	System.out.println(",passwd=" + obj.get("passwd"));
	    	count ++;
	    }
	    System.out.println("共有： " + count + "个");
	}
	
 
	/**
	 * 分页查询
	 * 
	 * @param db
	 * @param name
	 * @param start
	 * @param pageSize
	 */
	public static void page(DB db,String name,int start,int pageSize){
		DBCollection dbColl = db.getCollection(name);
		BasicDBObject sort = new BasicDBObject();
		sort.put("name",1);
		DBCursor cur = dbColl.find().sort(sort).skip(start).limit(pageSize);;
		int count = 0;
	    while (cur.hasNext()) {
	    	DBObject obj = cur.next();
	    	System.out.print("name=" + obj.get("name"));
	    	System.out.print(",email=" + obj.get("email"));
	    	System.out.println(",passwd=" + obj.get("passwd"));
	    	count ++;
	    }
	    System.out.println("共有： " + count + "个");
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		DB db = getDB("mydb");
		collections(db);
		String name = "users";
		System.out.println("count()=================================================");
		count(db,name);
		System.out.println("query()=================================================");
		query(db,name);
		System.out.println("page()=================================================");
		page(db,name,10, 10);
	}
 
}