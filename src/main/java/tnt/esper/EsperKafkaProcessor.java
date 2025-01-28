package tnt.esper;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.compiler.client.EPCompileException;

import com.espertech.esper.runtime.client.*;
import tnt.esper.data_types.distributing.AverageSensorSpeed;
import tnt.esper.data_types.landing.FlattenedSensorData;
import tnt.esper.data_types.distributing.SpeedDecrease;
import tnt.esper.data_types.processing.PreprocessedSensorData;
import tnt.esper.listeners.AvgSpeedListener;
import tnt.esper.listeners.FlatteningListener;
import tnt.esper.listeners.SpeedDecreaseListener;
import tnt.generators.Events.SensorDataRecord;
import tnt.generators.SimpleDataGenerator;

import java.util.List;

public class EsperKafkaProcessor {

    public static void main(String[] args) {

        // 1) Esper-Konfiguration
        Configuration esperConfig = new Configuration();

        // Registriere das Original-Event (SensorDataRecord)
        esperConfig.getCommon().addEventType(SensorDataRecord.class);

        // Registriere die geflattete Struktur (FlattenedSensorData)
        esperConfig.getCommon().addEventType(FlattenedSensorData.class);
        esperConfig.getCommon().addEventType(PreprocessedSensorData.class);
        esperConfig.getCommon().addEventType(AverageSensorSpeed.class);

        // Registriere den StauAlarmEvent Typ
        esperConfig.getCommon().addEventType(SpeedDecrease.class);

        // 2) Runtime anlegen
        EPRuntime runtime = EPRuntimeProvider.getDefaultRuntime(esperConfig);
        runtime.initialize();

        String epl = """
           @name('FlattenSensorData') select * from SensorDataRecord;

           @name('PreprocessSensorData') insert into PreprocessedSensorData select timestamp, sensorId, measurement as speed, 'm/s' as unit from FlattenedSensorData where measurement > 0;

           create context WindowBySensorId partition by sensorId from PreprocessedSensorData;
           @name('CalculateAverageSpeed') context WindowBySensorId insert into AverageSensorSpeed select sensorId, current_timestamp as beginTimestamp, 10, avg(speed) from PreprocessedSensorData#time_batch(10 sec) group by sensorId having avg(speed) is not null;

           create context AvgWindowBySensorId partition by sensorId from AverageSensorSpeed;
           @name('speedAlarm')
                   context AvgWindowBySensorId
                   select first_avg.sensorId as sensorId,
                          first_avg.averageSpeed as averageSpeedOne,
                          second_avg.averageSpeed as averageSpeedTwo,
                          third_avg.averageSpeed as averageSpeedThree
                   from pattern[every(first_avg = AverageSensorSpeed) -> second_avg = AverageSensorSpeed -> third_avg = AverageSensorSpeed]
                   where second_avg.averageSpeed / first_avg.averageSpeed < 0.75 and second_avg.averageSpeed + 2 > third_avg.averageSpeed and
                   second_avg.averageSpeed - 2 < third_avg.averageSpeed;
        """ ;


        // 3.1) Kompilieren und Deployen
        EPCompiler compiler = EPCompilerProvider.getCompiler();
        CompilerArguments cargs = new CompilerArguments(esperConfig);

        // Debug: EPL anzeigen
        System.out.println("EPL to compile:\n" + epl);

        EPDeployment deployment = null;

        try {
            EPCompiled compiled = compiler.compile(epl, cargs);
            deployment = runtime.getDeploymentService().deploy(compiled);

        } catch (EPCompileException  e) {
            e.printStackTrace(); // Erweiterte Fehlermeldungen
            System.err.println("Fehler beim Kompilieren/Deployen: " + e.getMessage());
            return;
        } catch (EPDeployException e) {
            throw new RuntimeException(e);
        }

        // Listener für SensorDataRecordSelect
        EPStatement statement = runtime.getDeploymentService().getStatement(deployment.getDeploymentId(), "FlattenSensorData");
        statement.addListener(new FlatteningListener(runtime));

        EPStatement avgSpeedStatement = runtime.getDeploymentService().getStatement(deployment.getDeploymentId(), "CalculateAverageSpeed");
        avgSpeedStatement.addListener(new AvgSpeedListener());

        EPStatement jamStatement = runtime.getDeploymentService().getStatement(deployment.getDeploymentId(), "speedAlarm");
        jamStatement.addListener(new SpeedDecreaseListener());



        // Daten generieren und an Esper senden
        SimpleDataGenerator generator = new SimpleDataGenerator(
                1000, 3, 7, 900, 1100, 70, 100
        );

        boolean jam_started = false;
        while (true) {
            List<SensorDataRecord> batch = generator.tick();
            //batch.forEach(System.out::println);

            if(generator.getVehicleLeftCounter() > 5 && !jam_started) {
                System.out.println("Create jam ...");
                jam_started = true;
                generator.set_jam_at(700);
            }

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
