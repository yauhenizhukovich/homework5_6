package com.gmail.supersonicleader.service.model;

public class UserGroupDTO {

    private int id;
    private String name;

    private UserGroupDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private int id;
        private String name;

        private Builder() {}

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public UserGroupDTO build() {
            return new UserGroupDTO(this);
        }

    }

}
