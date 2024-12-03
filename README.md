# Cover Letter Maker

A smart, efficient application designed to simplify the job application process by generating tailored cover letters based on job postings. Whether you're applying for your dream role or managing multiple applications, Cover Letter Maker helps you create professional and personalized cover letters with ease.

---

## 🌟 Key Features

- **Automated Cover Letter Generation**  
  Parses job posting details from a provided document (in a specific format) and substitutes them into a predefined cover letter template, replacing placeholders with relevant information.

- **Customizable Templates**  
  Use the built-in templates or upload your own to suit your style and preferences.

- **Dynamic Field Mapping**  
  Easily map parsed job data to the appropriate sections of the cover letter.

- **Progress Tracking**  
  Manage your job applications by saving and categorizing generated cover letters.

- **Cross-Platform Support**  
  With the transition to a Progressive Web App (PWA) using Next.js, Cover Letter Maker will run seamlessly across desktop and mobile platforms, enabling offline access and improved performance.

---

## 🛠️ Technologies Used

- **Frontend:**  
  - Next.js *(PWA version)*  
  - JavaFX *(legacy desktop version)*  

- **Backend:**  
  - Node.js *(for PWA backend logic)*  
  - Java *(for parsing and generation logic in the legacy version)*  

- **Data Parsing and Processing:**  
  - Custom parsing logic to extract data from job posting documents.  

- **Storage:**  
  - Local file system *(legacy)*  
  - IndexedDB *(PWA)*  

---

## 🚀 Getting Started

### Prerequisites

- For the PWA: A modern browser with PWA support (e.g., Chrome, Edge, Firefox).
- For the legacy version: Java Runtime Environment (JRE).

### Installation

#### PWA Version

1. Clone the repository:
   ```bash
   git clone https://github.com/A-Mac2513/cover-letter-maker.git
   ```

2. Navigate to the project directory:
   ```bash
   cd VERSION_02/cover-letter-maker
   ```

3. Install dependencies:
   ```bash
   npm install
   ```

4. Start the development server:
   ```bash
   npm run dev
   ```

5. Access the app at [http://localhost:3000](http://localhost:3000).


#### Legacy Desktop Version

1. Clone the repository:
   ```bash
   git clone https://github.com/A-Mac2513/cover-letter-maker.git
   ```

2. Navigate to the project directory:
   ```bash
   cd VERSION_01/Custom_CL
   ```

3. Run the application:
   ```bash
   java -jar Custom_CL.jar
   ```


## 📜 License

This project is licensed under the GNU Lesser General Public License (LGPL) Version 2.1, February 1999. See the [LICENSE](LICENSE) file for details.


## 🤝 Contributing

Contributions are welcome! If you’d like to improve the app or suggest features, please follow these steps:

1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
    ```bash
    git commit -m "Add feature name"
    ```
4. Push to the branch:
    ```bash
    git push origin feature-name
    ```
5. Open a pull request.


## 🛠️ Future Enhancements
- Enhanced document parsing with support for additional formats.
- Integration with cloud storage services for saving and syncing cover letters.
- Multilingual support for international job applications.
- AI-powered cover letter suggestions and improvements.

## 🙋 Support
If you have any issues or questions, please feel free to open an issue in this repository or contact the developer at amacdonald952@gmail.com.