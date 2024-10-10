package tnt.cqrs_writer.framework;

import tnt.cqrs_writer.commands.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandlerOf {
    Class<? extends Command> value();
}
