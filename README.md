# HTML-Compiler
## This is a Java FX program that allows a user to specify the directory of nav, footer, and project and compile them into one.

## Features
All features below will be explained later.
* User can specify: nav, footer and project directory
* Ignore file, of directories and files in the root of th eproject to ignore
* Use of html class currentPage

## Nav, Footer, Project Directories

### Nav and Footer Directories
Both the nav and footer should have their own foler each with its corresponding files. 
Each file in either the nav or footer directory shoud be named #.html, where the # corresponds to the depth.
Depth in this application is defined as how deep a file is in the priject directory. Files at the root have a depth of 1, those in a folder in root will have a dpeth of 2 and so on. 
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

##Ignore File
The ignore file is a text file in the CSV format, where each directory or foler in the root of the project is seperated with a comma.
An example of such file would be one that ignores the css folder and a google.html file.

This  file would be written: css, google.html

Now both of these files will be ignored, increading runtime as the css directory won't have to be mapped and the potential for an error either the google.html file is removed.

Do note that files that don't end in .html are automatically ignored, for example sitemap.xml doesn't need to be in the ignore file.