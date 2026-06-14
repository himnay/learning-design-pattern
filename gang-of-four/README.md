# Gang of Four — Design Patterns in Java

This module implements all **23 classic GoF design patterns** organized into three categories.  
Each pattern has a real-world analogy, a working Java example, and a `demo()` method you can run.

**Base package:** `com.org.pattern.gangoffour`  
**Java version:** 25 | **Spring Boot:** 4.1.0

---

## Table of Contents

- [Creational Patterns](#creational-patterns)
  - [Singleton](#1-singleton)
  - [Factory Method](#2-factory-method)
  - [Abstract Factory](#3-abstract-factory)
  - [Builder](#4-builder)
  - [Prototype](#5-prototype)
- [Structural Patterns](#structural-patterns)
  - [Adapter](#6-adapter)
  - [Bridge](#7-bridge)
  - [Composite](#8-composite)
  - [Decorator](#9-decorator)
  - [Facade](#10-facade)
  - [Flyweight](#11-flyweight)
  - [Proxy](#12-proxy)
- [Behavioral Patterns](#behavioral-patterns)
  - [Chain of Responsibility](#13-chain-of-responsibility)
  - [Command](#14-command)
  - [Iterator](#15-iterator)
  - [Mediator](#16-mediator)
  - [Memento](#17-memento)
  - [Observer](#18-observer)
  - [State](#19-state)
  - [Strategy](#20-strategy)
  - [Template Method](#21-template-method)
  - [Visitor](#22-visitor)
  - [Interpreter](#23-interpreter)

---

## Creational Patterns

Creational patterns deal with **object creation**, abstracting the instantiation process to make a system independent of how its objects are created, composed, and represented.

---

### 1. Singleton

**Intent:** Ensure a class has only one instance and provide a global access point to it.

**Analogy:** A shared database connection pool — there must be exactly one pool managing all connections.

**When to use:**
- Configuration managers
- Connection pools
- Logging services
- Thread pools

**Package:** `creational/singleton`

| File | Role |
|------|------|
| `DatabaseConnection.java` | Thread-safe singleton using double-checked locking + `volatile` |

**Key implementation detail:**
```java
private static volatile DatabaseConnection instance;

public static DatabaseConnection getInstance() {
    if (instance == null) {
        synchronized (DatabaseConnection.class) {
            if (instance == null) {
                instance = new DatabaseConnection("jdbc:postgresql://localhost:5432/appdb");
            }
        }
    }
    return instance;
}
```
The `volatile` keyword prevents the CPU from reordering the write to `instance` before the constructor completes. The double-check avoids acquiring the lock on every call after initialization.

**Demo output:**
```
DatabaseConnection created: jdbc:postgresql://localhost:5432/appdb
Same instance? true
[Query #1] on [jdbc:postgresql://localhost:5432/appdb]: SELECT * FROM users
[Query #2] on [jdbc:postgresql://localhost:5432/appdb]: SELECT * FROM orders
```

---

### 2. Factory Method

**Intent:** Define an interface for creating an object, but let subclasses (or a factory class) decide which class to instantiate.

**Analogy:** A notification service that creates Email, SMS, or Push notifications based on a channel type.

**When to use:**
- When the exact type of object to create isn't known until runtime
- When you want to decouple creation from usage
- Payment gateways, logger providers, serializers

**Package:** `creational/factorymethod`

| File | Role |
|------|------|
| `Notification.java` | Product interface |
| `EmailNotification.java` | Concrete product |
| `SmsNotification.java` | Concrete product |
| `PushNotification.java` | Concrete product |
| `NotificationFactory.java` | Factory with static `create(channel)` method |

**Key implementation detail:**
```java
public static Notification create(String channel) {
    return switch (channel.toUpperCase()) {
        case "EMAIL" -> new EmailNotification();
        case "SMS"   -> new SmsNotification();
        case "PUSH"  -> new PushNotification();
        default      -> throw new IllegalArgumentException("Unknown channel: " + channel);
    };
}
```

**Demo output:**
```
Email to [user@example.com]: Your order has been shipped!
SMS to [user@example.com]: Your order has been shipped!
Push notification to device [user@example.com]: Your order has been shipped!
```

---

### 3. Abstract Factory

**Intent:** Provide an interface for creating **families of related objects** without specifying their concrete classes.

**Analogy:** A cross-platform UI toolkit — create Windows-style or macOS-style buttons and checkboxes without the client knowing which platform it's running on.

**When to use:**
- When a system must be independent of how its products are created
- When you want to enforce that products from one family are used together
- Theme systems, cross-platform UI, database driver sets

**Package:** `creational/abstractfactory`

| File | Role |
|------|------|
| `GUIFactory.java` | Abstract factory interface |
| `WindowsFactory.java` | Concrete factory for Windows |
| `MacFactory.java` | Concrete factory for macOS |
| `Button.java` | Abstract product |
| `Checkbox.java` | Abstract product |
| `WindowsButton.java`, `MacButton.java` | Concrete products |
| `WindowsCheckbox.java`, `MacCheckbox.java` | Concrete products |
| `Application.java` | Client that uses the factory |

**Key implementation detail:**
```java
// Client code never imports concrete classes
public Application(GUIFactory factory) {
    this.button = factory.createButton();
    this.checkbox = factory.createCheckbox();
}
```

**Demo output:**
```
Rendering Windows-style button
Rendering Windows-style checkbox [ ]
Rendering macOS-style button
Rendering macOS-style checkbox [ ]
```

---

### 4. Builder

**Intent:** Separate the construction of a complex object from its representation, allowing the same construction process to create different representations.

**Analogy:** Ordering a customized pizza — you specify each attribute step by step and call `build()` at the end.

**When to use:**
- Objects with many optional parameters (avoid telescoping constructors)
- When construction involves multiple steps
- Immutable objects with complex initialization

**Package:** `creational/builder`

| File | Role |
|------|------|
| `Pizza.java` | Product with inner `Builder` class |

**Key implementation detail:**
```java
Pizza margherita = new Pizza.Builder()
    .size("Large")
    .crust("Thin")
    .sauce("Tomato")
    .toppings(List.of("Mozzarella", "Basil"))
    .build();
```
`Pizza` has a private constructor; the only way to create it is through the `Builder`. The `build()` method validates required fields before constructing.

**Demo output:**
```
Pizza{size=Large, crust=Thin, sauce=Tomato, toppings=[Mozzarella, Basil], extraCheese=false, glutenFree=false}
Pizza{size=Medium, crust=Stuffed, sauce=Pesto, toppings=[Bell Peppers, Mushrooms, Olives], extraCheese=true, glutenFree=true}
```

---

### 5. Prototype

**Intent:** Specify the kinds of objects to create using a prototypical instance, and create new objects by **copying** this prototype.

**Analogy:** A graphics editor that clones existing shapes rather than constructing from scratch — especially useful when construction is expensive.

**When to use:**
- When object creation is costly (database lookups, complex initialization)
- When you need many instances that differ only slightly
- Copy-on-write scenarios

**Package:** `creational/prototype`

| File | Role |
|------|------|
| `Shape.java` | Abstract prototype with copy constructor |
| `Circle.java` | Concrete prototype |
| `Rectangle.java` | Concrete prototype |
| `ShapeRegistry.java` | Cache of prototype instances |

**Key implementation detail:**
```java
// Copy constructor — deep clone via constructor, not Object.clone()
public Circle(Circle source) {
    super(source);
    if (source != null) this.radius = source.radius;
}

@Override
public Circle clone() {
    return new Circle(this);
}
```
Using copy constructors instead of `Cloneable` avoids the pitfalls of Java's shallow `Object.clone()`.

**Demo output:**
```
Original: Circle{radius=5.0, color=Red, area=78.54}
Clone (modified): Circle{radius=5.0, color=Green, area=78.54}
Same instance? false
```

---

## Structural Patterns

Structural patterns deal with **object composition**, creating relationships between objects to form larger structures while keeping them flexible and efficient.

---

### 6. Adapter

**Intent:** Convert the interface of a class into another interface that clients expect. Lets incompatible interfaces work together.

**Analogy:** An audio player that natively plays MP3 but needs to play VLC and MP4 files — the `MediaAdapter` bridges the gap.

**When to use:**
- Integrating legacy code with new interfaces
- Using third-party libraries with incompatible APIs
- Wrapping external services

**Package:** `structural/adapter`

| File | Role |
|------|------|
| `MediaPlayer.java` | Target interface (client uses this) |
| `AdvancedMediaPlayer.java` | Adaptee interface (incompatible) |
| `VlcPlayer.java`, `Mp4Player.java` | Concrete adaptees |
| `MediaAdapter.java` | Adapter — wraps adaptee, implements target |
| `AudioPlayer.java` | Client — uses `MediaPlayer`, delegates to adapter |

**Key implementation detail:**
```java
public class MediaAdapter implements MediaPlayer {
    private final AdvancedMediaPlayer advancedPlayer;

    public MediaAdapter(String audioType) {
        this.advancedPlayer = switch (audioType.toLowerCase()) {
            case "vlc" -> new VlcPlayer();
            case "mp4" -> new Mp4Player();
            default    -> throw new IllegalArgumentException("Unsupported: " + audioType);
        };
    }
}
```

**Demo output:**
```
Playing MP3 file: song.mp3
Playing VLC file: movie.vlc
Playing MP4 file: video.mp4
Unsupported format: avi
```

---

### 7. Bridge

**Intent:** Decouple an abstraction from its implementation so that the two can vary independently.

**Analogy:** Drawing shapes (abstraction) with different rendering APIs like SVG or Canvas (implementation). New shapes and new APIs can be added independently.

**When to use:**
- When you want to avoid a permanent binding between abstraction and implementation
- When both abstractions and implementations should be extensible via subclassing
- Switching between SQL and NoSQL databases, different rendering engines

**Package:** `structural/bridge`

| File | Role |
|------|------|
| `DrawingAPI.java` | Implementor interface |
| `SvgDrawingAPI.java`, `CanvasDrawingAPI.java` | Concrete implementors |
| `Shape.java` | Abstraction — holds reference to `DrawingAPI` |
| `CircleShape.java` | Refined abstraction |

**Key implementation detail:**
```java
// Abstraction holds a reference to the implementor (bridge)
public abstract class Shape {
    protected final DrawingAPI drawingAPI;
    // ...
}

// Client composes abstraction + implementor at runtime
Shape svgCircle = new CircleShape(10, 20, 5, new SvgDrawingAPI());
Shape canvasCircle = new CircleShape(10, 20, 5, new CanvasDrawingAPI());
```

**Demo output:**
```
SVG: <circle cx='10.0' cy='20.0' r='5.0'/>
Canvas: ctx.arc(10.0, 20.0, 5.0, 0, 2*Math.PI)
SVG: <circle cx='10.0' cy='20.0' r='10.0'/>
```

---

### 8. Composite

**Intent:** Compose objects into **tree structures** to represent part-whole hierarchies. Lets clients treat individual objects and compositions uniformly.

**Analogy:** A file system — a directory can contain files or other directories; both expose the same `getSize()` and `print()` interface.

**When to use:**
- Tree structures (org charts, menus, DOM, file systems)
- When clients should ignore the difference between individual objects and compositions

**Package:** `structural/composite`

| File | Role |
|------|------|
| `FileSystemComponent.java` | Component interface |
| `File.java` | Leaf node |
| `Directory.java` | Composite node — contains children |

**Key implementation detail:**
```java
// Directory's size is the recursive sum of all children
@Override
public long getSize() {
    return children.stream().mapToLong(FileSystemComponent::getSize).sum();
}
```

**Demo output:**
```
+ root/ (7680 bytes)
  + src/ (2560 bytes)
    - Main.java (2048 bytes)
    - Config.java (512 bytes)
  + test/ (1024 bytes)
    - MainTest.java (1024 bytes)
  - pom.xml (4096 bytes)
Total size: 7680 bytes
```

---

### 9. Decorator

**Intent:** Attach additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing for extending functionality.

**Analogy:** A coffee shop — start with `SimpleCoffee` and wrap it with `MilkDecorator`, `SugarDecorator`, or `VanillaDecorator`, each adding to the description and cost.

**When to use:**
- Adding behavior to individual objects without affecting others of the same class
- When subclassing leads to an explosion of classes
- Java I/O streams (`BufferedReader` wrapping `FileReader`)

**Package:** `structural/decorator`

| File | Role |
|------|------|
| `Coffee.java` | Component interface |
| `SimpleCoffee.java` | Concrete component |
| `CoffeeDecorator.java` | Abstract decorator — wraps a `Coffee` |
| `MilkDecorator.java`, `SugarDecorator.java`, `VanillaDecorator.java` | Concrete decorators |

**Key implementation detail:**
```java
// Decorators wrap the component and delegate, then add their own behavior
public class MilkDecorator extends CoffeeDecorator {
    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Milk";
    }
    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + 0.25;
    }
}
```

**Demo output:**
```
Simple Coffee -> $1.00
Simple Coffee, Milk -> $1.25
Simple Coffee, Milk, Sugar -> $1.35
Simple Coffee, Milk, Sugar, Vanilla -> $1.85
```

---

### 10. Facade

**Intent:** Provide a simplified interface to a complex subsystem.

**Analogy:** A home theater system with a DVD player, projector, sound system, and lights. Instead of operating each device separately, a `HomeTheaterFacade` provides `watchMovie()` and `endMovie()`.

**When to use:**
- Providing a simple interface to a complex library or framework
- Layered architecture (service layer as facade over repositories and external APIs)
- Reducing dependencies between client and subsystem

**Package:** `structural/facade`

| File | Role |
|------|------|
| `DVDPlayer.java`, `Projector.java`, `SoundSystem.java`, `Lights.java` | Complex subsystem components |
| `HomeTheaterFacade.java` | Facade — orchestrates all subsystems |

**Key implementation detail:**
```java
public void watchMovie(String movie) {
    lights.dim(10);
    projector.on();
    projector.setWideScreenMode();
    sound.on();
    sound.setSurroundSound();
    sound.setVolume(8);
    dvd.on();
    dvd.play(movie);
}
```

**Demo output:**
```
--- Getting ready to watch: Inception ---
Lights: Dimmed to 10%
Projector: ON
...
DVD Player: Playing 'Inception'
```

---

### 11. Flyweight

**Intent:** Use sharing to efficiently support a large number of **fine-grained objects**. Separates shared intrinsic state from unique extrinsic state.

**Analogy:** Rendering a forest with thousands of trees — the species/texture (`TreeType`) is shared; position (`x, y`) is per-tree.

**When to use:**
- Large numbers of similar objects consuming too much memory
- Game engines (particles, tiles, characters)
- Font glyph rendering, string interning

**Package:** `structural/flyweight`

| File | Role |
|------|------|
| `TreeType.java` | Flyweight — shared intrinsic state (name, color, texture) |
| `Tree.java` | Context — holds extrinsic state (x, y) + reference to flyweight |
| `TreeFactory.java` | Flyweight factory with cache |
| `Forest.java` | Client |

**Key implementation detail:**
```java
// Only 2 TreeType objects created for 8 trees
public static TreeType getTreeType(String name, String color, String texture) {
    String key = name + "-" + color + "-" + texture;
    return cache.computeIfAbsent(key, k -> new TreeType(name, color, texture));
}
```

**Demo output:**
```
TreeType created: Oak
TreeType created: Pine
...
Unique TreeType objects created: 2 (for 8 trees)
```

---

### 12. Proxy

**Intent:** Provide a surrogate or placeholder for another object to control access to it.

**Analogy:** Lazy-loading images — the `ProxyImage` holds the filename and only loads the actual image from disk when `display()` is first called.

**When to use:**
- Virtual proxy: defer expensive initialization (lazy loading)
- Protection proxy: control access based on permissions
- Remote proxy: hide remote call complexity
- Caching proxy: cache results of expensive operations

**Package:** `structural/proxy`

| File | Role |
|------|------|
| `Image.java` | Subject interface |
| `RealImage.java` | Real subject — expensive to create |
| `ProxyImage.java` | Proxy — controls access, lazy-loads `RealImage` |

**Key implementation detail:**
```java
@Override
public void display() {
    if (realImage == null) {
        realImage = new RealImage(fileName); // loaded only on first call
    }
    realImage.display();
}
```

**Demo output:**
```
Image object created, but not yet loaded.
First display call:
RealImage: Loading 'photo.jpg' from disk...
RealImage: Displaying 'photo.jpg'
Second display call (no disk load):
RealImage: Displaying 'photo.jpg'
```

---

## Behavioral Patterns

Behavioral patterns deal with **communication between objects**, defining how objects interact and distribute responsibility.

---

### 13. Chain of Responsibility

**Intent:** Pass requests along a chain of handlers. Each handler either handles the request or passes it to the next handler.

**Analogy:** Customer support tiers — a ticket goes to L1; if unresolved, escalates to L2, then L3.

**When to use:**
- More than one object may handle a request
- Request handlers are unknown a priori
- Middleware pipelines, event handling, logging filters

**Package:** `behavioral/chainofresponsibility`

| File | Role |
|------|------|
| `SupportHandler.java` | Abstract handler with `setNext()` and `escalate()` |
| `SupportTicket.java` | Request object |
| `Level1Support.java`, `Level2Support.java`, `Level3Support.java` | Concrete handlers |

**Key implementation detail:**
```java
// Fluent chaining
Level1Support l1 = new Level1Support();
l1.setNext(new Level2Support()).setNext(new Level3Support());
l1.handle(ticket);
```

**Demo output:**
```
L1 Support resolved ticket #1: Password reset
L1 escalating ticket #2...
L2 Support resolved ticket #2: Account billing issue
L1 escalating ticket #3...
L2 escalating ticket #3...
L3 Engineering resolved ticket #3: Data corruption in prod
```

---

### 14. Command

**Intent:** Encapsulate a request as an object, enabling parameterization, queuing, logging, and undo/redo.

**Analogy:** A smart home remote control — pressing a button executes a `Command`; pressing undo reverses the last command.

**When to use:**
- Undo/redo functionality
- Transaction queuing and scheduling
- Macro recording
- GUI button actions

**Package:** `behavioral/command`

| File | Role |
|------|------|
| `Command.java` | Command interface with `execute()` and `undo()` |
| `Light.java` | Receiver |
| `LightOnCommand.java`, `LightOffCommand.java` | Concrete commands |
| `RemoteControl.java` | Invoker — maintains command history |

**Key implementation detail:**
```java
// Invoker stores history for undo
public void press(Command command) {
    command.execute();
    history.push(command);
}

public void undoLast() {
    if (!history.isEmpty()) {
        history.pop().undo();
    }
}
```

**Demo output:**
```
Living Room light: ON
Bedroom light: ON
Living Room light: OFF
-- Undo --
Undo -> Living Room light: ON
Undo -> Bedroom light: OFF
```

---

### 15. Iterator

**Intent:** Provide a way to **sequentially access** elements of a collection without exposing its internal representation.

**Analogy:** A library catalog that can be traversed forward or in reverse, regardless of how books are stored internally.

**When to use:**
- Uniform traversal over different collection types
- Multiple simultaneous traversals needed
- Supporting different traversal strategies (forward, reverse, filtered)

**Package:** `behavioral/iterator`

| File | Role |
|------|------|
| `Book.java` | Element type |
| `BookCollection.java` | Aggregate — implements `Iterable<Book>`, provides forward and reverse iterators |

**Key implementation detail:**
```java
// Standard iterator — works with for-each
for (Book book : library) { ... }

// Custom reverse iterator
var iter = library.reverseIterator();
while (iter.hasNext()) { System.out.println(iter.next()); }
```

---

### 16. Mediator

**Intent:** Define an object that encapsulates how a set of objects interact. Reduces direct references between objects, promoting loose coupling.

**Analogy:** A chat room — users don't send messages directly to each other; they send through the `ChatRoom` mediator, which dispatches to all other participants.

**When to use:**
- Many-to-many interactions between objects lead to tight coupling
- Air traffic control systems
- UI forms where fields interact

**Package:** `behavioral/mediator`

| File | Role |
|------|------|
| `ChatMediator.java` | Mediator interface |
| `ChatRoom.java` | Concrete mediator |
| `ChatUser.java` | Abstract colleague |
| `ConcreteUser.java` | Concrete colleague |

**Key implementation detail:**
```java
// ChatRoom dispatches to everyone except the sender
@Override
public void sendMessage(String message, ChatUser sender) {
    users.stream()
         .filter(u -> u != sender)
         .forEach(u -> u.receive(message, sender.getName()));
}
```

**Demo output:**
```
[Alice] sends: Hello everyone!
[Bob] received from [Alice]: Hello everyone!
[Carol] received from [Alice]: Hello everyone!
[Bob] sends: Hi Alice!
[Alice] received from [Bob]: Hi Alice!
[Carol] received from [Bob]: Hi Alice!
```

---

### 17. Memento

**Intent:** Capture and externalize an object's internal state so it can be restored later, **without violating encapsulation**.

**Analogy:** Text editor undo — each save captures a `EditorMemento`; undo pops the stack and restores the previous state.

**When to use:**
- Undo/redo in editors
- Transaction rollback
- Game save states
- Snapshot-based debugging

**Package:** `behavioral/memento`

| File | Role |
|------|------|
| `EditorMemento.java` | Memento — stores `content` and `cursorPosition` |
| `TextEditor.java` | Originator — creates and restores mementos |
| `EditorHistory.java` | Caretaker — manages the undo stack |

**Key implementation detail:**
```java
// Originator creates and restores from memento without exposing internals
public EditorMemento save() {
    return new EditorMemento(content, cursorPosition);
}

public void restore(EditorMemento memento) {
    this.content = memento.getContent();
    this.cursorPosition = memento.getCursorPosition();
}
```

**Demo output:**
```
After 'Hello': Editor{content='Hello', cursor=5}
After ', World': Editor{content='Hello, World', cursor=12}
After '!!!': Editor{content='Hello, World!!!', cursor=15}
Undo -> Editor{content='Hello, World', cursor=12}
Undo -> Editor{content='Hello', cursor=5}
```

---

### 18. Observer

**Intent:** Define a one-to-many dependency so that when one object changes state, all its dependents are notified and updated automatically.

**Analogy:** Stock market — traders subscribe to a stock's price feed; when the price changes, all subscribed traders are notified.

**When to use:**
- Event-driven architectures
- MVC — model notifies views of changes
- Message brokers, reactive programming
- Spring `ApplicationEvent` system

**Package:** `behavioral/observer`

| File | Role |
|------|------|
| `StockObserver.java` | Observer interface |
| `StockMarket.java` | Subject — maintains subscriber list, publishes changes |
| `StockTrader.java` | Concrete observer |

**Key implementation detail:**
```java
public void setPrice(double newPrice) {
    double change = newPrice - price;
    this.price = newPrice;
    observers.forEach(o -> o.onPriceChanged(symbol, price, change));
}
```

**Demo output:**
```
[Alice] AAPL price: $183.50 (+3.50)
[Bob] AAPL price: $183.50 (+3.50)
  *** ALERT: significant move of 3.50!
[Alice] AAPL price: $175.00 (-8.50)
  *** ALERT: significant move of -8.50!
-- Bob unsubscribes --
[Alice] AAPL price: $190.00 (+15.00)
  *** ALERT: significant move of 15.00!
```

---

### 19. State

**Intent:** Allow an object to alter its behavior when its internal state changes. The object will appear to change its class.

**Analogy:** A traffic light — its behavior (what to show next) depends on its current color (state).

**When to use:**
- Object behavior depends on its state and must change at runtime
- Large conditional statements based on object state
- Order lifecycle, vending machine states, TCP connection states

**Package:** `behavioral/state`

| File | Role |
|------|------|
| `TrafficLightState.java` | State interface |
| `RedState.java`, `GreenState.java`, `YellowState.java` | Concrete states |
| `TrafficLight.java` | Context — delegates behavior to current state |

**Key implementation detail:**
```java
// Context delegates all behavior to its current state
public void change() {
    state.handle(this); // state transitions itself
}

// Each state knows its successor
public class GreenState implements TrafficLightState {
    public void handle(TrafficLight light) {
        System.out.println("GREEN: Go! Switching to YELLOW.");
        light.setState(new YellowState());
    }
}
```

**Demo output:**
```
Initial: RED
RED: Stop! Switching to GREEN.
GREEN: Go! Switching to YELLOW.
YELLOW: Caution! Switching to RED.
...
```

---

### 20. Strategy

**Intent:** Define a family of algorithms, encapsulate each one, and make them interchangeable. Lets the algorithm vary independently from the clients that use it.

**Analogy:** Choosing a sorting algorithm at runtime — `BubbleSort` for small arrays, `QuickSort` for general use, `MergeSort` for stable sorts.

**When to use:**
- Multiple variants of an algorithm
- Replacing conditionals that select behavior
- Payment processing strategies, compression algorithms, validation rules

**Package:** `behavioral/strategy`

| File | Role |
|------|------|
| `SortStrategy.java` | Strategy interface |
| `BubbleSortStrategy.java` | Concrete strategy |
| `QuickSortStrategy.java` | Concrete strategy |
| `MergeSortStrategy.java` | Concrete strategy |
| `Sorter.java` | Context — uses a `SortStrategy` |

**Key implementation detail:**
```java
// Strategy can be swapped at runtime
Sorter sorter = new Sorter(new BubbleSortStrategy());
sorter.sort(data);

sorter.setStrategy(new QuickSortStrategy());
sorter.sort(data);
```

**Demo output:**
```
Using: BubbleSort
BubbleSort result: [1, 2, 3, 5, 8, 9]
Using: QuickSort
QuickSort result: [1, 2, 3, 5, 8, 9]
Using: MergeSort
MergeSort result: [1, 2, 3, 5, 8, 9]
```

---

### 21. Template Method

**Intent:** Define the **skeleton of an algorithm** in a base class, deferring some steps to subclasses. Subclasses redefine certain steps without changing the algorithm's structure.

**Analogy:** A data mining pipeline — extract, parse, analyze, report. The structure is always the same; what changes per format (CSV vs PDF) are the `extractData` and `parseData` steps.

**When to use:**
- Multiple classes share the same algorithm structure but differ in certain steps
- Preventing code duplication in related classes
- Frameworks that define invariant parts and let users hook into specific steps

**Package:** `behavioral/templatemethod`

| File | Role |
|------|------|
| `DataMiner.java` | Abstract class with `mine()` template method |
| `CsvDataMiner.java` | Overrides `extractData` and `parseData` for CSV |
| `PdfDataMiner.java` | Overrides `extractData`, `parseData`, and `sendReport` for PDF |

**Key implementation detail:**
```java
// Template method — final prevents subclasses from altering the algorithm structure
public final void mine(String path) {
    String rawData = extractData(path);   // abstract — must override
    String parsed  = parseData(rawData);  // abstract — must override
    String analysis = analyzeData(parsed); // has default, can override
    sendReport(analysis);                  // has default, can override
}
```

**Demo output:**
```
CSV: Reading file data.csv
CSV: Parsing comma-separated data
Report: Analysis of [parsed-csv-rows]: found 42 patterns

PDF: Extracting text from report.pdf
PDF: Parsing PDF structure
PDF Report (emailed): Analysis of [parsed-pdf-text]: found 42 patterns
```

---

### 22. Visitor

**Intent:** Represent an operation to be performed on elements of an object structure. Visitor lets you define a new operation **without changing** the classes of the elements on which it operates.

**Analogy:** Computing area and perimeter for shapes — instead of adding `computeArea()` and `computePerimeter()` to each shape class, visitors encapsulate these operations externally.

**When to use:**
- Many distinct and unrelated operations need to be performed on an object structure
- The object structure rarely changes but you often need to add new operations
- AST traversal in compilers, tax calculation systems, document export

**Package:** `behavioral/visitor`

| File | Role |
|------|------|
| `ShapeVisitor.java` | Visitor interface |
| `VisitableShape.java` | Element interface with `accept(ShapeVisitor)` |
| `Circle.java`, `Rectangle.java`, `Triangle.java` | Concrete elements |
| `AreaCalculatorVisitor.java` | Concrete visitor |
| `PerimeterCalculatorVisitor.java` | Concrete visitor |

**Key implementation detail:**
```java
// Double dispatch — shape calls back the visitor with its own type
public class Circle implements VisitableShape {
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this); // dispatches to visit(Circle)
    }
}
```

**Demo output:**
```
-- Area --
Circle area (r=5.0): 78.54
Rectangle area (4.0 x 6.0): 24.00
Triangle area (base=3.0, h=4.0): 6.00
-- Perimeter --
Circle perimeter (r=5.0): 31.42
Rectangle perimeter: 20.00
Triangle perimeter: 12.00
```

---

### 23. Interpreter

**Intent:** Given a language, define a representation for its grammar and provide an interpreter that uses the representation to interpret sentences in that language.

**Analogy:** A boolean expression evaluator for access control rules — `(ADMIN OR MANAGER) AND ACTIVE`.

**When to use:**
- Simple domain-specific languages (DSLs)
- Query languages, filter expressions, rule engines
- SQL parsers, regex engines (conceptually)

**Package:** `behavioral/interpreter`

| File | Role |
|------|------|
| `Expression.java` | Abstract expression |
| `TerminalExpression.java` | Leaf — checks if context contains a token |
| `OrExpression.java` | Non-terminal — evaluates `left OR right` |
| `AndExpression.java` | Non-terminal — evaluates `left AND right` |
| `InterpreterDemo.java` | Builds and evaluates the expression tree |

**Key implementation detail:**
```java
// Composing an expression tree
Expression isAdmin   = new TerminalExpression("ADMIN");
Expression isManager = new TerminalExpression("MANAGER");
Expression isActive  = new TerminalExpression("ACTIVE");

Expression hasRole   = new OrExpression(isAdmin, isManager);
Expression canAccess = new AndExpression(hasRole, isActive);

canAccess.interpret("ADMIN ACTIVE");   // true
canAccess.interpret("USER ACTIVE");    // false
canAccess.interpret("ADMIN SUSPENDED"); // false
```

**Demo output:**
```
Context: ADMIN ACTIVE          -> Access: true
Context: MANAGER ACTIVE        -> Access: true
Context: USER ACTIVE           -> Access: false
Context: ADMIN SUSPENDED       -> Access: false
```

---

## Pattern Summary Table

| # | Pattern | Category | Key Intent |
|---|---------|----------|-----------|
| 1 | Singleton | Creational | One instance globally |
| 2 | Factory Method | Creational | Delegate instantiation to factory |
| 3 | Abstract Factory | Creational | Create families of related objects |
| 4 | Builder | Creational | Step-by-step complex object construction |
| 5 | Prototype | Creational | Clone existing objects |
| 6 | Adapter | Structural | Make incompatible interfaces compatible |
| 7 | Bridge | Structural | Decouple abstraction from implementation |
| 8 | Composite | Structural | Uniform tree structures |
| 9 | Decorator | Structural | Dynamically add behavior |
| 10 | Facade | Structural | Simplify complex subsystem |
| 11 | Flyweight | Structural | Share fine-grained objects |
| 12 | Proxy | Structural | Control object access |
| 13 | Chain of Responsibility | Behavioral | Pass request along handler chain |
| 14 | Command | Behavioral | Encapsulate requests as objects |
| 15 | Iterator | Behavioral | Sequential access to collection |
| 16 | Mediator | Behavioral | Centralize object communication |
| 17 | Memento | Behavioral | Capture and restore object state |
| 18 | Observer | Behavioral | Notify dependents of state changes |
| 19 | State | Behavioral | Change behavior with state |
| 20 | Strategy | Behavioral | Swap algorithms at runtime |
| 21 | Template Method | Behavioral | Define algorithm skeleton |
| 22 | Visitor | Behavioral | Add operations without modifying classes |
| 23 | Interpreter | Behavioral | Evaluate grammar/expressions |

---

## Running the Examples

Each pattern class has a static `demo()` method. You can invoke them from `GangOfFourApplication`:

```java
// Example: run specific demos
DatabaseConnection.demo();
NotificationFactory.demo();
Pizza.demo();
// etc.
```

Or run the Spring Boot app:
```bash
./mvnw spring-boot:run
```

Compile and run tests:
```bash
./mvnw test
```
