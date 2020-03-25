package com.ptu.zxk.service.admin;

import java.util.List;

import com.ptu.zxk.entity.admin.Authority;
import org.springframework.stereotype.Service;

/**
 * Ȩ��service�ӿ�
 * @author llq
 *
 */
@Service
public interface AuthorityService {
	public int add(Authority authority);
	public int deleteByRoleId(Long roleId);
	public List<Authority> findListByRoleId(Long roleId);
}
