package com.org.pattern.behavioral.interpreter;

/**
 * Interpreter — defines a grammar for a language and provides an interpreter to deal with that grammar.
 *
 * Real-world analogy: Boolean expression evaluator for simple access-control rules.
 */
public interface Expression {
    boolean interpret(String context);
}
