package com.yaxin.cms;

import com.yaxin.cms.dao.SlideshowDao;
import com.yaxin.cms.util.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
class CmsApplicationTests {

    @Autowired
    private SlideshowDao slideshowDao;

    @Test
    void testMD5() {
        System.out.println(MD5Utils.MD5("admin"));
        System.out.println(MD5Utils.MD5("briup"));
    }

//    @Test
//    void testPage() {
//        IPage<Slideshow> p = new Page<>(1, 2);
//        LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
//        String status = "启用";
//        String desc = null;
//        qw.eq(status != null, Slideshow::getStatus, status);
//        qw.like(desc != null, Slideshow::getDescription, desc);
//        qw.orderByDesc(Slideshow::getUploadTime);
//
//        IPage<Slideshow> p2 = slideshowDao.selectPage(p, qw);
//        System.out.println("当前页码值：" + p2.getCurrent());
//        System.out.println("每页显示数：" + p2.getSize());
//        System.out.println("总页数：" + p2.getPages());
//        System.out.println("总条数：" + p2.getTotal());
//        System.out.println("当前页数据：" + p2.getRecords());
//    }

}
