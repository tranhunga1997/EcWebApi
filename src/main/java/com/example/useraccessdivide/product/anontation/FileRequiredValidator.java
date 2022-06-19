package com.example.useraccessdivide.product.anontation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileRequiredValidator implements ConstraintValidator<FileRequired, MultipartFile> {
    
	@Override
    public void initialize(FileRequired constraint) {}
    
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return value != null && !value.getOriginalFilename().isEmpty();
	}

}
