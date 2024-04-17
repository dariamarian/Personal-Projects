# YouTube Comment Filter Automation

Automate YouTube comment analysis with UiPath RPA for efficient sentiment categorization and reporting.

## Overview

- **Purpose:** Streamline comment analysis on YouTube.
- **Objectives:**
  1. Enhance Efficiency in Comment Analysis
  2. Streamline Categorization through AI
  3. Speed up Comment Processing

## Prerequisites

- Filled Process Definition Document
- Test Data for Development
- Valid Email and YouTube Link
- Robust Internet Connection

## AS IS Process

- **Process Time:** >1 hour
- **Frequency:** 3 videos/day
- **Tools:** Excel, Chrome, OpenAI GPT-3.5

## TO BE Process

Automate:

1. Access YouTube and Locate Video
2. Scrape Comments (maxResults)
3. Categorize Comments
4. Compile Report
5. Analyze Data for Insights
6. Generate Comment Analysis Report
7. Send Report via Email

## In Scope for RPA

- YouTube Video Selection
- Web Scraping for Comments
- Comment Categorization
- Results Compilation
- Email Notification
- Exception Handling
- User Interface Interactions
- Data Validation

## Out of Scope for RPA

- Handwritten Data Entry
- Highly Dynamic Web Pages

## Exception Handling

Handle known business exceptions and OpenAI rate limits.

## Reporting

- Type: YouTube Comment Filter - Analysis Report
- Update Frequency: After each analysis
- Tools: UiPath, Excel, SMTP

### Email template:

**From:** raportgenerationemail@gmail.com

**To:** [Recipient's email]

**Subject:** YouTube Comment Filter - Analysis Report


Dear [Recipient's email],



We hope this email finds you well.

Attached, you'll find the analysis report for the video/s:
- [Bullets for video(s) name(s)]

Should you have any questions or require further information, please don't hesitate to reach out.

Best regards,

Dev Team  

Contact us: raportgenerationemail@gmail.com


## Additional Documentation

- [Video Recording of the Process](https://www.youtube.com/watch?v=PnRUnP_1Nig)
- [Business Exceptions Flows](https://www.youtube.com/watch?v=fxz8f5Agv58)

For inquiries, contact: raportgenerationemail@gmail.com