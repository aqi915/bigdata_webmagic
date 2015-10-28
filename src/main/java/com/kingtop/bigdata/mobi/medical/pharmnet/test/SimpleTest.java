package com.kingtop.bigdata.mobi.medical.pharmnet.test;

import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Pattern;

import com.mongodb.*;
import com.mongodb.util.JSON;

/**
 * <b>function:</b>MongoDB 简单示例
 * 
 * @author hoojo
 * @createDate 2011-5-24 下午02:42:29
 * @file SimpleTest.java
 * @package com.hoo.test
 * @project MongoDB
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SimpleTest {

	public static void main(String[] args) throws UnknownHostException, MongoException {
		Mongo mongo = new Mongo("192.168.3.107", 27017);
		DB db = mongo.getDB("mydb");
		DBCollection course = db.getCollection("myCollectionTest");// 对myMongoDB数据库中course集合进行操作
		// DBCursor cur = course.find();
		// System.out.println(cur.count());
		// 添加操作
		// 下面分别是创建文档的几种方式：1. .append() 2. .put() 3. 通过map 4. 将json转换成DBObject对象
		// DBObject english = new BasicDBObject().append("name",
		// "english").append("score", 5).append("id", 1);
		// course.insert(english);
		//
		// DBObject math = new BasicDBObject();
		// math.put("id", 2);
		// math.put("name", "math");
		// math.put("score", 10);
		// course.insert(math);
		//
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("name", "physics");
		// map.put("score", 10);
		// map.put("id", 3);
		// DBObject physics = new BasicDBObject(map);
		// course.insert(physics);
		//
		// String json = "{'name':'chemistry','score':10,'id':4}";
		// DBObject chemistry = (DBObject) JSON.parse(json);
		// course.insert(chemistry);
		//
		// List<DBObject> courseList = new ArrayList<DBObject>();
		// DBObject chinese = new BasicDBObject().append("name",
		// "chinese").append("score", 10).append("id", 5);
		// DBObject history = new BasicDBObject().append("name",
		// "history").append("score", 10).append("id", 6);
		// courseList.add(chinese);
		// courseList.add(history);
		// course.insert(courseList);

		// // 添加内嵌文档
		// String json2 = "
		// {'name':'english','score':10,'teacher':[{'name':'柳松','id':'1'},{'name':'柳松松','id':2}]}";
		// DBObject english2 = (DBObject) JSON.parse(json);
		// course.insert(english2);
		//
		// List<DBObject> list = new ArrayList<DBObject>();
		// list.add(new BasicDBObject("name", "柳松").append("id", 1));
		// list.add(new BasicDBObject("name", "柳松松").append("id", 2));
		// DBObject english3 = new BasicDBObject().append("name",
		// "english").append("score", 10).append("teacher", list);

		// // 查询
		// // 查询所有、查询一个文档、条件查询
		 DBCursor cur = course.find();
		 while (cur.hasNext()) {
		 DBObject document = cur.next();
		// System.out.println(document.get("name"));
		 }
		
		 DBObject document = course.findOne();
		 String name = (String) document.get("name");
		 System.out.println(name);

		// 查询学分=5的
		// DBObject query1 = new BasicDBObject("name","chemistry");
		// DBObject query2 = new BasicDBObject("id",new BasicDBObject("$gt",3));
		// DBCursor cur2 = course.find(query2);
		// // 条件表达式：$lt(<) $ne(<>) $lte(<=) $gte(>=) $in $nin $all $exists
		// // $or $nor $where $type等等
		// System.out.println("$gte try in");
		// try {
		// while (cur2.hasNext()) {
		// DBObject document1 = cur2.next();
		// System.out.println(document1.get("name"));
		// System.out.println("$gte trying");
		// }
		// } finally {
		// cur2.close();
		// }
		// System.out.println(cur2.count());
		// System.out.println("$gte try out");
		// System.out.println("查询id in 1/3/5：" + course.find(new
		// BasicDBObject("id", new BasicDBObject(QueryOperators.IN, new int[] {
		// 1, 3, 5 }))).toArray());
		// System.out.println("查询name exists 排序：" + course.find(new
		// BasicDBObject("name", new BasicDBObject(QueryOperators.EXISTS,
		// true))).toArray());
		// 查找并修改(其它字段都没了)
		// DBObject newDocument = course.findAndModify(new
		// BasicDBObject("score",2), new BasicDBObject("score",6));

		// 更新操作
		// q：更新条件 o:更新后的对象
		// course.update(new BasicDBObject("score",15), new
		// BasicDBObject("test",5));
		// course.update(new BasicDBObject("score",5), new
		// BasicDBObject("$set",new BasicDBObject("isRequired",true)));
		// 两个的区别是，第一个更新是将{"test":15}这个文档替换原来的文档，
		// 第二个更新添加了条件表达式$set，是在原来文档的基础上添加"isRequired"这个键
		// 条件表达式：$set $unset $push $inc $push $push $addToSet $pull $pullAll
		// $pop等等

		// 当_id相同时，执行save方法相当于更新操作 (只能用_id属性，才有更新效果)
		// course.save(new BasicDBObject("name","math").append("_id", 1));
		// course.save(new BasicDBObject("name","数学1").append("_id",
		// "560258f1b70f06184c33fc3d"));
		// course.update(new BasicDBObject("_id","560258f1b70f06184c33fc3d"),
		// new BasicDBObject("$set",new BasicDBObject("name","历史")));

		// 删除符合条件的文档
		course.remove(new BasicDBObject("score", "4"));

		// 删除集合及所有文档 （整个表删除了）
		// course.drop();
		// <span style="font-family:Arial, Helvetica, sans-serif;"><span
		// style="white-space: normal;"></span></span>

		// System.out.println("findAndModify: " + course.findAndModify(new
		// BasicDBObject("name", "历史"), new BasicDBObject("score", "4")));

		// 模糊匹配
//		DBCollection dbColl = db.getCollection("myCollectionTest");
//		Pattern pattern = Pattern.compile("^.*name8.*$", Pattern.CASE_INSENSITIVE);
//		BasicDBObject query = new BasicDBObject();
//		query.put("name", pattern);
//		BasicDBObject sort = new BasicDBObject();
//		// 1,表示正序； －1,表示倒序
//		sort.put("name", 1);
//		DBCursor cur = dbColl.find(query).sort(sort);
//		int count = 0;
//		while (cur.hasNext()) {
//			DBObject obj = cur.next();
//			System.out.print("name=" + obj.get("name"));
//			System.out.print(",email=" + obj.get("email"));
//			System.out.println(",passwd=" + obj.get("passwd"));
//			count++;
//		}
//		System.out.println("共有： " + count + "个");

	}
}
