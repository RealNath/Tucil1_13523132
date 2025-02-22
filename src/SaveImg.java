import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

class ImageSaver {
    public static void saveAsImage(String filename, char[][] board, int cellPixel) {
        int rows = board.length;
        int cols = board[0].length;
        int width = cols * cellPixel;
        int height = rows * cellPixel;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // bg hitam
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        Map<Character, Color> colorMap = new HashMap<>();
        colorMap.put('A', new Color(255, 0, 0));
        colorMap.put('B', new Color(0, 255, 0));
        colorMap.put('C', new Color(0, 0, 255));
        colorMap.put('D', new Color(255, 255, 0));
        colorMap.put('E', new Color(255, 0, 255));
        colorMap.put('F', new Color(0, 255, 255));
        colorMap.put('G', new Color(255, 165, 0));
        colorMap.put('H', new Color(152, 251, 152));
        colorMap.put('I', new Color(0, 0, 205));
        colorMap.put('J', new Color(255, 215, 0));
        colorMap.put('K', new Color(139, 0, 139));
        colorMap.put('L', new Color(255, 105, 180));
        colorMap.put('M', new Color(0, 128, 128));
        colorMap.put('N', new Color(128, 0, 0));
        colorMap.put('O', new Color(255, 69, 0));
        colorMap.put('P', new Color(0, 128, 0));
        colorMap.put('Q', new Color(0, 139, 139));
        colorMap.put('R', new Color(139, 69, 19));
        colorMap.put('S', new Color(138, 43, 226));
        colorMap.put('T', new Color(128, 0, 128));
        colorMap.put('U', new Color(135, 206, 235));
        colorMap.put('V', new Color(176, 196, 222));
        colorMap.put('W', new Color(199, 21, 133));
        colorMap.put('X', new Color(255, 131, 0));
        colorMap.put('Y', new Color(221, 160, 221));
        colorMap.put('Z', new Color(0, 191, 255));

        // Gambar
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                char ch = board[i][j];
                int x = j * cellPixel;
                int y = i * cellPixel;
                if (ch == ' ') {
                    g2d.setColor(Color.GRAY);
                    g2d.fillRect(x, y, cellPixel, cellPixel);
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.drawLine(x, y, x + cellPixel, y + cellPixel);
                    g2d.drawLine(x + cellPixel, y, x, y + cellPixel);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, cellPixel, cellPixel);
                    g2d.setStroke(new BasicStroke(1.0f));
                } else if (ch != ',') {
                    Color c = colorMap.getOrDefault(ch, Color.DARK_GRAY);
                    g2d.setColor(c);
                    g2d.fillRect(x, y, cellPixel, cellPixel);
                }
            }
        }

        // huruf di tiap sel
        g2d.setFont(new Font("Arial", Font.BOLD, cellPixel / 2));
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                char ch = board[i][j];
                if (ch != ',') {
                    int x = j * cellPixel;
                    int y = i * cellPixel;
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(Character.toString(ch), x + cellPixel / 4, y + (3 * cellPixel) / 4);
                }
            }
        }

        g2d.dispose();

        try {
            ImageIO.write(image, "jpg", new File(filename));
            System.out.println("Gambar solusi berhasil disimpan sebagai " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}