package com.fit.iuh.user_service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fit.iuh.user_service.dto.base.ApiResponse;
import com.fit.iuh.user_service.dto.base.PageResponse;
import com.fit.iuh.user_service.dto.request.UpsertRoleRequest;
import com.fit.iuh.user_service.dto.response.RoleResponse;
import com.fit.iuh.user_service.service.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/roles")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminRoleController {

    RoleService roleService;

    @PostMapping()
    public ApiResponse<@NonNull Void> createRole(
            @RequestBody @Valid UpsertRoleRequest upsertRoleRequest) {
        roleService.createRole(upsertRoleRequest);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping()
    public ApiResponse<PageResponse<RoleResponse>> findAllRoles(
            @PageableDefault(value = 10) Pageable pageable) {
        PageResponse<RoleResponse> page = new PageResponse<>(
                roleService.findAllRoles(pageable));

        return ApiResponse.<PageResponse<RoleResponse>>builder()
                .result(page)
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<RoleResponse>> findAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.findAllRoles())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> findById(@PathVariable("id") String id) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.findById(id))
                .build();
    }

    @PostMapping("/{id}")
    public ApiResponse<@NonNull Void> updateRole(
            @RequestBody @Valid UpsertRoleRequest upsertRoleRequest,
            @PathVariable("id") String id) {
        roleService.updateRole(id, upsertRoleRequest);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<@NonNull Void> deleteRole(
            @PathVariable("id") String id) {
        roleService.deleteRoleById(id);
        return ApiResponse.<Void>builder().build();
    }

}
