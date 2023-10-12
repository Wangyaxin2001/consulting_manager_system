package com.yaxin.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yaxin.cms.bean.dto.LogExportParam;
import com.yaxin.cms.bean.dto.LogParam;
import com.yaxin.cms.bean.vo.LogVO;

import java.util.List;

public interface ILogService {

    // 分页+条件查询日志信息
    IPage<LogVO> query(LogParam param);

    //根据条件查询待导出的数据
    List<LogVO> queryForExport(LogExportParam logExportParam);
}
