package NEODatabase;

import java.util.Comparator;

public class ApproachDateComparator implements Comparator<NearEarthObject> {
    public ApproachDateComparator() {
        super();
    }

    public int compare(NearEarthObject leftSide, NearEarthObject rightSide) {
        return leftSide.getClosestApproachDate().compareTo(rightSide.getClosestApproachDate());
    }
}
