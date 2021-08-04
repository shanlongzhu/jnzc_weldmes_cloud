package com.shth.das.sys.weldmesdb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.sys.weldmesdb.mapper.SxWeldMapper;
import com.shth.das.sys.weldmesdb.service.SxWeldService;
import com.shth.das.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
public class SxWeldServiceImpl implements SxWeldService {

    @Autowired
    SxWeldMapper sxWeldMapper;

    @Override
    public int insertSxWeld(SxWeldModel sxWeldModel) {
        int result = 0;
        try {
            QueryWrapper<SxWeldModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("weld_ip", sxWeldModel.getWeldIp());
            //根据焊机IP查询个数
            Integer count = sxWeldMapper.selectCount(queryWrapper);
            //如果已经存在，则直接返回，不再增加
            if (null != count && count > 0) {
                return 0;
            }
            //查询焊机编号最大值，并增加1作为新的焊机编号
            QueryWrapper<SxWeldModel> wrapper = new QueryWrapper<>();
            wrapper.select("max(weld_no) as weldNo");
            SxWeldModel sxWeld = sxWeldMapper.selectOne(wrapper);
            if (null != sxWeld) {
                int weldNo = Integer.parseInt(sxWeld.getWeldNo());
                weldNo++;
                sxWeldModel.setWeldNo(CommonUtils.stringLengthJoint(String.valueOf(weldNo), 4));
            }
            result = sxWeldMapper.insert(sxWeldModel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    @Override
    public SxWeldModel getSxWeldByWeldIp(String weldIp) {
        if (CommonUtils.isNotEmpty(weldIp)) {
            QueryWrapper<SxWeldModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("weld_ip", weldIp);
            return sxWeldMapper.selectOne(queryWrapper);
        }
        return null;
    }
}
