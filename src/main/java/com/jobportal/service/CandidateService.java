package com.jobportal.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.CandidateDTO;
import com.jobportal.dto.CandidateExperienceDTO;
import com.jobportal.dto.SearchCandidateDTO;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.Interview;
import com.jobportal.pagination.Pagination;

@Service
public interface CandidateService {

    /**
     * Adds or updates a candidate.
     * @param candidateDto The candidate data transfer object containing candidate information.
     * @return The added or updated candidate data.
     * @throws NotFoundException if the candidate to update is not found.
     * @throws CustomException for other custom exceptions that may occur during processing.
     */
    public CandidateDTO addOrUpdateCandidate(CandidateDTO candidateDto) throws NotFoundException, CustomException;

    /**
     * Deletes a candidate by ID.
     * @param id The unique identifier of the candidate.
     * @return A confirmation message of the deletion.
     * @throws NotFoundException if no candidate is found with the provided ID.
     */
    public void deleteCandidateById(Long id) throws NotFoundException;

    /**
     * Retrieves a paginated list of candidates.
     * @param pageNo The page number of the result set.
     * @param pageSize The number of records per page.
     * @return A pagination object containing candidate data.
     */
    public Pagination getCandidates(Integer pageNo, Integer pageSize);

    /**
     * Searches candidates by name.
     * @param name The name of the candidate to search.
     * @return A list of CandidateDTO objects.
     */
    public List<CandidateDTO> searchByNameSkills(String name);

    /**
     * Finds a candidate by their ID.
     * @param id The unique identifier of the candidate.
     * @return The Candidate entity if found.
     */
    public Candidate findById(Long id);

    /**
     * Searches candidates by name and skills.
     * @param name The name of the candidate.
     * @param skills The skills of the candidate.
     * @return A list of CandidateDTO objects matching the criteria.
     */
//    public Page<Candidate> searchCandidates(String name, String skills);

    /**
     * Retrieves a list of scheduled interviews for a specific candidate.
     * @param candidateId The unique identifier of the candidate.
     * @param pageNumber The page number of the result set.
     * @param pageSize The number of records per page.
     * @return A page of Interview entities.
     */
    public Page<Interview> scheduleInterviewListOfCandidate(Long candidateId, Integer pageNumber, Integer pageSize);

    /**
     * Searches candidates based on detailed criteria.
     * @param searchCandidateDTO The DTO containing search criteria.
     * @param pageNumber The page number of the result set.
     * @param pageSize The number of results per page.
     * @return A Pagination object containing the results.
     */
    public Page<Candidate> searchCandidate(SearchCandidateDTO searchCandidateDTO, Integer pageNumber, Integer pageSize);

    /**
     * Retrieves a single candidate by their ID.
     * @param candidateId The unique identifier of the candidate.
     * @return The CandidateDTO if the candidate is found.
     * @throws NotFoundException if no candidate is found with the provided ID.
     */
    public CandidateDTO getSingleCandidate(Long candidateId) throws NotFoundException;

    /**
     * Changes the active status of a candidate.
     * @param id The unique identifier of the candidate.
     * @param active The new active status of the candidate.
     * @return A string message indicating the result of the operation.
     * @throws NotFoundException if no candidate is found with the provided ID.
     */
    public void changeCandidateStatus(Long id, Boolean active) throws NotFoundException,CustomException;

    /**
     * Validates the uniqueness of an email for a candidate.
     * @param candidateId The ID of the candidate for exclusion in the uniqueness check.
     * @param candidateEmail The email to be validated.
     * @return true if the email is unique; otherwise false.
     */
    public boolean validateEmail(Long candidateId, String candidateEmail);

    /**
     * Validates the phone number of a candidate.
     * @param candidateId The ID of the candidate for exclusion in the uniqueness check.
     * @param candidatePhoneno The phone number to be validated.
     * @return true if the phone number is unique; otherwise false.
     */
    public boolean validatePhoneno(Long candidateId, Long candidatePhoneno);
    /**
     * Validates the candidateDTO 
     * @param candidateDTO
     * @return
     */
    public Boolean validateCandidate(CandidateDTO candidateDTO);
    /**
     * 
     * @param list
     * @return
     */
    public Boolean validateExperinces(Set<CandidateExperienceDTO> list );
}
