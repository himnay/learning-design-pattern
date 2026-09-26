package com.org.pattern.creational.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Each singleton variant keeps the promise its README section makes. */
class SingletonGuardsTest {

    @Test
    @DisplayName("Double-checked locking hands every concurrent caller the same instance")
    void doubleCheckedLockingIsThreadSafe() throws Exception {
        assertThat(instancesSeenConcurrently(ThreadSafeSingleton::getInstance)).hasSize(1);
    }

    @Test
    @DisplayName("The holder idiom hands every concurrent caller the same instance")
    void holderIdiomIsThreadSafe() throws Exception {
        assertThat(instancesSeenConcurrently(HolderSingleton::getInstance)).hasSize(1);
    }

    @Test
    @DisplayName("Both thread-safe variants can be used in one run (the demo used to crash)")
    void demoRunsBothVariants() {
        assertThatCode(ThreadSafeSingleton::demo).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Reflection cannot create a second instance once one exists")
    void reflectionIsBlocked() throws Exception {
        ReflectionSafeSingleton.getInstance();
        HolderSingleton.getInstance();
        assertReflectionRefused(ReflectionSafeSingleton.class);
        assertReflectionRefused(HolderSingleton.class);
    }

    @Test
    @DisplayName("Deserialization returns the existing instance via readResolve()")
    void serializationKeepsSingleInstance() throws Exception {
        SerializationSafeSingleton original = SerializationSafeSingleton.getInstance();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(original);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            assertThat(in.readObject()).isSameAs(original);
        }
    }

    @Test
    @DisplayName("clone() is refused")
    void cloneIsBlocked() {
        assertThatThrownBy(() -> CloneSafeSingleton.getInstance().clone())
                .isInstanceOf(CloneNotSupportedException.class);
    }

    @Test
    @DisplayName("The JVM itself refuses to create enum constants reflectively")
    void enumCannotBeReflected() {
        Constructor<?> ctor = SingletonEnum.class.getDeclaredConstructors()[0];
        ctor.setAccessible(true);
        assertThatThrownBy(() -> ctor.newInstance("INSTANCE", 1)).isInstanceOf(IllegalArgumentException.class);
    }

    private static Set<Object> instancesSeenConcurrently(Callable<Object> getInstance) throws Exception {
        int threads = 32;
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService pool = Executors.newFixedThreadPool(threads)) {
            List<Future<Object>> futures = IntStream.range(0, threads)
                    .mapToObj(i -> pool.submit(() -> {
                        start.await();
                        return getInstance.call();
                    }))
                    .toList();
            start.countDown();
            Set<Object> seen = Collections.newSetFromMap(new IdentityHashMap<>());
            for (Future<Object> future : futures) {
                seen.add(future.get());
            }
            return seen;
        }
    }

    private static void assertReflectionRefused(Class<?> type) throws Exception {
        Constructor<?> ctor = type.getDeclaredConstructor();
        ctor.setAccessible(true);
        assertThatThrownBy(ctor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseInstanceOf(IllegalStateException.class);
    }
}
