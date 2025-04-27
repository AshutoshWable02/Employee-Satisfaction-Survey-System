
# Employee Satisfaction Survey System

## Overview

The **Employee Satisfaction Survey System** is a web-based platform designed to collect, analyze, and present feedback from employees regarding their job satisfaction. The system allows **administrators** to create and manage surveys, while **employees** can anonymously submit their responses. The system provides insightful analysis of survey results, visualized through **pie charts** and **bar graphs**, with download options available for further use.

## Features

### Admin Features:
- **Create and Manage Surveys**: Admin can create, edit, and delete surveys.
- **Send Survey Invitations**: Admin can manually send survey invitations to employees via email.
- **Survey Analysis**: Admin can view collective responses and generate insights through charts (e.g., pie and bar charts).
- **Department-Wise Breakdown**: Admin can filter and view survey results by department.
- **Download Options**: Admin can download the survey results in **PDF** formats.

### Employee Features:
- **Anonymous Responses**: Employees can submit their responses anonymously without revealing personal identity.
- **Survey Access**: Employees receive unique passwords to access the surveys and submit responses.
- **Survey Completion**: Employees can fill out surveys without registering/logging in, only requiring an email and password to start.

## Tech Stack

- **Frontend**: React.js, HTML, CSS, JavaScript
- **Backend**: Spring Boot 3, Jakarta EE
- **Database**: MySQL
- **Authentication**: Email-based password system for employee access
- **Charting**: JavaScript chart libraries (e.g., Chart.js for visualizations)

## Database Structure

- **Employee**: Stores employee information.
- **Survey**: Stores the survey questions and metadata.
- **SurveyResponse**: Stores employee responses to survey questions.
- **SurveyInvite**: Stores the invitation details sent to employees.

## Usage

1. **Admin Login**: The admin can log in using a dedicated admin account to create, manage, and send surveys.
2. **Employee Login**: Employees can access their surveys by using a unique survey link and password provided by the admin.
3. **Survey Analysis**: Admins can view the survey results in the form of pie charts and bar graphs. The results can be filtered by department.

## Authors

- **Ashutosh Wable**
- **Samiksha Chakre**


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
