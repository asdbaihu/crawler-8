package entry;

import io.CreateURL;
import thread.ThreadJobSpider;

import java.util.List;
/**
 * Created by 阿朕 on 2017/4/22.
 * 函数入口
 */
        public class Main{
            static List<String> urls;

            public static void main(String[] args) {

        //获取总url
        urls = new CreateURL().getUrls("jobTest");
        //生成爬虫
        new ThreadJobSpider(urls).start();
    }
}
