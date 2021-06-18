package matcherapp.domain;

import matcherapp.utils.StateList;


/**
 * Represents a fragment of NFA: has one input states and possibly several output states.
 */
public class Fragment {

    private State input;
    private StateList outputs = new StateList();

    /**
     * Constructor for new fragment, takes state as a parameter.
     * @param state State to be added as input and output.
     */
    public Fragment(State state) {
        this.input = state;
        outputs.add(state);
    }

    /**
     * Adds state given as parameter to be one output state of this fragment.
     * @param state State to be added as output state.
     */
    public void pushOutput(State state) {
        outputs.add(state);
    }

    /**
     * Sets given state as a output state to all states in the output list in this fragment.
     * @param out State to be added as a output to all output states in this fragment.
     */
    public void setOutputStates(State out) {
        for (State s : outputs.getAll()) {
            s.setOut(out);
        }
    }

    /**
     * Sets input state of this fragment to one given as a parameter.
     * @param input State to be set as a input state of this fragment.
     */
    public void setInput(State input) {
        this.input = input;
    }

    /**
     * Returns input state of this fragment.
     * @return Input state of this fragment.
     */
    public State getInput() {
        return input;
    }

    /**
     * Returns StateList object containing all output States of this fragment.
     * @return StateList object containing all output States of this fragment.
     */
    public StateList getOutputs() {
        return outputs;
    }

}
