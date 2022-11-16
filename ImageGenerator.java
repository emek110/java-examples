import java.util.LinkedList;

public class ImageGenerator implements ImageGeneratorConfigurationInterface, ImageGeneratorInterface {

    private LinkedList<LinkedList<Integer[]>> commands;
    private LinkedList<LinkedList<Integer[]>> reDo;
    boolean flag = true; // dodawanie instrukcji do historii (anulowanie przy redo)

    private boolean[][] canvas;
    private boolean[][] startCopy;

    private int maxCommands;
    private int posX = 0;
    private int posY = 0;

    ImageGenerator() {
        commands = new LinkedList<>();
        reDo = new LinkedList<>();
    }

    @Override
    public void setCanvas(boolean[][] canvas) {
        this.canvas = canvas;
        startCopy = new boolean[canvas.length][canvas[0].length];
        for (int i = 0; i < canvas.length; i++) {
            for (int i1 = 0; i1 < canvas[0].length; i1++) {
                startCopy[i][i1] = this.canvas[i][i1];
            }
        }
    }

    @Override
    public void setInitialPosition(int col, int row) {
        canvas[col][row] = true;
        posX = col;
        posY = row;

        for (int i = 0; i < canvas.length; i++) {
            for (int i1 = 0; i1 < canvas[0].length; i1++) {
                startCopy[i][i1] = this.canvas[i][i1];
            }
        }
    }

    @Override
    public void maxUndoRedoRepeatCommands(int commands) {
        maxCommands = commands;
    }

    @Override
    public void up(int steps) {
        if (posY + steps < canvas[0].length) {
            for (int k = 0; k < steps; k++) {
                canvas[posX][++posY] = true;
            }


            if (commands.size() == maxCommands) {
                commands.removeFirst();
            }

            if (flag) commands.addLast(new LinkedList<>());

            commands.getLast().add(new Integer[2]);
            Integer[] com = commands.getLast().getLast();
            com[0] = 1;
            com[1] = steps;

        }
    }

    @Override
    public void down(int steps) {
        if (posY - steps < canvas[0].length) {
            for (int k = 0; k < steps; k++) {
                canvas[posX][--posY] = true;
            }

            if (commands.size() == maxCommands) {
                commands.removeFirst();
            }

            if (flag) commands.addLast(new LinkedList<>());

            commands.getLast().add(new Integer[2]);
            Integer[] com = commands.getLast().getLast();
            com[0] = 2;
            com[1] = steps;
        }
    }

    @Override
    public void left(int steps) {
        if (posX - steps < canvas.length) {
            for (int k = 0; k < steps; k++) {
                canvas[--posX][posY] = true;
            }

            if (commands.size() == maxCommands) {
                commands.removeFirst();
            }

            if (flag) commands.addLast(new LinkedList<>());

            commands.getLast().add(new Integer[2]);
            Integer[] com = commands.getLast().getLast();
            com[0] = 3;
            com[1] = steps;
        }
    }

    @Override
    public void right(int steps) {
        if (posX + steps < canvas.length) {
            for (int k = 0; k < steps; k++) {
                canvas[++posX][posY] = true;
            }

            if (commands.size() == maxCommands) {
                commands.removeFirst();
            }

            if (flag) commands.addLast(new LinkedList<>());

            commands.getLast().add(new Integer[2]);
            Integer[] com = commands.getLast().getLast();
            com[0] = 4;
            com[1] = steps;
        }
    }

    @Override
    public void repeat(int commands) {

        flag = false;
        this.commands.addLast(new LinkedList<>());

        for (int i = this.commands.size() - commands - 1; i < this.commands.size() - 1; i++) {
            LinkedList<Integer[]> tmp = this.commands.get(i);
            for (int i1 = 0; i1 < tmp.size(); i1++) {
                Integer[] singleCommand = tmp.get(i1);
                switch (singleCommand[0]) {
                    case 1:
                        up(singleCommand[1]);
                        break;
                    case 2:
                        down(singleCommand[1]);
                        break;
                    case 3:
                        left(singleCommand[1]);
                        break;
                    case 4:
                        right(singleCommand[1]);
                        break;
                }
            }
        }

        flag = true;

    }

    @Override
    public void undo(int commands) {
        for (int i = this.commands.size() - 1; i >= this.commands.size() - commands; i--) {
            LinkedList<Integer[]> tmp = this.commands.get(i);
            for (int i1 = tmp.size() - 1; i1 >= 0; i1--) {
                Integer[] singleCommand = tmp.get(i1);
                switch (singleCommand[0]) {
                    case 1:
                        for (int k = 0; k < singleCommand[1]; k++) {
                            if (startCopy[posX][posY] == false)
                                canvas[posX][posY] = false;
                            posY -= 1;
                        }
                        break;
                    case 2:
                        for (int k = 0; k < singleCommand[1]; k++) {
                            if (startCopy[posX][posY] == false)
                                canvas[posX][posY] = false;
                            posY += 1;
                        }
                        break;
                    case 3:
                        for (int k = 0; k < singleCommand[1]; k++) {
                            if (startCopy[posX][posY] == false)
                                canvas[posX][posY] = false;
                            posX += 1;
                        }
                        break;
                    case 4:
                        for (int k = 0; k < singleCommand[1]; k++) {
                            if (startCopy[posX][posY] == false)
                                canvas[posX][posY] = false;
                            posX -= 1;
                        }
                        break;
                }
            }
        }
        for (int i = this.commands.size() - commands; i < this.commands.size(); ) {
            reDo.add(this.commands.getLast());
            this.commands.removeLast();
        }
    }

    @Override
    public void redo(int commands) {
        flag = false;
        this.commands.addLast(new LinkedList<>());

        for (int i = this.reDo.size() - 1; i >= this.reDo.size() - commands; i--) {
            LinkedList<Integer[]> tmp = this.reDo.get(i);
            for (int i1 = 0; i1 < tmp.size(); i1++) {
                Integer[] singleCommand = tmp.get(i1);
                switch (singleCommand[0]) {
                    case 1:
                        up(singleCommand[1]);
                        break;
                    case 2:
                        down(singleCommand[1]);
                        break;
                    case 3:
                        left(singleCommand[1]);
                        break;
                    case 4:
                        right(singleCommand[1]);
                        break;
                }
            }
        }

        for (int i = this.reDo.size() - commands; i < this.reDo.size(); ) {
            reDo.removeLast();
        }

        flag = true;

    }

    //up=1 down=2 left=3 right=4 repeat=5 undo=6 redo=7
}