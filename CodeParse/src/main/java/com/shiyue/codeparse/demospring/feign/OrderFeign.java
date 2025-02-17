package com.shiyue.codeparse.demospring.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @description:
 * @fileName: OrderFeign
 * @author: wanghui
 * @createAt: 2025/02/17 02:46:52
 * @updateBy:
 * @copyright:
 */
@FeignClient(name = "${spring.application.medinsurbusiness.name}",fallbackFactory = OrderFeignFallBackImpl.class,path = "/api")
public interface OrderFeign {
    /**
     * 查询患者关系字典
     * @param insurDictQueryNoPageParam 查询患者关系字典
     * @return 查询患者关系字典
     */
    @PostMapping({"v2/insurDict/queryByDictType"})
    Long queryByDictType(@RequestBody Long insurDictQueryNoPageParam);
}