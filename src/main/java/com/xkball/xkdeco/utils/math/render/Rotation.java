package com.xkball.xkdeco.utils.math.render;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.xkball.xkdeco.utils.exception.ModelParseException;

@Desugar
public record Rotation(float angle, Axis axis, Vec3f origin, boolean rescale) {

    public static final float scale45 = (float) Math.sqrt(2);
    public static final float scale22d5 = (float) Math.sqrt(2 - scale45);
    public static Rotation defaultRotation = new Rotation(0, Axis.X, new Vec3f(0, 0, 0), false);

    public static Rotation parseFromJson(JsonObject jsonObject) throws ModelParseException {
        var angle = jsonObject.get("angle")
            .getAsFloat();
        var axis_r = jsonObject.get("axis")
            .getAsString();
        Axis axis = switch (axis_r.toLowerCase()) {
            case "x" -> Axis.X;
            case "y" -> Axis.Y;
            case "z" -> Axis.Z;
            default -> null;
        };
        var origin = Vec3f.readFromJsonArray(
            jsonObject.get("origin")
                .getAsJsonArray());
        var rescale = jsonObject.has("rescale") && jsonObject.get("rescale")
            .getAsBoolean();
        if (axis == null) throw new ModelParseException("missing axis");
        return new Rotation(angle, axis, origin, rescale);
    }

    public static Rotation readOptional(JsonObject elementSrc) throws ModelParseException {
        if (elementSrc.has("rotation")) return Rotation.parseFromJson(
            elementSrc.get("rotation")
                .getAsJsonObject());
        else return defaultRotation;
    }

    public Transformation toTransformation() {
        var scaleV = 1f;
        if (rescale) {
            if (angle == 45f || angle == -45f) scaleV = scale45;
            else if (Math.abs(angle) == 22.5f) scaleV = scale22d5;
        }
        var sx = axis == Axis.X ? 1f : scaleV;
        var sy = axis == Axis.Y ? 1f : scaleV;
        var sz = axis == Axis.Z ? 1f : scaleV;
        return Transformation.build(new Vec3f(origin.x()/16f,origin.y()/16f,origin.z()/16f),
            new Vec3f(sx, sy, sz),
            new Vec4f(axis.vecPositive, (float) Math.toRadians(angle)));
    }
}
