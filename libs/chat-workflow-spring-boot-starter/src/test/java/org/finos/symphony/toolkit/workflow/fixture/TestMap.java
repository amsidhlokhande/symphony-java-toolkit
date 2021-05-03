package org.finos.symphony.toolkit.workflow.fixture;

import org.finos.symphony.toolkit.workflow.java.Exposed;
import org.finos.symphony.toolkit.workflow.java.Work;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Work(name = "TEst Form having Map Template", instructions = "map-template")
public class TestMap {
    private String mapName;
    private Map<String, String> stateCapitals;

    public TestMap() {
    }

    public TestMap(String mapName, Map<String, String> stateCapitals) {
        this.mapName = mapName;
        this.stateCapitals = stateCapitals;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public Map<String, String> getStateCapitals() {
        return stateCapitals;
    }

    public void setStateCapitals(Map<String, String> stateCapitals) {
        this.stateCapitals = stateCapitals;
    }

    @Exposed
    public TestMap viewmap() {
        Map<String, String> stateCapitals = new HashMap<>();
        stateCapitals.put("MH", "Mumbai");
        stateCapitals.put("DEL", "Delhi");
        stateCapitals.put("KA", "Bangalore");

        TestMap testMap = new TestMap("State-Capitals", stateCapitals);
        return testMap;
    }

    @Exposed
    public TestMap addstatecapital(StateCapital stateCapital) {
        if (StringUtils.hasText(stateCapital.getState()) && StringUtils.hasText(stateCapital.getCapital())) {
            if (null != this) {
                this.getStateCapitals().put(stateCapital.getState(), stateCapital.getCapital());
                return this;
            } else {
                Map<String, String> stateCapitals = new HashMap<>();
                stateCapitals.put("MH", "Mumbai");
                stateCapitals.put("DEL", "Delhi");
                stateCapitals.put("KA", "Bangalore");
                stateCapitals.put(stateCapital.getState(), stateCapital.getCapital());
                TestMap testMap = new TestMap("State-Capitals", stateCapitals);
                return testMap;
            }
        } else {
            throw new RuntimeException("State or Capital should not ber null");
        }
    }

    @Exposed
    public TestMap removestatecapital(StateCapital stateCapital) {
        if (StringUtils.hasText(stateCapital.getState()) && StringUtils.hasText(stateCapital.getCapital())) {
            if (null != this) {
                this.getStateCapitals().remove(stateCapital.getState());
                return this;
            } else {
                throw new RuntimeException("No State found with state " + stateCapital.getState());
            }
        } else {
            throw new RuntimeException("State or Capital should not ber null");
        }
    }
}
