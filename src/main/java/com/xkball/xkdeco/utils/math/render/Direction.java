package com.xkball.xkdeco.utils.math.render;

public enum Direction {

    NONE(Axis.X,false),
    DOWN(Axis.Y,false),
    UP(Axis.Y,true),
    NORTH(Axis.Z,false),
    SOUTH(Axis.Z,true),
    WEST(Axis.X,false),
    EAST(Axis.X,true);

    public final boolean positive;
    public final Axis axis;

    Direction(Axis axis, boolean positive) {
        this.positive = positive;
        this.axis = axis;
    }

    public static Direction readFromString(final String s) {
        return switch (s.toLowerCase()) {
            case "down" -> Direction.DOWN;
            case "up" -> Direction.UP;
            case "north" -> Direction.NORTH;
            case "south" -> Direction.SOUTH;
            case "west" -> Direction.WEST;
            case "east" -> Direction.EAST;
            default -> NONE;
        };
    }
}
