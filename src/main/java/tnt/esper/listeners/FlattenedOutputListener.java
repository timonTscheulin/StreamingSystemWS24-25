package tnt.esper.listeners;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.*;
import java.util.Arrays;

public class FlattenedOutputListener implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents,
                       EPStatement stmt, EPRuntime rt) {
        if (newEvents != null) {
            Arrays.stream(newEvents).forEach(eventBean -> {
                System.out.println("[FlattenedOutputListener] " + eventBean.getUnderlying());
            });
        }
    }
}
