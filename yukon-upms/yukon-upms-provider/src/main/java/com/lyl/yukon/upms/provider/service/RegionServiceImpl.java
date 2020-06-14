package com.lyl.yukon.upms.provider.service;

import com.lyl.yukon.common.entity.upms.AreaDO;
import com.lyl.yukon.common.entity.upms.CityDO;
import com.lyl.yukon.common.entity.upms.ProvinceDO;
import com.lyl.yukon.common.entity.upms.StreetDO;
import com.lyl.yukon.upms.api.service.IRegionService;
import com.lyl.yukon.upms.provider.dao.AreaDOMapper;
import com.lyl.yukon.upms.provider.dao.CityDOMapper;
import com.lyl.yukon.upms.provider.dao.ProvinceDOMapper;
import com.lyl.yukon.upms.provider.dao.StreetDOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2019/08/08 16:12
 **/
@Service("regionService")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class RegionServiceImpl implements IRegionService {

    private static Logger logger = LoggerFactory.getLogger(RegionServiceImpl.class);

    @Autowired
    private ProvinceDOMapper provinceDOMapper;
    @Autowired
    private CityDOMapper cityDOMapper;
    @Autowired
    private AreaDOMapper areaDOMapper;
    @Autowired
    private StreetDOMapper streetDOMapper;


    @Override
    public List<ProvinceDO> getProvinceList() {
        return provinceDOMapper.selectAll();
    }

    @Override
    public List<CityDO> getCityList(String provinceCode) {
        return cityDOMapper.selectByProvinceCode(provinceCode);
    }

    @Override
    public List<AreaDO> getAreaList(String cityCode) {
        return areaDOMapper.selectByCityCode(cityCode);
    }

    @Override
    public List<StreetDO> getStreetList(String areaCode) {
        return streetDOMapper.selectByAreaCode(areaCode);
    }
}
