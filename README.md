# HTML-Compiler
## This is a Java FX program that allows a user to specify the directory of nav, footer, and project and compile them into one.

## Features
All features below will be explained later.
* User can specify: nav, footer and project directory
* Ignore file, of directories and files in the root of th eproject to ignore
* Use of html class currentPage

## Nav, Footer, Project Directories

### Nav and Footer Directories
Both the nav and footer should have their own folder each with its corresponding files. 
Each file in either the nav or footer directory should be named #.html, where the # corresponds to the depth.
Depth in this application is defined as how deep a file is in the project directory. Files at the root have a depth of 1, those in a folder in root will have a depth of 2 and so on. 
So, as a file gets deeper in the project, links to other files and pages in the website can change.
That is why each nav and footer file gets name corresponding to the depth of the html file it should be inserted into once compiled.

### Project Directory 
The project directory, is just the directory where all of the html files to be compiled are stored.
Each file in the project that needs compiled had to have flags to show the program where to insert the header and footer.
  
The flags are 
```html
<!--nav-->       To show where to start inserting the nav
<!--/nav-->      To show where to stop inserting the nav
<!--footer-->    To show where to start inserting the footer
<!--/footer-->   To show where to stop inserting the footer
```

## Ignore File
The ignore file is a text file in the CSV format, where each directory or file in the root of the project is seperated with a comma.
An example of such file would be one that ignores the css folder and a google.html file.

This  file would be written: css, google.html

Now both of these files will be ignored, increasing runtime as the css directory won't have to be mapped and the potential for an error either the google.html file is removed.

Do note that files that don't end in .html are automatically ignored, for example sitemap.xml doesn't need to be in the ignore file.

## currentPage HTML Class
This is a class I often use myself when doing a web page, on the <a> element of the page the user is on the html class="currentPage" is there so that on the nav it will look different from the rest.
This different can be anything the developer devices, usually a different color font.

The way the program determines the current page is it looks at a file and finds the name if its containing folder.
After that it then looks in the nav and finds the line that corresponds to that directory and appends the class="currentPage" there.

This is what the html would look like:
```html
<li>
    <p><a class="currentPage" href="../calendar/">CALENDAR</a></p>
</li>
```
It is important to note that for the program to insert the html class the project must be laid out a certain way.
1. In the root there may be multiple html files, however only the index.html (the homepage) will have the class="currentPage"
2. After that all other pages that should have the class="currentPage" should be in their own folder
    2. If a folder has multiple files in it then it will appear as the current page is the index.html
    2. For example, if there is a directory called join, with multiple html files in it, the currentPage will be join on the nav