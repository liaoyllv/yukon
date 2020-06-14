package com.lyl.yukon.upms.api.service;


import com.lyl.yukon.common.entity.upms.AreaDO;
import com.lyl.yukon.common.entity.upms.CityDO;
import com.lyl.yukon.common.entity.upms.ProvinceDO;
import com.lyl.yukon.common.entity.upms.StreetDO;

import java.util.List;

/**
 * 区域service
 *
 * @author liaoyl
 */
public interface IRegionService {

    /**
     * 省列表
     */
    List<ProvinceDO> getProvinceList();

    /**
     * 市列表
     *
     * @param provinceCode 省代码
     */
    List<CityDO> getCityList(String provinceCode);

    /**
     * 区列表
     *
     * @param cityCode 市代码
     */
    List<AreaDO> getAreaList(String cityCode);

    /**
     * 街道列表
     *
     * @param areaCode 区代码
     */
    List<StreetDO> getStreetList(String areaCode);


}
