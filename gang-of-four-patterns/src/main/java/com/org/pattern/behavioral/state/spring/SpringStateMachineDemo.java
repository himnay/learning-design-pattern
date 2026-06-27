package com.org.pattern.behavioral.state.spring;

/**
 * Spring Boot — State Pattern
 *
 * Spring State Machine (spring-statemachine) implements the State pattern.
 * It manages transitions between well-defined states via events/triggers.
 *
 * Real-world use case: Order lifecycle management
 *   PENDING → PAID → PROCESSING → SHIPPED → DELIVERED
 *                  ↘ CANCELLED
 *
 * Spring State Machine provides:
 *   - State enum definition
 *   - Event enum definition
 *   - Guard conditions (transition allowed?)
 *   - Actions (side effects on transition)
 *   - Listeners for state change events
 *   - Persistence (JPA, Redis) for stateful workflows
 *
 * This demo shows a simplified version of the same concept
 * (without the spring-statemachine dependency).
 */

// ── State and Event enums (Spring State Machine uses the same structure) ──────
enum OrderState  { PENDING, PAID, PROCESSING, SHIPPED, DELIVERED, CANCELLED }
enum OrderEvent  { PAY, START_PROCESSING, SHIP, DELIVER, CANCEL }

// ── State handler (equivalent to spring-statemachine StateContext action) ─────
interface OrderStateHandler {
    OrderState on(OrderEvent event, String orderId);
    OrderState getState();
}

class PendingState implements OrderStateHandler {
    @Override public OrderState getState() { return OrderState.PENDING; }
    @Override public OrderState on(OrderEvent event, String orderId) {
        return switch (event) {
            case PAY    -> { System.out.println("[PENDING→PAID] payment received for " + orderId); yield OrderState.PAID; }
            case CANCEL -> { System.out.println("[PENDING→CANCELLED] order cancelled");             yield OrderState.CANCELLED; }
            default     -> { System.out.println("[PENDING] invalid event: " + event);               yield OrderState.PENDING; }
        };
    }
}

class PaidState implements OrderStateHandler {
    @Override public OrderState getState() { return OrderState.PAID; }
    @Override public OrderState on(OrderEvent event, String orderId) {
        return switch (event) {
            case START_PROCESSING -> { System.out.println("[PAID→PROCESSING] warehouse notified"); yield OrderState.PROCESSING; }
            case CANCEL           -> { System.out.println("[PAID→CANCELLED] refund initiated");    yield OrderState.CANCELLED; }
            default               -> { System.out.println("[PAID] invalid event: " + event);       yield OrderState.PAID; }
        };
    }
}

class ProcessingState implements OrderStateHandler {
    @Override public OrderState getState() { return OrderState.PROCESSING; }
    @Override public OrderState on(OrderEvent event, String orderId) {
        return switch (event) {
            case SHIP -> { System.out.println("[PROCESSING→SHIPPED] tracking number assigned"); yield OrderState.SHIPPED; }
            default   -> { System.out.println("[PROCESSING] invalid event: " + event);          yield OrderState.PROCESSING; }
        };
    }
}

class TerminalState implements OrderStateHandler {
    private final OrderState state;
    TerminalState(OrderState state) { this.state = state; }
    @Override public OrderState getState() { return state; }
    @Override public OrderState on(OrderEvent event, String orderId) {
        System.out.println("Terminal state " + state + " — no further transitions");
        return state;
    }
}

class ShippedState implements OrderStateHandler {
    @Override public OrderState getState() { return OrderState.SHIPPED; }
    @Override public OrderState on(OrderEvent event, String orderId) {
        return switch (event) {
            case DELIVER -> { System.out.println("[SHIPPED→DELIVERED] delivery confirmed"); yield OrderState.DELIVERED; }
            default      -> { System.out.println("[SHIPPED] invalid event: " + event);     yield OrderState.SHIPPED; }
        };
    }
}

// ── Context — owns and transitions the state ──────────────────────────────────
class OrderStateMachine {

    private OrderStateHandler currentHandler;
    private final String orderId;

    public OrderStateMachine(String orderId) {
        this.orderId = orderId;
        this.currentHandler = new PendingState();
        System.out.println("Order " + orderId + " created in state: " + currentHandler.getState());
    }

    public void sendEvent(OrderEvent event) {
        OrderState next = currentHandler.on(event, orderId);
        currentHandler = resolveHandler(next);
    }

    public OrderState getState() { return currentHandler.getState(); }

    private OrderStateHandler resolveHandler(OrderState state) {
        return switch (state) {
            case PENDING    -> new PendingState();
            case PAID       -> new PaidState();
            case PROCESSING -> new ProcessingState();
            case SHIPPED    -> new ShippedState();
            default         -> new TerminalState(state);
        };
    }
}

public class SpringStateMachineDemo {

    public static void demo() {
        System.out.println("=== Spring State Pattern — Order State Machine Demo ===");

        OrderStateMachine sm = new OrderStateMachine("ORD-001");
        sm.sendEvent(OrderEvent.PAY);
        sm.sendEvent(OrderEvent.START_PROCESSING);
        sm.sendEvent(OrderEvent.SHIP);
        sm.sendEvent(OrderEvent.DELIVER);
        System.out.println("Final state: " + sm.getState());

        System.out.println("""

            Spring State Machine equivalent (spring-statemachine dependency):
              @Configuration
              @EnableStateMachine
              public class OrderStateMachineConfig
                      extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {

                  @Override
                  public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) {
                      states.withStates()
                          .initial(OrderState.PENDING)
                          .states(EnumSet.allOf(OrderState.class))
                          .end(OrderState.DELIVERED)
                          .end(OrderState.CANCELLED);
                  }

                  @Override
                  public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) {
                      transitions
                          .withExternal().source(PENDING).target(PAID).event(PAY)
                              .action(paymentReceivedAction())
                          .and()
                          .withExternal().source(PAID).target(PROCESSING).event(START_PROCESSING)
                          .and()
                          .withExternal().source(PROCESSING).target(SHIPPED).event(SHIP)
                          .and()
                          .withExternal().source(SHIPPED).target(DELIVERED).event(DELIVER);
                  }
              }

              // Usage
              stateMachine.sendEvent(OrderEvent.PAY);
              stateMachine.sendEvent(OrderEvent.SHIP);
            """);
    }
}
