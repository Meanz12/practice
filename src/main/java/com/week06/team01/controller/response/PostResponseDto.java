package com.week06.team01.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.week06.team01.controller.request.PostRequestDto;
import com.week06.team01.domain.Post;
import lombok.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {
    //private String name;
    private String title;
    private String content;
    private String image_url;
    private int likenum;
    private int star;
    private Long id;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public PostResponseDto(Post post) {
        this.id=post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image_url = post.getImage_url();
        this.likenum = post.getLikenum();
        this.star = post.getStar();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
