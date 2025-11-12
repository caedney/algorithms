package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_25.Building;

/******************************************************************************
 * Exercise_1_4_25Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_25Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_25Test {
    @Test
    public void buildingRejectsABreakingFloorOutsideTheBuilding() {
        assertThrows(IllegalArgumentException.class, () -> new Building(100, 2, 0));
        assertThrows(IllegalArgumentException.class, () -> new Building(100, 2, 101));
    }

    @Test
    public void buildingRejectsAThrowFromOutsideTheBuilding() {
        Building building = new Building(100, 2, 50);

        assertThrows(IllegalArgumentException.class, () -> building.doesEggBreakFrom(0));
        assertThrows(IllegalArgumentException.class, () -> building.doesEggBreakFrom(101));
    }

    @Test
    public void buildingRefusesAThrowOnceEveryEggIsBroken() {
        // One egg: break it, and the next throw must be refused, not counted.
        // A draft checked eggsBroken > totalEggs and allowed one throw too many.
        Building building = new Building(100, 1, 50);

        assertTrue(building.doesEggBreakFrom(50));
        assertThrows(IllegalStateException.class, () -> building.doesEggBreakFrom(1));
        assertEquals(1, building.eggsThrown);
    }

    @Test
    public void fixedStrideFindsTheMainExample() {
        // N = 100, F = 8: egg 1 breaks on its first jump (floor 10),
        // egg 2 walks 1..8 and breaks on 8.
        Building building = new Building(100, 2, 8);

        assertEquals(8, Exercise_1_4_25.fixedStrideSearch(building));
        assertEquals(9, building.eggsThrown);
        assertEquals(2, building.eggsBroken);
    }

    @Test
    public void fixedStrideFindsTheWriteUpTraces() {
        // N = 20, stride 5, jump floors 5, 10, 15, 20.
        assertFixedStrideCost(20, 3, 1 + 3); // break at 5; walk 1, 2, 3
        assertFixedStrideCost(20, 10, 2 + 4); // break at 10; walk 6..9 all hold, infer 10
        assertFixedStrideCost(20, 14, 3 + 4); // break at 15; walk 11..14
        assertFixedStrideCost(20, 19, 4 + 4); // break at 20; walk 16..19 — the worst case, N/s + s - 1
    }

    @Test
    public void fixedStrideFindsTheRoof() {
        // F = N: every jump below the roof holds, the jump from the roof breaks,
        // and the walk clears the last gap and infers N with the second egg intact.
        Building building = new Building(100, 2, 100);

        assertEquals(100, Exercise_1_4_25.fixedStrideSearch(building));
        assertEquals(10 + 9, building.eggsThrown);
        assertEquals(1, building.eggsBroken);
    }

    @Test
    public void fixedStrideClampsTheLastJumpToTheRoof() {
        // N = 50, stride 8: jumps 8, 16, 24, 32, 40, 48 hold and the next would be
        // 56. A draft let it overshoot, fell out of the loop and threw instead of
        // testing floors 49 and 50.
        assertFixedStrideCost(50, 49, 7 + 1); // break at 50 (clamped); walk 49 breaks
        assertFixedStrideCost(50, 50, 7 + 1); // break at 50; walk 49 holds, infer 50
    }

    @Test
    public void fixedStrideHandlesTheSmallestBuildings() {
        assertFixedStrideCost(1, 1, 1); // stride 1: jump 1 breaks; nothing to walk
        assertFixedStrideCost(2, 1, 2); // stride 2: jump 2 breaks; walk 1 breaks
        assertFixedStrideCost(2, 2, 2); // jump 2 breaks; walk 1 holds, infer 2
        assertFixedStrideCost(3, 2, 2); // stride 2: jump 2 breaks; walk 1 holds, infer 2
        assertFixedStrideCost(3, 3, 2); // jump 2 holds; jump 3 (clamped) breaks; nothing to walk
    }

    @Test
    public void fixedStrideFindsEveryFloorForEverySmallN() {
        // Exhaustive: every F for every N up to 300, with the egg budget enforced
        // by the building and the throw count checked against the write-up's bound.
        for (int N = 1; N <= 300; N++) {
            int stride = (int) Math.ceil(Math.sqrt(N));
            int bound = N / stride + stride - 1;

            for (int F = 1; F <= N; F++) {
                assertFindsBreakingFloor(N, F, bound, Strategy.FIXED);
            }
        }
    }

    @Test
    public void fixedStrideWorstCaseIsBelowTwoRootN() {
        for (int N : new int[] { 20, 50, 100, 400, 1000, 10_000 }) {
            int worst = 0;

            for (int F = 1; F <= N; F++) {
                Building building = new Building(N, 2, F);
                Exercise_1_4_25.fixedStrideSearch(building);
                worst = Math.max(worst, building.eggsThrown);
            }

            assertTrue(worst <= 2 * Math.sqrt(N), "N = " + N + ": worst case " + worst);
        }
    }

    @Test
    public void fixedStrideAgreesWithTheHiddenFloorOnRandomBuildings() {
        Random random = new Random(25L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            int N = 1 + random.nextInt(5000);
            int F = 1 + random.nextInt(N);
            int stride = (int) Math.ceil(Math.sqrt(N));

            assertFindsBreakingFloor(N, F, N / stride + stride - 1, Strategy.FIXED);
        }
    }

    @Test
    public void growingStrideFindsTheWriteUpTraces() {
        // Jump floors are the triangular numbers 1, 3, 6, 10, ..., k(k+1)/2.
        assertGrowingStrideCost(100, 90, 13 + 12); // 13th jump (91) breaks; walk 79..90
        assertGrowingStrideCost(1000, 135, 16 + 15); // 16th jump (136) breaks; walk 121..135
    }

    @Test
    public void growingStrideCostDoesNotDependOnTheBuildingHeight() {
        // F = 150 costs the same in a 200-floor building as in a 10,000-floor one:
        // 17 jumps (the 17th, floor 153, breaks) and a walk from 137 to 150.
        assertGrowingStrideCost(200, 150, 17 + 14);
        assertGrowingStrideCost(10_000, 150, 17 + 14);

        // The fixed stride pays for the whole building: stride 100, 2 jumps, walk 101..150.
        Building fixed = new Building(10_000, 2, 150);
        assertEquals(150, Exercise_1_4_25.fixedStrideSearch(fixed));
        assertEquals(2 + 50, fixed.eggsThrown);
    }

    @Test
    public void growingStrideClampsTheLastJumpToTheRoof() {
        // N = 100: jumps 1, 3, ..., 91 hold and the next would be 105, clamped to 100.
        assertGrowingStrideCost(100, 100, 14 + 8); // 14th jump (100) breaks; walk 92..99 hold, infer 100
        assertGrowingStrideCost(100, 95, 14 + 4); // walk 92, 93, 94, 95
    }

    @Test
    public void growingStrideHandlesTheSmallestBuildings() {
        assertGrowingStrideCost(1, 1, 1); // jump 1 breaks; nothing to walk
        assertGrowingStrideCost(2, 1, 1); // jump 1 breaks
        assertGrowingStrideCost(2, 2, 2); // jump 1 holds; jump 2 (clamped from 3) breaks; nothing to walk
        assertGrowingStrideCost(3, 2, 3); // jump 1 holds; jump 3 breaks; walk 2 breaks
        assertGrowingStrideCost(3, 3, 3); // jump 1 holds; jump 3 breaks; walk 2 holds, infer 3
    }

    @Test
    public void growingStrideFindsEveryFloorForEverySmallN() {
        // Exhaustive: every F for every N up to 300. The bound is 2k - 1, where k is
        // the jump that first reaches F, computed here from the triangular numbers
        // independently of the implementation.
        for (int N = 1; N <= 300; N++) {
            for (int F = 1; F <= N; F++) {
                assertFindsBreakingFloor(N, F, 2 * jumpsToReach(F) - 1, Strategy.GROWING);
            }
        }
    }

    @Test
    public void growingStrideWorstCaseFitsTwoRootTwoRootF() {
        // Over every F up to N, the throw count never exceeds 2√2·√F + 1: the
        // constant c in the exercise's ~c√F is 2√2.
        int N = 10_000;

        for (int F = 1; F <= N; F++) {
            Building building = new Building(N, 2, F);
            Exercise_1_4_25.growingStrideSearch(building);
            assertTrue(building.eggsThrown <= 2 * Math.sqrt(2) * Math.sqrt(F) + 1,
                    "F = " + F + ": " + building.eggsThrown + " throws");
        }
    }

    @Test
    public void growingStrideAgreesWithTheHiddenFloorOnRandomBuildings() {
        Random random = new Random(25L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            int N = 1 + random.nextInt(5000);
            int F = 1 + random.nextInt(N);

            assertFindsBreakingFloor(N, F, 2 * jumpsToReach(F) - 1, Strategy.GROWING);
        }
    }

    private enum Strategy {
        FIXED, GROWING
    }

    /**
     * Runs the strategy on a building whose breaking floor is F and checks the
     * three things the exercise asks for: the right floor, at most two broken
     * eggs, and a throw count within the bound.
     */
    private static void assertFindsBreakingFloor(int N, int F, int bound, Strategy strategy) {
        Building building = new Building(N, 2, F);
        int found = strategy == Strategy.FIXED
                ? Exercise_1_4_25.fixedStrideSearch(building)
                : Exercise_1_4_25.growingStrideSearch(building);
        String context = "N = " + N + ", F = " + F + " (" + strategy + ")";

        assertEquals(F, found, "wrong floor: " + context);
        assertTrue(building.eggsBroken <= 2, "broke " + building.eggsBroken + " eggs: " + context);
        assertTrue(building.eggsThrown <= bound, building.eggsThrown + " throws exceeds " + bound + ": " + context);
    }

    private static void assertFixedStrideCost(int N, int F, int expectedThrows) {
        Building building = new Building(N, 2, F);

        assertEquals(F, Exercise_1_4_25.fixedStrideSearch(building), "N = " + N + ", F = " + F);
        assertEquals(expectedThrows, building.eggsThrown, "throws for N = " + N + ", F = " + F);
    }

    private static void assertGrowingStrideCost(int N, int F, int expectedThrows) {
        Building building = new Building(N, 2, F);

        assertEquals(F, Exercise_1_4_25.growingStrideSearch(building), "N = " + N + ", F = " + F);
        assertEquals(expectedThrows, building.eggsThrown, "throws for N = " + N + ", F = " + F);
    }

    /** The smallest 𝑘 whose triangular number 𝑘(𝑘+1)/2 is at least 𝐹: the jump that first reaches 𝐹. */
    private static int jumpsToReach(int F) {
        int k = 0;

        for (int position = 0; position < F; k++)
            position += k + 1;

        return k;
    }
}
