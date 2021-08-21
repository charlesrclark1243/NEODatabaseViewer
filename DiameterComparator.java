package NEODatabase;

import java.util.Comparator;

public class DiameterComparator implements Comparator<NearEarthObject> {
    public DiameterComparator() {
        super();
    }

    public int compare(NearEarthObject leftSide, NearEarthObject rightSide) {
        return Double.compare(leftSide.getAverageDiameter(), rightSide.getAverageDiameter());
    }
}
