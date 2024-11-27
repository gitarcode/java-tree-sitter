package ch.usi.si.seart.treesitter;

import java.util.Iterator;
import lombok.Getter;

/**
 * Specialized iterator that can be used to generate suggestions and improve syntax error
 * diagnostics. To get symbols valid in an {@code ERROR} node, use the lookahead iterator on its
 * first leaf node state. For {@code MISSING} nodes, a lookahead iterator created on the previous
 * non-extra leaf node may be appropriate.
 *
 * @since 1.12.0
 * @author Ozren Dabić
 */
public class LookaheadIterator extends External implements Iterator<Symbol> {

  private final boolean hasNext;

  private final @Getter Language language;

  LookaheadIterator(long pointer, boolean hasNext, Language language) {
    super(pointer);
    this.hasNext = hasNext;
    this.language = language;
  }

  @Override
  protected native void delete();

  @Override
  public boolean hasNext() {
    return hasNext;
  }

  @Override
  public native Symbol next();
}
