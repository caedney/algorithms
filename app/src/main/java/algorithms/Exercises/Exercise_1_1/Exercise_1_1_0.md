# Q & A

### 🙋‍♂️ What is Java bytecode?

A low-level version of your program that runs on the Java _virtual machine_. This level of abstraction makes it easier for the developers of Java to ensure that our programs run on a broad variety of devices.

Java **bytecode** is the **intermediate, platform-independent representation** of a Java program that the **Java compiler** (`javac`) produces after compiling your `.java` source code.

Here’s how it works:

1. **Source code** → You write a Java program in a .java file.

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }
}
```

2. **Compilation** → The Java compiler (`javac`) translates the `.java` file into a `.class` file containing bytecode.
3. **Bytecode** → This is a set of **low-level instructions** for the **Java Virtual Machine (JVM)**, not for a specific operating system or hardware.

- It’s not human-readable like Java source code.
- It’s more abstract than native machine code, which makes it portable.

4. **Execution** → The **JVM interprets or Just-In-Time (JIT) compiles** the bytecode into native machine instructions that the host system’s CPU can execute.

### 🙋‍♂️ It seems wrong that Java should just let `int`s overflow and give bad values. Shouldn't Java automatically check for overflow?

This issue is a contentious one among programmers. The short answer is that the lack of such checking is one reason such types are called _primitive_ data types. A little knowledge can go a long way in avoiding such problems. We use the `int` type for small numbers (less than ten decimal digits), and the `long` type when values run into the billions or more.

In Java (and in most mainstream languages like C, C++, and C# by default), primitive integer types (`int`, `long`, etc.) have **fixed bit-widths**. For example, a Java `int` is always 32 bits, signed. Because of this:

- Arithmetic is performed **modulo 2³²** for int (and **modulo 2⁶⁴** for long).
- If a result doesn’t fit in the available bits, the excess is silently discarded (wrapping around), which is what we call **overflow**.

So, for example:

```java
int x = Integer.MAX_VALUE;
int y = x + 1; // wraps around to Integer.MIN_VALUE
```

#### Why Java doesn't automatically check

1. **Performance**: Overflow checks on every integer operation would make programs slower, especially since arithmetic is so common.
2. **Simplicity**: Java aims to give consistent, predictable behavior across all platforms. Wrapping arithmetic matches the underlying CPU behavior, so the compiler can just emit native instructions.
3. **Historical precedent**: Languages like C established this convention decades ago, and Java inherited it to avoid surprising developers who expected wraparound.

### 🙋‍♂️ What is the value of `Math.abs(-2147483648)`?

`-2147483648`. This strange (but true) result is a typical example of the effects of integer overflow.

In Java,`Math.abs(int a)` returns the absolute value of an integer.
But there’s a **corner case** when dealing with `Integer.MIN_VALUE`:

- `Integer.MIN_VALUE = -2147483648`
- `Integer.MAX_VALUE = 2147483647`

Since the absolute value of `-2147483648` would be `2147483648`, which is _one more than_ `Integer.MAX_VALUE`, it cannot be represented in a 32-bit signed `int`.

👉 Therefore, `Math.abs(-2147483648)` returns `-2147483648`, not a positive number.

This is a well-known quirk due to integer overflow in two’s complement arithmetic.

### 🙋‍♂️ How can I initialize a `double` variable to infinity?

Java has built-in constants available for this purpose: `Double.POSITIVE_INFINITY` and `Double.NEGATIVE_INFINITY`.

### 🙋‍♂️ Can you compare a `double` to an `int`?

Not without doing a type conversion, but remember that Java usually does the requisite type conversion automatically. For example, if `x` is an `int` with the value 3, then the expression (`x < 3.1`) is `true` — Java converts `x` to `double` (because `3.1` is a `double` literal) before performing the comparison.

### 🙋‍♂️ What happens if I use a variable before initializing it to a value?

Java will report a compile-time error if there is any path through your code that would lead to the use of an uninitialized variable.

### 🙋‍♂️ What are the values of `1/0` and `1.0/0.0` as Java expressions?

The first generates a runtime _exception_ for division by zero (which stops your program because the value is undefined); the second has the value `Infinity`.

In Java, the results of division by zero depend on whether you are working with **integers** or **floating-point numbers**:

1. `1 / 0`

- Here, both operands are integers (`int`).
- Integer division by zero is **not defined**.
- At **compile time**, Java does not catch this (unless the compiler can determine the denominator is literally `0`).
- At **runtime**, it throws an exception:

```
Exception in thread "main" java.lang.ArithmeticException: / by zero
```

So `1 / 0` → `ArithmeticException`.

2. `1.0 / 0.0`

- Here, both operands are floating-point (`double`).
- IEEE-754 floating-point rules apply.
- Division of a positive finite number by `+0.0` yields **positive infinity** (`Infinity`).
- Division of a negative finite number by `+0.0` yields **negative infinity** (`-Infinity`).
- Division of `0.0 / 0.0` yields `NaN` (not a number)

So `1.0 / 0.0` → `Infinity` (a `double` constant).

✅ Summary:

- `1 / 0` → throws `ArithmeticException`
- `1.0 / 0.0` → evaluates to `Infinity`

### 🙋‍♂️ Can you use `<` and `>` to compare `String` variables?

No. Those operators are defined only for primitive types. See page 80.

### 🙋‍♂️ What is the result of division and remainder for negative integers?

The quotient `a/b` rounds toward `0`; the remainder `a % b` is defined such that `(a / b) * b + a % b` is always equal to `a`. For example, `-14/3` and `14/-3` are both `-4`, but `-14 % 3` is `-2` and `14 % -3` is `2`.

1. Division (/)

- Java integer division **truncates toward zero**.
- That means the fractional part is discarded, not rounded down like in some other languages.

```java
System.out.println( 7 /  3); //  2
System.out.println(-7 /  3); // -2  (truncated toward 0, not -3)
System.out.println( 7 / -3); // -2
System.out.println(-7 / -3); //  2
```

2. Remainder (%)

- The remainder in Java always has the same sign as the dividend (the left operand).
- Formula: `a == (a / b) * b + (a % b)` will always hold true.

```java
System.out.println( 7 %  3); //  1
System.out.println(-7 %  3); // -1 (same sign as -7)
System.out.println( 7 % -3); //  1 (same sign as 7)
System.out.println(-7 % -3); // -1 (same sign as -7)
```

### 🙋‍♂️ Why do we say (`a && b`) and not (`a & b`)?

The operators `&`, `|`, and `^` are _bitwise_ logical operations for integer types that do _and_, _or_, and _exclusive or_ (respectively) on each bit position. Thus the value of `10&6` is `14` and the value of `10^6` is `12`. We use these operators rarely (but occasionally) in this book.
The operators `&&` and `||` are valid only in boolean expressions and are included separately because of _short-circuiting_: an expression is evaluated left-to-right and the evaluation stops when the value is known.

### 🙋‍♂️ Is ambiguity in nested `if` statements a problem?

Yes. In Java, when you write if

```
<expr1› if <expr2> <stmntA> else ‹stmntB>
```

it is equivalent to

```
if <expr1> { if <expr2> <stmntA> else <stmntB> }
```

even if you might have been thinking

```
if <expr1> { if <expr2> <stmntA> } else <stmntB>
```

Using explicit braces is a good way to avoid this _dangling else_ pitfall.

### 🙋‍♂️ What is the difference between a `for` loop and its `while` formulation?

The code in the `for` loop header is considered to be in the same block as the `for` loop body. In a typical `for` loop, the incrementing variable is not available for use in later statements; in the corresponding `while` loop, it is. This distinction is often a reason to use a `while` instead of a `for` loop.

### 🙋‍♂️ Some Java programmers use int `a[]` instead of `int[] a` to declare arrays. What's the difference?

In Java, both are legal and equivalent. The former is how arrays are declared in C. The latter is the preferred style in Java since the type of the variable `int[]` more clearly indicates that it is an _array_ of integers.

### 🙋‍♂️ Why do array indices start at `0` instead of `1`?

This convention originated with machine-language programming, where the address of an array element would be computed by adding the index to the address of the beginning of an array. Starting indices at `1` would entail either a waste of space at the beginning of the array or a waste of time to subtract the `1`.

In low-level languages like C (which influenced many later languages), an array is represented as a block of contiguous memory.

- The name of the array points to the address of the first element.
- To access the i-th element, the runtime calculates:

```
address(𝐴[𝑖]) = base_address + 𝑖 × element_size
```

If indexing started at 1, the formula would require an extra subtraction:

```
address(𝐴[𝑖]) = base_address + (𝑖 − 1) × element_size
```

By starting at 0, the calculation is simpler and faster.

### 🙋‍♂️ If `a[]` is an array, why does `StdOut.println(a)` print out a hexadecimal integer, such as `@f62373`, instead of the elements of the array?

Good question. It is printing out the memory address of the array, which, unfortunately, is rarely what you want.

1. `System.out.println(a)` calls `a.toString()`.
   In Java, when you pass an object to `println`, it implicitly calls that object’s `toString()` method.
2. Arrays don’t override `toString()`.
   Unlike `ArrayList` or `String`, which override `toString()` to give a nice human-readable representation, Java arrays inherit `toString()` from `Object`.
3. `Object.toString()`’s default behavior is:

```
getClass().getName() + "@" + Integer.toHexString(hashCode())
```

- `getClass().getName()` → `"[I"` means "array of ints".
- `@` → separator.
- `Integer.toHexString(hashCode())` → some hash-based hex number (like `f62373`).

This is why you see things like:

```
[I@f62373
```

- `[` = array
- `I` = primitive `int`
- `@f62373` = hash code in hex

### 🙋‍♂️ Why are we not using the standard Java libraries for input and graphics?

We `are` using them, but we prefer to work with simpler abstract models. The Java libraries behind `StdIn` and `StdDraw` are built for production programming, and the libraries and their APIs are a bit unwieldy. To get an idea of what they are like, look at the code in `StdIn.java` and `StdDraw.java`.

### 🙋‍♂️ Can my program reread data from standard input?

No. You only get one shot at it, in the same way that you cannot undo `println()`.

### 🙋‍♂️ What happens if my program attempts to read after standard input is exhausted?

You will get an error. `StdIn.isEmpty()` allows you to avoid such an error by checking whether there is more input available.

### 🙋‍♂️ What does this error message mean?

```
Exception in thread "main" java.lang.NoClassDefFoundError: StdIn
```

You probably forgot to put `StdIn.java` in your working directory.

### 🙋‍♂️ Can a static method take another static method as an argument in Java?

No. Good question, since many other languages do support this capability.

1. Traditional Java (Before Java 8)

In **classic Java** (pre-Java 8), **methods were not first-class citizens**. That means you **cannot pass a method directly** to another method, static or otherwise. There was no syntax for this:

```java
public class Example {
    public static void foo() { }

    public static void bar() {
        // You cannot do something like:
        // anotherMethod(foo);   <- NOT allowed
    }
}
```

So if your source is older or based on Java 7 or earlier, then it is **correct**: Java did not allow passing methods as arguments.

2. Java 8 and Later (With Lambdas and Method References)

Starting from **Java 8**, Java introduced:

- **Functional interfaces** (interfaces with a single abstract method, e.g., `Runnable`, `Callable`, `Function<T,R>`).
- **Lambda expressions** and **method references**.

Now you can **effectively pass a static method as an argument**, using **method references**, as in my previous examples:

```java
public static int square(int x) { return x * x; }
applyFunction(Example::square, 5); // works in Java 8+
```

Here, `Example::square` is a **method reference** matching the signature of a functional interface.
