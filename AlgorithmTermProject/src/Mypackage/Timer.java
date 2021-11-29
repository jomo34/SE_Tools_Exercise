package Mypackage;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;



public class Timer implements Runnable{

	

	
	public Timer() {
		
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				String time = getCurrentTime();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		
		String time = hour+":"+min+":"+sec;
		return time;
	}
	
	public static void main(String[] args) {
		new Timer();

	}

}
