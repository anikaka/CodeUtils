package com.tongyan.zhengzhou.common.entities;

import java.util.ArrayList;
import java.util.List;


import com.tongyan.zhengzhou.act.R;


/**
 * 
 * @Title: LineInfoTree.java 
 * @author Rubert
 * @date 2015-3-18 PM 01:30:06 
 * @version V1.0 
 * @Description: 线路信息Node
 */
public class LineTreeNode {
	
	private final static int img_tree_space_x = R.drawable.horizontal_space_x;
	
	private String mNodeName;
	private String mNodeCode;
	private int mNodeLevel;
	private LineTreeNode mParentNode;
	private boolean isHasChild;// 是否有子节点
	private boolean isExpanded;// 是否处于展开
	private ArrayList<LineTreeNode> mChildList;// 子节点数组
	private boolean isLastSibling;// 是否是同级节点的最后一个
	private ArrayList<Integer> mSpaceList;// 组织结构线条数组
	
	public LineTreeNode(String nodeName) {
		this.mNodeName =nodeName;
		this.mNodeCode = "";
		this.mParentNode = null;
		this.mNodeLevel = 1;
		this.isHasChild = false;
		this.isExpanded = false;
		this.mChildList = new ArrayList<LineTreeNode>();
		this.isLastSibling = false;
		this.setmSpaceList(new ArrayList<Integer>());
	}
	public LineTreeNode(String nodeName, String nodeCode, Boolean isHasChild, Boolean isExpanded, int mLevel) {
		this.mNodeName = nodeName;
		this.mNodeCode = nodeCode;
		this.mParentNode = null;
		this.mNodeLevel = mLevel;
		this.isHasChild = isHasChild;
		this.isExpanded = isExpanded;
		this.mChildList = null;
		if(isHasChild) {
			this.mChildList = new ArrayList<LineTreeNode>();
		}
		this.isLastSibling = false;
		this.setmSpaceList(new ArrayList<Integer>());
	}
	
	public void addChild(LineTreeNode mNode) {
		mNode.mParentNode = this;
		if(mNode.mParentNode != null && mNode.mParentNode.getmChildList() != null && mNode.mParentNode.getmChildList().size() > 0) {// 将之前的同级节点的置为非最后一个节点
			List<LineTreeNode> siblingList = mNode.mParentNode.getmChildList();
			mNode.mParentNode.getmChildList().get(siblingList.size() - 1).setLastSibling(false);
		}
		this.mChildList.add(mNode);
		this.isHasChild = true;
		mNode.mNodeLevel = this.mNodeLevel + 1;
		mNode.isLastSibling = true;
		for(int i = 1; i < mNode.mNodeLevel; i ++) 
		mNode.getmSpaceList().add(img_tree_space_x);
	}
	
	public String getmNodeName() {
		return mNodeName;
	}

	public void setmNodeName(String mNodeName) {
		this.mNodeName = mNodeName;
	}

	public String getmNodeCode() {
		return mNodeCode;
	}

	public void setmNodeCode(String mNodeCode) {
		this.mNodeCode = mNodeCode;
	}

	public int getmNodeLevel() {
		return mNodeLevel;
	}

	public void setmNodeLevel(int mNodeLevel) {
		this.mNodeLevel = mNodeLevel;
	}

	public LineTreeNode getmParentNode() {
		return mParentNode;
	}

	public void setmParentNode(LineTreeNode mParentNode) {
		this.mParentNode = mParentNode;
	}

	public boolean isHasChild() {
		return isHasChild;
	}

	public void setHasChild(boolean isHasChild) {
		this.isHasChild = isHasChild;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	public ArrayList<LineTreeNode> getmChildList() {
		return mChildList;
	}

	public void setmChildList(ArrayList<LineTreeNode> mChildList) {
		this.mChildList = mChildList;
	}

	public boolean isLastSibling() {
		return isLastSibling;
	}

	public void setLastSibling(boolean isLastSibling) {
		this.isLastSibling = isLastSibling;
	}

	public ArrayList<Integer> getmSpaceList() {
		return mSpaceList;
	}

	public void setmSpaceList(ArrayList<Integer> mSpaceList) {
		this.mSpaceList = mSpaceList;
	}
	
}
