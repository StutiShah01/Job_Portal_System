package com.jobportal.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.EmployerDTO;
import com.jobportal.dto.SearchEmployerDTO;
import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;
import com.jobportal.pagination.Pagination;

public interface EmployerService {
	
	/**
	 * Adds a new employer or updates an existing employer in the database.
	 * @param employerDto The employer data transfer object containing employer data.
	 * @return The saved or updated employer data as EmployerDTO.
	 * @throws NotFoundException if the employer to update does not exist.
	 * @throws CustomException for other custom exceptions that may occur during processing.
	 */
	public EmployerDTO addOrUpdateEmployer(EmployerDTO employerDto) throws NotFoundException, CustomException;
	
	/**
	 * Deletes an employer by their ID.
	 * @param id The unique ID of the employer to be deleted.
	 * @return A confirmation message indicating deletion status.
	 * @throws NotFoundException if no employer is found with the provided ID.
	 */
	public void deleteEmployerById(Long id) throws NotFoundException;
	
	/**
	 * Retrieves a list of employers filtered by the given criteria.
	 * @param name The name to filter by.
	 * @param industry The industry to filter by.
	 * @param companySize The company size to filter by.
	 * @return A list of EmployerDTO objects that match the filters.
	 */
	public List<EmployerDTO> findEmployersByFilters(String name, String industry, String companySize);
	
	/**
	 * Retrieves a paginated list of employers.
	 * @param pageNo The page number to retrieve.
	 * @param pageSize The number of entries per page.
	 * @return A Pagination object containing the list of employers.
	 */
	public Pagination getEmployers(Integer pageNo, Integer pageSize);
	
	/**
	 * Retrieves a paginated list of jobs posted by a specific employer.
	 * @param id The employer's ID whose job list is to be retrieved.
	 * @param pageNumber The page number of the result set.
	 * @param pageSize The number of jobs per page.
	 * @return A Page of Job entities.
	 * @throws NotFoundException if no employer is found with the provided ID.
	 */
	public Page<Job> getEmployersJobList(Long id, Integer PageNumber, Integer PageSize) throws NotFoundException;
	
	/**
	 * Searches for employers based on various filters encapsulated in SearchEmployerDTO.
	 * @param searchEmployerDTO The DTO containing search criteria.
	 * @param pageNumber The page number of the result set.
	 * @param pageSize The number of results per page.
	 * @return A Page of Employer entities.
	 * @throws CustomException if there is an error processing the search.
	 */
	public Page<Employer> searchEmployers(SearchEmployerDTO searchEmployerDTO, Integer pageNumber, Integer pageSize) throws CustomException;
	
	/**
	 * Retrieves the details of a single employer by their ID.
	 * @param employerId The unique ID of the employer.
	 * @return An EmployerDTO containing the employer's details.
	 * @throws NotFoundException if no employer is found with the provided ID.
	 */
	public EmployerDTO getSingleEmployer(Long employerId) throws NotFoundException;
	
	/**
	 * Changes the active status of an employer.
	 * @param id The unique ID of the employer.
	 * @param active The desired active status (true or false).
	 * @return A string message indicating the status change.
	 * @throws NotFoundException if no employer is found with the provided ID.
	 */
	public void changeEmployerStatus(Long id, Boolean active) throws NotFoundException,CustomException;
	
	
	/**
	 * Validates the uniqueness of an email for an employer.
	 * @param id The employer's ID to exclude from the check (used during updates).
	 * @param email The email to check for uniqueness.
	 * @return True if the email is unique, false otherwise.
	 */
	public Boolean validateEmail(Long id, String email);
	
	/**
	 * Validates the uniqueness of a website URL for an employer.
	 * @param id The employer's ID to exclude from the check (used during updates).
	 * @param website The website URL to check for uniqueness.
	 * @return True if the website is unique, false otherwise.
	 */
	public Boolean validateWebsite(Long id, String website);
	
}

