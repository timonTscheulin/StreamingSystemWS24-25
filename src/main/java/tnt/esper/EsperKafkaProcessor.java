package tnt.esper;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.*;
import com.espertech.esper.runtime.client.*;
import tnt.esper.entities.FlattenedSensorData;
import tnt.esper.listeners.FlattenedOutputListener;
import tnt.esper.listeners.SensorEventListener;
import tnt.esper.udf.FlattenSensorEvent;
import tnt.generators.Events.SensorDataRecord;
import tnt.generators.SimpleDataGenerator;

import java.util.List;

public class EsperKafkaProcessor {

    public static void main(String[] args) {

        // 1) Esper-Konfiguration
        Configuration esperConfig = new Configuration();

        //esperConfig.configure("esper_config.xml");

        // Registriere das Original-Event (SensorDataRecord) ...
        esperConfig.getCommon().addEventType(SensorDataRecord.class);
        // ... und die geflattete Struktur (FlattenedSensorData)
        esperConfig.getCommon().addEventType(FlattenedSensorData.class);

        // 2) Runtime anlegen
        EPRuntime runtime = EPRuntimeProvider.getDefaultRuntime(esperConfig);
        runtime.initialize();

        String testEPL = "@name('getFlattenSensorsEvents') select * from flattenedData;";
        String registerUDF = "insert into flattenedData select unnest(flatten(*)) from " + SensorDataRecord.class.getSimpleName() + ";";
        // 3.1) Kompilieren und Deployen
        EPCompiler compiler = EPCompilerProvider.getCompiler();
        CompilerArguments cargs = new CompilerArguments(esperConfig);
        String combinedEPL = registerUDF + testEPL;

        EPDeployment deployment = null;

        try {
            EPCompiled compiled = compiler.compile(combinedEPL, cargs);
            deployment = runtime.getDeploymentService().deploy(compiled);

        } catch (EPCompileException | EPDeployException e) {
            System.err.println("Fehler beim Kompilieren/Deployen: " + e.getMessage());
            return;
        }

        EPStatement statement = runtime.getDeploymentService().getStatement(deployment.getDeploymentId(),"getFlattenSensorsEvents");
        statement.addListener(new FlattenedOutputListener());


        // generate data
        SimpleDataGenerator generator = new SimpleDataGenerator(
                100, 3, 3, 900, 1100, 70, 100
        );

        while (true) {
            List<SensorDataRecord> batch = generator.tick();
            batch.forEach(System.out::println);

            for (SensorDataRecord rec : batch) {
                runtime.getEventService().sendEventBean(
                        rec,
                        SensorDataRecord.class.getSimpleName()
                );
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}