import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class BinaryTree implements BinaryTreeInterface {

    Node root;

    @Override
    public boolean add(int value) {
        if(root == null) {
            root = new Node(value);
            return true;
        }

        Node previous = root;
        Node tmp = root;

        while(tmp != null){
            if(tmp.getValue() == value)
                return false;

            previous = tmp;
            if(tmp.getValue() < value) {
                tmp = tmp.getRight();
            } else {
                tmp = tmp.getLeft();
            }
        }

        if(previous.getValue() < value) {
            previous.setRight(new Node(value));
        } else {
            previous.setLeft(new Node(value));
        }
        return true;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public Optional<List<Integer>> getPathTo(int value) {

        LinkedList<Integer> lista = new LinkedList<>();
        Node tmp = getRoot();
        while(tmp != null) {
            if(tmp.getValue() == value)
                return Optional.ofNullable(lista);
            lista.addLast(tmp.getValue());

            if(tmp.getValue() < value) {
                tmp = tmp.getRight();
            } else {
                tmp = tmp.getLeft();
            }
        }

        return Optional.empty();
    }
}


class test {
    public static void main(String[] args) {
        BinaryTree drzewo = new BinaryTree();
        int[] tab = {10,12,13,8,7,8,5,4,14,19,2};
        for (int inti : tab) {
            drzewo.add(inti);
        }
        System.out.println(drzewo.getPathTo(10));

    }
}
