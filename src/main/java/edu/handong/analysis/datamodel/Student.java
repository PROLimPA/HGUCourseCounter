package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Student{
	private String studentId;
	private ArrayList<Course> courseTaken;
	private HashMap<String, Integer> semestersByYearAndSemester;
	//key: Year-Semester	e.g., 2003-1
	
	public Student(String studentId) {
		this.studentId = studentId;
	}
	
	public void addCourse(Course newRecord) {
		courseTaken = new ArrayList<Course>();
		courseTaken.add(newRecord);
	}
	
	public HashMap<String, Integer> getSemestersByYearAndSemester(){
		semestersByYearAndSemester = new HashMap<String, Integer>();
		
		return semestersByYearAndSemester;
	}
	
	public int getNumCourseInNthSemester(int semester) {
		
	}
}