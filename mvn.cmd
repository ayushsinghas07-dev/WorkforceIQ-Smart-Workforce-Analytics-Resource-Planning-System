@echo off
setlocal
set "MAVEN_HOME=C:\Users\nisha\.gemini\antigravity\scratch\maven\apache-maven-3.9.6"
set "CLASSWORLDS_JAR=%MAVEN_HOME%\boot\plexus-classworlds-2.7.0.jar"
set "CLASSWORLDS_CONF=%MAVEN_HOME%\bin\m2.conf"

java -classpath "%CLASSWORLDS_JAR%" "-Dclassworlds.conf=%CLASSWORLDS_CONF%" "-Dmaven.home=%MAVEN_HOME%" "-Dmaven.multiModuleProjectDirectory=%CD%" org.codehaus.plexus.classworlds.launcher.Launcher %*
