package com.example.hrms.biz.user.model;

import com.example.hrms.enumation.RoleEnum;
import lombok.Data;

@Data
public class User {
    private String username;
    private String employee_name;
    private String password;
    private Long departmentId;
    private RoleEnum role_name;
    private boolean isSupervisor;
    private String status;
    private String email;
    private String department_name;  // department_name

    // Setter cho role_name từ String (đã có)
    public void setRole_name(String roleName) {
        this.role_name = RoleEnum.fromString(roleName);
    }

    // Getter và Setter cho department_name
    public String getDepartmentName() {
        return this.department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    // Getter và Setter cho supervisor (đã có)
    public void setSupervisor(boolean isSupervisor) {
        this.isSupervisor = isSupervisor;
    }
}
