package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.common.entity.upms.AreaDO;
import com.lyl.yukon.common.entity.upms.CityDO;
import com.lyl.yukon.common.entity.upms.ProvinceDO;
import com.lyl.yukon.common.entity.upms.StreetDO;
import lombok.Data;

@Data
public class RegionListVO {
    /**
     * 区域类型：0-省，1-市，2-区，3-街道
     */
    private String type;

    /**
     * 区域代码
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    public RegionListVO(ProvinceDO province) {
        this.type = SystemConstant.REGION_TYPE_PROVINCE;
        this.code = province.getProvinceCode();
        this.name = province.getProvinceName();
    }

    public RegionListVO(CityDO city) {
        this.type = SystemConstant.REGION_TYPE_CITY;
        this.code = city.getCityCode();
        this.name = city.getCityName();
    }

    public RegionListVO(AreaDO area) {
        this.type = SystemConstant.REGION_TYPE_AREA;
        this.code = area.getAreaCode();
        this.name = area.getAreaName();
    }

    public RegionListVO(StreetDO street) {
        this.type = SystemConstant.REGION_TYPE_STREET;
        this.code = street.getStreetCode();
        this.name = street.getStreetName();
    }

}