package com.shiyue.codeparse.demospring.entity.spring.po;



import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @fileName: ArticlePO
 * @author: wanghui
 * @createAt: 2025/03/13 01:54:47
 * @updateBy:
 * @copyright:
 */
@TableName("article")
@Data
public class ArticlePO implements Serializable {

    @TableId
    private Long articleId;

    private String title;

    private String content;
}