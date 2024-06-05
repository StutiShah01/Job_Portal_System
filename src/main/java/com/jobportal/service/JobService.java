package com.jobportal.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.JobDTO;
import com.jobportal.dto.SearchJobDTO;
import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;

@Service
public interface JobService {
    
    /**
     * Adds a new job or updates an existing job in the database.
     * @param jobdto The JobDTO containing the job details.
     * @return The added or updated JobDTO.
     * @throws NotFoundException if the job to update does not exist.
     * @throws CustomException for other custom exceptions that may occur during processing.
     */
    public JobDTO addOrUpdateJob(JobDTO jobdto) throws NotFoundException, CustomException;

    /**
     * Deletes a job based on its ID.
     * @param id The unique identifier of the job.
     * @return A confirmation message indicating deletion status.
     * @throws NotFoundException if no job is found with the provided ID.
     */
    public String deleteJob(Long id) throws NotFoundException;

    /**
     * Finds a job by its title, posted date, and employer.
     * @param title The title of the job.
     * @param date The posted date of the job.
     * @param employer The employer who posted the job.
     * @return The Job entity if found; otherwise null.
     */
    public Job findByTitleAndPostedDateAndEmployer(String title, Date date, Employer employer);

    /**
     * Retrieves a job by its ID.
     * @param id The unique identifier of the job.
     * @return The Job entity if found; otherwise null.
     */
    public Job findByJob(Long id);

    /**
     * Searches for jobs based on various criteria provided within SearchJobDTO and paginates the results.
     * @param searchJobDTO The DTO containing the search criteria.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The number of entries per page.
     * @return A Pagination object containing the list of jobs.
     * @throws CustomException if there is an error during the search process.
     */
    public Page<Job> searchJob(SearchJobDTO searchJobDTO, Integer pageNumber, Integer pageSize) throws CustomException;
    /**
     * 
     * Changes the activity status for the particular job
     * @param id
     * @param active
     * @return
     * @throws NotFoundException
     */
    public void changeJobStatus(Long id, Boolean active) throws NotFoundException,CustomException;

}
