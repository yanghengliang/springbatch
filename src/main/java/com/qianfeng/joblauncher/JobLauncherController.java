package com.qianfeng.joblauncher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLauncherController {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job jobLauncherDemoJob;
	
	// RestFul风格。。。
	@GetMapping("/job/{msg}")
	public String jobRun1(@PathVariable String msg) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		//把接收到的参数值传给任务。
		JobParameters parameters = new JobParametersBuilder().addString("msg", msg).toJobParameters();
		//启动任务，并把参数传给任务
		jobLauncher.run(jobLauncherDemoJob, parameters);
		
		return "job success";
		
	}

}
