package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Student{
	private String studentId;
	private ArrayList<Course> courseTaken = new ArrayList<Course>();
	private HashMap<String, Integer> semestersByYearAndSemester;
	//key: Year-Semester	e.g., 2003-1
	
	public Student(String studentId) {
		this.studentId = studentId;
	}
	
	public void addCourse(Course newRecord) {
		courseTaken.add(newRecord);
	}
	
	public ArrayList<Course> getCourse(){
		return courseTaken;
	}
	
	public HashMap<String, Integer> getSemestersByYearAndSemester(){
		HashMap<String, Integer> keyAndValue = new HashMap<String, Integer>();
		
		int i = 0;
		int nthSemester = 1;
		while(i < courseTaken.size()-1) {
			Course firstCourse = courseTaken.get(i);
			Course secondCourse = courseTaken.get(i+1);
			
			if(firstCourse.getSemesterCourseTaken() != secondCourse.getSemesterCourseTaken() || firstCourse.getYearTaken() != secondCourse.getYearTaken()) {
				String yearTaken = Integer.toString(firstCourse.getYearTaken());
				String semesterCourseTaken = Integer.toString(firstCourse.getSemesterCourseTaken());
				String yearAndSemesterOfSemesters = yearTaken + "-" + semesterCourseTaken;
				keyAndValue.put(yearAndSemesterOfSemesters, nthSemester);
				nthSemester++;
			}
			if((i+1) == courseTaken.size()-1 && (firstCourse.getSemesterCourseTaken() != secondCourse.getSemesterCourseTaken()
					|| firstCourse.getYearTaken() != secondCourse.getYearTaken())) {
				String yearTaken = Integer.toString(secondCourse.getYearTaken());
				String semesterCourseTaken = Integer.toString(secondCourse.getSemesterCourseTaken());
				String yearAndSemesterOfSemesters = yearTaken + "-" + semesterCourseTaken;
				keyAndValue.put(yearAndSemesterOfSemesters, nthSemester);
			}
				
			i++;
		}
		
		return keyAndValue;
	}
	
	public int getNumCourseInNthSemester(int semester) {
		semestersByYearAndSemester = getSemestersByYearAndSemester();
		int yearTaken = 0;
		int semesterTaken = 0;
		int numCourseInNthSemster = 0;
		
		for(String nthSemester : semestersByYearAndSemester.keySet()) {
			if(semestersByYearAndSemester.get(nthSemester) == semester) {
				yearTaken = Integer.parseInt(nthSemester.split("-")[0]);
				semesterTaken = Integer.parseInt(nthSemester.split("-")[1]);
				break;
			}
		}
		
		for(int i = 0; i < courseTaken.size(); i++) {
			Course reservedCourse = courseTaken.get(i);
			if(yearTaken == reservedCourse.getYearTaken() && semesterTaken == reservedCourse.getSemesterCourseTaken()) {
				numCourseInNthSemster++;
			}
		}
		
		return numCourseInNthSemster;
	}
	
	public int getTotalNumberOfSemestersRegistered() {
		semestersByYearAndSemester = getSemestersByYearAndSemester();
		int totalNumberOfSemestersRegistered = 0;
		for(String nthSemester : semestersByYearAndSemester.keySet()) totalNumberOfSemestersRegistered++;
		
		return totalNumberOfSemestersRegistered;
	}
}