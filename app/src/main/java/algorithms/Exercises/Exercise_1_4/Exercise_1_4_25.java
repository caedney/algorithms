package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.25
 * 
 * <p>
 * <i>Throwing two eggs from a building</i>. Consider the previous question, but
 * now suppose you only have two eggs, and your cost model is the number of
 * throws. Devise a strategy to determine 𝐹 such that the number of throws is
 * at most 2√𝑁, then find a way to reduce the cost to ~𝑐√𝐹. This is analogous
 * to a situation where search hits (egg intact) are much cheaper than misses
 * (egg broken).
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_25
 * ./gradlew test --tests "Exercise_1_4_25Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_25 {
    static class Building {
        int totalFloors;
        int totalEggs;
        int breakingFloor;
        int eggsThrown;
        int eggsBroken;

        public Building(int totalFloors, int totalEggs, int breakingFloor) {
            if (breakingFloor < 1 || breakingFloor > totalFloors) {
                throw new IllegalArgumentException("Can only throw eggs from within the building.");
            }

            this.totalFloors = totalFloors;
            this.totalEggs = totalEggs;
            this.breakingFloor = breakingFloor;
        }

        public boolean doesEggBreakFrom(int currentFloor) {
            if (eggsBroken >= totalEggs) {
                throw new IllegalStateException("There are no eggs left to throw.");
            }

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
     * Jump-and-walk with a fixed stride 𝑠 = ⌈√𝑁⌉. Egg 1 is thrown from 𝑠, 2𝑠,
     * 3𝑠, … (the last jump clamped to the roof) until it breaks; egg 2 then
     * walks the gap below that floor one floor at a time, from one above the
     * last floor that held. Clearing the whole gap means the breaking floor is
     * the jump floor above it. Worst case is 𝐹 just below the last full-stride
     * jump, where egg 1 uses all ⌊𝑁/𝑠⌋ jumps and egg 2 all 𝑠 − 1 walks.
     *
     * Cost: at most 2√𝑁 throws, at most 2 broken eggs.
     */
    public static int fixedStrideSearch(Building building) {
        int stride = (int) Math.ceil(Math.sqrt(building.totalFloors));
        int prevJump = 0;
        int currentJump = stride;

        while (currentJump <= building.totalFloors) {
            if (building.doesEggBreakFrom(currentJump)) {
                int currentFloor = prevJump + 1;

                while (currentFloor < currentJump) {
                    if (building.doesEggBreakFrom(currentFloor)) {
                        return currentFloor;
                    } else {
                        currentFloor++;
                    }
                }

                return currentFloor;
            } else {
                prevJump = currentJump;
                currentJump = Math.min(currentJump + stride, building.totalFloors);
            }
        }

        throw new IllegalStateException("Egg must have been broken by now.");
    }

    /**
     * Jump-and-walk with a stride that grows by one floor each jump, so egg 1
     * is thrown from the triangular numbers 1, 3, 6, 10, … (the last jump
     * clamped to the roof); egg 2 walks the gap below the breaking jump as in
     * the fixed-stride search. Breaking on the 𝑘th jump leaves a gap of 𝑘 − 1
     * floors, so the cost is at most 2𝑘 − 1, and since the 𝑘th jump floor is
     * 𝑘(𝑘+1)/2 ≈ 𝑘²/2, 𝑘 ≈ √(2𝐹) and 𝑐 = 2√2.
     *
     * Cost: ~𝑐√𝐹 throws, at most 2 broken eggs — depends on the answer, not 𝑁.
     */
    public static int growingStrideSearch(Building building) {
        int stride = 1;
        int prevJump = 0;
        int currentJump = stride;

        while (currentJump <= building.totalFloors) {
            if (building.doesEggBreakFrom(currentJump)) {
                int currentFloor = prevJump + 1;

                while (currentFloor < currentJump) {
                    if (building.doesEggBreakFrom(currentFloor)) {
                        return currentFloor;
                    } else {
                        currentFloor++;
                    }
                }

                return currentFloor;
            } else {
                prevJump = currentJump;
                stride++;
                currentJump = Math.min(currentJump + stride, building.totalFloors);
            }
        }

        throw new IllegalStateException("Egg must have been broken by now.");
    }

    public static void main(String[] args) {
        Building building = new Building(100, 2, 90);
        int floor = growingStrideSearch(building);
        System.out.println("An egg will break from floor: " + floor);
        System.out.println("Number of eggs thrown: " + building.eggsThrown);
        System.out.println("Number of eggs broken: " + building.eggsBroken);
    }
}
