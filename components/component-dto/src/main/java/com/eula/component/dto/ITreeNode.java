package com.eula.component.dto;

import java.util.List;

/**
 * 树形结构实体接口
 * @author gengxiankun
 */
public interface ITreeNode extends IDto {

    /**
     * 获取主键 ID
     * @return 主键 ID
     */
    Long getId();

    /**
     * 设置层级
     */
    void setLevel(Integer level);

    /**
     * 获取层级
     */
    Integer getLevel();

    /**
     * 获取父类 ID
     * @return 父类 ID
     */
    Long getParentId();

    /**
     * 获取名称
     * @return 名称
     */
    String getName();

    /**
     * 设置父类名称
     * @param parentName 父类名称
     */
    void setParentName(String parentName);

    /**
     * @param childrenNodeList 子集
     */
    void setChildren(List<? extends ITreeNode> childrenNodeList);

}
