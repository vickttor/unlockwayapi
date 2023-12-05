package unlockway.unlockway_api.azure.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class BlobStorage {

    protected static final String  constr = "AccountName=unlockways3;" +
            "AccountKey=7/MeekOD9pwgKTIK6nBLXv0yGuN2Mp3mBV5HZHv41ggz+zScLIFlXBYrrLEwnMdQIhZJZdxRRsEU+AStR++FVw==;" +
            "EndpointSuffix=core.windows.net;" +
            "DefaultEndpointsProtocol=https;";


    public static void removePhotoFromAzureBlobStorage(String filename, String containerName) {
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(constr)
                .containerName(containerName)
                .blobName(filename)
                .buildClient();
        try {
            blobClient.deleteIfExists();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String storePhotoIntoAzureBlobStorage(MultipartFile photo, String containerName) {

        String filename = UUID.randomUUID().toString();

        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(constr)
                .containerName(containerName)
                .blobName(filename)
                .buildClient();

        try {
            BlobHttpHeaders headers = new BlobHttpHeaders()
                    .setContentType(photo.getContentType());

            blobClient.uploadWithResponse(photo.getInputStream(), photo.getSize(), null, headers, null, null, null, null, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return filename;
    }

}
