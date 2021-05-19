package matcherapp.domain;

import java.util.ArrayList;

/**
 * Represents a fragment of NFA: has one input and possibly several outputs.
 */
public class Fragment {

    private State input;
    private ArrayList<State> outputs = new ArrayList<>();

    public Fragment(State state) {
        this.input = state;
        outputs.add(state);
    }

    public void pushOutput(State s) {
        outputs.add(s);
    }

    public void setOutputStates(State out) {
        for (State s : outputs) {
            s.setOut(out);
        }
    }

    public void setInput(State input) {
        this.input = input;
    }

    public State getInput() {
        return input;
    }

    public ArrayList<State> getOutputs() {
        return outputs;
    }

}
