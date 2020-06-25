package com.gmail.supersonicleader.service.model;

public class CountOfUsersByGroupDTO {

    private String name;
    private int count;

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    private CountOfUsersByGroupDTO(Builder builder) {
        name = builder.name;
        count = builder.count;
    }

    @Override
    public String toString() {
        return "CountOfUsersByGroupDTO{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private int count;

        private Builder() {}

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder count(int val) {
            count = val;
            return this;
        }

        public CountOfUsersByGroupDTO build() {
            return new CountOfUsersByGroupDTO(this);
        }

    }

}
