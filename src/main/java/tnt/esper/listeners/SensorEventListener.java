package tnt.esper.listeners;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

public class SensorEventListener implements UpdateListener {

    public SensorEventListener() {
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents,
                       EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return;
        }

        for (EventBean eventBean : newEvents) {
            System.out.println("[" + getClass().getName() + "] "
                    + statement.getName()
                    + " -> "
                    + eventBean.getUnderlying());
        }
    }
}