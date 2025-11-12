package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_24.Building;

/******************************************************************************
 * Exercise_1_4_24Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_24Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_24Test {
    @Test
    public void findsTheTraceExample() {
        // N = 100, F = 8 is the write-up's trace: 1, 2, 4 survive, 8 breaks,
        // then 6 and 7 survive inside (4, 8]. Six throws, one broken egg.
        Building building = new Building(100, 8);

        assertEquals(8, Exercise_1_4_24.findFloor(building));
        assertEquals(6, building.eggsThrown);
        assertEquals(1, building.eggsBroken);
    }

    @Test
    public void findsTheFirstFloorWithOneThrow() {
        // The first probe, floor 1, breaks: lo and hi are both 1 and the
        // binary search has nothing to do.
        Building building = new Building(100, 1);

        assertEquals(1, Exercise_1_4_24.findFloor(building));
        assertEquals(1, building.eggsThrown);
        assertEquals(1, building.eggsBroken);
    }

    @Test
    public void findsTheTopFloor() {
        // The doubling overshoots 100 and is capped at the roof.
        assertEquals(100, Exercise_1_4_24.findFloor(new Building(100, 100)));
        assertEquals(64, Exercise_1_4_24.findFloor(new Building(64, 64)));
        assertEquals(1, Exercise_1_4_24.findFloor(new Building(1, 1)));
    }

    @Test
    public void findsTheFloorsAboveTheLastPowerOfTwo() {
        // A draft used lo as the probe and capped it at hi, so after 64
        // survived it set lo = 100 = hi, exited without throwing from the roof,
        // and returned 100 for every F from 65 to 99.
        for (int F = 65; F <= 99; F++)
            assertEquals(F, Exercise_1_4_24.findFloor(new Building(100, F)), "F = " + F);
    }

    @Test
    public void rejectsFloorsOutsideTheBuilding() {
        assertThrows(IllegalArgumentException.class, () -> new Building(100, 0)); // ground
        assertThrows(IllegalArgumentException.class, () -> new Building(100, 101)); // above the roof
        assertThrows(IllegalArgumentException.class, () -> new Building(100, -1)); // negative
    }

    @Test
    public void rejectsThrowsFromOutsideTheBuilding() {
        // Without this guard an uncapped doubling (probe = probe * 2, no
        // Math.min) passed every other test: it threw from floor 128 of a
        // 100-storey building, was told "breaks", and narrowed down correctly.
        Building building = new Building(100, 50);

        assertThrows(IllegalArgumentException.class, () -> building.doesEggBreakFrom(0));
        assertThrows(IllegalArgumentException.class, () -> building.doesEggBreakFrom(101));
        assertEquals(0, building.eggsThrown, "a refused throw is not counted");
    }

    @Test
    public void findsEveryFloorForEverySmallN() {
        // Exhaustive: both finders, every F for every N up to 300.
        for (int N = 1; N <= 300; N++)
            for (int F = 1; F <= N; F++)
                assertFindsFloor(N, F);
    }

    @Test
    public void slowVersionBreaksEveryEggWhenFIsOne() {
        // The write-up's worst case for part 1: every probe is at or above
        // the threshold, so throws and broken eggs are both ⌈lg N⌉.
        for (int N : new int[] { 2, 3, 4, 100, 1000, 65_536, 1_000_000 }) {
            Building building = new Building(N, 1);
            Exercise_1_4_24.findFloorSlow(building);

            assertEquals(lgCeil(N), building.eggsThrown, "throws, N = " + N);
            assertEquals(lgCeil(N), building.eggsBroken, "broken, N = " + N);
        }
    }

    @Test
    public void fastVersionCostDependsOnFNotN() {
        // The point of part 2: the same F costs the same in a much taller
        // building, while the slow version pays lg N regardless.
        Building small = new Building(100, 8);
        Building tall = new Building(1_000_000, 8);
        Exercise_1_4_24.findFloor(small);
        Exercise_1_4_24.findFloor(tall);

        assertEquals(6, small.eggsThrown);
        assertEquals(6, tall.eggsThrown);

        Building slow = new Building(1_000_000, 8);
        Exercise_1_4_24.findFloorSlow(slow);

        assertEquals(20, slow.eggsThrown);
    }

    @Test
    public void handlesALargeBuilding() {
        int N = 1_000_000;

        assertFindsFloor(N, 3);
        assertFindsFloor(N, 524_288); // 2^19, the last full doubling step
        assertFindsFloor(N, 524_289); // one above it
        assertFindsFloor(N, N);
    }

    @Test
    public void agreesWithBruteForceOnRandomBuildings() {
        Random random = new Random(24L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            int N = 1 + random.nextInt(10_000); // 1..10000
            int F = 1 + random.nextInt(N); // 1..N
            assertFindsFloor(N, F);
        }
    }

    /**
     * Runs both finders on an 𝑁-story building with threshold 𝐹 and checks
     * the answer against a floor-by-floor scan, plus the two cost bounds the
     * exercise asks for: ~lg 𝑁 throws and broken eggs for the slow version,
     * ~2 lg 𝐹 throws and ~lg 𝐹 broken eggs for the fast one.
     */
    private static void assertFindsFloor(int N, int F) {
        String context = "N = " + N + ", F = " + F;
        int expected = bruteForce(new Building(N, F));

        Building slow = new Building(N, F);
        assertEquals(expected, Exercise_1_4_24.findFloorSlow(slow), "slow: " + context);
        assertTrue(slow.eggsThrown <= lgCeil(N), "slow throws: " + context);
        assertTrue(slow.eggsBroken <= lgCeil(N), "slow broken: " + context);

        Building fast = new Building(N, F);
        assertEquals(expected, Exercise_1_4_24.findFloor(fast), "fast: " + context);
        assertTrue(fast.eggsThrown <= Math.max(1, 2 * lgCeil(F)), "fast throws: " + context);
        assertTrue(fast.eggsBroken <= Math.max(1, lgCeil(F)), "fast broken: " + context);
    }

    /** Throws from every floor in turn, bottom up, until an egg breaks. */
    private static int bruteForce(Building building) {
        for (int floor = 1; floor <= building.totalFloors; floor++)
            if (building.doesEggBreakFrom(floor))
                return floor;

        throw new AssertionError("no floor breaks an egg in " + building.totalFloors + " floors");
    }

    /** ⌈lg 𝑥⌉ — the number of doublings that take 1 up to at least 𝑥. */
    private static int lgCeil(int x) {
        int k = 0;

        for (long d = 1; d < x; d *= 2)
            k++;

        return k;
    }
}
