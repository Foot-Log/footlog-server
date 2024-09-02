package footlogger.footlog.service;

import ch.qos.logback.core.status.ErrorStatus;
import com.amazonaws.internal.http.ErrorCodeParser;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.ion.IonException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String upload(MultipartFile image) {
        //입력받은 이미지 파일이 비었는지 검증
        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new IllegalArgumentException("image is empty");
        }
        //uploadImage를 호출하여 S3에 저장된 이미지의 public url 반환
        return this.uploadImage(image);
    }

    private String uploadImage(MultipartFile image) {
        //확장자 명이 올바른지 확인
        this.validateImageFileExtention(image.getOriginalFilename());
        //이미지 업로드
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 업로드 실패");
        }
    }

    //파일 확장자 검사
    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if(lastDotIndex == -1) {
            throw new IllegalArgumentException("파일 확장자 없음");
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if(!allowedExtentionList.contains(extension)) {
            throw new IllegalArgumentException("올바른 확장자 아님");
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        //원본 파일명
        String originalFilename = image.getOriginalFilename();
        //확장자 명
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        //변경된 파일명
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        //image를 byte[]로 변환
        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        //metadata 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(bytes.length);

        //S3에 요청할 때 사용할 byteInputStream 생성
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            //S3로 putObject 할 때 사용할 요청 객체
            //생성자 : bucket 이름, 파일 명, byteInputStream, metadata
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);

            //실제로 S3에 이미지 데이터를 넣는 부분
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new IllegalArgumentException("데이터 오류");
        } finally {
            byteArrayInputStream.close();
            is.close();
        }
        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void delete(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);

        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new IllegalArgumentException("이미지 삭제 실패");
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new IllegalArgumentException("이미지 삭제 실패");
        }
    }
}
