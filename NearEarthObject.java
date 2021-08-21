package NEODatabase;

import java.util.Date;

public class NearEarthObject {
    private int referenceID;
    private String name;
    private String orbitingBody;
    private double absoluteMagnitude;
    private double averageDiameter;
    private double missDistance;
    private boolean isDangerous;
    private Date closestApproachDate;

    public NearEarthObject(int referenceID, String name, String orbitingBody, double absoluteMagnitude,
                           double minimumDiameter, double maximumDiameter, double missDistance,
                           boolean isDangerous, long closestDateTimestamp) {
        this.referenceID = referenceID;
        this.name = name;
        this.orbitingBody = orbitingBody;
        this.absoluteMagnitude = absoluteMagnitude;
        this.averageDiameter = (minimumDiameter + maximumDiameter) / 2;
        this.missDistance = missDistance;
        this.isDangerous = isDangerous;
        this.closestApproachDate = new Date(closestDateTimestamp);
    }

    public int getReferenceID() {
        return referenceID;
    }

    public String getName() {
        return name;
    }

    public String getOrbitingBody() {
        return orbitingBody;
    }

    public double getAbsoluteMagnitude() {
        return absoluteMagnitude;
    }

    public double getAverageDiameter() {
        return averageDiameter;
    }

    public double getMissDistance() {
        return missDistance;
    }

    public boolean getIsDangerous() {
        return isDangerous;
    }

    public Date getClosestApproachDate() {
        return closestApproachDate;
    }

    public boolean equals(Object object) {
        if (!(object instanceof NearEarthObject candidate)) {
            return false;
        }

        boolean sameReferenceID = this.referenceID == candidate.referenceID;
        boolean sameName = this.name.equals(candidate.name);
        boolean sameOrbitingBody = this.orbitingBody.equals(candidate.orbitingBody);
        boolean sameAbsoluteMagnitude = this.absoluteMagnitude == candidate.absoluteMagnitude;
        boolean sameAverageDiameter = this.averageDiameter == candidate.averageDiameter;
        boolean sameMissDistance = this.missDistance == candidate.missDistance;
        boolean sameIsDangerous = this.isDangerous == candidate.isDangerous;
        boolean sameClosestApproachDate = this.closestApproachDate.equals(candidate.closestApproachDate);

        return sameReferenceID && sameName && sameOrbitingBody && sameAbsoluteMagnitude &&
                sameAverageDiameter && sameMissDistance && sameIsDangerous && sameClosestApproachDate;
    }
}
