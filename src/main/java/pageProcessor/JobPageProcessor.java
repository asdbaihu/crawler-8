package pageProcessor;

import io.WriteLocationDB;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/29.
 * author: 阿朕
 * 定制爬取器爬取的内容：
 * 1. 抓取网站的相关配置，包括编码、抓取间隔、重试次数等；
 * 2. 定义如何抽取页面信息，并保存下来；
 * 3. 从页面发现后续的url地址来抓取。
 */
public class JobPageProcessor implements PageProcessor {
    private Site site;
    private String date;
    //private WriteDB writeDB;
    private WriteLocationDB writeLocationDB;

//http://bj.58.com/javakfgongchengshi/pve_5356_6/
    public JobPageProcessor() {
        // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
        site = Site.me().setSleepTime(100);

        //获得当前时间
        Date d = new Date();
        System.out.println("当前日期：" + d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);
        date = dateNowStr;

        //连接数据库
        //writeDB = WriteDB.getInstance();
        writeLocationDB = WriteLocationDB.getInstance();
    }

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        if (!page.getUrl().toString().contains(".html")) {
            // 部分三：从页面发现后续的url地址来抓取
            List<String> tempUrl;
            tempUrl = page.getHtml().links().regex("(http://[a-z]+\\.baixing\\.com/[a-z]+/[a-z]+[0-9]+\\.html)").all();
            //System.out.println(tempUrl.size());
            tempUrl.add(page.getHtml().$(".list-pagination > li:nth-child(13) > a:nth-child(1)","href").toString());
            page.addTargetRequests(tempUrl);
            //System.out.println(tempUrl.size());
//
            return;
        }
        try {
            page.getHtml().$(".recommend-category > h3:nth-child(1)", "text").toString().length();
        }catch (Exception e){
            //要获取的内容：
            page.putField("date", date);
            //.firm-name > a:nth-child(1)
            page.putField("location", page.getHtml().$(".breadcrumb > a:nth-child(3)", "text"));
            page.putField("uuid", page.getUrl().toString().substring(page.getUrl().toString().lastIndexOf('/')+1, page.getUrl().toString().lastIndexOf('.')));
            page.putField("company", page.getHtml().$(".poster-name", "text"));
            page.putField("jobName", page.getHtml().$(".viewad-content > div:nth-child(1) > div:nth-child(1) > h1:nth-child(1)", "text"));
            page.putField("education", page.getHtml().$(".meta-学历", "text"));
            page.putField("workYears", page.getHtml().$(".meta-工作年限", "text"));
            page.putField("salary", page.getHtml().$(".price", "text"));
            page.putField("workArea", page.getHtml().$("div.viewad-meta2-item:nth-child(1) > span:nth-child(2)", "text"));
            page.putField("numberOfDemand", page.getHtml().$(".meta-招聘人数", "text"));

            //数据整理并传输至数据库
            //writeDB.transferToDB(page);
            writeLocationDB.transferToDB(page);
        }
    }
    //site构造器
    public Site getSite() {
        return site;
    }
}
