package com.projects.vimal.lightcolorchange_poc;

import java.util.List;

/**
 * Created by vimal on 12/8/18.
 */

public class GroupLight {
    private List<Light> states;

    public GroupLight(List<Light> states) {
        setStates(states);
    }

    public List<Light> getStates() {
        return states;
    }

    public void setStates(List<Light> states) {
        this.states = states;
    }
}
