package com.meng.crm.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meng.crm.dao.jpa.DictRepository;
import com.meng.crm.entity.Storage;

@Service
public class StorageService extends BaseService<Storage, Long> {
}
