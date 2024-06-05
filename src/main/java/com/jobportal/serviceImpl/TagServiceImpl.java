package com.jobportal.serviceImpl;



import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.TagDTO;
import com.jobportal.entity.Tags;
import com.jobportal.mappers.TagMapper;
import com.jobportal.repository.TagsRepository;
import com.jobportal.service.TagService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

	private final TagsRepository tagsRepository;
	private final TagMapper tagMapper;
	
	@Override
	public TagDTO addTag(TagDTO tagDTO) {
		Tags tags=tagMapper.toTags(tagDTO);
		tags.setTagName(tagDTO.getTagName().toLowerCase());
		Tags savedTags=tagsRepository.save(tags);
		return tagMapper.toTagDTO(savedTags);
	}

	@Override
	public Boolean existsTag(String tagName) {
		
		return tagsRepository.existsByTagNameIgnoreCase(tagName);
	}

	@Override
	public Page<Tags> getAllTags(Integer pageNumber,Integer pageSize) {
		Pageable pageable =PageRequest.of(pageNumber, pageSize);
		Page<Tags> pages=tagsRepository.findAll(pageable);
		return pages;
	}

	@Override
	public void deleteTag(Long id) throws NotFoundException {
		Optional<Tags> tags=tagsRepository.findById(id);
		if(!tags.isPresent())
		{
			throw new NotFoundException("Tag not found with ID: " + id);
		}
		Tags tagToDelete=tags.get();
		tagsRepository.delete(tagToDelete);
//		return "Tag Deleted Successfully";
	}

}
