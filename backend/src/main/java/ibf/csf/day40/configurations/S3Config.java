package ibf.csf.day40.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

    @Value("${spaces.key.secret}")
    private String secretKey;

    @Value("${spaces.key.access}")
    private String accessKey;

    @Bean
    public AmazonS3 createS3Client() {
        BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);

        // https://csf-day39.sgp1.digitaloceanspaces.com
        EndpointConfiguration endpoint = new EndpointConfiguration(
                "sgp1.digitaloceanspaces.com", "sgp1");

        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .build();
    }
}
