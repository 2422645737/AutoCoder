package com.shiyue.codeparse.demospring.feign;

import com.shiyue.codeparse.demospring.feign.fallback.OrderFeignFallBack;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @fileName: OrderFeignFallBackImpl
 * @author: wanghui
 * @createAt: 2025/02/17 03:13:50
 * @updateBy:
 * @copyright:
 */

@Component
public class OrderFeignFallBackImpl implements FallbackFactory<OrderFeign> {
    @Override
    public OrderFeign create(Throwable cause) {
        return new OrderFeignFallBack(cause);
    }
}