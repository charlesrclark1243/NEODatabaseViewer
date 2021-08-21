package NEODatabase;

import java.util.Comparator;

public class MissDistanceComparator implements Comparator<NearEarthObject> {
    public MissDistanceComparator() {
        super();
    }

    public int compare(NearEarthObject leftSide, NearEarthObject rightSide) {
        return Double.compare(leftSide.getMissDistance(), rightSide.getMissDistance());
    }
}
