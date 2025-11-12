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
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_24 {
    static class Building {
        int totalFloors;
        int floorEggBreaksFrom;
        int eggsThrown;
        int eggsBroken;

        public Building(int totalFloors, int floorEggBreaksFrom) {
            if (floorEggBreaksFrom < 1 || floorEggBreaksFrom > totalFloors) {
                throw new IllegalArgumentException("Can only throw eggs from within the building.");
            }

            this.totalFloors = totalFloors;
            this.floorEggBreaksFrom = floorEggBreaksFrom;
        }

        public boolean doesEggBreakFrom(int currentFloor) {
            if (currentFloor < 1 || currentFloor > totalFloors) {
                throw new IllegalArgumentException("Can only throw eggs from within the building.");
            }

            eggsThrown++;

            boolean eggBreaks = currentFloor >= floorEggBreaksFrom;

            if (eggBreaks) {
                eggsBroken++;
            }

            return eggBreaks;
        }
    }

    public static int findFloorSlow(Building building) {
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

    public static int findFloor(Building building) {
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
        int floor = findFloor(building);
        System.out.println("An egg will break from floor: " + floor);
        System.out.println("Number of eggs thrown: " + building.eggsThrown);
        System.out.println("Number of eggs broken: " + building.eggsBroken);
    }
}
