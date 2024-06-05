package com.jobportal.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.ApplicationDTO;
import com.jobportal.dto.EmployerWithJobDTO;
import com.jobportal.dto.JobWithApplicationDTO;
import com.jobportal.entity.Application;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.Job;

@Service
public interface ApplicationService {

    /**
     * Submits a new job application.
     * @param applicationDTO The data transfer object containing the application details.
     * @return The saved application data as ApplicationDTO.
     * @throws NotFoundException if the job or candidate referenced in the application is not found.
     * @throws Exception for other exceptions that may occur during the process.
     */
    public ApplicationDTO addApplication(ApplicationDTO applicationDTO) throws NotFoundException, Exception;

    /**
     * Checks if a job and candidate exist in the system.
     * @param job The job to check.
     * @param candidate The candidate to check.
     * @return True if both the job and candidate exist, otherwise false.
     */
    public Boolean ExsistJobAndCandidate(Job job, Candidate candidate);

    /**
     * Withdraws an application by its ID.
     * @param id The unique identifier of the application to be withdrawn.
     * @return A string message indicating the result of the operation.
     * @throws NotFoundException if no application is found with the provided ID.
     */
    public void withDrawApplication(Long id) throws NotFoundException;

    /**
     * Retrieves a paginated list of applications for a specific candidate.
     * @param candidateId The unique identifier of the candidate.
     * @param pageNumber The page number of the result set.
     * @param pageSize The number of records per page.
     * @return A Pagination object containing the applications.
     * @throws NotFoundException if no candidate is found with the provided ID.
     * @throws CustomException for other custom exceptions that may occur during processing.
     */
    public Page<Application> applicationlistforCandidate(Long candidateId, Integer pageNumber, Integer pageSize) throws NotFoundException, CustomException;

    /**
     * Retrieves all applications submitted for a specific job.
     * @param jobId The unique identifier of the job.
     * @return A JobWithApplicationDTO containing the job details and a list of its applications.
     * @throws NotFoundException if no job is found with the provided ID.
     */
    public JobWithApplicationDTO applicationlistforJob(Long jobId) throws NotFoundException;

    /**
     * Retrieves a list of all applications along with associated job and candidate details for a specific employer.
     * @param employerId The unique identifier of the employer.
     * @return A list of ApplicationWithJobCandidateDTO objects.
     * @throws NotFoundException if no employer is found with the provided ID.
     */
//    public List<ApplicationWithJobCandidateDTO> employersApplicationList(long employerId) throws NotFoundException;

    /**
     * Checks if an application exists by its ID.
     * @param applicationId The unique identifier of the application.
     * @return True if the application exists, otherwise false.
     */
    public Boolean checkApplicationExists(Long applicationId);

    /**
     * Retrieves a comprehensive list of all applications for a specific employer, including job details.
     * @param employerId The unique identifier of the employer.
     * @return An EmployerWithJobDTO containing the employer details and a list of all related applications.
     * @throws NotFoundException if no employer is found with the provided ID.
     */
    public EmployerWithJobDTO employersApplicationList(Long employerId) throws NotFoundException;
}
