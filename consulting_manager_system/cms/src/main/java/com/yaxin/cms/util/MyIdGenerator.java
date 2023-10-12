package com.yaxin.cms.util;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.stereotype.Component;

@Component
public class MyIdGenerator  implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
        // 填充自己的Id生成器，
        return IdGenerator.generateId();
    }
}
