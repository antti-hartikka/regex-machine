package matcherapp.domain;

import matcherapp.utils.StateList;


/**
 * Represents a fragment of NFA: has one input and possibly several outputs.
 */
public class Fragment {

    private State input;
    private StateList outputs = new StateList();

    public Fragment(State state) {
        this.input = state;
        outputs.add(state);
    }

    public void pushOutput(State s) {
        outputs.add(s);
    }

    public void setOutputStates(State out) {
        for (State s : outputs.getAll()) {
            s.setOut(out);
        }
    }

    public void setInput(State input) {
        this.input = input;
    }

    public State getInput() {
        return input;
    }

    public StateList getOutputs() {
        return outputs;
    }

}
