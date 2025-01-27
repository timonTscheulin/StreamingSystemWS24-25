package tnt.esper.listeners;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

public class AvgSpeedListener implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement epStatement, EPRuntime epRuntime) {
        if (newEvents == null) {
            return;
        }

        for (EventBean eventBean : newEvents) {
            System.out.println(eventBean.getUnderlying().toString());
        }
    }
}
