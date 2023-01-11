package com.sein.entity;

import javax.persistence.*;

@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String replyContent;
    @Column
    private Long likeCount;
}
