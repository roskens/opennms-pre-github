package org.opennms.vaadin.applicationstack.provider;

/**
 * The {@linkplain HealthCalculator} calculates the health for each layer.
 * 
 * @author marskuh
 *
 */
// TODO define as Interface?
// TODO implement
public class HealthCalculator {
	 // TODO implement computeGood
    public float computeGood() {
        int good = 0;
        return 0;
//        int sum = getSum();
//        for (NodeDummy eachNode : nodes) {
//            good += eachNode.getGood();
//        }
//        return good == 0 || sum == 0 ? 0 : (float) 100 / (float) sum * (float) good;
    }

    // TODO implement computeDeath
    public float computeDeath() {
        int death = 0;
        return 0;
//        int sum = getSum();
//        for (NodeDummy eachNode : nodes) {
//            death += eachNode.getDeath();
//        }
//        return death == 0 || sum == 0 ? 0 : (float) 100 / (float) sum * (float) death;
    }

    // TODO implement computeProblems
    public float computeProblems() {
        int problem = 0;
        return 0;
//        int sum = getSum();
//        for (NodeDummy eachNode : nodes) {
//            problem += eachNode.getProblems();
//        }
//        return problem == 0 || sum == 0 ? 0 : (float) 100 / (float) sum * (float) problem;
    }

    public int getSum() {
        int sum = 0;
//        for (NodeDummy eachNode : nodes) {
//            sum += eachNode.getSum();
//        }
        return sum;
    }

}
