# JarFix

Quick tool to "patch" a specific class in a jar file, replacing all occurrences of target string. NOTE: This tool is merely altering the class file directly in the jar, it is not decompiling / recompiling, and so can easily result in a broken jar if you are not careful what you are doing.
The jar to change, class, source and target string are given in a properties file jarfix.properties:

~~~~
jarfile=[path/to/the/jarfile.jar]
classname=[path/to/the/classname.class]
search=[string to search for]
replace=[string to replace with]
~~~~

e.g.
~~~~
jarfile=c\:/signfix/bundle531/version0.0/aem-adobesign-sdk-1.1.18.jar
classname=com/adobe/sign/utils/ApiClient.class
search=https\://secure.na1.echosign.com
replace=https\://secure.na2.echosign.com
~~~~

Build and run with maven.
