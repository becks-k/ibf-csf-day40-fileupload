package ibf.csf.day40.repositories;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class S3Repo {
    
    @Autowired
    private AmazonS3 s3;

    public String saveToS3(MultipartFile file, String firstName, String lastName) throws IOException {
        
        // User metadata
        Date timestamp = new Date();
        Map<String, String> userData = new HashMap<>();
        userData.put("name", String.format("%s %s", firstName, lastName));
        userData.put("upload-timestamp", timestamp.toString());
        userData.put("filename", file.getOriginalFilename());

        // Object metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userData);

        // Generate unique key for file
        String key = UUID.randomUUID().toString().substring(0, 8);

        // bucket name, key name, input stream, metadata
        PutObjectRequest putReq = new PutObjectRequest("ibf-csf", key, file.getInputStream(), metadata);
        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(putReq);
        
        // Return image url; need to know key
        return s3.getUrl("ibf-csf", key).toString();

    }
}
