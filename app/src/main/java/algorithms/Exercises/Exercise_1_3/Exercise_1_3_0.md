# Q & A

### 🙋‍♂️ Not all programming languages have generics, even early versions of Java. What are the alternatives?

One alternative is to maintain a different implementation for each type of data, as mentioned in the text. Another is to build a stack of `Object` values, then cast to the desired type in client code for `pop()`. The problem with this approach is that type mismatch errors cannot be detected until run time. But with generics, if you write code to push an object of the wrong type on the stack, like this:

```java
Stack<Apple> stack = new Stack<Apple>;
Apple a = new Apple;
...
Orange b = new Orange;
...
stack.push(a);
...
stack.push(b); // compile-time error
```

you will get a compile-time error:

```
push (Apple) in Stack<Apple> cannot be applied to (Orange)
```

This ability to discover such errors at compile time is reason enough to use generics.

### 🙋‍♂️ Why does Java disallow generic arrays?

Experts still debate this point. You might need to become one to understand it! For starters, learn about _covariant arrays_ and _type erasure_.

### 🙋‍♂️ How do I create an array of stacks of strings?

Use a cast, such as the following:

```java
Stack<String>[] a = (Stack<String>[]) new Stack[N];
```

_Warning_: This cast, in client code, is different from the one described on page 134. You might have expected to use `Object` instead of `Stack`. When using generics, Java checks for type safety at compile time, but throws away that information at run time, so it is left with `Stack<Object>[]` or just `Stack[]`, for short, which we must cast to `Stack<String>[]`.

### 🙋‍♂️ What happens if my program calls `pop()` for an empty stack?

It depends on the implementation. For our implementation on page 149, you will get a `NullPointerException`. In our implementations on the booksite, we throw a runtime exception to help users pinpoint the error. Generally, including as many such checks as possible is wise in code that is likely to be used by many people.

### 🙋‍♂️ Why do we care about resizing arrays, when we have linked lists?

We will see several examples of ADT implementations that need to use arrays to perform other operations that are not easily supported with linked lists. `ResizingArrayStack` is a model for keeping their memory usage under control.

### 🙋‍♂️ Why declare `Node` as a nested class? Why `private`?

By declaring the nested class `Node` to be `private`, we restrict access to methods and instance variables within the enclosing class. One characteristic of a `private` nested class is that its instance variables can be directly accessed from within the enclosing class but nowhere else, so there is no need to declare the instance variables `public` or `private`. _Note for experts_: A nested class that is not static is known as an _inner_ class, so technically our Node classes are inner classes, though the ones that are not generic could be static.

### 🙋‍♂️ When I type `javac Stack.java` to run ALGORITHM 1.2 and similar programs, I find `Stack.class` _and_ a file `Stack$Node.class`. What is the purpose of that second one?

That file is for the inner class `Node`. Java's naming convention is to use `$` to separate the name of the outer class from the inner class.

### 🙋‍♂️ Are there Java libraries for stacks and queues?

Yes and no. Java has a built-in library called `java.util.Stack`, but you should avoid using it when you want a stack. It has several additional operations that are not normally associated with a stack, e.g., getting the `ith` element. It also allows adding an element to the bottom of the stack (instead of the top), so it can implement a queue! Although having such extra operations may appear to be a bonus, it is actually a curse.
We use data types not just as libraries of all the operations we can imagine, but also as a mechanism to precisely specify the operations we need. The prime benefit of doing so is that the system can prevent us from performing operations that we do not actually want. The `java.util.Stack` API is an example of a _wide interface_, which we generally strive to avoid.

### 🙋‍♂️ Should a client be allowed to insert `null` items onto a stack or queue?

This question arises frequently when implementing collections in Java. Our implementation (and Java's stack and queue libraries) do permit the insertion of null values.

### 🙋‍♂️ What should the `Stack` iterator do if the client calls `push()` or `pop()` during iterator?

Throw a `java.util.ConcurrentModificationException` to make it a _fail-fast iterator_. See 1.3.50.

### 🙋‍♂️ Can I use a _foreach_ loop with arrays?

Yes (even though arrays do not implement the `Iterable` interface). The following one-liner prints out the command-line arguments:

```java
public static void main (String[] args){
    for (Strings : args) StdOut.println(s);
}
```

### 🙋‍♂️ Can I use a _foreach_ loop with strings?

No. `String` does not implement `Iterable`.

### 🙋‍♂️ Why not have a single `Collection` data type that implements methods to add items, remove the most recently inserted, remove the least recently inserted, remove random, iterate, return the number of items in the collection, and whatever other operations we might desire? Then we could get them all implemented in a single class that could be used by many clients.

Again, this is an example of a _wide interface_. Java has such implementations in its `java.util.ArrayList` and `java.util.LinkedList` classes. One reason to avoid them is that it there is no assurance that all operations are implemented efficiently. Through-out this book, we use APIs as starting points for designing efficient algorithms and data structures, which is certainly easier to do for interfaces with just a few operations as opposed to an interface with many operations. Another reason to insist on narrow interfaces is that they enforce a certain discipline on client programs, which makes client code much easier to understand. If one client uses `Stack<String>` and another uses `Queue<Transaction>`, we have a good idea that the LIFO discipline is important to the first and the FIFO discipline is important to the second.
