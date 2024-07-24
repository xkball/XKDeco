package com.xkball.xkdeco.utils.math.render;

import net.minecraft.util.EnumFacing;

public enum EnumDirection {

    DOWN(Axis.Y,false, EnumFacing.DOWN),
    UP(Axis.Y,true, EnumFacing.UP),
    NORTH(Axis.Z,false, EnumFacing.NORTH),
    SOUTH(Axis.Z,true, EnumFacing.SOUTH),
    EAST(Axis.X,true, EnumFacing.EAST),
    WEST(Axis.X,false, EnumFacing.WEST);

    public final boolean positive;
    public final Axis axis;
    public final EnumFacing facing;

    EnumDirection(Axis axis, boolean positive, EnumFacing facing) {
        this.positive = positive;
        this.axis = axis;
        this.facing = facing;
    }

    public Vec3f toVec3f(){
        return this.positive ? this.axis.vecPositive : this.axis.vecNegative;
    }

    public static EnumDirection readFromString(final String s) {
        return switch (s.toLowerCase()) {
            case "down" -> EnumDirection.DOWN;
            case "up" -> EnumDirection.UP;
            case "north" -> EnumDirection.NORTH;
            case "south" -> EnumDirection.SOUTH;
            case "west" -> EnumDirection.WEST;
            case "east" -> EnumDirection.EAST;
            default -> null;
        };
    }
}
