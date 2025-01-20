package tnt.esper.listeners;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

import java.util.Arrays;

public class AvgValBySensorListener implements UpdateListener {


    @Override
    public void update(EventBean[] eventBeans, EventBean[] eventBeans1, EPStatement epStatement, EPRuntime epRuntime) {
        if (eventBeans == null) return;

        Arrays.stream(eventBeans).forEach(eventBean -> {
            Object sensorId = eventBean.get("sensorId");
            Object avgVal   = eventBean.get("avgVal");
            System.out.printf(
                    "[%s] sensorId=%s, avgVal=%.2f m/s%n",
                    epStatement.getName(),
                    sensorId,
                    avgVal
            );
        });
    }
}