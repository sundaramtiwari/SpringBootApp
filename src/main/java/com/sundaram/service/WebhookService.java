package com.sundaram.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sundaram.dao.WebhookDao;
import com.sundaram.entity.FBEntity;

@Service
public class WebhookService {

	@Autowired
	protected WebhookDao webhookDao;

	public FBEntity findFBEntityById(String fbEntityId) {
		return webhookDao.getFBEntityById(fbEntityId);
	}

	public String save(String id, String name) {
		String response = "Internal error occured.";

		if (StringUtils.isNotEmpty(id)) {

			FBEntity findFBEntityById = this.findFBEntityById(id);

			if (StringUtils.isNotEmpty(findFBEntityById.getId())) {
				response = "FB Entity already exists with Id: " + id;
			} else {
				FBEntity fbEntity = new FBEntity(id, name);
				response = this.save(fbEntity);
			}
		}

		return response;
	}

	public String save(FBEntity fbEntity) {
		return webhookDao.save(fbEntity);
	}
	
	public void createFBEntityTable () {
		webhookDao.createFBEntityTable();
	}
}
