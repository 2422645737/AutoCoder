package com.shiyue.codeparse.demospring.feign.fallback;

import com.shiyue.codeparse.demospring.feign.OrderFeign;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @fileName: OrderFeignFallBack
 * @author: wanghui
 * @createAt: 2025/02/17 03:14:41
 * @updateBy:
 * @copyright:
 */
public class OrderFeignFallBack implements OrderFeign {
    /**
     * 异常
     */
    private Throwable throwable;

    /**
     * 带参构造函数
     * @param throwable 异常
     */
    public OrderFeignFallBack(Throwable throwable) {
        this.throwable = throwable;
    }
    @Override
    public Long queryByDictType(Long insurDictQueryNoPageParam) {
        return null;
    }
}