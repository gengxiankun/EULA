package com.eula.component.dto.builder;

import com.eula.component.dto.ITreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理树形结构数据的工具类
 * @author gengxiankun
 */
public class TreeBuilder<T extends ITreeNode> {

    private final List<T> nodeList = new ArrayList<>();

    private Long topLevelId;

    private Boolean enableParentName;

    /**
     * 追加节点集合
     */
    public TreeBuilder<T> nodeList(List<T> nodeList) {
        this.nodeList.addAll(nodeList);
        return this;
    }

    /**
     * 追加节点
     */
    public TreeBuilder<T> node(T node) {
        this.nodeList.add(node);
        return this;
    }

    /**
     * 设置顶级节点 ID
     */
    public TreeBuilder<T> topLevelId(Long id) {
        this.topLevelId = id;
        return this;
    }

    /**
     * 启用父类名称
     */
    public TreeBuilder<T> enableParentName() {
        this.enableParentName = true;
        return this;
    }

    /**
     * 构建树形结构
     */
    public List<T> build() {
        if (this.topLevelId == null) {
            this.topLevelId = 0L;
        }
        return this.buildTree();
    }

    /**
     * 获取所有根节点
     */
    private List<T> getRootNode() {
        List<T> rootNodeList = new ArrayList<>();
        for (T node : this.nodeList) {
            if (this.topLevelId.equals(node.getParentId())) {
                rootNodeList.add(node);
            }
        }
        return rootNodeList;
    }

    /**
     * 根据每一个根节点构建树形结构
     */
    private List<T> buildTree() {
        List<T> treeNodeList = new ArrayList<>();
        for (T node : this.getRootNode()) {
            this.buildChildTree(node);
            treeNodeList.add(node);
        }
        return treeNodeList;
    }

    /**
     * 递归构建子树形结构
     */
    private T buildChildTree(T parentNode) {
        List<T> childNodeList = new ArrayList<>();
        for (T node : this.nodeList) {
            if (node.getParentId().equals(parentNode.getId())) {
                if (Boolean.TRUE.equals(this.enableParentName)) {
                    node.setParentName(parentNode.getName());
                }
                childNodeList.add(this.buildChildTree(node));
            }
        }
        parentNode.setChildren(childNodeList);
        return parentNode;
    }

}
