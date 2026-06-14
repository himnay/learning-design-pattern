package com.org.pattern.gangoffour.behavioral.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Iterator — provides a way to sequentially access elements without exposing the underlying collection.
 *
 * Real-world analogy: Iterating over a library's book catalog with different traversal strategies.
 */
public class BookCollection implements Iterable<Book> {

    private final List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public java.util.Iterator<Book> iterator() {
        return books.iterator();
    }

    public java.util.Iterator<Book> reverseIterator() {
        List<Book> reversed = new ArrayList<>(books);
        java.util.Collections.reverse(reversed);
        return reversed.iterator();
    }

    public static void demo() {
        System.out.println("=== Iterator Pattern Demo ===");
        BookCollection library = new BookCollection();
        library.addBook(new Book("Design Patterns", "GoF", 1994));
        library.addBook(new Book("Clean Code", "Robert Martin", 2008));
        library.addBook(new Book("Refactoring", "Martin Fowler", 1999));

        System.out.println("Forward:");
        for (Book book : library) {
            System.out.println("  " + book);
        }

        System.out.println("Reverse:");
        var iter = library.reverseIterator();
        while (iter.hasNext()) {
            System.out.println("  " + iter.next());
        }
    }
}
