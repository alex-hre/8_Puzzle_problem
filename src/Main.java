import java.io.*;
import java.util.*;

// To measure memory and time, uncomment lines No.: 228, 230, 233, 240, 243, 244
// To make time and memory measurements more correct, comment out line No. 160

class EightPuzzle {

    int[][] goalState = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}}; // Target state

    int changer;

    public EightPuzzle(int data) {
        changer = data;
    }

    // Function for estimating the distance from the current state to the target state

    int heuristic(int[][] currentState) {

        int count = 0;

        switch (changer){
            case (1):
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int value = currentState[i][j];
                        if (value != 0) {
                            int goalRow = (value - 1) / 3;
                            int goalCol = (value - 1) % 3;
                            int distance = Math.abs(i - goalRow) + Math.abs(j - goalCol);
                            count += distance;
                        }
                    }
                }

            case (0):
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (currentState[i][j] != goalState[i][j] && currentState[i][j] != 0) {
                            count++;
                        }
                    }
                }

        }

        return count;
    }


    // Search function
    void greedySearch(int[][] initialState) {
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.heuristicValue));
        Set<String> visited = new HashSet<>();

        Node initialNode = new Node(initialState, 0, heuristic(initialState), null);
        queue.add(initialNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (Arrays.deepEquals(currentNode.state, goalState)) {
                // We have reached the target state
                // currentNode contains the path to the target
                printSolution(currentNode);
                return;
            }

            visited.add(Arrays.deepToString(currentNode.state));

            // Generating possible actions
            int[][] currentState = currentNode.state;

            // Let's find an empty cell
            int row = -1, col = -1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (currentState[i][j] == 0) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            // left(if possible)
            if (col > 0) { // Checking lateral boundaries
                int[][] newState = cloneState(currentState);
                newState[row][col] = newState[row][col - 1];
                newState[row][col - 1] = 0;
                int hValue = heuristic(newState);
                Node newNode = new Node(newState, currentNode.cost + 1, hValue, currentNode);
                if (!visited.contains(Arrays.deepToString(newNode.state))) {
                    queue.add(newNode);
                }
            }


            // right (if possible)
            if (col < 2) {
                int[][] newState = cloneState(currentState);
                newState[row][col] = newState[row][col + 1];
                newState[row][col + 1] = 0;
                int hValue = heuristic(newState);
                Node newNode = new Node(newState, currentNode.cost + 1, hValue, currentNode);
                if (!visited.contains(Arrays.deepToString(newNode.state))) {
                    queue.add(newNode);
                }
            }

            // down (if possible)
            if (row < 2) {
                int[][] newState = cloneState(currentState);
                newState[row][col] = newState[row + 1][col];
                newState[row + 1][col] = 0;
                int hValue = heuristic(newState);
                Node newNode = new Node(newState, currentNode.cost + 1, hValue, currentNode);
                if (!visited.contains(Arrays.deepToString(newNode.state))) {
                    queue.add(newNode);
                }
            }

            // up (if possible)
            if (row > 0) {
                int[][] newState = cloneState(currentState);
                newState[row][col] = newState[row - 1][col];
                newState[row - 1][col] = 0;
                int hValue = heuristic(newState);
                Node newNode = new Node(newState, currentNode.cost + 1, hValue, currentNode);
                if (!visited.contains(Arrays.deepToString(newNode.state))) {
                    queue.add(newNode);
                }
            }


        }

        System.out.println("Solution not found");
    }

    // Function for cloning state
    int[][] cloneState(int[][] state) {
        int[][] newState = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newState[i][j] = state[i][j];
            }
        }
        return newState;
    }

    // Output
    void printSolution(Node node) {
        int count = 0;
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        for (Node n : path) {
            printState(n.state);
            count++;
        }
        System.out.println("Amount of steps:" + count);
    }

    // Display
    void printState(int[][] state) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

class Node {
    int[][] state;
    int cost;
    int heuristicValue;
    Node parent;

    Node(int[][] state, int cost, int heuristicValue, Node parent) {
        this.state = state;
        this.cost = cost;
        this.heuristicValue = heuristicValue;
        this.parent = parent;
    }
}

public class Main {
    public static void main(String[] args) {


        System.out.println("Chose heuristic: 0 - mismatched tiles(Heur. #1), 1 - Manhattan distance(Heur. #2)");

        Scanner scanner1 = new Scanner(System.in);
        int data = scanner1.nextInt();
        while (data != 0 && data != 1)
        {
            data = scanner1.nextInt();
        }

        EightPuzzle puzzleSolver = new EightPuzzle(data);

        try {
            File file = new File("C:\\Users\\asususer\\IdeaProjects\\UI_Z1_d\\src\\configurations.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                int[][] initialState = new int[3][3];

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (scanner.hasNextInt()) {
                            initialState[i][j] = scanner.nextInt();
                        }
                    }
                }

                scanner.nextLine();

                /////////////////////////////////////
                //Runtime runtime = Runtime.getRuntime();
                // Run the garbage collector
                //runtime.gc();

                // Time counting
                //long c = System.currentTimeMillis();

                /////
                puzzleSolver.greedySearch(initialState);
                /////

                // Print time in millis
                //System.out.println("Used time in millis: " + (double) (System.currentTimeMillis() - c));

                // Calculate the used memory
                //long memory = runtime.totalMemory() - runtime.freeMemory();
                //System.out.println("Used memory is bytes: " + memory);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}


