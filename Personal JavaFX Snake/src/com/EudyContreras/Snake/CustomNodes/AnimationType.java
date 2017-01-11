package com.EudyContreras.Snake.CustomNodes;


public enum AnimationType {
    FADE_OUT,
    FLAP_RIGHT,
    POP_AND_SPAND,
    FLY_FROM_DOWN,
    FLY_FROM_UP,
    ROTATE_RIGHT,
    SPEED_LEFT,
    SPEED_RIGHT,
    TRANSITION_DOWN,
    TRANSITION_LEFT,
    TRANSITION_RIGHT,
    TRANSITION_TOP,
    ZOOM_IN,
    POP_OUT,
    NONE;

    public String getName() {
        return toString();
    }
}
