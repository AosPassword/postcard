package org.wuneng.web.postcard.services;

import org.springframework.web.multipart.MultipartFile;
import org.wuneng.web.postcard.beans.CheckResult;

public interface ProfilePhotoService {
    CheckResult downloadPhoto(MultipartFile file,Integer id);
}
