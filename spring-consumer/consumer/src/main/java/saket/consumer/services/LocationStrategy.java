package saket.consumer.services;

import saket.consumer.domain.EventDTO;

public class LocationStrategy implements ITypeStrategy {
    @Override
    public String getTopicType() {
        return "saket.location";
    }

    @Override
    public boolean handle(EventDTO event) {
        // TODO Auto-generated method stub
        return false;
    }
}
