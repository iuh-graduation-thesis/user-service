package com.fit.iuh.user_service.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fit.iuh.user_service.dto.request.UpsertPermissionRequest;
import com.fit.iuh.user_service.dto.response.PermissionResponse;
import com.fit.iuh.user_service.service.PermissionService;

import lombok.NonNull;

public class PermissionServiceImpl implements PermissionService {

    @Override
    public void createPermission(UpsertPermissionRequest upsertPermissionRequest) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deletePermissionById(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Page<@NonNull PermissionResponse> findAllPermissions(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PermissionResponse> findAllPermissions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updatePermission(UpsertPermissionRequest upsertPermissionRequest, String id) {
        // TODO Auto-generated method stub
        
    }
    
}
