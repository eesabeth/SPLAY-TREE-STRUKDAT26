import java.util.Scanner;

// Struktur Node
class SplayTreeNode {
    int key;
    SplayTreeNode left, right, parent;

    public SplayTreeNode(int key) {
        this.key = key;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}

// Logika Splay Tree
class SplayTree {
    SplayTreeNode root;

    public SplayTree() {
        this.root = null;
    }

    // Rotasi Zig
    private void zig(SplayTreeNode x) {
        SplayTreeNode y = x.parent;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) this.root = x;
        else if (y == y.parent.right) y.parent.right = x;
        else y.parent.left = x;
        x.right = y;
        y.parent = x;
    }

    // Rotasi Zag
    private void zag(SplayTreeNode x) {
        SplayTreeNode y = x.parent;
        y.right = x.left;
        if (x.left != null) x.left.parent = y;
        x.parent = y.parent;
        if (y.parent == null) this.root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.left = y;
        y.parent = x;
    }

    // Splay (menarik node x ke root)
    public void splay(SplayTreeNode x) {
        while (x.parent != null) {
            SplayTreeNode parent = x.parent;
            SplayTreeNode grandparent = parent.parent;

            if (grandparent == null) {
                if (x == parent.left) zig(x);
                else zag(x);
            } else {
                if (x == parent.left && parent == grandparent.left) {
                    zig(parent); zig(x);
                } else if (x == parent.right && parent == grandparent.right) {
                    zag(parent); zag(x);
                } else if (x == parent.right && parent == grandparent.left) {
                    zag(x); zig(x);
                } else {
                    zig(x); zag(x);
                }
            }
        }
    }

    // Fungsi Insert
    public void insert(int key) {
        SplayTreeNode node = new SplayTreeNode(key);
        SplayTreeNode y = null;
        SplayTreeNode x = this.root;

        while (x != null) {
            y = x;
            if (node.key < x.key) x = x.left;
            else if (node.key > x.key) x = x.right;
            else {
                splay(x); 
                return;
            }
        }

        node.parent = y;
        if (y == null) root = node;
        else if (node.key < y.key) y.left = node;
        else y.right = node;

        splay(node); // Tarik node baru ke root
    }

    // Fungsi Search
    public boolean search(int key) {
        SplayTreeNode x = root;
        SplayTreeNode lastNode = null;
        
        while (x != null) {
            lastNode = x;
            if (key == x.key) {
                splay(x); // Splay ke root
                return true;
            } else if (key < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        
        // Jika tidak ketemu, splay node terakhir yang diakses ke root
        if (lastNode != null) {
            splay(lastNode);
        }
        return false;
    }

    // Fungsi Cetak Visualisasi Tree
    public void printVisual(SplayTreeNode node, String prefix, boolean isLeft, boolean isRoot) {
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

// Class Main
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SplayTree tree = new SplayTree();
        boolean jalan = true;

        System.out.println("== PROGRAM SPLAY TREE INTERAKTIF ==");
        
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
                    System.out.println(">> Angka " + insertVal + " dimasukkan dan di-splay (Zig/Zag) ke Root.");
                    System.out.println("Struktur Tree Baru:");
                    tree.printVisual(tree.root, "", false, true);
                    break;
                case 2:
                    System.out.print("Angka yang ingin dicari: ");
                    int searchVal = scanner.nextInt();
                    boolean found = tree.search(searchVal);
                    if (found) {
                        System.out.println(">> Angka " + searchVal + " DITEMUKAN dan di-splay ke Root.");
                    } else {
                        System.out.println(">> Angka " + searchVal + " TIDAK DITEMUKAN. Node terakhir diakses di-splay ke Root.");
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