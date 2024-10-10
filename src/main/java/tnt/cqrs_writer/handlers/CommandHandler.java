package tnt.cqrs_writer.handlers;

import tnt.cqrs_writer.commands.Command;

public interface CommandHandler<T extends Command> {
    void handle(T command);
}
