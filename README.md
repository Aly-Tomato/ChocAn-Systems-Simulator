This project is used to provide data processing portions of the ChocAn system (a fictional system for CS300).
    Requirements Doc:   https://docs.google.com/document/d/1o3gu3sPVz-Qbbxp4Pi2eDYI_ijRIRFU5tgz03PTzfm0/edit?usp=sharing
    Design Doc:         https://docs.google.com/document/d/1i6cvvDyHuR04P4A1nUWZgMZvVRzFk_xQmMkc9EyHeaI/edit?usp=sharing


Compilation Conventions (assumes Unix system):

	The system should be able to be compiled with the below line without any errors or warnings.
		javac -cp .:./externalJars/json-simple-1.1.1.jar *.java

	Note: there are two warnings caused by using any of json.simple's classes and are unavoidable.
		Note: ChocAnSystem.java uses unchecked or unsafe operations.
		Note: Recompile with -Xlint:unchecked for details.

	The system should be runnable with the below line without any errors or warnings.
		java -cp .:./externalJars/json-simple-1.1.1.jar ChocAnSystem

	The system's javadocs should be generated with the below line without any errors or warnings.
		javadoc -d chocAnJavadocs *.java


Coding Conventions:

	Tab, so the spacing can set to whatever the individual wants.
	Try to keep functions ~25 lines max.
	Some level of avoiding nesting.
	No ternary operator.
	camelCase.
	Declare variables at the top of the scope they are in.
	Descriptive variable names.
	Javadocs are required for public functions.
	Have helper functions be in order of appearance after the public it’s in.
	Always have a clean compile.
	Spaces after controls statements and condition parentheses.
		Example
		if (true) {
		}
	Functions will have curly-braces on the same line as the function line.
		Example
		public static void foo() {
		}


Git Conventions:

	Small, frequent commits.


JSON Stuctures

	See ChocAnSystem.java for these, in fillMemberDirectory() and fillProviderDirectory()

