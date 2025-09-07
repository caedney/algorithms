package algorithms;

import org.junit.jupiter.api.Test;
import java.util.ConcurrentModificationException;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StackFailFastTest {
    @Test
    void testFailFastOnPush() {
        StackFailFast<Integer> stack = new StackFailFast<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        var iterator = stack.iterator();
        iterator.next(); // move once

        // Modify stack during iteration with push
        stack.push(4);

        // Iterator should now throw ConcurrentModificationException
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testFailFastOnPop() {
        StackFailFast<Integer> stack = new StackFailFast<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        var iterator = stack.iterator();
        iterator.next(); // move once

        // Modify stack during iteration with pop
        stack.pop();

        // Iterator should now throw ConcurrentModificationException
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testNoExceptionWhenUnmodified() {
        StackFailFast<Integer> stack = new StackFailFast<>();
        stack.push(1);
        stack.push(2);

        var iterator = stack.iterator();

        // Should iterate normally if stack not modified
        while (iterator.hasNext()) {
            iterator.next();
        }
    }
}
