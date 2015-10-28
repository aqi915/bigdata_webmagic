package com.kingtop.bigdata.mobi.medical.yihucom.mongo;


import java.util.List;
import java.util.Map;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public interface MongoDao {

	DB getDb();
	
	DBCollection getCollection(String collectionName);
	
	boolean insert(String collectionName,DBObject object); 
	
	boolean update(String collectionName,DBObject query,DBObject updateValue); 
	
	List<DBObject> find(String collectionName, Map<String,Object> map, int num);
	
	List<DBObject> findAll(String collectionName);
	
}
