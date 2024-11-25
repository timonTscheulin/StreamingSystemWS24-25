package tnt.eventstore.event_contract.position;

import tnt.cqrs_writer.framework.events.DomainBaseEvent;
import tnt.eventstore.event_contract.vehicle.StoreBaseVehicle;

public class StoreHelperPosition extends StoreBasePosition {
        /**
         * A helper class representing the "Position" domain in the event store.
         *
         * This class is not intended to be instantiated as an actual event but serves as
         * a utility to retrieve the event domain ("Position") associated with all vehicle-related events.
         *
         * Use this class for tasks such as filtering or querying events by their domain
         * without needing to instantiate a specific event type (e.g., `StorePositionOccupied`).
         *
         * Note: Methods like `toDomainEvent()` and `getEventType()` are unsupported and will
         * throw an exception if invoked, as this class is meant solely for meta-level operations.
         *
         * Example Usage:
         * <pre>{@code
         * String domain = new StoreHelperPosition().getEventDomain();
         * }</pre>
         *
         * @see StoreBaseVehicle
         */

        public StoreHelperPosition() {
            super(0,0);
        }

        @Override
        public DomainBaseEvent toDomainEvent() {
            throw new UnsupportedOperationException("Not supported. Its a helper event and supports only to retrieve the domain of the event group.");
        }

        @Override
        public String getEventType() {
            throw new UnsupportedOperationException("Not supported. Its a helper event and supports only to retrieve the domain of the event group.");
        }

}
