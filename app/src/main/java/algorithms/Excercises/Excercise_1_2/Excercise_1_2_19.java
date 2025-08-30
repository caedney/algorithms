package algorithms.Excercises.Excercise_1_2;

/**
 * Excercise 1.2.19
 * 
 * <p>
 * <i>Parsing</i>. Develop the parse constructors for your {@code Date} and
 * {@code Transaction} implementations of EXERCISE 1.2.13 that take a single
 * {@code String} argument to specify the initialization values, using the
 * formats given in the table below.
 * </p>
 * 
 * <p>
 * <i>Partial solution:</i>
 * </p>
 * 
 * {@snippet :
 * public void Date(String date) {
 *     String[] fields = date.split("/");
 *     month = Integer.parseInt(fields[0]);
 *     day = Integer.parseInt(fields[1]);
 *     year = Integer.parseInt(fields[2]);
 * }
 * }
 * 
 * <table>
 * <thead>
 * <tr>
 * <th>type</th>
 * <th>format</th>
 * <th>example</th>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td>{@code Date}</td>
 * <td>integers separated by slashes</td>
 * <td>{@code 22/05/1939}</td>
 * </tr>
 * <tr>
 * <td>{@code Transaction}</td>
 * <td>customer, date, and amount, separated by whitespace</td>
 * <td>{@code Turing 22/05/1939 11.99}</td>
 * </tr>
 * </tbody>
 * </table>
 */
public class Excercise_1_2_19 {
    public int Date(String date) {
        String[] fields = date.split("/");
        int month = Integer.parseInt(fields[0]);
        int day = Integer.parseInt(fields[1]);
        int year = Integer.parseInt(fields[2]);

        return month + day + year;
    }

    public static void main(String[] args) {
    }
}
