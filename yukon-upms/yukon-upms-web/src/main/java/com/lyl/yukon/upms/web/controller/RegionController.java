package com.lyl.yukon.upms.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lyl.yukon.common.redis.RedisRepository;
import com.lyl.yukon.common.base.BaseController;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import com.lyl.yukon.common.entity.upms.AreaDO;
import com.lyl.yukon.common.entity.upms.CityDO;
import com.lyl.yukon.common.entity.upms.ProvinceDO;
import com.lyl.yukon.common.entity.upms.StreetDO;
import com.lyl.yukon.upms.api.service.IRegionService;
import com.lyl.yukon.upms.web.param.RegionParam;
import com.lyl.yukon.upms.web.vo.RegionListVO;
import com.lyl.yukon.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>区域 controller</p>
 *
 * @author liaoyl
 * @version 1.0 2019/08/08 16:14
 **/
@RestController
@CrossOrigin
@Api(tags = "区域controller")
@RequestMapping("/upms/region")
public class RegionController extends BaseController {

    @Reference(version = "1.0.0")
    private IRegionService regionService;

    @Autowired
    private RedisRepository redis;

    @ApiOperation(value = "省列表")
    @PostMapping(value = "/province/list")
    public RestResponseModel provinceList() {
        redis.setExpire("timeout","1",100);
        System.out.println(redis.get("timeout"));
        Map<String, Object> result = new HashMap<>();
        List<ProvinceDO> provinceList = regionService.getProvinceList();
        List<RegionListVO> voList = new LinkedList<>();
        provinceList.forEach(province -> voList.add(new RegionListVO(province)));
        result.put("provinceList", voList);
        return success(result);
    }

    @ApiOperation(value = "市列表")
    @PostMapping(value = "/city/list")
    public RestResponseModel cityList(@RequestBody RegionParam param) {
        if (StringUtils.isBlank(param.getRegionCode())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        Map<String, Object> result = new HashMap<>();
        List<CityDO> cityList = regionService.getCityList(param.getRegionCode());
        List<RegionListVO> voList = new LinkedList<>();
        cityList.forEach(city -> voList.add(new RegionListVO(city)));
        result.put("cityList", voList);
        return success(result);
    }

    @ApiOperation(value = "区列表")
    @PostMapping(value = "/area/list")
    public RestResponseModel areaList(@RequestBody RegionParam param) {
        if (StringUtils.isBlank(param.getRegionCode())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        Map<String, Object> result = new HashMap<>();
        List<AreaDO> areaList = regionService.getAreaList(param.getRegionCode());
        List<RegionListVO> voList = new LinkedList<>();
        areaList.forEach(area -> voList.add(new RegionListVO(area)));
        result.put("areaList", voList);
        return success(result);
    }

    @ApiOperation(value = "街道列表")
    @PostMapping(value = "/street/list")
    public RestResponseModel streetList(@RequestBody RegionParam param) {
        if (StringUtils.isBlank(param.getRegionCode())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        Map<String, Object> result = new HashMap<>();
        List<StreetDO> streetList = regionService.getStreetList(param.getRegionCode());
        List<RegionListVO> voList = new LinkedList<>();
        streetList.forEach(street -> voList.add(new RegionListVO(street)));
        result.put("streetList", voList);
        return success(result);
    }


}
