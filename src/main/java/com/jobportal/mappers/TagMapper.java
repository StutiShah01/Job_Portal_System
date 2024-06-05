package com.jobportal.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jobportal.dto.TagDTO;
import com.jobportal.entity.Tags;


@Component
public class TagMapper {

	public TagDTO toTagDTO(Tags tag)
	{
		TagDTO dto=new TagDTO();
		BeanUtils.copyProperties(tag, dto);
		return dto;
	}
	public Tags toTags(TagDTO tagdto)
	{
		Tags tags=new Tags();
		BeanUtils.copyProperties(tagdto, tags);
		return tags;
	}
	public List<TagDTO> toTagDTOList(List<Tags> tagList)
	{
		List<TagDTO> tagDtoList=new ArrayList<>();
		for(Tags tag:tagList)
		{
		tagDtoList.add(toTagDTO(tag));
		}
		return tagDtoList;
	}
	
}
