package com.learn.ems.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PresignedUrlGeneratorService {

    /**
     * Generate a url of an image
     * @param image - image file that you want to generate an url
     * @return - this function return map<string,string>, that indicate
     * <public_id, value> - public id of the image
     * <url, value> - url of the image
     */
    Map<String, String> generate(MultipartFile image);

}
