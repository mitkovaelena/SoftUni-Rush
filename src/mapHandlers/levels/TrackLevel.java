package mapHandlers.levels;

import constants.MapConstants;
import constants.ResourcesConstants;

public enum TrackLevel {
    FIRST_LEVEL(MapConstants.FIRST_LEVEL_DRUNK_DRIVERS,
            ResourcesConstants.TRACK_BACKGROUND_PATH,
            MapConstants.FIRST_LEVEL_MIN_X,
            MapConstants.FIRST_LEVEL_MAX_X),
    SECOND_LEVEL(MapConstants.SECOND_LEVEL_DRUNK_DRIVERS,
            ResourcesConstants.SECOND_TRACK_BACKGROUND_PATH,
            MapConstants.SECOND_LEVEL_MIN_X,
            MapConstants.SECOND_LEVEL_MAX_X);

    private int value;
    private String path;
    private int minLeftSide;
    private int maxRightSide;

    TrackLevel(int value, String path, int minLeftSide, int maxRightSide) {
        this.value = value;
        this.path = path;
        this.minLeftSide = minLeftSide;
        this.maxRightSide = maxRightSide;
    }

    public int getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }

    public int getMinLeftSide() {
        return minLeftSide;
    }

    public int getMaxRightSide() {
        return maxRightSide;
    }
}