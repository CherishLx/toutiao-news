package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * ThreadLocal类，当前线程ThreadLocal与线程存放的副本变量的一个映射，
 * 别的线程无法访问当前线程的副本值，形成了线程之间副本的隔离，互不影响
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
