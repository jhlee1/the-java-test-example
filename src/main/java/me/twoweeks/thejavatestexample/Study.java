package me.twoweeks.thejavatestexample;

/**
 * Created by Joohan Lee on 2020/03/27
 */
public class Study {

  private StudyStatus studyStatus;
  private int limit;
  private String name;

  public StudyStatus getStudyStatus() {
    return studyStatus;
  }

  public int getLimit() {
    return limit;
  }

  public String getName() {
    return name;
  }

  public Study(int limit, String name) {
    this.limit = limit;
    this.name = name;
  }

  public Study(int limit) {
    if (limit < 0) {
      throw new IllegalArgumentException("Limit은 0보다 커야한다.");
    }
    this.limit = limit;
  }


  @Override
  public String toString() {
    return "Study{" +
        "studyStatus=" + studyStatus +
        ", limit=" + limit +
        ", name='" + name + '\'' +
        '}';
  }
}
