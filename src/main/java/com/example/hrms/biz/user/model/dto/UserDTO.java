package com.example.hrms.biz.user.model.dto;

import com.example.hrms.biz.user.model.User;
import com.example.hrms.biz.user.service.UserService;
import com.example.hrms.enumation.RoleEnum;
import lombok.Data;

public class UserDTO {

    @Data
    public static class Req {
        private String username;
        private String employee_name;
        private String password;
        private Long departmentId;
        private String department_name;
        private RoleEnum role_name;
        private boolean isSupervisor;
        private String status;
        private String email;
        private UserService userService;

        public User toUser() {
            User user = new User();
            user.setUsername(this.username);
            user.setEmployee_name(this.employee_name);
            user.setPassword(this.password);

            // Lấy departmentId nếu chỉ có department_name
            if (this.departmentId == null && this.department_name != null) {
                Integer deptId = userService.getDepartmentIdByName(this.department_name);
                if (deptId == null) {
                    throw new RuntimeException("Department không tồn tại: " + this.department_name);
                }
                this.departmentId = deptId.longValue();
            }

            user.setDepartmentId(this.departmentId);
            user.setRole_name(String.valueOf(this.role_name));
            user.setSupervisor(this.isSupervisor);
            user.setStatus(this.status);
            user.setEmail(this.email);
            return user;
        }


    }

    @Data
    public static class Resp {
        private String username;
        private String employee_name;
        private Long departmentId;
        private String department_name;
        private RoleEnum role_name;
        private boolean isSupervisor;
        private String status;
        private String email;


        public void setIsSupervisor(boolean isSupervisor) {
            this.isSupervisor = isSupervisor;
        }
    }
    @Data
    public static class DepartmentAndRole {
        private Long departmentId;
        private RoleEnum role_name;
    }
    @Data
    public static class UpdateReq {
        private String username;
        private String employee_name;
        private String password;
        private Long departmentId;
        private String department_name;
        private RoleEnum role_name;
        private boolean isSupervisor;
        private String status;
        private String email;

    }
    @Data
    public static class ChangePasswordReq {
        private String oldPassword;
        private String newPassword;
    }

}