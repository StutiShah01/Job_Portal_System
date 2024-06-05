
package com.jobportal.service;

import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.InterviewDTO;

@Service
public interface InterviewService {

    /**
     * Schedules a new interview.
     * @param interviewdto The InterviewDTO containing the interview details.
     * @return The scheduled interview data as an InterviewDTO.
     * @throws CustomException if there is an error during the interview scheduling process.
     */
    public InterviewDTO scheduleInterview(InterviewDTO interviewdto) throws CustomException;

    /**
     * Updates the details of an existing interview.
     * @param interviewdto The InterviewDTO containing the updated details of the interview.
     * @return The updated interview data as an InterviewDTO.
     * @throws CustomException if there is an error during the update process.
     */
    public InterviewDTO updateInterview(InterviewDTO interviewdto) throws CustomException;

    /**
     * Checks if an interview is scheduled for a specific application.
     * @param applicationId The unique identifier of the application to check.
     * @return True if an interview is scheduled, otherwise false.
     */
    public boolean checkInterviewSchedule(Long applicationId);

    /**
     * Cancels a scheduled interview.
     * @param interviewId The unique identifier of the interview to be canceled.
     * @return A string message indicating the result of the operation.
     * @throws NotFoundException if no interview is found with the provided ID.
     * @throws CustomException if there is an error during the cancellation process.
     */
    public void cancelInterview(Long interviewId) throws NotFoundException, CustomException;

    /**
     * Changes the status of an interview.
     * @param id The unique identifier of the interview.
     * @param status The new status to be set for the interview.
     * @return A string message indicating the result of the operation.
     * @throws NotFoundException if no interview is found with the provided ID.
     */
    public String changeInterviewStatus(Long id, String status) throws NotFoundException;
}
