/*
Project: Custom Cover Letter Creator
Created By: Andrew Macdonald
Created On: 2023-07-15

File Name : FileIOController.java
Revision History:
         Andrew Macdonald, 2023-07-15 : Created
*/

package com.andrew_macdonald.custom_cl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.regex.RegularExpression;

public class FileIOController {

    //region Variables
    final String site_name = "JobBank.ca";
    String folder_path = "";
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
    private int instance_count_role_name = 2;
    private int instance_count_company_name = 3;
    //endregion Variables

    //region Functional Methods

    // Gets the folder path from File.getPath()
    // return: the folder path as a string without the file name at the end
    private void get_folder_path() {
        StringBuilder path = new StringBuilder();
        List<String> directories = List.of(folder_path.split("\\\\"));
        for (int i = 0; i < directories.size() - 1; i++) {
            path.append(directories.get(i)).append("\\");
        }
        folder_path = path.toString();
    }

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
                folder_path = job_posting.getPath();
            } else {
                outputText.setText("No job posting selected");
            }
        } catch (Exception e) {
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
        int start = paragraph.getText().indexOf(word_to_find) + (word_to_find.length()); // The company name always starts after the word "details" when it comes from JobBank.ca.
        String company_name = paragraph.getText().substring(start).trim();
        company_name = (new ArrayList<>(Arrays.asList(company_name.toLowerCase().split(" "))))
                .stream()
                .map(word -> Character.toTitleCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
        return company_name.trim();
    }

    // Get the job role name from the body of the job posting
    // return: the job role name as a string
    private String get_job_role_name() {
        boolean done = false;
        List<IBodyElement> body = job_posting.getBodyElements();
        //TODO: Find a better way to get the job role without hardcoding the index
        XWPFParagraph paragraph = (XWPFParagraph) body.get(0); // The job role name is always the first paragraph when it comes from JobBank.ca.
        return paragraph.getText().replace("VERIFIED", "\s").replaceAll('\u00A0' + "", " ").trim();
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
    private List<Integer> find_start_end_indexes(List<IBodyElement> body, String section_name) {
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
        for (int i = start_index + 1; i < end_index - 1; i++) {
            if (body.get(i) instanceof XWPFParagraph) {
                body_para = (XWPFParagraph) body.get(i);
                if (body_para.getText().length() > 0) {
                    if (i == start_index + 1)
                        job_tasks.add(" " + body_para.getText().toLowerCase() + ", ");
                    else
                        job_tasks.add(body_para.getText().toLowerCase() + ", ");
                }
            }
        }
        if (job_tasks.size() < 1) {
            job_tasks.add(" gather client requirements, ");
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
        int start_index = find_start_end_indexes(body, "Skills").get(0); // Change Skills to Computer and technology knowledge
        int end_index = find_start_end_indexes(body, "Skills").get(1);
        XWPFParagraph body_para;
        List<String> job_skills = new ArrayList<>();
        for (int i = start_index; i < end_index - 1; i++) {
            if (body.get(i) instanceof XWPFParagraph) {
                body_para = (XWPFParagraph) body.get(i);
                body_para.createRun();
                int font_size = body_para.getRuns().get(0).getFontSize();
                if (font_size == 13) {
                    break;
                } else if (body_para.getText().length() > 0) {
                    if (i == start_index)
                        job_skills.add(" " + body_para.getText().toLowerCase() + ", ");
                    else
                        job_skills.add(body_para.getText().toLowerCase() + ", ");
                }
            }
        }
        if (job_skills.size() < 1) {
            job_skills.add("C#, ");
            job_skills.add(".NET, ");
            job_skills.add("Java, ");
            job_skills.add("Python, ");
            job_skills.add("APIs, ");
            job_skills.add("SQL (and many of its variations), ");
            job_skills.add("and many database management applications (MySQL, MS SQL Server, etc...).");
        } else {
            job_skills.add("and many database management applications (MySQL, MS SQL Server, etc...).");
        }
        return job_skills;
    }

    // Get the custom fields to replace and replace them with the appropriate content
    // param paragraphs: the paragraphs of the job posting
    // param paragraph: the paragraph to search for custom fields
    // param index: the index of the paragraph to search for custom fields
    private void find_replace_custom_fields(List<XWPFParagraph> paragraphs, XWPFParagraph paragraph, int index) { //, List<String> replacement_contents) {
        String search_para = paragraph.getText();
        String role_name = get_job_role_name();
        String company_name = get_company_name().trim().replaceAll('\u00A0' + "", " ").trim();
        for (Pattern pattern : custom_fields) {
            Matcher matcher = pattern.matcher(search_para);
            if (matcher.find()) {
                switch (matcher.group()) {
                    case "<DATE>":
                        XWPFParagraph date = paragraphs.get(index);
                        date.createRun();
                        date.getRuns().get(0).setText(get_date(), 0);
                        XWPFParagraph found_paragraph = paragraphs.get(index);
                        break;
                    case "<COMPANY_NAME>":
                        XWPFParagraph company = paragraphs.get(index);
                        company.createRun();
                        if (instance_count_company_name == 3) {
                            company.getRuns().get(0).setText(company_name, 0);
                            instance_count_company_name--;
                        } else if (instance_count_company_name == 2) {
                            company.getRuns().get(7).setText(company_name, 0);
                            instance_count_company_name--;
                        } else if (instance_count_company_name == 1) {
                            company.getRuns().get(5).setText(company_name, 0);
                            instance_count_company_name--;
                        }
                        found_paragraph = paragraphs.get(index);
                        break;
                    case "<ROLE>":
                        XWPFParagraph role = paragraphs.get(index);
                        role.createRun();
                        if (instance_count_role_name == 2) {
                            role.getRuns().get(1).setText(role_name, 0);
                            instance_count_role_name--;
                        } else if (instance_count_role_name == 1) {
                            role.getRuns().get(1).setText(role_name, 0);
                            instance_count_role_name--;
                        }
                        found_paragraph = paragraphs.get(index);
                        break;
                    case "<LOCATION>":
                        XWPFParagraph location = paragraphs.get(index);
                        location.createRun();
                        location.getRuns().get(0).setText(get_job_location(), 0);
                        found_paragraph = paragraphs.get(index);
                        break;
                    case "<JOB_NUMBER>":
                        XWPFParagraph job_number = paragraphs.get(index);
                        job_number.createRun();
                        job_number.getRuns().get(0).setText(get_job_number(), 0);
                        found_paragraph = paragraphs.get(index);
                        break;
                    case "<TASKS>":
                        StringBuilder replacement_tasks = new StringBuilder();
                        List<String> task_strings = get_tasks();
                        for (String task : task_strings) {
                            replacement_tasks.append(task);
                        }
                        XWPFParagraph tasks = paragraphs.get(index);
                        tasks.createRun();
                        tasks.getRuns().get(5).setText(String.valueOf(replacement_tasks), 0);
                        found_paragraph = paragraphs.get(index);
                        break;
                    case "<SKILLS>":
                        StringBuilder replacement_skills = new StringBuilder();
                        List<String> skill_strings = get_skills();
                        for (String skill : skill_strings) {
                            replacement_skills.append(skill);
                        }
                        XWPFParagraph skills = paragraphs.get(index);
                        skills.createRun();
                        skills.getRuns().get(8).setText(String.valueOf(replacement_skills), 0);
                        found_paragraph = paragraphs.get(index);
                        break;
                    case "<SITE_NAME>":
                        XWPFParagraph site_name = paragraphs.get(index);
                        site_name.createRun();
                        site_name.getRuns().get(3).setItalic(true);
                        site_name.getRuns().get(3).setText(this.site_name, 0);
                        found_paragraph = paragraphs.get(index);
                        break;
                }
            }
        }
    }

    // Write the custom cover letter to a file and save it
    // param template_cover_letter: the template cover letter
    private void write_custom_cover_letter(XWPFDocument template_cover_letter) throws IOException {
        String file_name = "Cover Letter.docx";
        File file = new File(folder_path);
        // Check if the directory exists, if not, create it
        if (!file.exists()) {
            boolean file_made = file.mkdirs();
        }
        // Create the output stream and write the file
        FileOutputStream out_stream = new FileOutputStream(new File(folder_path + "\\" + file_name));
        template_cover_letter.write(out_stream);

        // Close the output stream
        out_stream.close();
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
            instance_count_role_name = 2;
            instance_count_company_name = 3;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Event handler for the "Make Custom CL" button
    // Makes a custom cover letter for the job posting
    // param event: the event that triggered the event handler
    @FXML
    protected void onMakeCustomCLButtonClick() throws IOException {
        outputText.setText("Creating ...");

        // Get the folder path
        get_folder_path();
        outputText.setText("Got folder path ...");


        // get the template for the cover letter
//        XWPFDocument template_cover_letter = new XWPFDocument(new FileInputStream("./templates/CL_Template.docx")); // This works from the IDE
        XWPFDocument template_cover_letter = new XWPFDocument(new FileInputStream("D:\\cover_letter_maker\\VERSION_01\\Custom_CL\\templates\\CL_Template.docx")); // This works from the JAR
        outputText.setText("Got Template ...");

        // Get all paragraphs from template
        List<XWPFParagraph> paragraphs = template_cover_letter.getParagraphs();
        outputText.setText("Got paragraphs ...");

        // Find that paragraph that contains the custom field
        int index = 0;
        for (XWPFParagraph paragraph : paragraphs) {
            if (!Objects.equals(paragraph.getText(), "")) {
                // Replace the custom field with the appropriate text
                find_replace_custom_fields(paragraphs, paragraph, index);
            }
            index++;
        }
        // Write the cover letter to a file
        write_custom_cover_letter(template_cover_letter);
        outputText.setText("Writing ...");

        // Close the template
        template_cover_letter.close();
        outputText.setText("Cover Letter Created!");

    }

    //endregion Event Handlers
}