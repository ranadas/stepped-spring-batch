package com.rdas.springbatchexamples.multithreadedstep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

public class StepExecutionNotificationListener extends StepExecutionListenerSupport{
	
	private static final Logger logger = LoggerFactory.getLogger(StepExecutionNotificationListener.class);

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info("\nAfter step");
		return super.afterStep(stepExecution);
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info("\nBefore step");
		super.beforeStep(stepExecution);
	}
	
	
}
