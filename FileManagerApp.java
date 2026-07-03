import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public class FileManagerApp {
    static Scanner sc = new Scanner(System.in);

    // ─── Create File ───
    static void createFile() throws IOException {
        System.out.print("Enter file path to create: ");
        String path = sc.nextLine();
        File f = new File(path);
        if (f.createNewFile())
            System.out.println("File created: " + f.getAbsolutePath());
        else
            System.out.println("File already exists.");
    }

    // ─── Delete File/Folder ───
    static void deleteFile() {
        System.out.print("Enter path to delete: ");
        String path = sc.nextLine();
        File f = new File(path);
        if (f.exists()) {
            System.out.println(f.delete() ? "Deleted successfully." : "Could not delete.");
        } else System.out.println("Path not found.");
    }

    // ─── Rename File ───
    static void renameFile() {
        System.out.print("Enter current path: "); String oldPath = sc.nextLine();
        System.out.print("Enter new name: ");     String newName = sc.nextLine();
        File oldFile = new File(oldPath);
        File newFile = new File(oldFile.getParent(), newName);
        System.out.println(oldFile.renameTo(newFile) ? "Renamed to " + newName : "Rename failed.");
    }

    // ─── Create Directory ───
    static void createDirectory() {
        System.out.print("Enter directory path: ");
        String path = sc.nextLine();
        File dir = new File(path);
        System.out.println(dir.mkdirs() ? "Directory created: " + dir.getAbsolutePath() : "Already exists or failed.");
    }

    // ─── List Files ───
    static void listFiles() {
        System.out.print("Enter directory path (press Enter for current): ");
        String path = sc.nextLine().trim();
        if (path.isEmpty()) path = System.getProperty("user.dir");
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) { System.out.println("Not a valid directory."); return; }
        System.out.println("\n===== Contents of: " + dir.getAbsolutePath() + " =====");
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) { System.out.println("Empty directory."); return; }
        for (File f : files) {
            String type = f.isDirectory() ? "[DIR] " : "[FILE]";
            System.out.printf("%s %-40s %8d bytes%n", type, f.getName(), f.length());
        }
    }

    // ─── Read File ───
    static void readFile() throws IOException {
        System.out.print("Enter file path to read: ");
        String path = sc.nextLine();
        File f = new File(path);
        if (!f.exists()) { System.out.println("File not found."); return; }
        System.out.println("\n===== File Content: " + f.getName() + " =====");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) System.out.println(line);
        }
    }

    // ─── Write File ───
    static void writeFile() throws IOException {
        System.out.print("Enter file path to write: ");
        String path = sc.nextLine();
        System.out.println("Enter content (type DONE on new line to finish):");
        StringBuilder content = new StringBuilder();
        String line;
        while (!(line = sc.nextLine()).equals("DONE")) content.append(line).append("\n");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(content.toString());
        }
        System.out.println("File written successfully.");
    }

    // ─── Copy File ───
    static void copyFile() throws IOException {
        System.out.print("Source file: "); String src = sc.nextLine();
        System.out.print("Destination: "); String dst = sc.nextLine();
        Files.copy(Paths.get(src), Paths.get(dst), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File copied.");
    }

    // ─── File Info ───
    static void fileInfo() throws IOException {
        System.out.print("Enter file path: ");
        String path = sc.nextLine();
        File f = new File(path);
        if (!f.exists()) { System.out.println("Not found."); return; }
        BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
        System.out.println("\n===== File Info =====");
        System.out.println("Name    : " + f.getName());
        System.out.println("Path    : " + f.getAbsolutePath());
        System.out.printf("Size    : %d bytes%n", f.length());
        System.out.println("Type    : " + (f.isDirectory() ? "Directory" : "File"));
        System.out.println("Created : " + attr.creationTime());
        System.out.println("Modified: " + attr.lastModifiedTime());
        System.out.println("Readable: " + f.canRead() + " | Writable: " + f.canWrite());
    }

    public static void main(String[] args) {
        System.out.println("===== File Manager Application =====");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Create File     2. Delete File/Dir   3. Rename File");
            System.out.println("4. Create Folder   5. List Directory    6. Read File");
            System.out.println("7. Write File      8. Copy File         9. File Info  10. Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt(); sc.nextLine();
            try {
                switch (ch) {
                    case 1:  createFile();      break;
                    case 2:  deleteFile();      break;
                    case 3:  renameFile();      break;
                    case 4:  createDirectory(); break;
                    case 5:  listFiles();       break;
                    case 6:  readFile();        break;
                    case 7:  writeFile();       break;
                    case 8:  copyFile();        break;
                    case 9:  fileInfo();        break;
                    case 10: running = false;   break;
                    default: System.out.println("Invalid.");
                }
            } catch (IOException e) {
                System.out.println("[Error] " + e.getMessage());
            }
        }
        sc.close();
    }
}
