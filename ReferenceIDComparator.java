package NEODatabase;

import java.util.Comparator;

public class ReferenceIDComparator implements Comparator<NearEarthObject> {
    public ReferenceIDComparator() {
        super();
    }

    public int compare(NearEarthObject leftSide, NearEarthObject rightSide) {
        return Integer.compare(leftSide.getReferenceID(), rightSide.getReferenceID());
    }
}
