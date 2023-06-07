public class LeftistHeap{
    public class Node {
        Term data;
        int npd;

        Node left;
        Node right;

        // General Node Constructor
        public Node(Term data, Node left, Node right){
            this.data = data;
            this.left = left;
            this.right = right;
            this.npd = 0;
        }

        // Single Element Node Constructor
        public Node(Term data) {
            this(data, null, null);
        }
    }

    private Node root;

    // Constructor for heap
    public LeftistHeap() {
        root = null;
    }

    //Checks if heap is empty
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Helper function for merging the possibly diverging heaps
     * @param rightHeap Heap to be merged into root
     */
    public void merge(LeftistHeap rightHeap) {
        if (this == rightHeap){
            return;
        }

        root = merge(root, rightHeap.root);
        rightHeap.root = null;
    }

    /**
     * Helper function for merging 2 diverging heaps
     * @param node1 root of left heap
     * @param node2 root of right heap
     * @return newly merged "single" heap root
     */
    public Node merge(Node node1, Node node2){

        // Checking for nulls in either of the nodes
        if (node1 == null) { return node2; }
        if (node2 == null) { return node1; }

        // Deciding if node belongs on right or left
        if (node1.data.compareTo(node2.data) < 0) {

            Node tempNode = node1;
            node1 = node2;
            node2 = tempNode;
        }

        node1.right = merge(node1.right, node2);

        // If the left of the node is null, swap the right and left children
        if (node1.left == null) {
            node1.left = node1.right;
            node1.right = null;
        } else {
            // Else, check the null pointer distances between left and right children of left node
            if (node1.left.npd < node1.right.npd) {
                Node tempNode = node1.left;
                node1.left = node1.right;
                node1.right = tempNode;
            }

            node1.npd = node1.right.npd + 1;
        }
        return node1;
    }

    // Node Insertion function
    public void insert(Term term) {
        root = merge(new Node(term), root);
    }

    /**
     * Returns the root (the max) and re-merges the tree.
     * @return Node w/ max word occurrence value
     */
    public Term deleteMax() {
        if (isEmpty()) { return null; }

        Term maxTerm = root.data;

        root = merge(root.left, root.right);
        return maxTerm;
    }

    /**
     * Return a string displaying the tree contents as an indented heap.
     */
    public String toString() {

        String heapString = "";
        int nodeDepth = 0;
        heapString += inOrderReverseParser(root, nodeDepth);
        return heapString;
    }

    /**
     *
     * @param node The node to continue from
     * @param nodeDepth The current depth of the node in the heap
     * @return The string to be sent to the toString function
     */
    private String inOrderReverseParser(Node node, int nodeDepth){

        String heapString = "";
        if (node == null) { return ""; }

        heapString += inOrderReverseParser(node.right, nodeDepth + 1);

        // Include indentation processor for each progressive node depth
        heapString += "  ".repeat(Math.max(0, nodeDepth)) + node.data.toString();


        heapString += inOrderReverseParser(node.left, nodeDepth + 1);

        return heapString;
    }
}