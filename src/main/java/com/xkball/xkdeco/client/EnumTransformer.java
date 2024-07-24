package com.xkball.xkdeco.client;

import javax.annotation.Nullable;

public enum EnumTransformer {
    NONE,
    THIRD_PERSON_LEFT_HAND/* todo 未实现 */,
    THIRD_PERSON_RIGHT_HAND/* 仅物品渲染 */,
    FIRST_PERSON_LEFT_HAND/* todo 未实现 */,
    FIRST_PERSON_RIGHT_HAND/* 仅物品渲染 */,
    HEAD/* todo 未实现 */,
    GUI/* 仅物品渲染 */,
    //物品实体
    GROUND/* 仅物品渲染 */,
    //物品展示框
    FIXED/* todo 未实现 */;

    @Nullable
    public static EnumTransformer getEnumTransformer(final String name) {
        return switch (name.toLowerCase()) {
            case "third_person_left_hand" -> THIRD_PERSON_LEFT_HAND;
            case "third_person_right_hand" -> THIRD_PERSON_RIGHT_HAND;
            case "first_person_left_hand" -> FIRST_PERSON_LEFT_HAND;
            case "first_person_right_hand" -> FIRST_PERSON_RIGHT_HAND;
            case "head" -> HEAD;
            case "gui" -> GUI;
            case "ground" -> GROUND;
            case "fixed" -> FIXED;
            case "none" -> NONE;
            //防止传入错误名称也产生变换
            default -> null;
        };
    }
}
