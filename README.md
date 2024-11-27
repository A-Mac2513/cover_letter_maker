# Cover Letter Maker
A tool to customize cover letter templates based on job postings.

## Features
- **Dynamic Placeholder Replacement:** Customize a cover letter template by replacing placeholders like `<ROLE>` and `<COMPANY_NAME>` with details from a job posting.
- **Simple UI:** A single button to process the job posting and generate a cover letter.
- **Output Location:** The customized cover letter is saved in the same directory as the input file.

## Getting Started
### Prerequisites
- **Java 11+**
- **Apache POI Library** (for `.docx` manipulation)
- **JavaFX** (for the UI)

### Usage
1. Run the app (via IDE or JAR file).
2. Click **"Select Job Posting"** to choose a `.txt` or `.docx` file containing the job posting.
3. Click **"Make Custom CL"** to generate the cover letter.
4. The cover letter (`Cover Letter.docx`) will be saved in the same directory as the input file.

## Lessons Learned
- Resolved issues with non-breaking spaces (`\u00A0`) affecting string replacements.
- Addressed file path differences when running the app as a JAR file.

## Roadmap
### Version 2 (Planned)
- Dropdown to handle multiple job posting formats.
- Manual data entry/editing post-parsing.
- PowerShell integration for `.pdf` conversion.

### Version 3 (Planned)
- Web scraping for job postings.
- Automatic application submission.
- Resume customization.
