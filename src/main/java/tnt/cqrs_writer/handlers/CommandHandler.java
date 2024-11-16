package tnt.cqrs_writer.handlers;

import tnt.cqrs_writer.commands.Command;
import tnt.cqrs_writer.framework.events.DomainBaseEvent;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;

public interface CommandHandler<T extends Command> {
    List<DomainBaseEvent> handle(T command) throws InstanceAlreadyExistsException, InstanceNotFoundException;
}
