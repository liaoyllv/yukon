package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.upms.api.dto.OrgListDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>机构列表</p>
 *
 * @author liaoyl
 * @version 1.0 2019/08/8 10:50
 **/

@Data
public class OrgListVO {

    /**
     * 编号
     */
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 负责人名称
     */
    private String masterName;

    /**
     * 负责人电话
     */
    private String masterPhone;

    /**
     * 地址
     */
    private String address;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 机构列表VO
     *
     * @param orgList 原机构列表
     * @return list
     */
    public static List<OrgListVO> OrgListVo(List<OrgListDTO> orgList) {
        LinkedList<OrgListVO> listVos = new LinkedList<>();
        for (OrgListDTO org : orgList) {
            OrgListVO vo = new OrgListVO();
            BeanUtils.copyProperties(org, vo);
            vo.orgId = org.getId();
            vo.orgName = org.getOfficeName();
            vo.address = org.getProvince() + org.getCity() + org.getArea() + org.getStreet() + org.getAddress();
            listVos.add(vo);
        }
        return listVos;
    }
}
