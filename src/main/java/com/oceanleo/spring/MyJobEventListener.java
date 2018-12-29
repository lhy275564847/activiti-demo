package com.oceanleo.spring;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

/**
 * @author haiyang.li
 */
public class MyJobEventListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent event) {
        System.out.println("MyJobEventListener Event Type:" + event.getType());
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
