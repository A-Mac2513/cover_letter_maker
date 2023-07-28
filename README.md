# cover_letter_maker
My personal app to use a job posting to customize a cover letter template to the posting.

## Version 1 ##
In this version the app will have limited functionality.  It will have single button as the UI, in which once it has been clicked the user chooses the file, and the Custom Cover Letter will be generated in the same directory the file was chosen from.
### Things I learned ###
I used the app to remove the word "VERIFIED" from the string that came back when I grabbed the Role_Name.  Problem was it replaced it with a non-breaking space instead of a whitespace.  So I had no idea why i was getting a role name with extra spaces at the end in my cover letter file.  After 2hrs of playing with regex and string manipulation, I got the bright idea (should have maybe started here) to print out the ascii code, look that up to find out what it was, then use the unicode for a non-breaking space to put into the replaceAll().
When I created a jar file, and ran that jar file, some of the File paths no longer worked as they did in the IDE.


## Version 2 ##
In this version the app will have the same functionality as v1, however, added will be a drop down to select where the job posting came from (the most popular 3 or 4 to start).
From this dropdown selection, the app with know the template of the specific job posting format and find the needed information this way.
May also have a form that the information can be manually enetered in or edited after parsing the file.

## Version 3 ##
In this version of the app my hope is the app will scrape the information from the job posting directly from the web.  Display the information on a form, user confirms or edits then confirms.
The app will also generate the email and send it.  Or mayble even apply for the user via the given method from the job posting. May try to customize the RESUME in this version as well.
