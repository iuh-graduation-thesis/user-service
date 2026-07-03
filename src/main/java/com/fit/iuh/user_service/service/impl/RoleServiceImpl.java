package com.fit.iuh.user_service.service.impl;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fit.iuh.user_service.dto.request.role.UpsertRoleRequest;
import com.fit.iuh.user_service.dto.response.role.RoleResponse;
import com.fit.iuh.user_service.service.RoleService;

public class RoleServiceImpl implements RoleService {

    @Override
    public void createRole(UpsertRoleRequest upsertRoleRequest) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteRoleById(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Page<@NonNull RoleResponse> findAllRoles(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RoleResponse> findAllRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RoleResponse findById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateRole(String id, UpsertRoleRequest upsertRoleRequest) {
        // TODO Auto-generated method stub
    }
    
}
