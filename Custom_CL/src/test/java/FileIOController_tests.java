/*
Project: Custom Cover Letter Creator
Created By: Andrew Macdonald
Created On: 2023-07-15

File Name : FileIOController_tests.java
Revision History:
         Andrew Macdonald, 2023-07-15 : Created
*/

//Make unit tests for the FileIOController class
package com.andrew_macdonald.custom_cl;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class FileIOController_tests {
    //region Variables
    String site_name = "JobBank.ca";
    String company_name = "";
    String company_location = "";
    String job_number = "";
    String role_name = "";
    List<String> tasks = new ArrayList<>();
    List<String> skills = new ArrayList<>();
    String folder_name = "";
    String todays_date = getDate();
    File job_posting = null;
    XWPFDocument cover_letter = null;
    final Pattern date_pattern = Pattern.compile("<DATE>");
    final Pattern company_pattern = Pattern.compile("<COMPANY_NAME>");
    final Pattern role_pattern = Pattern.compile("<ROLE>");
    final Pattern location_pattern = Pattern.compile("<LOCATION>");
    final Pattern job_number_pattern = Pattern.compile("<JOB_NUMBER>");
    final Pattern tasks_pattern = Pattern.compile("<TASKS>");
    final Pattern skills_pattern = Pattern.compile("<SKILLS>");
    final Pattern site_name_pattern = Pattern.compile("<SITE_NAME>");
    List<Pattern> custom_fields = List.of(new Pattern[]{
            date_pattern,
            company_pattern,
            role_pattern,
            location_pattern,
            job_number_pattern,
            tasks_pattern,
            skills_pattern,
            site_name_pattern
    });
    //endregion Variables

    //region Tests
    //region Test getDate()
    @Test
    void getDate() {
        //Arrange
        String expected = "2023-07-15";
        //Act
        String actual = getDate();
        //Assert
        assertEquals(expected, actual);
    }
    //endregion Test getDate()

    //region Test getJobPosting()
    @Test
    void getJobPosting() {
        //Arrange
        String expected = "src\\main\\java\\com\\andrew_macdonald\\custom_cl\\job_posting.txt";
        //Act
        String actual = getJobPosting();
        //Assert
        assertEquals(expected, actual);
    }
    //endregion Test getJobPosting()

