package com.jobportal.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jobportal.dto.JobDTO;
import com.jobportal.dto.JobWithApplicationDTO;
import com.jobportal.entity.Job;

@Component
public class JobMapper {

		public Job toJob(JobDTO jobdto)
		{
			Job job=new Job();
			BeanUtils.copyProperties(jobdto, job);
			return job;
		}
		public JobDTO tojobDtO(Job job)
		{
			JobDTO dto =new JobDTO();
			BeanUtils.copyProperties(job, dto);
			dto.setEmployerId(job.getEmployer().getId());
			dto.setTagId(job.getTags().getId());
			return dto;
		
		}
		public JobWithApplicationDTO toJobWithApplicationDTO(Job job)
		{
			JobWithApplicationDTO dto=new JobWithApplicationDTO();
			BeanUtils.copyProperties(job, dto);
			dto.setEmployerId(job.getEmployer().getId());
			return dto;
		}
		public List<JobDTO> jobDTOs(List<Job> jobs)
		{
			List<JobDTO> jobdtolist = new ArrayList<>();
			for (Job job : jobs) {
					jobdtolist.add(tojobDtO(job));
			}
			return jobdtolist;
		}
		
}
