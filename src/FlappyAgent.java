public class FlappyAgent {
    public static class StateDTO {
        public int birdPos;
        public int birdSpeed;
        public int objectType;
        public int objectDistance;
        public int objectHeight;

        public StateDTO(int birdPos, int birdSpeed, int objectType, int objectDistance, int objectHeight) {
            this.birdPos = birdPos;
            this.birdSpeed = birdSpeed;
            this.objectType = objectType;
            this.objectDistance = objectDistance;
            this.objectHeight = objectHeight;
        }

        @Override
        public String toString() {
            return "StateDTO{" +
                    "birdPos=" + birdPos +
                    ", birdSpeed=" + birdSpeed +
                    ", objectType=" + objectType +
                    ", objectDistance=" + objectDistance +
                    ", objectHeight=" + objectHeight +
                    '}';
        }
    }
    public static class QTable implements java.io.Serializable {
        public double[][][][][][] table;

        public QTable() {

        }

        public QTable(int[] stateSpaceSize, int actionDimension) {
            table = new double[stateSpaceSize[0]][stateSpaceSize[1]][stateSpaceSize[2]][stateSpaceSize[3]][stateSpaceSize[4]][actionDimension];
        }

        public double[] getActions(StateDTO state) {
            return table[state.birdPos][state.birdSpeed][state.objectType][state.objectDistance][state.objectHeight];
        }

        public QTable copy() {
            QTable res = new QTable();
            res.table = this.table.clone();
            return res;
        }
    }

    QTable qTable;
    int[] actionSpace;
    int nIterations;
    double alpha = 0.1;
    double gamma = 0.5;
    boolean test = false;

    public FlappyAgent(int[] observationSpaceSize, int[] actionSpace, int nIterations) {
        this.qTable = new QTable(observationSpaceSize,actionSpace.length);
        this.actionSpace = actionSpace;
        this.nIterations = nIterations;
    }

    public int step(StateDTO state) {
        return findMax(state);
    }

    private int findMax(StateDTO state) {
        double[] templ =  qTable.getActions(state);
        double max = -1;
        int maxIndex = 0;
        for(int ii = 0; ii < templ.length; ii++){
            if(templ[ii] > max){
                maxIndex = ii;
                max = templ[ii];
            }
        }
        return maxIndex;
    }

    public void epochEnd(int epochRewardSum) {

    }

    public void learn(StateDTO oldState, int action, StateDTO newState, double reward) {
        qTable.getActions(oldState)[action] = qTable.getActions(oldState)[action] + alpha * (reward + gamma * qTable.getActions(newState)[calc(newState)] - qTable.getActions(oldState)[action]);
    }

    public int calc(StateDTO newState){
        return findMax(newState);
    }

    public void trainEnd() {
        test = true;
    }
}
