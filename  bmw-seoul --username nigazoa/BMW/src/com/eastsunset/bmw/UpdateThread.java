package com.eastsunset.bmw;

class UpdateThread extends Thread
{
		
	 private UpdateListener listener;
	 
	 	UpdateThread(UpdateListener listener){
	 		this.listener = listener;
	 	}
	 
       @Override
       public void run()
       {
       	while(true){
       		try {
       			synchronized(this){
       				wait();
       			}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//update("http://115.84.164.20:19082/traffic/BusInfoSearch.do?category=1&busSearch=1111");
				listener.updateFinished();
				
       	}
       }
       
       public void setNotify(){
			synchronized(this){
				notify();
			}
       }
}