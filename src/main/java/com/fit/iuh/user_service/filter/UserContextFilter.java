package com.fit.iuh.user_service.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class UserContextFilter implements Filter {

    /**
     * Nhận request sau khi đã đi qua gateway.
     * Các header X-User-* được chuyển thành Authentication của Spring Security
     * và context nội bộ để code phía sau biết request đang thuộc về user nào.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String email = httpRequest.getHeader("X-User-Email");
        String userId = httpRequest.getHeader("X-User-Id");
        String role = httpRequest.getHeader("X-User-Role");
        String authoritiesRaw = httpRequest.getHeader("X-User-Authorities");

        /*
         * Chuyển danh sách permission từ header sang GrantedAuthority.
         * Spring Security sẽ dựa vào dữ liệu này khi kiểm tra các annotation như @PreAuthorize.
         */
        List<SimpleGrantedAuthority> authorities = Collections.emptyList();

        if (authoritiesRaw != null && !authoritiesRaw.isBlank()) {
            authorities = Arrays.stream(authoritiesRaw.split(","))
                    .filter(auth -> !auth.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }

        if (hasText(role)) {
            List<SimpleGrantedAuthority> roleAuthorities = List.of(
                    new SimpleGrantedAuthority(normalizeRole(role))
            );
            authorities = authorities.isEmpty()
                    ? roleAuthorities
                    : java.util.stream.Stream
                    .concat(authorities.stream(), roleAuthorities.stream())
                    .distinct()
                    .toList();
        }

        if (hasText(email) || hasText(userId) || !authorities.isEmpty()) {
            var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else SecurityContextHolder.clearContext();


        /*
         * Lưu định danh user vào holder riêng của service.
         * Các tầng business có thể đọc email/userId hiện tại mà không cần parse lại request header.
         */
        if (hasText(email) || hasText(userId)) {
            UserContextHolder context = UserContextHolder
                    .builder()
                    .email(email)
                    .userId(userId)
                    .build();

            UserContextHolder.set(context);
        } else UserContextHolder.clear();


        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContextHolder.clear();
            SecurityContextHolder.clearContext();
        }

    }

    /**
     * Xác định một giá trị header có thể sử dụng được hay không.
     * Header null, rỗng hoặc toàn khoảng trắng đều được xem là không có dữ liệu.
     */
    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * Đưa role về format thống nhất để so sánh và phân quyền.
     * Prefix ROLE_ nếu có sẽ bị bỏ, sau đó role được chuyển sang chữ hoa.
     */
    private String normalizeRole(String role) {
        return role.replaceFirst("^ROLE_", "").toUpperCase();
    }
}
