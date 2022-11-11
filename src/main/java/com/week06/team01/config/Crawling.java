package com.week06.team01.config;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

public class Crawling {
    // 크롤링으로 네이버 영화 메인 순위 따오기(순위와 제목)
    public static void main(String[] args) {
        String rankUrl = "https://movie.naver.com/movie/sdb/rank/rmovie.naver?sel=cur";
        Connection conn = Jsoup.connect(rankUrl);

        try {
            Document document = conn.get();
            Elements titles = document.select("#old_content table tbody tr");
            Elements stars = document.select("#old_content table tbody tr");
            Elements ranks = document.select("#old_content table tbody tr");

            for(int i =0; i < 10; i++) {
                String title = titles.select("td.title div a").get(i).text();
                String star = stars.select("td.point").get(i).text();
                String rank = ranks.select("td:nth-child(1) img").get(i).attr("alt");

                System.out.println(rank+"위 "+title+"("+star+")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
