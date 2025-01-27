package tnt.esper.listeners;

import com.espertech.esper.runtime.client.*;
import com.espertech.esper.common.client.EventBean;
import tnt.esper.data_types.landing.FlattenedSensorData;
import tnt.generators.Events.SensorDataRecord;

public class FlatteningListener implements UpdateListener {

    private final EPRuntime runtime;

    public FlatteningListener(EPRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents,
                       EPStatement statement, EPRuntime epService) {
        if (newEvents == null) {
            return;
        }

        for (EventBean eventBean : newEvents) {
            // "Rohes" SensorDataRecord Objekt holen
            SensorDataRecord record = (SensorDataRecord) eventBean.getUnderlying();
            // Liste mit Messwerten durchgehen
            int idx = 0;
            for (Double measurement : record.getValue()) {
                // Für jeden Einzelwert ein FlattenedSensorData-Event erzeugen …
                FlattenedSensorData flattened = new FlattenedSensorData(
                        record.getTimestamp().toInstant().toEpochMilli(),
                        record.getSensorId(),
                        measurement,
                        idx
                );
                // … und direkt an Esper zurückschicken:
                runtime.getEventService().sendEventBean(
                        flattened,
                        "FlattenedSensorData"// Name des Event-Typs
                );
            }
        }
    }
}
