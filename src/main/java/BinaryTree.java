import java.util.Arrays;
import java.util.Scanner;

public class BinaryTree {
    public static void main(String[] args) {
        Tree tree = new Tree();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(3);
        tree.add(7);
        tree.add(12);
        tree.add(16);
        tree.add(4);
        tree.add(2);
        tree.add(1);
        tree.info();
        tree.outputWidht();
        System.out.println(tree.delete(1, true));
        tree.info();
        tree.outputWidht();

    }
}

class Tree {

    private Node root;//begin of tree

    private int[] levels;//information of number of levels and their fullness

    Tree() {
        levels = new int[10];
    }

    static Tree ConsoleInputeTree() {
        Tree tree = new Tree();
        System.out.println("Enter values of nodes of three");
        System.out.println("For exit enter [7777]");
        Scanner scanner = new Scanner(System.in);
        int input=0;
        while (input!=7777) {
            input=scanner.nextInt();
            tree.add((int)input);
        }
        return tree;
    }

    public boolean delete(int x, boolean update) {
        if (root == null)
            return false;//tree is empty and can not contain any node
        if (update) {
            TraversalTreeWidth traversalTreeWidth = new TraversalTreeWidth() {
                @Override
                void doAction(Node node, boolean isLevel) {
                    if (isLevel)
                        return;
                    addToValues(node.getValue());
                }
            };
            traversalTreeWidth.travesal(root, getDepth());
            levels = new int[10];
            int array[] = traversalTreeWidth.getValues();
            int initialLength = array.length;
            Arrays.sort(array);
            array = Arrays.stream(array).filter(y -> y != x).toArray();
            if (initialLength == array.length) {
                System.out.println("Node : " + x + " did not found");
                return false;
            }
            root = reorganize(null, array, 0);
            return true;
        }
        if (x == root.getValue())
            System.out.println("Node : " + x + "has son or sons and can not be deleted");
        else if (x < root.getValue())
            return deleteNode(x, root, 0, Sign.MINUS);
        else
            return deleteNode(x, root, 0, Sign.PULS);
        return true;
    }

    private boolean deleteNode(int x, Node node, int depth, Sign sign) {
        if (node == null) {
            System.out.println("Node did not found");
            return false;
        }
        if (x == node.getValue()) {
            if ((node.getlSon() != null) || (node.getrSon() != null)) {
                System.out.println("Node : " + x + " has son or sons and can not be deleted");
                return false;
            } else {
                levelUpdate(depth, Sign.MINUS);
                if (sign == Sign.MINUS)
                    node.getParent().setlSon(null);
                else if (sign == Sign.PULS)
                    node.getParent().setrSon(null);
                return true;
            }
        } else if (x < node.getValue())
            return deleteNode(x, node.getlSon(), ++depth, Sign.MINUS);
        else
            return deleteNode(x, node.getrSon(), ++depth, Sign.PULS);
    }

    public void info() {
        System.out.println("Count of elements" + Arrays.toString(levels));
        System.out.println("Depth of tree " + getDepth() + "\n\n");
    }

    public void update() {
        TraversalTreeWidth traversalTreeWidth = new TraversalTreeWidth() {
            @Override
            void doAction(Node node, boolean isLevel) {
                if (isLevel)
                    return;
                addToValues(node.getValue());
            }
        };
        traversalTreeWidth.travesal(root, getDepth());
        levels = new int[10];
        int array[] = traversalTreeWidth.getValues();
        Arrays.sort(array);
        root = reorganize(null, array, 0);
    }

    // for first calling parent  = null, depth = 0
    private Node reorganize(Node parent, int[] array, int depth) {
        if (array.length == 0)
            return null;
        if (array.length == 1) {
            levels[depth]++;
            //System.out.println("value: "+array[0]+"\tdepth: "+depth);
            return new Node(array[0], parent);
        }
        int center = Math.floorDiv(array.length, 2);
        int rootValue = array[center];
        Node node = new Node(rootValue, parent);
        //System.out.println("value: "+node.getValue()+"\tdepth: "+depth);
        Node left = reorganize(node, Arrays.copyOf(array, center), depth + 1);
        Node right = reorganize(node, Arrays.copyOfRange(array, center + 1, array.length), depth + 1);
        node.setlSon(left);
        node.setrSon(right);
        levels[depth]++;
        return node;
    }

    public void outputWidht() {
        TraversalTreeWidth output = new TraversalTreeWidth() {
            @Override
            void doAction(Node node, boolean isLevel) {
                if (isLevel) {
                    System.out.println();
                    return;
                }
                System.out.print(node.getValue() + "\t\t");
                addToValues(node.getValue());
            }
        };
        output.travesal(root, getDepth());
        System.out.println();
        //System.out.println("\n\n"+Arrays.toString(output2.getValues()));
    }

    private boolean[] toBooleanArray(int x, int num) {
        boolean result[] = new boolean[num];
        for (int i = 0; i < num; i++) {
            int tmp = (int) Math.pow(2, num - i - 1);
            result[i] = x >= tmp;
            if (result[i])
                x -= tmp;
        }
        return result;
    }

    private void printSpaces(int numberSymbols) {
        int widght = 4 * getDepth() / (numberSymbols + 1);//((int) Math.pow(2, getDepth())+6*(getDepth()-1))/(numberSymbols+1);
        for (int j = 0; j < widght; j++)
            System.out.print(" ");
    }

    private void printInOrder(boolean[] order) {
        Node node = root;
        if (order == null) {
            printSpaces(0);
            System.out.println(node.getValue());
            return;
        }
        for (int i = 0; i < order.length; i++) {
            if (!(order[i]) && (node.getlSon() != null))
                node = node.getlSon();
            else if ((order[i]) && (node.getrSon() != null))
                node = node.getrSon();
            else {
                printSpaces(order.length);
                System.out.print('x');
                return;
            }
        }
        printSpaces(order.length);
        System.out.print(node.getValue());
    }

    private int getDepth() {
        int i = 0;
        while (levels[i] != 0)
            i++;
        return i;
    }

    private void levelUpdate(int level, Sign sign) {
        if (sign == Sign.PULS)
            levels[level]++;
        else if (sign == Sign.MINUS)
            levels[level]--;
    }

    public void add(int x) {
        if (root == null) { // set value, if root doesn't exist
            root = new Node(x, null);
            levels[0] = 1;
            return;
        }
        if (x == root.getValue())//end
            return;
        add(x, root, 1);
    }

    private void add(int x, Node parent, int depth) {
        if (x < parent.getValue()) {
            if (parent.getlSon() == null) {
                Node lSon = new Node(x, parent);
                parent.setlSon(lSon);
                levelUpdate(depth, Sign.PULS);
                return;
            } else if (x == parent.getlSon().getValue())
                return;
            else {
                add(x, parent.getlSon(), depth + 1);
                return;
            }
        } else if (parent.getrSon() == null) {
            Node rSon = new Node(x, parent);
            parent.setrSon(rSon);
            levelUpdate(depth, Sign.PULS);
            return;
        } else if (x == parent.getrSon().getValue())
            return;
        else {
            add(x, parent.getrSon(), depth + 1);
            return;
        }

    }

    private enum Sign {PULS, MINUS}

}

class Node {
    private Node parent;
    private Node lSon;
    private Node rSon;
    private int value;


    public Node(int value, Node parent) {
        this.value = value;
        this.parent = parent;
    }

    public int getValue() {
        return value;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setlSon(Node lSon) {
        this.lSon = lSon;
    }

    public void setrSon(Node rSon) {
        this.rSon = rSon;
    }

    public Node getlSon() {
        return lSon;
    }

    public Node getrSon() {
        return rSon;
    }

    public Node getParent() {
        return parent;
    }
}

abstract class TraversalTree {
    private Node root;

    public TraversalTree(Node root) {
        this.root = root;
    }

    public void traversal(int depth) {
        if (root != null)
            inOrder(null);
        else
            return;
        for (int i = 1; i < depth; i++) {
            boolean order[];
            for (int number = 0; number < Math.pow(2, i); number++) {
                order = toBooleanArray(number, i);
                inOrder(order);
            }
        }
    }

    private boolean[] toBooleanArray(int x, int num) {
        boolean result[] = new boolean[num];
        for (int i = 0; i < num; i++) {
            int tmp = (int) Math.pow(2, num - i - 1);
            result[i] = x >= tmp;
            if (result[i])
                x -= tmp;
        }
        return result;
    }

    private void inOrder(boolean[] order) {
        Node node = root;
        if (order == null) {
            doAction(node, false);
            return;
        }
        for (int i = 0; i < order.length; i++) {
            if (!(order[i]) && (node.getlSon() != null))
                node = node.getlSon();
            else if ((order[i]) && (node.getrSon() != null))
                node = node.getrSon();
            else {
                return;
            }
        }
        doAction(node, false);
    }

    abstract void doAction(Node node, boolean isLevel);

}

abstract class TraversalTreeWidth {

    private int[] values;
    private int number = 0;

    public void addToValues(int x) {
        values[number++] = x;
    }

    public TraversalTreeWidth() {
        values = new int[20];
    }

    public int[] getValues() {
        return Arrays.copyOf(values, number);
    }

    private void traversalDepth(Node node, int depth) {
        if (node == null)
            return;
        if (node.getlSon() != null) {
            node = node.getlSon();
            if (depth == 0)
                doAction(node, false);
            else
                traversalDepth(node, depth - 1);
            node = node.getParent();
        }
        if (node.getrSon() != null) {
            node = node.getrSon();
            if (depth == 0)
                doAction(node, false);
            else
                traversalDepth(node, depth - 1);
        }
        return;
    }

    public int[] travesal(Node node, int depth) {
        if (node != null)
            doAction(node, false);
        else
            return values;
        for (int i = 0; i < depth; i++) {
            doAction(null, true);
            traversalDepth(node, i);
        }
        return values;
    }

    abstract void doAction(Node node, boolean isLevel);
    //isLevel using for special action in case transition to next level
}
