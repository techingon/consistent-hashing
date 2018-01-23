package com.echx;

import java.io.Serializable;

/**
 * 
 * @author echov
 */
public class Trigger implements Serializable{
	
	private static final long serialVersionUID = -4238947011584134229L;

	private String id;

	private String name;

	private String cron;

	private String job_id;

	private int timeout;

	private long lastRunTime = -1;

	private Status _status = Status.INITED;

	private int status = _status.getIntValue();


	public Trigger(){
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}


	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Status _getStatus(){
		return Status.fromIntValue(status);
	}

	public long getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	public String getJob_id() {
		return job_id;
	}

	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static enum Status{

		INITED(1),
		DEPLOYED(2),
		UNDEPLOYED(3);

		private int status;
		Status(int status){
			this.status = status;
		}
		public int getIntValue(){
			return status;
		}

		public static Status fromIntValue(int status){
			for (Status s:
					Status.values()) {
				if(s.getIntValue() == status){
					return s;
				}
			}
			throw new IllegalStateException("Trigger Status:"+status+" not exist!");
		}
	}
}
