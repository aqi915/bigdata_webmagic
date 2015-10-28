package com.kingtop.bigdata.mobi.medical.yihucom.test;

import com.kingtop.bigdata.mobi.medical.yihucom.mongo.MongoDaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoUpdateTest {

	public static void main(String [] args){
		
//		DBObject math = new BasicDBObject();
//		math.put("id", 2);
//		math.put("name", "math");
//		math.put("score", 10);
//		course.insert(math);
		
		DBObject query = new BasicDBObject();
		query.put("name", "math");
		
		DBObject updateValue = new BasicDBObject();
		updateValue.put("skill", "test_skill");
		updateValue.put("desc", "test_desc");
		DBObject set = new BasicDBObject("$set",updateValue);
		MongoDaoImpl.getInstance().update("myCollectionupdate", query, set);
	}
	
	
}
