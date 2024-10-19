package tnt.cqrs_writer.handlers;

import tnt.cqrs_writer.commands.Command;

import javax.management.InstanceAlreadyExistsException;

public interface CommandHandler<T extends Command> {
    void handle(T command) throws InstanceAlreadyExistsException;
}
