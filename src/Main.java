import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    static int N,M,P;
    static char[][] board;
    static List<Piece> pieces = new ArrayList<>();
    static boolean solutionFound = false;
    static char[][] finalBoard;
    static long iterCount = 0;

    static class Piece {
        char letter;
        List<int[]> baseShape;
        List<List<int[]>> orientations;

        public Piece(List<String> lines) {
            letter = lines.get(0).trim().charAt(0);
            baseShape = new ArrayList<>();
            for (int i=0; i<lines.size(); i++) {
                String line = lines.get(i);
                for (int j=0; j<line.length(); j++) {
                    if (line.charAt(j)==letter) baseShape.add(new int[]{i,j});
                }
            }
            normalize(baseShape);
            orientations = generateOrientations(baseShape);
        }

        // Buat titik paling kiri atas = (0,0)
        private void normalize(List<int[]> shape) {
            int minRow = Integer.MAX_VALUE;
            int minCol = Integer.MAX_VALUE;
            for (int[] p : shape) {
                if (p[0]<minRow) minRow = p[0];
                if (p[1]<minCol) minCol = p[1];
            }
            for (int[] p : shape) {
                p[0] -= minRow;
                p[1] -= minCol;
            }
        }

        // Rotasi dan pencerminan
        private List<List<int[]>> generateOrientations(List<int[]> base) {
            Set<String> unique = new HashSet<>();
            List<List<int[]>> result = new ArrayList<>();
            for (int r = 0; r < 4; r++) {
                List<int[]> rotated = rotate(base, r);
                normalize(rotated);
                String key = coordString(rotated);
                if (!unique.contains(key)) {
                    unique.add(key);
                    result.add(copyShape(rotated));
                }
                // Tambahkan orientasi mirror (horizontal)
                List<int[]> mirrored = mirror(rotated);
                normalize(mirrored);
                key = coordString(mirrored);
                if (!unique.contains(key)) {
                    unique.add(key);
                    result.add(copyShape(mirrored));
                }
            }
            return result;
        }

        // Rotasi searah jarum jam sebanyak n kali
        private List<int[]> rotate(List<int[]> shape, int n) {
            List<int[]> newShape = copyShape(shape);
            for (int t=0; t<n; t++) {
                for (int[] p : newShape) {
                    int temp = p[0];
                    p[0] = p[1];
                    p[1] = -temp;
                }
                normalize(newShape);
            }
            return newShape;
        }

        // Pencerminan vertikal (terhadap sumbu horizontal)
        private List<int[]> mirror(List<int[]> shape) {
            List<int[]> res = new ArrayList<>();
            for (int[] p : shape) {
                res.add(new int[]{p[0], -p[1]});
            }
            return res;
        }

        // Konversi koordinat dalam format string "(x,y)"
        private String coordString(List<int[]> shape) {
            List<String> pts = new ArrayList<>();
            for (int[] p : shape) {
                pts.add(p[0] + "," + p[1]);
            }
            Collections.sort(pts);
            return String.join(";", pts);
        }

        // Copy list titik
        private List<int[]> copyShape(List<int[]> shape) {
            List<int[]> copy = new ArrayList<>();
            for (int[] p : shape) {
                copy.add(new int[]{p[0], p[1]});
            }
            return copy;
        }
    }

    // pencarian solusi dengan brute force/backtracking
    static void solve(int pieceIdx) {
        //jika semua piece berhasil dipasang
        if (pieceIdx == pieces.size()) {
            solutionFound = true;
            finalBoard = new char[N][M];
            for (int i=0; i<N; i++) {
                for (int j=0; j<M; j++) {
                    finalBoard[i][j] = board[i][j];
                }
            }
            return;
        }
        Piece piece = pieces.get(pieceIdx);
        for (List<int[]> orientation : piece.orientations) {
            for (int i=0; i<N; i++) {
                for (int j=0; j<M; j++) {
                    iterCount++;
                    if (canPlace(orientation, i, j)) {
                        place(orientation, i, j, piece.letter);
                        solve(pieceIdx + 1);
                        if (solutionFound) return;
                        place(orientation, i, j, ',');
                    }
                }
            }
        }
    }

    // Menempatkan/menghapus piece pada board
    static void place(List<int[]> orientation, int row, int col, char ch) {
        for (int[] p : orientation) {
            int r = row + p[0];
            int c = col + p[1];
            board[r][c] = ch;
        }
    }
    static boolean canPlace(List<int[]> orientation, int row, int col) {
        for (int[] p : orientation) {
            int r = row + p[0];
            int c = col + p[1];
            if (r<0||r>=N||c<0||c>=M||board[r][c]!=',') return false;
        }
        return true;
    }

    // Print board dengan warna
    static void printColored(char[][] boardOutput) {
        Map<Character, String> colorMap = new HashMap<>();
        String[] colors = {
            "\u001B[38;5;196m", // merah 1
            "\u001B[38;5;46m",  // hijau 1
            "\u001B[38;5;21m",  // biru 1
            "\u001B[38;5;226m", // kuning 1
            "\u001B[38;5;129m", // ungu 1
            "\u001B[38;5;51m",  // biru 2
            "\u001B[38;5;208m", // orange 1
            "\u001B[38;5;118m", // hijau 2
            "\u001B[38;5;27m",  // biru 3
            "\u001B[38;5;220m", // kuning 2
            "\u001B[38;5;99m",  // magenta
            "\u001B[38;5;201m", // pink
            "\u001B[38;5;33m",  // biru 4
            "\u001B[38;5;160m", // merah 2
            "\u001B[38;5;202m", // orange 2
            "\u001B[38;5;82m",  // hijau 3
            "\u001B[38;5;34m",  // hijau 4
            "\u001B[38;5;94m",  // coklat
            "\u001B[38;5;135m", // ungu 2
            "\u001B[38;5;165m", // ungu 3
            "\u001B[38;5;75m",  // biru 5
            "\u001B[38;5;123m", // biru 6
            "\u001B[38;5;161m", // ungu 4
            "\u001B[38;5;179m", // cream
            "\u001B[38;5;141m", // ungu 5
            "\u001B[38;5;76m"   // hijau 5
        };        
        int idx = 0;
        for (Piece p: pieces) {
            if (!colorMap.containsKey(p.letter)) {
                colorMap.put(p.letter, colors[idx]);
                idx++;
            }
        }
        String resetColor = "\u001B[0m";
        for (int i=0; i<boardOutput.length; i++) {
            for (int j=0; j<boardOutput[i].length; j++) {
                char ch = boardOutput[i][j];
                if ((ch!=',') && (colorMap.containsKey(ch))) System.out.print(colorMap.get(ch)+ch+resetColor);
                else System.out.print(ch);
            }
            System.out.println();
        }
    }

    // Save solusi ke file
    static void saveSolution(String filename, char[][] solutionBoard/*, long searchTime, long iterations*/) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            for (int i=0; i<solutionBoard.length; i++) {
                writer.println(new String(solutionBoard[i]));
            }
            writer.println();
            // writer.println("Waktu pencarian: " + searchTime + " ms");
            // writer.println("Banyak kasus yang ditinjau: " + iterations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void solveMain(String fileName) {
        Scanner sc = new Scanner(System.in);
        try {
            // Membaca seluruh baris dari file
            List<String> allLines = Files.readAllLines(Paths.get(fileName));
            if (allLines.size()<2) {
                System.out.println("File tidak valid.");
                sc.close();
                return;
            }

            // Baris pertama: N M P
            String[] parts = allLines.get(0).trim().split("\\s+");
            if (parts.length < 3) {
                System.out.println("Baris pertama harus berisi tiga angka: N M P");
                sc.close();
                return;
            }
            N = Integer.parseInt(parts[0]);
            M = Integer.parseInt(parts[1]);
            P = Integer.parseInt(parts[2]);
            String config = allLines.get(1).trim().toLowerCase();

            int currentIdx = 2;
            int pieceCellTotal = 0;
            if (config.equals("custom")) {
                board = new char[N][M];
                for (int i=0; i<N; i++) {
                    String line = allLines.get(currentIdx);
                    currentIdx++;
                    for (int j=0; j<M; j++) {
                        char cell = line.charAt(j);
                        cell = Character.toLowerCase(cell);
                        if (cell=='x') {
                            board[i][j] = ',';
                            pieceCellTotal++;
                        } else if (cell=='.') board[i][j] = ' ';
                        else {
                            System.out.println("Karakter pada file teks tidak valid.");
                            sc.close();
                            return;
                        }
                    }
                }
            } else if (config.equals("default")) {
                board = new char[N][M];
                for (int i=0; i<N; i++) {
                   for (int j=0; j<M; j++) {
                       board[i][j] = ',';
                   }
                }
            } else {
                System.out.println("Konfigurasi pada file teks tidak valid.");
                sc.close();
                return;
            }

            List<String> remainingLines = new ArrayList<>();
            for (int i=currentIdx; i<allLines.size(); i++) {
                String line = allLines.get(i);
                if (line.trim().isEmpty()) continue;
                remainingLines.add(line);
            }

            // Pengelompokan baris untuk tiap piece.
            int idx = 0;
            for (int i=0; i<P; i++) {
                if (idx >= remainingLines.size()) break;
                List<String> pieceLines = new ArrayList<>();
                String firstLine = remainingLines.get(idx);
                pieceLines.add(firstLine);
                char pieceChar = firstLine.trim().charAt(0);
                idx++;
                while (idx < remainingLines.size()) {
                    String nextLine = remainingLines.get(idx);
                    if ((!nextLine.trim().isEmpty()) && (nextLine.trim().charAt(0)==pieceChar)) {
                        pieceLines.add(nextLine);
                        idx++;
                    } else break;
                }
                pieces.add(new Piece(pieceLines));
            }

            // cek apakah dipastikan tidak muat atau dipastikan ada kelebihan slot/cell
            int totalCells = 0;
            for (Piece p : pieces) {
                totalCells += p.baseShape.size();
            }
            if (config.equals("custom")) {
                if (totalCells!=pieceCellTotal) {
                    System.out.println("Solusi tidak ditemukan.");
                    sc.close();
                    return;
                }
            } else if (config.equals("default")) {
                if (totalCells != N*M) {
                    System.out.println("Solusi tidak ditemukan.");
                    sc.close();
                    return;
                }
            }

            long startTime = System.currentTimeMillis();
            solve(0);
            long endTime = System.currentTimeMillis();
            long searchTime = endTime - startTime;
            
            if (solutionFound) {
                System.out.println("\nSolusi ditemukan.");
                printColored(finalBoard);
                System.out.println("\nWaktu pencarian: " + searchTime + " ms");
                System.out.println("Banyak kasus yang ditinjau: " + iterCount);
            } else System.out.println("Solusi tidak ditemukan.");

            String answer;
            do {
                System.out.print("\nApakah anda ingin menyimpan solusi? (Y/n): ");
                answer = sc.nextLine().trim().toLowerCase();
                if (answer.equals("y")) {
                    System.out.print("Masukkan nama file teks output (tanpa \".txt\"): ");
                    String outFileName = "../test/"+sc.nextLine().trim()+".txt";
                    saveSolution(outFileName, finalBoard/*, searchTime, iterCount*/);
                    System.out.println("Solusi berhasil disimpan ke " + outFileName);
                }
            } while (!answer.equals("n"));
            
            String answerImg;
            do {
                System.out.print("\nApakah anda ingin menyimpan solusi sebagai gambar? (Y/n): ");
                answerImg = sc.nextLine().trim().toLowerCase();
                if (answerImg.equals("y")) {
                    System.out.print("Masukkan nama file gambar output (tanpa \".jpg\"): ");
                    String outFileName = "../test/"+sc.nextLine().trim()+".jpg";
                    ImageSaver.saveAsImage(outFileName, finalBoard, 100);
                }
            } while (!answerImg.equals("n"));
            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan.");
        } catch (NoSuchFileException e) {
            System.out.println("File tidak ditemukan.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Masukkan nama file teks (tanpa \".txt\"): ");
        String fileName = "../src/"+sc.nextLine().trim()+".txt";
        solveMain(fileName);
        sc.close();
    }
}