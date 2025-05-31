package br.com.dio.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; 

public class NotifierService {
    private final List<EventListener> clearSpaceListener = new CopyOnWriteArrayList<>();

    public void subscribe(EventEnum eventType, EventListener listener) {
        if (eventType == EventEnum.CLEAR_SPACE) clearSpaceListener.add(listener);
    }

    public void notify(EventEnum eventType) {
        if (eventType == EventEnum.CLEAR_SPACE) {
            clearSpaceListener.parallelStream()
                              .forEach(listener -> listener.update(eventType));
        }
    }


}