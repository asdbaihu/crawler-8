package thread;

import pageProcessor.JobPageProcessor;
import us.codecraft.webmagic.Spider;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 2017/5/30.
 * author: 阿朕
 * 将爬虫的启动入口写到线程中， 不影响其他爬虫的的启动；
 * 使用webmagic中Spider的thread方法设置多线程。
 */
public class ThreadJobSpider extends Thread {
    private ListIterator<String> urls;

    public ThreadJobSpider(List<String> urls){
        this.urls = urls.listIterator();
    }

    public void run(){
        Spider spider = Spider.create(new JobPageProcessor());
        spider.thread(20);
        while(urls.hasNext()){
            spider.addUrl(urls.next());
            spider.run();
        }
    }
}
