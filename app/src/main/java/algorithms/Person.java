package algorithms;

/**
 * Instance Methods
 *
 * - <b>Belong to an object instance</b>, not the class itself.
 * - Can access <b>instance fields</b> (<code>this.field</code>) and <b>other
 * instance methods</b>.
 * - Invoked on an object of the class.
 * 
 * Person p = new Person();
 * p.setName("Alice");
 * System.out.println(p.getName());
 */

public class Person {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
