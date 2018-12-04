package com.wangku.miaodan.core.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wangku.miaodan.core.dao.OrderMapper;
import com.wangku.miaodan.core.dao.RequitMapper;
import com.wangku.miaodan.core.dao.StoredOrderMapper;
import com.wangku.miaodan.core.dao.UserMapper;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.model.Requit;
import com.wangku.miaodan.core.model.StoreOrder;
import com.wangku.miaodan.core.service.IRequitService;

@Service
public class RequitServiceImpl implements IRequitService {
	
	@Autowired
	private RequitMapper requitMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private StoredOrderMapper storeOrderMapper; 
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public List<Requit> list(int page, int size) {
		int start = page;
		return requitMapper.list(start, size);
	}

	@Override
	public void save(Requit requit) {
		requit.setUpdateTime(new Date());
		requitMapper.insert(requit);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStatus(Long requitId, int status) {
		Requit requit = requitMapper.selectByPrimaryKey(requitId);
		requit.setStatus((byte)status);
		requitMapper.updateByPrimaryKey(requit);
		if (status == 1) {
			StoreOrder storeOrder = storeOrderMapper.detail(requit.getUserId(), requit.getOrderId());
			userMapper.increTimesByMobile(storeOrder.getMobile(), storeOrder.getIsTd());
			Order order = new Order();
			order.setId(storeOrder.getOrderId());
			order.setStatus(0);
			orderMapper.updateByPrimaryKey(order);
		}
	}

	@Override
	public Requit detail(long orderId, String mobile) {
		return requitMapper.selectByOrderId(orderId, mobile);
	}

}
