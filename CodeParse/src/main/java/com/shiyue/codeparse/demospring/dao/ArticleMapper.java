package com.shiyue.codeparse.demospring.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyue.codeparse.demospring.entity.spring.po.ArticlePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @fileName: DemoMapper
 * @author: wanghui
 * @createAt: 2025/03/12 04:03:04
 * @updateBy:
 * @copyright:
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticlePO> {
    /**
     * 查找文章
     * @param ids
     * @return {@link List }<{@link ArticlePO }>
     */
    List<ArticlePO> findArticle(@Param("ids") List<Long> ids);

    /**
     * 删除文章
     * @param id
     * @return {@link List }<{@link ArticlePO }>
     */
    List<ArticlePO> deleteArticle(@Param("id") Long id);
}