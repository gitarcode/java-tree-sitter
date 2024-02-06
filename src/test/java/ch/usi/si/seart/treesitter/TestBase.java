package ch.usi.si.seart.treesitter;

import org.mockito.Mockito;

import java.util.List;

public abstract class TestBase {

    protected static final Language invalid;

    protected final Node empty = new Node.Null();
    protected final Node treeless = new Node(1, 1, 1, 1, 1L, null);
    protected final Point _0_0_ = new Point(0, 0);
    protected final Point _0_1_ = new Point(0, 1);
    protected final Point _1_0_ = new Point(1, 0);
    protected final Point _1_1_ = new Point(1, 1);
    protected final Point _2_2_ = new Point(2, 2);

    static {
        LibraryLoader.load();
        invalid = Mockito.mock(Language.class);

        Mockito.when(invalid.name()).thenReturn("INVALID");
        Mockito.when(invalid.ordinal()).thenReturn(Language.values().length);

        Mockito.when(invalid.getId()).thenReturn(0L);
        Mockito.when(invalid.getVersion()).thenReturn(0);
        Mockito.when(invalid.getTotalFields()).thenReturn(0);
        Mockito.when(invalid.getTotalStates()).thenReturn(0);
        Mockito.when(invalid.getTotalSymbols()).thenReturn(0);
        Mockito.when(invalid.getSymbols()).thenReturn(List.of());

        Mockito.when(invalid.nextState(Mockito.any())).thenCallRealMethod();
    }
}
