package saket.consumer.services;

import org.springframework.stereotype.Component;

import saket.consumer.domain.EventDTO;

@Component
public class LocationStrategy implements ITypeStrategy {
    @Override
    public String getTopicType() {
        return "saket.location";
    }

    @Override
    public boolean handle(EventDTO event) {
        
        return false;
    }
}
