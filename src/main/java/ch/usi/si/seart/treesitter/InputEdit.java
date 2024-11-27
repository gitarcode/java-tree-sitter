package ch.usi.si.seart.treesitter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an edit operation on a section of source code. Contains information pertaining to the
 * starting byte offset and position, as well as the former and current end byte offsets and
 * positions.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@Getter
@AllArgsConstructor
public class InputEdit {

  private final int startByte;
  private final int oldEndByte;
  private final int newEndByte;
  private final Point startPoint;
  private final Point oldEndPoint;
  private final Point newEndPoint;
}
