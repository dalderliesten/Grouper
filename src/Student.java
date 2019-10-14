import java.util.List;

/**
 * Class to store students to improve code readability. Responsible for mostly
 * data storage and within student calculations.
 * 
 * @author David Alderliesten
 *
 */
public class Student implements Comparable<Student> {
	// Variables to store relevant student information and grade.
	int studentID;
	double averageGrade;

	// Variable to store participation status.
	boolean isActive;

	/**
	 * Constructor for the student object, which generates the object and calculates
	 * relevant aspects required for the tool. Also sets passed arguments as local
	 * variables.
	 * 
	 * @param studentID the identification parameter for the student.
	 * @param grades    the list of grades for the student.
	 */
	public Student(int studentID, List<Double> grades) {
		this.studentID = studentID;
		this.isActive = false;

		for (Double currentGrade : grades) {
			this.averageGrade += currentGrade;
			this.isActive = true;
		}

		this.averageGrade = this.averageGrade / grades.size();
	}

	/**
	 * Returns a string representation of the Student object.
	 * 
	 * @return a string with information.
	 */
	@Override
	public String toString() {
		return "Student: " + this.studentID + ", with an average grade of: " + this.averageGrade;
	}

	/**
	 * Forces comparison on average grades, in ascending order, meaning lowest grade
	 * first and highest last.
	 */
	@Override
	public int compareTo(Student other) {
		return Double.compare(this.averageGrade, other.averageGrade);
	}

}
