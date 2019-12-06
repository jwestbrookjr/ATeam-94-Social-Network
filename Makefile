.PHONY = compile run jar runjar zip all clean

#TODO: edit with path to your javac (java compiler)
JC = C:\Program Files\AdoptOpenJDK\jdk-11.0.4.11-hotspot\bin\javac

#TODO: edit with path to your java (java runtime environment)
JRE =  java 

#TODO: edit with path to your module-path for javafx
MP = --module-path "C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml

#TODO: edit with your classpath from Eclipse 
CP = -classpath "C:\Users\peasl\Salmo\CS400\ATeam;C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx.graphics.jar" application.Main

SRC = application/*.java   

APP = application.Main 

ARGS = # place your command line args here

compile:
	$(JC) $(CP) $(SRC) 

run:
	$(JRE) $(MP) $(CP) $(APP) $(ARGS)

jar:
	jar -cvmf manifest.txt executable.jar .

runjar:
	$(JRE) $(MP) -jar executable.jar $(ARGS)

# Create zip file for submitting to handin
zip: 
	jar -cMf ateam.zip README.txt validFile.txt InvalidFile.txt executable.jar manifest.txt screenshot* application

#Eclipse's "Show Command Line"
all:
	C:\Program Files\AdoptOpenJDK\jdk-11.0.4.11-hotspot\bin\javaw.exe --module-path "C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 -p "C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx.base.jar;C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx.controls.jar;C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx.fxml.jar;C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx.graphics.jar;C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx.media.jar;C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx.swing.jar;C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx.web.jar;C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx-swt.jar" -classpath "C:\Users\peasl\Salmo\CS400\ATeam;C:\Users\peasl\Salmo\CS400\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib\javafx.graphics.jar" application.Main


# Remove generated files
clean:
	rm -f application/*.class
	rm -f executable.jar