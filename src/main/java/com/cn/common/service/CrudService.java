/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.cn.common.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cn.common.persistence.BaseEntity;
import com.cn.common.persistence.CrudMapper;
import com.cn.page.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Service基类
 */
public abstract class CrudService<D extends CrudMapper<Example,Entity>, Entity extends BaseEntity,Example> extends BaseService<Entity>{

	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public AjaxResponse<Entity> get(String id) {
		return getAjaxResponse(dao.selectByPrimaryKey(id));
	}

	/**
	 * 获取单条数据
	 * @param example
	 * @return
	 */
	public AjaxResponse<Entity> get(Example example) {
		List<Entity> list = dao.selectByExample(example);
		if(list!=null&&list.size()>0){
			return getAjaxResponse(list.get(0));
		}
		return null;
	}

	/**
	 * 查询列表数据
	 * @param example
	 * @return
	 */
	public AjaxResponse<List<Entity>> findList(Example example) {
		return getAjaxResponseList(dao.selectByExample(example));
	}

	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param example
	 * @return
	 */
	public AjaxResponse<PageInfo<Entity>> findPage(Integer page, Integer limit, Example example) {
		PageHelper.startPage(page, limit);
		List<Entity> list = dao.selectByExample(example);
		return getAjaxResponsePage(list);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	@Transactional
	public void save(Entity entity) {
		if (entity.getIsNewRecord()){
			entity.preInsert();
			dao.insertSelective(entity);
		}else{
			entity.preUpdate();
			dao.updateByPrimaryKeySelective(entity);
		}
	}

	/**
	 * 根据条件更新数据
	 * @param entity
	 */
	@Transactional
	public void updateByExample(Entity entity,Example example) {
			dao.updateByExample(entity,example);
	}

	/**
	 * 根据条件更新值不为空的数据
	 * @param entity
	 */
	@Transactional
	public void updateByExampleSelective(Entity entity,Example example) {
		dao.updateByExampleSelective(entity,example);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	@Transactional
	public void updateByPrimaryKey(Entity entity) {
		dao.updateByPrimaryKey(entity);
	}

	/**
	 * 删除数据
	 * @param id
	 */
	@Transactional
	public void delete(String id) {
		dao.deleteByPrimaryKey(id);
	}


	/**
	 * 删除全部数据
	 * @param ids
	 */
	@Transactional
	public void deleteAll(Collection<String> ids) {
		for (String id : ids) {
			dao.deleteByPrimaryKey(id);
		}
	}

}
