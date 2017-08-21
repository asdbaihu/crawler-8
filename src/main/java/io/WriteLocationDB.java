package io;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;

import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Administrator on 2017/5/30.
 * author: 阿朕.
 *
 * 创建mongoDB连接，并且使用transferToDB（）方法将JobPageProcessor中得到的page传输至数据库。
 * public static WriteLocationDB getInstance()方法用以生成数据库对象并避免重复生成。
 */
public class WriteLocationDB {
    private Map<String, Object> map = new HashMap<String, Object>();
    private MongoClient mongoClient;
    private DB db = null;
    private DBCollection dbCollection = null;
    private DBObject dbObject = null;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String DATABASE = "admin";
    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT  = 27017;
    private static final String collectionName  = "jobSoftMainCity3";
    private static WriteLocationDB instance;

    //初始化数据库连接
    public WriteLocationDB(){
        try {
            ServerAddress serverAddress = new ServerAddress(HOSTNAME,PORT);
            mongoClient = new MongoClient(serverAddress);
            db = mongoClient.getDB(DATABASE);
            dbCollection = db.getCollection(collectionName);
        } catch (UnknownHostException e) {
            logger.error("数据库连接失败：\n"+e.getMessage());
            System.out.println("数据库连接失败： ");
            e.printStackTrace();
        }
    }

    //实例化WriteLocationDB类， 并避免重复实例化；
    public static WriteLocationDB getInstance(){
        if (instance == null){
            instance = new WriteLocationDB();
        }

        return instance;
    }

    //将page作为参数接收， 并整理后传输至数据库。
    public void transferToDB(Page page){
        Set<Map.Entry<String, Object>> set = page.getResultItems().getAll().entrySet();
        for (Map.Entry<String, Object> me: set){
            map.put(me.getKey(), me.getValue().toString());
        }
        dbObject = new BasicDBObject(map);
        dbCollection.insert(dbObject);
    }
}
