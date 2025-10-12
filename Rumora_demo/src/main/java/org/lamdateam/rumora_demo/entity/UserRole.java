package org.lamdateam.rumora_demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    public UserRole(){}

    public UserRole(String roleName){ this.roleName = roleName; }

    public int getRoleId(){ return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    @Override
    public String toString(){
        return "Role{" +
                "roleId=" + roleId +
                ", roleName=" + roleName + '\'' +
                "}";
    }
}
