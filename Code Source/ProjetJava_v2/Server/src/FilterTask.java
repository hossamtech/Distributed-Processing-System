
import java.io.Serializable;
import java.util.UUID;

public class FilterTask implements Serializable {
    private final UUID taskId; // This is the unique ID for each FilterTask instance
    private byte[] image; // Nom du fichier image
    private String filterType; // Type de filter applique
    private float intensityValue;
    private int[][] imageMatrix = null;

    public FilterTask(byte[] image, String filterType, float intensityValue) {
        this.taskId = UUID.randomUUID(); // Généré un UUID unique
        this.image = image;
        this.filterType = filterType;
        this.intensityValue = intensityValue;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public byte[] getimage() {
        return image;
    }

    public String getFilterType() {
        return filterType;
    }

    public int[][] getImageMatrix() {
        return imageMatrix;
    }

    public void setImageMatrix(int[][] imageMatrix) {
        this.imageMatrix = imageMatrix;
    }

    public float getIntensityValue() {
        return intensityValue;
    }

}
