package com.jobportal.serviceImpl;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.InterviewDTO;
import com.jobportal.entity.Application;
import com.jobportal.entity.Interview;
import com.jobportal.enumconstants.ApplicationStatus;
import com.jobportal.enumconstants.InterviewStatus;
import com.jobportal.mappers.InterviewMapper;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.InterviewRepository;
import com.jobportal.service.InterviewService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InterviewServiceImpl implements InterviewService {

	@Autowired
	InterviewRepository interviewRepository;
	
	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	InterviewMapper interMapper;
	
	/**
     * Schedules an interview for an application.
     *
     * @param interviewdto The interview details to be scheduled.
     * @return InterviewDTO containing details of the scheduled interview.
     * @throws CustomException If the candidate has withdrawn the application or the interview date is before the application submission date.
     */
	@Override
	public InterviewDTO scheduleInterview(InterviewDTO interviewdto) throws CustomException {
		
		Interview interviewToSchedule=new Interview();
		Optional<Application> app=applicationRepository.findById(interviewdto.getApplicationId());
		Application application=app.get();
		interviewToSchedule.setApplication(application);
		interviewToSchedule.setInterviewType(interviewdto.getInterviewType());
		interviewToSchedule.setInterviewDate(interviewdto.getInterviewDate());
		if(application.getIsApplied() == false)
		{
			throw new CustomException("Candidate has withdrawed the application");
		}
		if(interviewdto.getInterviewDate().before(application.getSubmittedDate()) )
		{
			 throw new CustomException("Schedule proper interview date");
		}
		application.setApplicationStatus( ApplicationStatus.INTERVIEW_SCHEDULED.name());
		applicationRepository.save(application);
		Interview interviewSaved=interviewRepository.save(interviewToSchedule);
		InterviewDTO interviewDTOResponse= interMapper.toInterviewDTO(interviewSaved);
		log.info("Interview scheduled successfully for application: {}", interviewSaved.getApplication().getId());
		return interviewDTOResponse;
	}

	@Override
	public boolean checkInterviewSchedule(Long applicationId) {
		
		return interviewRepository.existsByApplication_Id(applicationId);
	}
	
	
	/**
     * Updates an existing interview schedule.
     *
     * @param interviewdto The updated interview details.
     * @return InterviewDTO containing details of the updated interview.
     * @throws CustomException If the interview is not found, the candidate has withdrawn the application, or the interview date is before the application submission date.
     */
	@Override
	public InterviewDTO updateInterview(InterviewDTO interviewdto) throws CustomException {
		Interview interview=interviewRepository.findById(interviewdto.getId()).orElseThrow(()->new CustomException("Interview Not Schedule"));
		Application application=applicationRepository.findById(interviewdto.getApplicationId()).orElseThrow(()->new CustomException("No Application Found"));
//		Application application=app.get();
		interview.setApplication(application);
		interview.setInterviewDate(interviewdto.getInterviewDate());
		interview.setInterviewType(interviewdto.getInterviewType());
		if(application.getIsApplied() == false)
		{
			throw new CustomException("Candidate has withdrawed the application");
		}
		if(interviewdto.getInterviewDate().before(application.getSubmittedDate()) )
		{
			 throw new CustomException("Schedule proper interview date");
		}
		application.setApplicationStatus(ApplicationStatus.INTERVIEW_SCHEDULED.name());
		applicationRepository.save(application);
		Interview updatedInterview=interviewRepository.save(interview);
		InterviewDTO interviewdtoresponse=interMapper.toInterviewDTO(updatedInterview);
		return interviewdtoresponse;
	}

	/**
	 * @param interviewId for which you want to cancel the interview
	 * @throws NotFoundException If the interview is not found
	 * @return String which give status 
	 * @throws CustomException 
	 */
	public void cancelInterview(Long interviewId) throws NotFoundException, CustomException {
		Interview interview=interviewRepository.findById(interviewId).orElseThrow(()->new NotFoundException("Interview Not Found"));
		Optional<Application> app=applicationRepository.findById(interview.getApplication().getId());
		Application application=app.get();
		if(interview.getInterviewStatus().equals(InterviewStatus.CANCELLED.name()))
		{
			throw new CustomException("Interview Already Cancelled");
		}
		application.setApplicationStatus(ApplicationStatus.CANCELLED.name());
		applicationRepository.save(application);
		interview.setInterviewStatus(InterviewStatus.CANCELLED.name());
		interviewRepository.save(interview);
		log.info("interview cancelled successfully ");
//		return "Interview Cancelled";
	}

	@Override
	public String changeInterviewStatus(Long id, String status) throws NotFoundException {
		Interview interview =interviewRepository.findById(id).orElseThrow(()->new NotFoundException("Interview Not Found"));
		Optional<Application> app=applicationRepository.findById(interview.getApplication().getId());
		Application application=app.get();
		String requestStatus=status.toUpperCase();
		if(requestStatus.equals(InterviewStatus.SELECTED.name()))
		{
		 Set<String> ineligibleStatuses = Set.of(InterviewStatus.CANCELLED.name(),
							                     InterviewStatus.REJECTED.name(),
							                     InterviewStatus.SELECTED.name());
			 if (!ineligibleStatuses.contains(interview.getInterviewStatus()))
			 {
					if(!application.getApplicationStatus().equals(ApplicationStatus.WITHDRAW.name()))
					{
					application.setApplicationStatus(ApplicationStatus.SELECTED.name());
					applicationRepository.save(application);
					}
					else {
						return "APPLICATION HAS ALREADY BEEN WITHDRAWED";
					}
					interview.setInterviewStatus(InterviewStatus.SELECTED.name());
					interviewRepository.save(interview);
					return "STATUS SET TO: "+InterviewStatus.SELECTED.name()+ "FOR INTERVIEW ID: "+interview.getId();
			}else{
						return "INTERVIEW STATUS IS ALREADY " + interview.getInterviewStatus() ;
				}
		}
		else if(requestStatus.equals(ApplicationStatus.REJECTED.name()))
		{
			if(interview.getInterviewStatus().equals(InterviewStatus.SCHEDULED.name()) || 
					interview.getInterviewStatus().equals(InterviewStatus.SELECTED.name()))
			{
				if(!application.getApplicationStatus().equals(ApplicationStatus.WITHDRAW.name()))
				{
				application.setApplicationStatus(ApplicationStatus.REJECTED.name());
				applicationRepository.save(application);
				}
				else {
					return "APPLICATION HAS ALREADY BEEN WITHDRAWED";
				}
				interview.setInterviewStatus(InterviewStatus.REJECTED.name());
				interviewRepository.save(interview);
				return "STATUS SET TO: "+InterviewStatus.REJECTED.name()+ " FOR INTERVIEW ID: "+interview.getId();
			}
			else
			{
				return "INTERVIEW IS ALREADY "+ interview.getInterviewStatus() ;
			}
			
		}
		else {
		return "Not Valid Status";
		}
	}

}
