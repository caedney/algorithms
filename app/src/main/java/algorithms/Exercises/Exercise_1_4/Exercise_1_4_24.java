package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.24
 * 
 * <p>
 * <i>Throwing eggs from a building</i>. Suppose that you have an 𝑁-story
 * building and plenty of eggs. Suppose also that an egg is broken if it is
 * thrown off floor 𝐹 or higher, and intact otherwise. First, devise a strategy
 * to determine the value of 𝐹 such that the number of broken eggs is ~lg 𝑁
 * when using ~lg 𝑁 throws, then find a way to reduce the cost to ~2 lg 𝐹.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_24
 * ./gradlew test --tests "Exercise_1_4_24Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_24 {
    static class Building {
        int totalFloors;
        int breakingFloor;
        int eggsThrown;
        int eggsBroken;

        public Building(int totalFloors, int breakingFloor) {
            if (breakingFloor < 1 || breakingFloor > totalFloors) {
                throw new IllegalArgumentException("Can only throw eggs from within the building.");
            }

            this.totalFloors = totalFloors;
            this.breakingFloor = breakingFloor;
        }

        public boolean doesEggBreakFrom(int currentFloor) {
            if (currentFloor < 1 || currentFloor > totalFloors) {
                throw new IllegalArgumentException("Can only throw eggs from within the building.");
            }

            eggsThrown++;

            boolean eggBreaks = currentFloor >= breakingFloor;

            if (eggBreaks) {
                eggsBroken++;
            }

            return eggBreaks;
        }
    }

    /**
     * Binary search over floors 1..𝑁. Each throw halves the range of floors that
     * could still be 𝐹, so the throw count is ~lg 𝑁 whatever 𝐹 is. Worst case
     * for broken eggs is 𝐹 = 1, where every probe is at or above the threshold and
     * every throw breaks an egg.
     * 
     * Cost: ~lg 𝑁 throws, ~lg 𝑁 broken eggs.
     */
    public static int binarySearch(Building building) {
        int lo = 1;
        int hi = building.totalFloors;

        while (lo < hi) {
            int midpoint = lo + (hi - lo) / 2;

            if (building.doesEggBreakFrom(midpoint)) {
                hi = midpoint;
            } else {
                lo = midpoint + 1;
            }
        }

        return lo;
    }

    /**
     * Doubling search, then binary search. The probe doubles (1, 2, 4, 8, ...)
     * until an egg breaks — ~lg 𝐹 throws, one broken egg — which brackets 𝐹 in
     * [lo, hi]: one above the last survivor, up to the floor that broke. The
     * bracket has fewer than 𝐹 floors, so a binary search inside it is ~lg 𝐹
     * more. lo and probe stay separate: a draft that merged them exited early once
     * the capped probe hit hi.
     *
     * Cost: ~2 lg 𝐹 throws, ~lg 𝐹 broken eggs — depends on the answer, not on 𝑁.
     */
    public static int doublingSearch(Building building) {
        int lo = 1;
        int hi = building.totalFloors;
        int probe = 1;

        while (lo < hi) {
            if (building.doesEggBreakFrom(probe)) {
                hi = probe;

                while (lo < hi) {
                    int midpoint = lo + (hi - lo) / 2;

                    if (building.doesEggBreakFrom(midpoint)) {
                        hi = midpoint;
                    } else {
                        lo = midpoint + 1;
                    }
                }
            } else {
                lo = probe + 1;
                probe = Math.min(probe * 2, hi);
            }
        }

        return lo;
    }

    public static void main(String[] args) {
        Building building = new Building(100, 8);
        int floor = doublingSearch(building);
        System.out.println("An egg will break from floor: " + floor);
        System.out.println("Number of eggs thrown: " + building.eggsThrown);
        System.out.println("Number of eggs broken: " + building.eggsBroken);
    }
}
