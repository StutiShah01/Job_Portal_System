package com.jobportal.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.Constants.SwaggerConstants;
import com.jobportal.Exceptions.CustomValidationException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.GenericResponse.GenericResponseHandlers;
import com.jobportal.Validators.TagValidator;
import com.jobportal.dto.TagDTO;
import com.jobportal.entity.Tags;
import com.jobportal.mappers.TagMapper;
import com.jobportal.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
@Slf4j
@Tag(name = SwaggerConstants.TAG_CONTROLLER, description = SwaggerConstants.TAG_CONTROLLER_DESCRIPTION)
public class TagController {
	private final TagValidator tagValidator;
	private final TagService tagService;
	private final MessageSource messageSource;
	private final TagMapper tagMapper;
	@InitBinder
	public void initbinder(WebDataBinder databinder)
	{
			databinder.addValidators(tagValidator);
	}
	
	@PostMapping("/add")
	@Operation(summary = SwaggerConstants.ADD_TAG_SUMMARY,description = SwaggerConstants.ADD_TAG_DESCRIPTION)
	@ApiResponse(
	                responseCode = "201",
	                description = SwaggerConstants.RESPONSE_TAG_CREATED,
	                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class))
	            ) 
	public ResponseEntity<?> addTags(@Valid @RequestBody TagDTO tagDTO,BindingResult bindingResult)
	{
		if (bindingResult.hasErrors()) {
		log.info("An error occured");

		List<String> errors = bindingResult.getFieldErrors().stream()
		        .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
		        .collect(Collectors.toList());
		    throw new CustomValidationException(errors); 
		}
		TagDTO response=tagService.addTag(tagDTO);
		return new GenericResponseHandlers.Builder()
				  .setData(response)
	              .setStatus(HttpStatus.CREATED)
	              .create();
		
	}
	
	@GetMapping("/alltags")
	 @Operation(
		        summary = SwaggerConstants.GET_ALL_TAGS_SUMMARY,
		        description = SwaggerConstants.GET_ALL_TAGS_DESCRIPTION)
		     
	@ApiResponse( responseCode = "200",
                 description = SwaggerConstants.RESPONSE_ALL_TAGS_RETRIEVED,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tags.class)))    
	public ResponseEntity<?> getAllTags(@RequestParam(required = false, value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(required = false, value = "size", defaultValue = "2") Integer pageSize)
	{
		Page<Tags> pages=tagService.getAllTags(pageNumber-1, pageSize);
		List<Tags> tagList=pages.getContent();
		List<TagDTO> responseList=tagMapper.toTagDTOList(tagList);
		
		return new GenericResponseHandlers.Builder()
				  .setPageNumber(pageNumber).setPageSize(pageSize.longValue()).setTotalCount(pages.getTotalElements())
				  .setTotalPages(pages.getTotalPages()).setHasNextPage(pages.hasNext()).setHasPreviousPage(pages.hasPrevious())
				  .setData(responseList)
	              .setStatus(HttpStatus.OK)
	              .create();
		
	}
	@DeleteMapping("/delete/{tagId}")
	@Operation(summary = SwaggerConstants.DELETE_TAG_SUMMARY_STRING)
	@ApiResponses(value= { 
			@ApiResponse(responseCode =   "200",description =SwaggerConstants.RESPONSE_TAG_DELETE)
			}
	)
	public ResponseEntity<?> deleteTag(@PathVariable("tagId") Long tagId) throws NotFoundException
	{
		tagService.deleteTag(tagId);
		return new GenericResponseHandlers.Builder()
				.setMessage(messageSource.getMessage("tag.deleted.successfully",null, LocaleContextHolder.getLocale()))
				.setStatus(HttpStatus.OK)
				.create();
	}
	
	
	
	
	
	

}
