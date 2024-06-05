package com.jobportal.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.TagDTO;
import com.jobportal.entity.Tags;

@Service
public interface TagService {
		
	public TagDTO addTag(TagDTO tagDTO);
	public Boolean existsTag(String tagName);
	public Page<Tags> getAllTags(Integer pageNumber,Integer pageSize);
	public void deleteTag(Long id) throws NotFoundException;
}
