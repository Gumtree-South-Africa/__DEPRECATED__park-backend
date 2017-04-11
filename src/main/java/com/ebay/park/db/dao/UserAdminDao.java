/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import com.ebay.park.db.entity.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdminDao extends JpaRepository<UserAdmin, Long> {

	public UserAdmin findByUsername(String username);

	public UserAdmin findByToken(String sessionToken);

	public UserAdmin findById(Long id);

	public UserAdmin findByEmail(String email);
}
