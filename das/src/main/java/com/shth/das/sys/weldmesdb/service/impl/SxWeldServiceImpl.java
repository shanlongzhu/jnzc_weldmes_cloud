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
            queryWrapper.eq("weld_cid", sxWeldModel.getWeldCid());
            //根据设备CID查询个数
            Integer count = sxWeldMapper.selectCount(queryWrapper);
            //如果已经存在，则直接返回，不再增加
            if (null != count && count != 0) {
                return 0;
            }
            //如果设备CID不为空，则取非0字符作为序号
            if (CommonUtils.isNotEmpty(sxWeldModel.getWeldCid())) {
                //去除第一个非0字符前面所有的0（ 00102030 --> 102030 ）
                String weldNo = sxWeldModel.getWeldCid().replaceFirst("^0*", "");
                sxWeldModel.setWeldNo(weldNo);
            } else {
                //查询焊机编号最大值，并增加1作为新的焊机编号
                QueryWrapper<SxWeldModel> wrapper = new QueryWrapper<>();
                wrapper.select("max(weld_no) as weldNo");
                SxWeldModel sxWeld = sxWeldMapper.selectOne(wrapper);
                if (null != sxWeld) {
                    int weldNo = Integer.parseInt(sxWeld.getWeldNo());
                    weldNo++;
                    //实现设备序号自增，如果没有，默认：0001
                    sxWeldModel.setWeldNo(CommonUtils.stringLengthJoint(String.valueOf(weldNo), 4));
                }
            }
            result = sxWeldMapper.insert(sxWeldModel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    @Override
    public SxWeldModel getSxWeldByWeldCid(String weldCid) {
        if (CommonUtils.isNotEmpty(weldCid)) {
            QueryWrapper<SxWeldModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("weld_cid", weldCid);
            return sxWeldMapper.selectOne(queryWrapper);
        }
        return null;
    }
}
