package com.qianfeng.joboperator;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job2")
public class JobOperatorController {
	
	@Autowired
	private JobOperator jobOperator; //注意需要自己生成Bean，不能由框架来自动注入!!!
	
	// RestFul风格。。。
	@GetMapping("/{msg2}")
	public String jobRun1(@PathVariable String msg2) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobException, JobInstanceAlreadyExistsException {
		//启动任务，同时传参数
		jobOperator.start("jobOperatorDemoJob", "msg="+msg2); //以字符串的形式指明了一下任务，并不存在任务对象，需要通过注册operator.setJobRegistry(jobRegistry);把具体的任务关联起来。
		return "job success";
		
	}

}
