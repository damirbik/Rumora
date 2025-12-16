package org.lamdateam.rumora_demo.dto;

public class UpdateUserRequestDto {

    private String username;
    private String password;

    public UpdateUserRequestDto() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}