package com.week06.team01.domain;
import com.week06.team01.controller.request.PostRequestDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likenum;
    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(name="member_id")
    private Member member;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String image_url;
    @Column(nullable = false)
    private int star;


    public void update(PostRequestDto requestDto){
        //this.name=requestDto.getName();
        this.title=requestDto.getTitle();
        this.content=requestDto.getContent();
        this.image_url=requestDto.getImage_url();
        this.star=requestDto.getStar();
    }

    public boolean validateMember(Member member) {
        return !this.member.getId().equals(member.getId());
    }
}
