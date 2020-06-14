package com.lyl.yukon.upms.provider.service;

import com.lyl.yukon.common.entity.upms.DictDO;
import com.lyl.yukon.upms.api.service.IDictService;
import com.lyl.yukon.upms.provider.dao.DictDOMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/17 16:12
 **/
@Service("dictService")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class DictServiceImpl implements IDictService {

    private static Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);

    @Autowired
    private DictDOMapper sysDictDOMapper;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean insertDict(DictDO dict) {
        return sysDictDOMapper.insertSelective(dict) > 0;
    }

    @Override
    public DictDO getDictById(String dictId) {
        return sysDictDOMapper.selectByPrimaryKey(dictId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateDict(DictDO dict) {
        return sysDictDOMapper.updateByPrimaryKeySelective(dict) > 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteDict(DictDO dict) {
        return sysDictDOMapper.updateByPrimaryKeySelective(dict) > 0;
    }

    @Override
    public PageInfo<DictDO> getDictPage(int pageNum, int pageSize, String dictType) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(sysDictDOMapper.selectPage(dictType));
    }

    @Override
    public boolean checkDictKey(String dictId, String dictKey) {
        return sysDictDOMapper.checkDictKey(dictId, dictKey) == null;
    }

    @Override
    public boolean checkDictValue(String dictId, String value) {
        return sysDictDOMapper.checkDictValue(dictId, value) == null;
    }


}
