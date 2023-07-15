/*
Project: Custom Cover Letter Creator
Created By: Andrew Macdonald
Created On: 2023-07-15

File Name : FileIOController.java
Revision History:
         Andrew Macdonald, 2023-07-15 : Created
*/

package com.andrew_macdonald.custom_cl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.*;

public class FileIOController {

    //region Variables
    String site_name = "JobBank.ca";
    String company_name = "";
    String company_location = "";
    String job_number = "";
    String role_name = "";
    List<String> tasks = new ArrayList<>();
    List<String> skills = new ArrayList<>();
    String folder_name = "";
    String todays_date = get_date();
    XWPFDocument job_posting = null;
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
    @FXML
    private Label outputText;
    //endregion Variables

    //region Functional Methods

    // Gets today's date as a string
    // return: today's date as a string
    private static String get_date() {
        Date today = new Date();
        String month = new SimpleDateFormat("MMMM").format(today);
        String day = new SimpleDateFormat("d").format(today);
        String year = new SimpleDateFormat("yyyy").format(today);
        return String.format("%s %s, %s", month, day, year);
    }

    // Gets the job posting from the user
    // return: the job posting as a XWPFDocument
    private XWPFDocument get_job_posting() throws IOException {
        File job_posting = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Job Posting");
        try {
            job_posting = fileChooser.showOpenDialog(new Stage());
            if (job_posting != null) {
                outputText.setText(job_posting.getName());
            } else {
                outputText.setText("No job posting selected");
            }
        } catch (Exception e) {
            System.out.println("No file selected");
            throw new RuntimeException(e);
        }
        assert job_posting != null;
        FileInputStream fis = new FileInputStream(job_posting);
        return new XWPFDocument(fis);
    }

    // Get the company name from the body of the job posting
    // return: the company name as a string
    private String get_company_name() {
        List<IBodyElement> body = job_posting.getBodyElements();
        //TODO: Find a better way to get the company name without hardcoding the index
        XWPFParagraph paragraph = (XWPFParagraph) body.get(1); // The company name is always the second paragraph when it comes from JobBank.ca.
        String word_to_find = "details";
        int start = paragraph.getText().indexOf(word_to_find) + (word_to_find.length() + 1); // The company name always starts after the word "details" when it comes from JobBank.ca.
        return paragraph.getText().substring(start).trim();
    }

    // Get the job role name from the body of the job posting
    // return: the job role name as a string
    private String get_job_role_name() {
        List<IBodyElement> body = job_posting.getBodyElements();
        //TODO: Find a better way to get the job role without hardcoding the index
        XWPFParagraph paragraph = (XWPFParagraph) body.get(0); // The job role name is always the first paragraph when it comes from JobBank.ca.
        return paragraph.getText().trim();
    }

    // Get the job location from the body of the job posting
    // return: the job location as a string
    private String get_job_location() {
        List<IBodyElement> body = job_posting.getBodyElements();
        //TODO: Find a better way to get the job location without hardcoding the index
        XWPFParagraph paragraph = (XWPFParagraph) body.get(3); // The job location is always the fourth paragraph when it comes from JobBank.ca.
        String word_to_find = "location";
        int start = paragraph.getText().indexOf(word_to_find) + (word_to_find.length() + 1); // The job location always starts after the word "location" when it comes from JobBank.ca.
        return paragraph.getText().substring(start).trim();
    }

    // Get the job number from the body of the job posting
    // return: the job number as a string
    private String get_job_number() {
        List<IBodyElement> body = job_posting.getBodyElements();
        //TODO: Find a better way to get the job number without hardcoding the index
        XWPFParagraph paragraph = (XWPFParagraph) body.get(9); // The job number is always the tenth paragraph when it comes from JobBank.ca.
        String word_to_find = "#";
        int start = paragraph.getText().indexOf(word_to_find) + (word_to_find.length()); // The job number always starts after the word "#" when it comes from JobBank.ca.
        return paragraph.getText().substring(start).trim();
    }

    // Find where the start and end indexes of a section are in the body of the job posting
    // param body: the body of the job posting
    // param section_name: the name of the section to find
    // return: a list of the start and end indexes of the section
    private List<Integer> find_start_end_indexes (List<IBodyElement> body, String section_name) {
        int start_index = -1;
        int end_index = -1;
        int number = 0;
        double font_size = 0;
        boolean start_found = false;
        for (int i = 0; i < body.size(); i++) {
            if (body.get(i) instanceof XWPFParagraph) {
                XWPFParagraph tmp_para = (XWPFParagraph) body.get(i);
                if (tmp_para.getText().contains(section_name)) {
                    start_index = i;
                    start_found = true;
                }
                if (start_found) {
                    if (Objects.equals(tmp_para.getText(), "")) {
                        number++;
                    } else {
                        tmp_para.createRun();
                        font_size = tmp_para.getRuns().get(0).getFontSize();
                        if (font_size == 17) {
                            end_index = i - number;
                            break;
                        }
                    }
                }
            }
        }
        return List.of(start_index, end_index);
    }

    // Get the tasks from the body of the job posting
    // param body: the body of the job posting
    // return: a list of the tasks
    private List<String> get_tasks() {
        List<IBodyElement> body = job_posting.getBodyElements();
        int start_index = find_start_end_indexes(body, "Tasks").get(0);
        int end_index = find_start_end_indexes(body, "Tasks").get(1);
        XWPFParagraph body_para;
        List<String> job_tasks = new ArrayList<>();
        for (int i = start_index; i < end_index - 1; i++) {
            if (body.get(i) instanceof XWPFParagraph) {
                body_para = (XWPFParagraph) body.get(i);
                if (body_para.getText().length() > 0) {
                    job_tasks.add(body_para.getText().toLowerCase() + ", ");
                }
            }
        }
        if (job_tasks.size() < 1) {
            System.out.println("No tasks found, using default tasks");
            job_tasks.add("gather client requirements, ");
            job_tasks.add("develop and produce documentation as part of the System Development Life Cycle, ");
            job_tasks.add("prepare mock-ups and storyboards, ");
            job_tasks.add("design and develop the website/software architecture, hardware, and software requirements");
            job_tasks.add(", and write, modify, and test website code");
        }
        return job_tasks;
    }

    // Get the skills from the body of the job posting
    // param body: the body of the job posting
    // return: a list of the skills
    private List<String> get_skills() {
        List<IBodyElement> body = job_posting.getBodyElements();
        int start_index = find_start_end_indexes(body, "Skills").get(0);
        int end_index = find_start_end_indexes(body, "Skills").get(1);
        XWPFParagraph body_para;
        List<String> job_skills = new ArrayList<>();
        for (int i = start_index; i < end_index - 1; i++) {
            if (body.get(i) instanceof XWPFParagraph) {
                body_para = (XWPFParagraph) body.get(i);
                if (body_para.getText().length() > 0) {
                    job_skills.add(body_para.getText().toLowerCase() + ", ");
                }
            }
        }
        if (job_skills.size() < 1) {
            System.out.println("No skills found, using default skills");
            job_skills.add("C#, ");
            job_skills.add(".NET, ");
            job_skills.add("Java, ");
            job_skills.add("Python, ");
            job_skills.add("APIs, ");
            job_skills.add("SQL (and many of its variations), ");
            job_skills.add("and many database management applications (MySQL, MS SQL Server, etc...).  ");
        }
        return job_skills;
    }

    //endregion Functional Methods

    //region Event Handlers

    // Event handler for the "Select Job Posting" button
    // Gets the job posting from the user
    // param event: the event that triggered the event handler
    @FXML
    protected void onSelectJobPostingButtonClick() {
        try {
            job_posting = get_job_posting();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error getting job posting");
            System.err.println(e.getMessage());
        }
    }


    //endregion Event Handlers
}