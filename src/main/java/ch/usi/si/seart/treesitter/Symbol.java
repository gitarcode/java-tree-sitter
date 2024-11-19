package ch.usi.si.seart.treesitter;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

/**
 * Represents a symbol in an abstract syntax {@link Tree}. Symbols are used to identify nodes in the
 * AST. Each symbol has an associated ID, {@link Type Type}, and name.
 *
 * @author Ozren Dabić
 * @since 1.6.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Symbol {

  private final int id;
  private final Type type;
  private final String name;

  @SuppressWarnings("unused")
  Symbol(int id, int ordinal, String name) {
    this(id, Type.get(ordinal), name);
  }

  @Override
  @Generated
  public String toString() {
    return String.format("Symbol(id: %d, type: %s, name: '%s')", id, type, name);
  }

  @Override
  @Generated
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Symbol symbol = (Symbol) obj;
    return id == symbol.id && type == symbol.type && Objects.equals(name, symbol.name);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(id, type, name);
  }

  /**
   * Enumeration representing the possible types of symbols. This includes:
   *
   * <ul>
   *   <li>Named nodes ({@link #REGULAR})
   *   <li>Anonymous nodes ({@link #ANONYMOUS})
   *   <li>Hidden nodes ({@link #AUXILIARY})
   * </ul>
   *
   * @author Ozren Dabić
   * @since 1.6.0
   */
  public enum Type {
    REGULAR,
    ANONYMOUS,
    AUXILIARY;

    private static final Type[] VALUES = values();

    private static Type get(int ordinal) {
      return VALUES[ordinal];
    }
  }
}
