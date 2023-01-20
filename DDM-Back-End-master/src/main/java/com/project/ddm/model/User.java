package com.project.ddm.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
//mapping
@Table(name = "user")
@JsonDeserialize(builder = User.Builder.class)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String username;
    @JsonIgnore
    @NotNull
    private String password;
    // field added and used by register/auth service
    @JsonIgnore
    private boolean enabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders;

    public User() {}
    private User(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
//        this.orders = builder.orders;
        this.enabled = enabled;
    }


    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    // method added by register/auth service
    public boolean isEnabled() {
        return enabled;
    }

    // method added by register/auth service
    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
//    public List<Order> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(List<Order> orders) {
//        this.orders = orders;
//    }

    public static class Builder {
        @JsonProperty("username")
        private String username;

        @JsonProperty("password")
        private String password;

//        @JsonProperty("orders")
//        private List<Order> orders;

        // field added and used by register/auth service
        @JsonProperty("enabled")
        private boolean enabled;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

//        public Builder setOrders(List<Order> orders) {
//            this.orders = orders;
//            return this;
//        }

        // method used by register/auth service
        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        public User build() {
            return new User(this);
        }
    }
}
