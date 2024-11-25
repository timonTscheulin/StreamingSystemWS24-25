package tnt.cqrs_writer.framework;

import org.reflections.Reflections;
import tnt.cqrs_writer.commands.Command;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.cqrs_writer.handlers.CommandHandler;
import tnt.eventstore.EventStore;
import tnt.eventstore.connectors.InMemoryEventStore;
import tnt.eventstore.connectors.ActiveMQProducerConnector;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandDispatcher {
    private final Map<Class<? extends Command>, CommandHandler<? extends Command>> handlers = new HashMap<>();
    //private final ActiveMQProducerConnector eventStore = new ActiveMQProducerConnector();
    private final EventStore eventStore;

    public CommandDispatcher(EventStore eventStore) {
        this.eventStore = eventStore;
        registerAnnotatedHandlers("tnt.cqrs_writer.handlers");
    }

    private void registerAnnotatedHandlers(String packageName) {
        Reflections reflections = new Reflections(packageName/*, new TypeAnnotationsScanner(), new SubTypesScanner()*/);
        Set<Class<?>> handlerClasses = reflections.getTypesAnnotatedWith(CommandHandlerOf.class);

        for (Class<?> handlerClass : handlerClasses) {
            CommandHandlerOf annotation = handlerClass.getAnnotation(CommandHandlerOf.class);
            Class<? extends Command> commandType = annotation.value();

            try {
                CommandHandler<?> handlerInstance = (CommandHandler<?>) handlerClass.getDeclaredConstructor().newInstance();
                if (handlers.containsKey(commandType)) {
                    throw new KeyAlreadyExistsException("You have tried to register two handlers on one command.");
                }
                handlers.put(commandType, handlerInstance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to register command handler of " + handlerClass.getName(), e);
            }
        }
    }

    public <T extends Command> void dispatch(T command) throws Exception {
        @SuppressWarnings("unchecked")
        CommandHandler<T> handler = (CommandHandler<T>) handlers.get(command.getClass());

        if(handler == null) {
            throw new UnsupportedOperationException("No handler registered for command " + command.getClass());
        }

        // @todo implement transactions for write into event store
        List<DomainBaseEvent> events = handler.handle(command);
        eventStore.store(events);
        //InMemoryEventStore.getInstance().store(events);

        // end of transaction
    }
}
