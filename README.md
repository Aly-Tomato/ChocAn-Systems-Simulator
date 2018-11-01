This project is used to provide data processing portions of the ChocAn system (a fictional system for CS300).
    Requirements Doc:   https://docs.google.com/document/d/1o3gu3sPVz-Qbbxp4Pi2eDYI_ijRIRFU5tgz03PTzfm0/edit?usp=sharing
    Design Doc:         https://docs.google.com/document/d/1i6cvvDyHuR04P4A1nUWZgMZvVRzFk_xQmMkc9EyHeaI/edit?usp=sharing


Compilation Conventions (assumes Unix system)
	The system should be able to be compiled with the below line without any errors or warnings
		javac *.java

	The system should be runnable with the below line without any errors or warnings.
		java ChocAnSystem

	The system should be able to be be jar-ed with the below line without any errors or warnings.
		jar -cfe ChocAnSystem.jar ChocAnSystem *.class *.java

	The system should be runnable with ChocAnSystem.jar, member_directory, and provider_directory in the same directory with the line below.
		java -jar ChocAnSystem.jar

	The system's javadocs should be generated with the below line without any errors or warnings.
		javadoc -d chocAnJavadocs *.java


Coding Conventions
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


Git Conventions
	Small, frequent commits.

