package tnt.cqrs_writer.framework;

import org.reflections.Reflections;
import tnt.cqrs_writer.commands.Command;
import tnt.cqrs_writer.handlers.CommandHandler;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandDispatcher {
    private final Map<Class<? extends Command>, CommandHandler<? extends Command>> handlers = new HashMap<>();

    public CommandDispatcher() {
        registerAnnotatedHandlers("src.main.java.tnt.cqrs_writer.handlers");
    }

    private void registerAnnotatedHandlers(String packageName) {
        Reflections reflections = new Reflections(packageName);
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

    public <T extends Command> void dispatch(T command) {
        @SuppressWarnings("unchecked")
        CommandHandler<T> handler = (CommandHandler<T>) handlers.get(command.getClass());

        if(handler == null) {
            throw new UnsupportedOperationException("No handler registered for command " + command.getClass());
        }

        handler.handle(command);
    }
}
