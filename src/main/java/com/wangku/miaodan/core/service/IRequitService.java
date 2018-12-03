package com.wangku.miaodan.core.service;

import java.util.List;

import com.wangku.miaodan.core.model.Requit;

public interface IRequitService {

	List<Requit> list(int page, int size);

	void save(Requit requit);

	void updateStatus(Long requitId, int status);

	Requit detail(long orderId, String mobile);

}
