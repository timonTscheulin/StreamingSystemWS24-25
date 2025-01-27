package tnt.esper.listeners;

import com.espertech.esper.runtime.client.*;
import com.espertech.esper.common.client.EventBean;
import tnt.esper.data_types.distributing.SpeedDecrease;

public class SpeedDecreaseListener implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return;
        }

        for (EventBean eventBean : newEvents) {
            SpeedDecrease decreaseEvent = (SpeedDecrease) eventBean.getUnderlying();
            System.out.println("Geschwindigkeitsabfall erkannt: " + decreaseEvent);
            // Hier kannst du zusätzliche Verarbeitung hinzufügen, z.B. Alarmierung
        }
    }
}