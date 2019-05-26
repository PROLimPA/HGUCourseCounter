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
		HashMap<String, Integer> keyAndValue = new HashMap<String, Integer>();
		
		int i = 0;
		int nthSemester = 1;
		while(i <= courseTaken.size()) {
			Course firstCourse = courseTaken.get(i);
			Course secondCourse = courseTaken.get(i+1);
			
			if(firstCourse.getSemesterCourseTaken() != secondCourse.getSemesterCourseTaken() || (i+1) == courseTaken.size()) {
				String yearTaken = Integer.toString(firstCourse.getYearTaken());
				String semesterCourseTaken = Integer.toString(firstCourse.getSemesterCourseTaken());
				String yearAndSemesterOfSemesters = yearTaken + "-" + semesterCourseTaken;
				keyAndValue.put(yearAndSemesterOfSemesters, nthSemester);
				nthSemester++;
			}
			i++;
		}
		
		return keyAndValue;
	}
	
	public int getNumCourseInNthSemester(int semester) {
		semestersByYearAndSemester = getSemestersByYearAndSemester();
		
		int numCourseInNthSemester = 0;
		
		for(String takenInfo : semestersByYearAndSemester.keySet()) {
			String yearTaken = takenInfo.split("-")[0];
			String semesterTaken = takenInfo.split("-")[1];
			
			String courseYearTaken = Integer.toString(courseTaken.get(0).getYearTaken());
			String courseSemesterTaken = Integer.toString(courseTaken.get(0).getSemesterCourseTaken());
			if(courseYearTaken == yearTaken && courseSemesterTaken==semesterTaken)
		}
		
		
		
		return 0;
	}
}