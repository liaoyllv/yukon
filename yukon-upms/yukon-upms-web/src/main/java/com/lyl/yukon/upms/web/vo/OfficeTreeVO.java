package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.upms.api.dto.OfficeTreeDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.LinkedList;
import java.util.List;

@Data
public class OfficeTreeVO {
    /**
     * id
     */
    private String officeId;

    /**
     * 名称
     */
    private String officeName;

    /**
     * 负责人
     */
    private String masterName;

    /**
     * 负责人电话
     */
    private String masterPhone;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 子组织架构
     */
    private List<OfficeTreeVO> children;


    public OfficeTreeVO(OfficeTreeDTO office) {
        this.officeId = office.getId();
        BeanUtils.copyProperties(office, this);
        this.children = OfficeTreeVo(new LinkedList<>(), office.getChildren());
    }

    /**
     * 递归生成VO
     *
     * @param result     结果
     * @param officeTree 组织架构树结构
     */
    public static List<OfficeTreeVO> OfficeTreeVo(List<OfficeTreeVO> result, List<OfficeTreeDTO> officeTree) {
        for (OfficeTreeDTO org : officeTree) {
            result.add(new OfficeTreeVO(org));
        }
        return result;
    }

}