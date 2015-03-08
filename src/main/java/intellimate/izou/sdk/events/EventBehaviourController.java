package intellimate.izou.sdk.events;

import intellimate.izou.identification.Identification;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * This class can control the Behaviour of the the Event, like which Output-Plugin should get the Event first.
 */
public class EventBehaviourController implements intellimate.izou.events.EventBehaviourController {
    private Function<List<Identification>, HashMap<Integer, List<Identification>>> outputPluginBehaviour;

    /**
     * this method sets the controls for the Output-Plugin Behaviour.
     * <p>
     * Supply a Function, which controls the OutputPlugin-Behaviour. You can set Priorities.
     * The output-plugin with the highest POSITIVE priority (in int) will be processed first. Negative priorities
     * are processed last (so outputPlugins with no priorities will be processed in between positive and negative
     * priorities)
     *
     * This function returns an HashMap, where the keys represent the associated Behaviour
     * and the values the Identification;
     * </p>
     * @param outputPluginBehaviour all the registered outputPlugins
     */
    public void controlOutputPluginBehaviour(Function<List<Identification>,
            HashMap<Integer, List<Identification>>> outputPluginBehaviour) {
        this.outputPluginBehaviour = outputPluginBehaviour;
    }

    /**
     * generates the data to control the Event
     * @param identifications the Identifications of the OutputPlugins
     * @return a HashMap, where the keys represent the associated Behaviour and the values the Identification;
     */
    @Override
    public HashMap<Integer, List<Identification>> getOutputPluginBehaviour(List<Identification> identifications) {
        if(outputPluginBehaviour == null) return new HashMap<>();
        return outputPluginBehaviour.apply(identifications);
    }
}
