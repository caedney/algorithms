# Q & A

### 🙋‍♂️ Why bother with data abstraction?

It helps us produce reliable and correct code. For example, in the 2000 presidential election, Al Gore received -16,022 votes on an electronic voting machine in Volusia County, Florida—the tally was clearly not properly encapsulated in the voting machine software!

### 🙋‍♂️ Why the distinction between primitive and reference types? Why not just have reference types?

Performance. Java provides the reference types `Integer`, `Double`, and so forth that correspond to primitive types that can be used by programmers who prefer to ignore the distinction. Primitive types are closer to the types of data that are supported by computer hardware, so programs that use them usually run faster than programs that use corresponding reference types.

### 🙋‍♂️ Do data types _have_ to be abstract?

No. Java also allows `public` and `protected` to allow some clients to refer directly to instance variables. As described in the text, the advantages of allowing client code to directly refer to data are greatly outweighed by the disadvantages of dependence on a particular representation, so all instance variables are `private` in our code. We also occasionally use `private` instance methods to share code among public methods.

### 🙋‍♂️ What happens if I forget to use `new` when creating an object?

To Java, it looks as though you want to call a static method with a return value of the object type. Since you have not defined such a method, the error message is the same as anytime you refer to an undefined symbol. If you compile the code

```java
Counter c = Counter("test");
```

you get this error message:

```
cannot find symbol
symbol  : method Counter(String)
```

You get the same kind of error message if you provide the wrong number of arguments
to a constructor.

### 🙋‍♂️ What happens if I forget to use `new` when creating an array of objects?

You need to use `new` for each object that you create, so when you create an array of 𝑁 objects, you need to use `new` 𝑁+1 times: once for the array and once for each of the objects. If you forget to create the array:

```java
Counter[] a;
a[0] = new Counter("test");
```

you get the same error message that you would get when trying to assign a value to any uninitialized variable:

```
variable a might not have been initialized
    a[0] = new Counter("test");
```

but if you forget to use `new` when creating an object within the array and then try to use it to invoke a method:

```java
Counter[] a = new Counter[2];
a[0].increment();
```

you get a `NullPointerException`.

### 🙋‍♂️ Why not write `StdOut.println(x.toString())` to print objects?

That code works fine, but Java saves us the trouble of writing it by automatically invoking the `toString()` method for any object, since `println()` has a method that takes an `Object` as argument.

### 🙋‍♂️ What is a _pointer_?

Good question. Perhaps that should be `NullReferenceException`. Like a Java reference, you can think of a _pointer_ as a machine address. In many programming languages, the pointer is a primitive data type that programmers can manipulate in many ways. But programming with pointers is notoriously error-prone, so operations provided for pointers need to be carefully designed to help programmers avoid errors.
Java takes this point of view to an extreme (that is favored by many modern programming-language designers). In Java, there is only _one_ way to create a reference (`new`) and only _one_ way to change a reference (with an assignment statement). That is, the only things that a programmer can do with references are to create them and copy them. In programming-language jargon, Java references are known as _safe pointers_, because Java can guarantee that each reference points to an object of the specified type (and it can determine which objects are not in use, for garbage collection). Programmers used to writing code that directly manipulates pointers think of Java as having no pointers at all, but people still debate whether it is really desirable to have unsafe pointers.

### 🙋‍♂️ Where can I find more details on how Java implements references and does garbage collection?

One Java system might differ completely from another. For example, one natural scheme is to use a pointer (machine address); another is to use a _handle_ (a pointer to a pointer). The former gives faster access to data; the latter provides for better garbage collection.

### 🙋‍♂️ What exactly does it mean to `import` a name?

Not much: it just saves some typing. You could type `java.util.Arrays` instead of `Arrays` everywhere in your code instead of using the `import` statement.

### 🙋‍♂️ What is the problem with implementation inheritance?

(i.e., using extends to inherit code from a superclass)

Subtyping makes modular programming more difficult for two reasons:

1. Any change in the superclass affects all subclasses. The subclass cannot be developed _independently_ of the superclass; indeed, it is _completely dependent_ on the superclass. This problem is known as the _fragile base class_ problem.
2. The subclass code, having access to instance variables, can subvert the intention of the superclass code. For example, the designer of a class like `Counter` for a voting system may take great care to make it so that `Counter` can only increment the tally by one (remember Al Gore's problem). But a subclass, with full access to the instance variable, can change it to any value whatever.

### 🙋‍♂️ How do I make a class immutable?

To ensure immutability of a data type that includes an instance variable of a mutable type, we need to make a local copy, known as a _defensive copy_. And that may not be enough. Making the copy is one challenge; ensuring that none of the instance methods change values is another.

### 🙋‍♂️ What is `null`?

It is a literal value that refers to no object. Invoking a method using the `null` reference is meaningless and results in a `NullPointerException`. If you get this error message, check to make sure that your constructor properly initializes all of its instance variables.

### 🙋‍♂️ Can I have a static method in a class that implements a data type?

Of course. For example, all of our classes have `main()`. Also, it is natural to consider adding static methods for operations that involve multiple objects where none of them naturally suggests itself as the one that should invoke the method. For example, we might define a static method like the following within `Point`:

```java
public static double distance(Point a, Point b) {
    return a.distTo(b);
}
```

Often, including such methods can serve to clarify client code.

### 🙋‍♂️ Are there other kinds of variables besides parameter, local, and instance variables?

If you include the keyword `static` in a class declaration (outside of any type) it creates a completely different type of variable, known as a _static variable_. Like instance variables, static variables are accessible to every method in the class; however, they are not associated with any object. In older programming languages, such variables are known as _global variables_, because of their global scope. In modern programming, we focus on limiting scope and therefore rarely use such variables. When we do, we will call attention to them.

### 🙋‍♂️ What is a _deprecated_ method?

A method that is no longer fully supported, but kept in an API to maintain compatibility. For example, Java once included a method `Character.isSpace()`, and programmers wrote programs that relied on using that method's behavior. When the designers of Java later wanted to support additional Unicode whitespace characters, they could not change the behavior of `isSpace()` without breaking client programs, so, instead, they added a new method, `Character.isWhiteSpace()`, and deprecated the old method. As time wears on, this practice certainly complicates APIs. Sometimes, entire classes are deprecated. For example, Java deprecated its `java.util.Date` in order to better support internationalization.
