package ch.usi.si.seart.treesitter;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a two-dimensional point with row and column coordinates. Points are an alternative to
 * byte ranges, and as such are used to represent more human-friendly positions of {@link Tree}
 * nodes within source code. Although {@link Node} positions within files should never be negative,
 * instances of this class can be created with negative row and column values for other purposes,
 * such as representing relative positions within a file or snippet.
 *
 * <p>Points are immutable, and operations on them will either yield a new instance, or existing
 * instances under certain conditions. For example, adding the origin point to any other point will
 * return the same instance.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Point implements Comparable<Point> {

  @Getter(lazy = true)
  private static final Point ORIGIN = new Point(0, 0);

  private final int row;
  private final int column;

  /**
   * Returns a string representation of this point in the format:
   *
   * <pre>{@code
   * row:column
   * }</pre>
   *
   * @return a string representation of this point
   */
  @Override
  @Generated
  public String toString() {
    return row + ":" + column;
  }

  /**
   * Checks if this point represents the origin, which is when both the row and the column are zero.
   * In byte range terms, this point also corresponds to zero.
   *
   * @return {@code true} if this is an origin point, {@code false} otherwise
   */
  public boolean isOrigin() {
    return equals(ORIGIN());
  }

  /**
   * Compares this point with another point for positional order. Points are compared by their row
   * values first, and if those are equal they are then compared by their column values.
   *
   * @param other the object to be compared
   * @return the row comparison result, or the column comparison result if the rows are equal
   * @throws NullPointerException if the other point is null
   * @since 1.5.1
   */
  @Override
  public int compareTo(@NotNull Point other) {
    Objects.requireNonNull(other, "Other point must not be null!");
    int compare = Integer.compare(row, other.row);
    return compare != 0 ? compare : Integer.compare(column, other.column);
  }

  /**
   * Adds another point to this point, resulting in a new point with coordinates equal to the sum of
   * the coordinates of this point and the other point.
   *
   * @param other the point to be added to this point
   * @return the resulting point
   * @throws NullPointerException if {@code other} is null
   * @since 1.5.1
   */
  public Point add(@NotNull Point other) {
    Objects.requireNonNull(other, "Other point must not be null!");
    if (isOrigin()) return other;
    if (other.isOrigin()) return this;
    return add(other.row, other.column);
  }

  /**
   * Subtracts another point from this point, resulting in a new point with coordinates equal to the
   * difference between the coordinates of this point and the other point.
   *
   * @param other the point to be subtracted from this point
   * @return the resulting point
   * @throws NullPointerException if {@code other} is null
   * @since 1.5.1
   */
  public Point subtract(@NotNull Point other) {
    Objects.requireNonNull(other, "Other point must not be null!");
    if (other.isOrigin()) return this;
    if (equals(other)) return ORIGIN();
    return add(-other.row, -other.column);
  }

  private Point add(int row, int column) {
    return new Point(this.row + row, this.column + column);
  }

  /**
   * Multiplies the coordinates of this point by a scalar value, resulting in a new point with
   * scaled coordinates.
   *
   * @param value the scalar value by which to multiply the coordinates of this point
   * @return the resulting point
   * @since 1.5.1
   */
  public Point multiply(int value) {
    switch (value) {
      case 0:
        return ORIGIN();
      case 1:
        return this;
      default:
        return new Point(row * value, column * value);
    }
  }
}
