# Zeller's Congruence

**Zeller’s Congruence** is a mathematical formula used to determine the day of
the week for any given date in the Gregorian or Julian calendar.

$$
h = \bigg(q + \bigg \lfloor \frac{13(m + 1)}{5}\bigg \rfloor + K + \bigg \lfloor \frac{K}{4} \bigg \rfloor + \bigg \lfloor \frac{J}{4} \bigg \rfloor + 5J \bigg) \bmod 7
$$

Where:

- **h** = day of the week (0 = Saturday, 1 = Sunday, …, 6 = Friday)
- **q** = day of the month
- **m** = month (March = 3, …, January = 13, February = 14 of the previous year)
- **K** = year of the century (year % 100)
- **J** = zero-based century (year / 100)

⚠️ January and February are treated as months 13 and 14 of the previous year.

Output:

| h   | Day       |
| --- | --------- |
| 0   | Saturday  |
| 1   | Sunday    |
| 2   | Monday    |
| 3   | Tuesday   |
| 4   | Wednesday |
| 5   | Thursday  |
| 6   | Friday    |

### Example

**July 20, 1969**

| Symbol | Meaning                      | Value |
| ------ | ---------------------------- | ----- |
| q      | Day of month                 | 20    |
| m      | Month                        | 7     |
| K      | Year of century (year % 100) | 69    |
| J      | Century (year ÷ 100)         | 19    |

<br />

$$
\begin{aligned}
h &= (20 + \lfloor \tfrac{13(7+1)}{5} \rfloor + 69 + \lfloor \tfrac{69}{4} \rfloor + \lfloor \tfrac{19}{4} \rfloor + 5(19)) \bmod 7 \\
&= (20 + 20 + 69 + 17 + 4 + 95) \bmod 7 \\
&= 225 \bmod 7 \\
&= 1
\end{aligned}
$$

<br />

$h = 1 \to $ **Sunday**  
**July 20, 1969** was a **Sunday**

### Example

**February 14, 1969**

| Symbol | Meaning                      | Value |
| ------ | ---------------------------- | ----- |
| q      | Day of month                 | 14    |
| m      | Month                        | 14    |
| K      | Year of century (year % 100) | 68    |
| J      | Century (year ÷ 100)         | 19    |

<br />

$$
\begin{aligned}
h &= (20 + \lfloor \tfrac{13(14+1)}{5} \rfloor + 68 + \lfloor \tfrac{68}{4} \rfloor + \lfloor \tfrac{19}{4} \rfloor + 5(19)) \bmod 7 \\
&= (20 + 39 + 68 + 17 + 4 + 95) \bmod 7 \\
&= 237 \bmod 7 \\
&= 1
\end{aligned}
$$

<br />

$h = 6 \to $ **Friday**  
**February 14, 1969** was a **Friday**
