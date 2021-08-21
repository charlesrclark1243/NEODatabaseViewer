package NEODatabase;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class NEODatabase extends ArrayList<NearEarthObject> {
    protected static final String API_KEY = // input your NASA NEO API key here.
    protected static final String API_ROOT = "https://api.nasa.gov/neo/rest/v1/neo/browse?";

    public NEODatabase() {
        super();
    }

    public String buildQueryURL(int pageNumber) throws IllegalArgumentException {
        if (pageNumber < 0 || pageNumber > 715) {
            throw new IllegalArgumentException("Invalid Input: Page number must be in the interval [0, 715].");
        }

        return API_ROOT + "page=" + pageNumber + "&api_key=" + API_KEY;
    }

    public void addAll(String queryURL) throws IllegalArgumentException {
        if (queryURL == null) {
            throw new IllegalArgumentException("Invalid Input: queryURL incorrect.");
        }

        String queryURLTailString = queryURL.substring(API_ROOT.length());
        if (!queryURLTailString.startsWith("page=")) {
            throw new IllegalArgumentException("Invalid Input: queryURL incorrect.");
        }

        if (!queryURLTailString.contains("&")) {
            throw new IllegalArgumentException("Invalid Input: queryURL incorrect.");
        }

        int secondParameterBeginningIndex = queryURLTailString.indexOf('&');
        int pageNumber = Integer.parseInt(queryURLTailString.substring("page=".length(),
                secondParameterBeginningIndex));
        if (pageNumber < 0 || pageNumber > 715) {
            throw new IllegalArgumentException("Invalid Input: queryURL incorrect.");
        }

        String queryURLTailStringLastParameter = queryURLTailString.substring(("page=" + pageNumber).length());
        if (!queryURLTailStringLastParameter.startsWith("&api_key=")) {
            throw new IllegalArgumentException("Invalid Input: queryURL incorrect.");
        }

        String apiKeyString = queryURLTailStringLastParameter.substring("&api_key=".length());
        if (!apiKeyString.equals(API_KEY)) {
            throw new IllegalArgumentException("Invalid Input: queryURL incorrect.");
        }

        try {
            URL getRequest = new URL(queryURL);
            JSONTokener tokener = new JSONTokener(getRequest.openStream());
            JSONObject root = new JSONObject(tokener);
            JSONArray array = root.getJSONArray("near_earth_objects");

            clear();
            int duplicateCount = 0;
            for (int index = 0; index < array.length(); index++) {
                JSONObject object = (JSONObject) array.get(index);

                int referenceId = Integer.parseInt((String) object.get("neo_reference_id"));
                String name = (String) object.get("name");
                double absoluteMagnitude = ((BigDecimal) object.get("absolute_magnitude_h")).doubleValue();
                double minDiameter = ((BigDecimal) object.getJSONObject("estimated_diameter").
                        getJSONObject("kilometers").get("estimated_diameter_min")).doubleValue();
                double maxDiameter = ((BigDecimal) object.getJSONObject("estimated_diameter").
                        getJSONObject("kilometers").get("estimated_diameter_max")).doubleValue();
                boolean isDangerous = (Boolean) object.get("is_potentially_hazardous_asteroid");

                JSONArray closeApproachData = object.getJSONArray("close_approach_data");
                long closestDateTimestamp = (Long) closeApproachData.getJSONObject(0).get("epoch_date_close_approach");
                double missDistance = Double.parseDouble((String) closeApproachData.getJSONObject(0).
                        getJSONObject("miss_distance").get("kilometers"));
                String orbitingBody = (String) closeApproachData.getJSONObject(0).get("orbiting_body");

                NearEarthObject newNeo = new NearEarthObject(referenceId, name, orbitingBody, absoluteMagnitude,
                        minDiameter, maxDiameter, missDistance, isDangerous, closestDateTimestamp);

                if (this.contains(newNeo)) {
                    duplicateCount++;
                } else {
                    this.add(newNeo);
                }
            }

            if (duplicateCount == array.length()) {
                System.out.println("Invalid Input: Indicated page already present in database." + "\n");
            }
        } catch (IOException ioException) {
            System.out.println("I/O Exception: Trace addAll method.");
        } catch (JSONException jsonException) {
            System.out.println("JSON Exception: Trace addAll method.");
            jsonException.printStackTrace();
        }
    }

    public void sort(Object object) throws IllegalArgumentException {
        if (object instanceof ReferenceIDComparator) {
            super.sort((ReferenceIDComparator) object);
        }
        else if (object instanceof DiameterComparator) {
            super.sort((DiameterComparator) object);
        }
        else if (object instanceof ApproachDateComparator) {
            super.sort((ApproachDateComparator) object);
        }
        else if (object instanceof MissDistanceComparator) {
            super.sort((MissDistanceComparator) object);
        }


    }

    public void clear() {
        super.clear();
    }

    public NearEarthObject[] getTable() throws ClassCastException {
        return castArray(new NearEarthObject[toArray().length], toArray());
    }

    @SuppressWarnings("unchecked")
    protected static <T extends C, C> T[] castArray(T[] newArray, C[] currentArray) throws ClassCastException {
        for (int index = 0; index < currentArray.length; index++) {
            newArray[index] = (T) currentArray[index];
        }

        return newArray;
    }
}
