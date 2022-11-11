package com.week06.team01.config;

import com.week06.team01.controller.request.PostRequestDto;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Crawling_imgUrl {

    public static void main(String[] args) {
        String imgUrl = "https://movie.naver.com/movie/bi/mi/basic.naver?code=204640#";
        Connection conn = Jsoup.connect(imgUrl);
        // #content > div.article > div.mv_info_area > div.poster > a > img
        //#content > div.article > div.wide_info_area > div.poster > a > img
        try {
            Document document = conn.get();
            Elements elements = document.select("div.poster a img");
            System.out.println(String.valueOf(elements.attr("src")));

//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}