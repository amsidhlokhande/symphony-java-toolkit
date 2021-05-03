package org.finos.symphony.toolkit.workflow.fixture;

import org.finos.symphony.toolkit.workflow.java.Work;

@Work
public class StateCapital {
    private String state;
    private String capital;

    public StateCapital() {
    }

    public StateCapital(String state, String capital) {
        this.state = state;
        this.capital = capital;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }
}
