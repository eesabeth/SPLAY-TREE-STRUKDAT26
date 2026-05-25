import java.util.Scanner;

class Node {
    int key;
    Node left;
    Node right;

    public Node(int key) {
        this.key = key;
        this.left = null;
        this.right = null;
    }
}

class TopDownSplayTree {
    Node root;

    public TopDownSplayTree() {
        this.root = null;
    }

    private Node topDownSplay(Node currentRoot, int targetKey) {
        if (currentRoot == null) {
            return null;
        }

        Node helper = new Node(0); 
        Node leftTreeMax = helper;  
        Node rightTreeMin = helper; 
        Node current = currentRoot;

        while (true) {
            if (targetKey < current.key) {
                if (current.left == null) break;

                if (targetKey < current.left.key) {
                    Node nextNode = current.left;
                    current.left = nextNode.right;
                    nextNode.right = current;
                    current = nextNode;
                    if (current.left == null) break;
                }

                rightTreeMin.left = current;
                rightTreeMin = current; 
                current = current.left; 
            } 
            else if (targetKey > current.key) {
                if (current.right == null) break;

                if (targetKey > current.right.key) {
                    Node nextNode = current.right;
                    current.right = nextNode.left;
                    nextNode.left = current;
                    current = nextNode;
                    if (current.right == null) break;
                }

                leftTreeMax.right = current;
                leftTreeMax = current;  
                current = current.right; 
            } 
            else {
                break; 
            }
        }

        leftTreeMax.right = current.left;
        rightTreeMin.left = current.right;
        current.left = helper.right;  
        current.right = helper.left;  

        return current;
    }

    public void insert(int key) {
        if (root == null) {
            root = new Node(key);
            return;
        }

        root = topDownSplay(root, key);

        if (root.key == key) return;

        Node newNode = new Node(key);
        if (key < root.key) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        root = newNode;
    }

    public boolean search(int key) {
        if (root == null) return false;

        root = topDownSplay(root, key);

        return root.key == key;
    }

    public void printVisual(Node node, String prefix, boolean isLeft, boolean isRoot) {
        if (node != null) {
            System.out.print(prefix);
            if (!isRoot) {
                System.out.print(isLeft ? "|-- Kiri  : " : "\\-- Kanan : ");
            } else {
                System.out.print("Root : ");
            }
            System.out.println(node.key);
            
            printVisual(node.left, prefix + (isLeft ? "|   " : "    "), true, false);
            printVisual(node.right, prefix + (isLeft ? "|   " : "    "), false, false);
        }
    }
}

public class MainTDST {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TopDownSplayTree tree = new TopDownSplayTree();
        boolean jalan = true;

        System.out.println("TOP-DOWN SPLAY TREE");
        
        while (jalan) {
            System.out.println("\nMenu:");
            System.out.println("1. Masukkan (Insert) Angka");
            System.out.println("2. Cari (Search) Angka");
            System.out.println("3. Cetak Bentuk Tree Saat Ini");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu (1/2/3/4): ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Input tidak valid!");
                scanner.next();
                continue;
            }
            
            int pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    System.out.print("Angka yang ingin dimasukkan: ");
                    int insertVal = scanner.nextInt();
                    tree.insert(insertVal);
                    System.out.println(">> Angka " + insertVal + " dimasukkan secara Top-Down.");
                    System.out.println("Struktur Tree Baru:");
                    tree.printVisual(tree.root, "", false, true);
                    break;
                case 2:
                    System.out.print("Angka yang ingin dicari: ");
                    int searchVal = scanner.nextInt();
                    boolean found = tree.search(searchVal);
                    if (found) {
                        System.out.println(">> Angka " + searchVal + " DITEMUKAN dan naik ke Root.");
                    } else {
                        System.out.println(">> Angka " + searchVal + " TIDAK DITEMUKAN. Node terdekat naik ke Root.");
                    }
                    System.out.println("Struktur Tree Baru:");
                    tree.printVisual(tree.root, "", false, true);
                    break;
                case 3:
                    System.out.println("Struktur Tree Saat Ini:");
                    if (tree.root == null) {
                        System.out.println("(Tree masih kosong)");
                    } else {
                        tree.printVisual(tree.root, "", false, true);
                    }
                    break;
                case 4:
                    System.out.println("Keluar dari program...");
                    jalan = false;
                    break;
                default:
                    System.out.println("Pilihan tidak ada!");
            }
        }
        scanner.close();
    }
}