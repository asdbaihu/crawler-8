package io;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/5/9.
 * author：阿朕
 *
 * 连接数据库（mongoDB）并将JobPageProcessor中得到的page传输至指定的数据库。
 */


public class WriteDB {
    private MongoClient mongoClient;
    private DB db = null;
    private DBCollection dbCollection = null;
    private DBObject dbObject = null;

    private static WriteDB instance;
    private static final String HOSTNAME = "120.24.97.160";
    private static final int PORT  = 27017;

    private static final String USERNAME = "fanzezhen";
    private static final String PASSWORD = "FZz19951106";
    private static final String DATABASE = "admin";

    private String collectionName;
    private HashMap<String,String> hashMap = new HashMap<String, String>();
    private Logger logger = LoggerFactory.getLogger(getClass());

    public WriteDB(){
        collectionName  = "resumes";
        try {       /*数据库登录验证*/
            ServerAddress serverAddress = new ServerAddress(HOSTNAME,PORT);

            MongoCredential mongoCredential = MongoCredential.createMongoCRCredential(USERNAME,DATABASE,PASSWORD.toCharArray());
            List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
            mongoCredentialList.add(mongoCredential);

            mongoClient = new MongoClient(serverAddress,mongoCredentialList);
            db = mongoClient.getDB(DATABASE);
            dbCollection = db.getCollection(collectionName);
        }catch(Exception e){
            logger.error("数据库连接失败：\n"+e.getMessage());
            System.out.println("数据库连接失败： ");
            e.printStackTrace();
        }
    }

    public static WriteDB getInstance(){
        if (instance == null){
            instance = new WriteDB();
        }
        return instance;
    }

    public void getMongo(){}

    public void transferToDB(Page page){
        hashMap.put(page.getResultItems().get("uuid").toString(),
                page.getResultItems().getAll().toString());
        dbObject = new BasicDBObject(hashMap);
        dbCollection.insert(dbObject);
    }
}
