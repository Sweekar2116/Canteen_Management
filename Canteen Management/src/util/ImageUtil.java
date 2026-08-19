package util;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * Utility class for loading and managing images from the img folder
 * Handles relative paths and provides caching for better performance
 */
public class ImageUtil {
    
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();
    
    /**
     * Get the image directory path
     */
    private static String getImagePath() {
        // Try multiple possible paths relative to current working directory
        String[] possiblePaths = {
            "img",
            "../img",
            "../../Canteen Management/img",
            System.getProperty("user.dir") + "/img",
            System.getProperty("user.dir") + "/../img"
        };
        
        for (String pathStr : possiblePaths) {
            File path = new File(pathStr);
            if (path.exists() && path.isDirectory()) {
                try {
                    return path.getCanonicalPath() + File.separator;
                } catch (Exception e) {
                    return path.getAbsolutePath() + File.separator;
                }
            }
        }
        
        // Default fallback
        return "img" + File.separator;
    }
    
    /**
     * Load an ImageIcon from the img folder with caching
     * @param fileName Name of the image file (e.g., "login.jpg", "welcome.jpg")
     * @return ImageIcon object or null if image not found
     */
    public static ImageIcon loadImage(String fileName) {
        if (imageCache.containsKey(fileName)) {
            return imageCache.get(fileName);
        }
        
        try {
            String imagePath = getImagePath() + fileName;
            File imgFile = new File(imagePath);
            
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                imageCache.put(fileName, icon);
                return icon;
            } else {
                System.err.println("Image not found: " + imagePath);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + fileName + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load an Image object from the img folder
     * @param fileName Name of the image file
     * @return Image object or null if not found
     */
    public static Image loadImageAsImage(String fileName) {
        ImageIcon icon = loadImage(fileName);
        if (icon != null) {
            return icon.getImage();
        }
        return null;
    }
    
    /**
     * Scale an ImageIcon to specified width and height
     * @param icon Original ImageIcon
     * @param width Target width
     * @param height Target height
     * @return Scaled ImageIcon
     */
    public static ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        if (icon == null) return null;
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }
    
    /**
     * Scale an image by filename
     * @param fileName Name of the image file
     * @param width Target width
     * @param height Target height
     * @return Scaled ImageIcon
     */
    public static ImageIcon loadAndScaleImage(String fileName, int width, int height) {
        ImageIcon icon = loadImage(fileName);
        return scaleImage(icon, width, height);
    }
    
    /**
     * Clear the image cache (useful for memory management)
     */
    public static void clearCache() {
        imageCache.clear();
    }
}
