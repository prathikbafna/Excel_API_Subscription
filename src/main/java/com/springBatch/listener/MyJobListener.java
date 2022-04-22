package com.springBatch.listener;

import java.util.Date;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListener implements JobExecutionListener{

	@Override
	public void beforeJob(JobExecution je) {
		System.out.println("Started Date and time : "+ new Date());
		System.out.println("Statue at Start : "+ je.getStatus());
		
	}

	@Override
	public void afterJob(JobExecution je) {
		// TODO Auto-generated method stub
		System.out.println("End Date and time : "+ new Date());
		System.out.println("Statue at End : "+ je.getStatus());
		
	}

}
