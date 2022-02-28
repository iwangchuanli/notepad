package com.ten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ten.entity.BillInfo;
import com.ten.from.BillPageFrom;
import com.ten.from.TimeFrom;

import java.util.List;

/**
 * 账单 接口
 *
* @date 2021-11-29 16:23:21
 */
public interface BillInfoService extends IService<BillInfo> {

    /**
     * 查询账单分页
     * @param pageParam 分页参数
        * @date 2021-11-29 16:23:21
     */
    Page<BillInfo> findPage(BillPageFrom pageParam);


    /**
     * 删除账单
     *
     * @param id 主键
        * @date 2021-11-29 16:23:21
     */
    void delete(String id);

    /**
     * description  根据父id查询数据
     * @param       parentId 父id
     * @return      java.util.List<com.ten.entity.BillInfo>
     * @author      shisen
     * date         2021/11/30 14:38
     */
    List<BillInfo> findByParentId(String parentId);

    /**
     * description  根据时间当前用户数据
     * @param       timeFrom 开始时间 结束时间
     * @return      java.util.List<com.ten.entity.BillInfo>
     * @author      shisen
     * date         2021/12/12 3:18
     */
    List<BillInfo> findUserTimeData(TimeFrom timeFrom);
}
