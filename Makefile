include Config
empty=
space=$(empty) $(empty)

SRC_DIR = $(shell find ./WEB-INF -type d)
DISPLAY_NAME = miao
DESCRPTION = My_Project

JAVA_SRC = $(foreach var, $(SRC_DIR),$(wildcard $(var)/*.java))
JAVA_CLASS = $(patsubst %.java,%.class,$(JAVA_SRC))
WEB_INFO_CONTENT=$(notdir $(patsubst %.java,%.info,$(JAVA_SRC)))
CONTENT = $(JAVA_CLASS)
JAVA_CLASS_SEARCH = $(foreach var, $(SRC_DIR),$(wildcard $(var)/*.class))

WEB-ROOT=/opt/tomcat/webapps
WEB-NAME=a
WEB-PATH=$(WEB-ROOT)/$(WEB-NAME)
WEB-INF=./WEB-INF

#CP = $(CLASSPATH):${PWD}/WEB-INF/lib/:${PWD}/WEB-INF/lib/*:${PWD}/WEB-INF/classes/*:${PWD}/WEB-INF/lib/*:$(subst $(space),:,$(SRC_DIR))
CP = $(CLASSPATH):${PWD}/WEB-INF/lib/*:${PWD}/WEB-INF/classes/*
SP = ${PWD}/WEB-INF/classes/
all:$(CONTENT)


start:
	@echo start at $(shell date)
end:
	@echo end at $(shell date)

server:
	$(CATALINA_HOME)/bin/shutdown.sh
	$(CATALINA_HOME)/bin/startup.sh


%.class:%.java
	@echo compile $<
	javac $< -g -classpath $(CP) -sourcepath $(SP) -encoding UTF-8 


info:web.start  $(WEB_INFO_CONTENT) LOG4J SESSION web.end

LOG4J:
	@echo '    <servlet>' >>  web.xml;
	@echo '    	<servlet-name>log4j-init</servlet-name>' >>  web.xml;
	@echo '    	<servlet-class>Log4jInit</servlet-class>' >>  web.xml;
	@echo '    	<init-param>' >>  web.xml;
	@echo '    		<param-name>log4j</param-name>' >>  web.xml;
	@echo '    		<param-value>WEB-INF/log4j.properties</param-value>' >>  web.xml;
	@echo '    	</init-param>' >>  web.xml;
	@echo '    	<load-on-startup>1</load-on-startup>' >>  web.xml;
	@echo '    </servlet>' >>  web.xml;
SESSION:
	@echo '    <session-config>' >>  web.xml;
	@echo '    <session-timeout>5</session-timeout>' >>  web.xml;
	@echo '    </session-config>' >> web.xml
%.info:
	@if [ $($*_NAME)x != x ]; then \
	echo '    <servlet>' >>  web.xml;\
	echo '        <servlet-name>$($*_NAME)</servlet-name>' >>  web.xml;\
	echo '        <servlet-class>$*</servlet-class>' >>  web.xml;\
	echo '    </servlet>' >>  web.xml;\
	echo '    <servlet-mapping>' >>  web.xml;\
	echo '        <servlet-name>$($*_NAME)</servlet-name>' >>  web.xml;\
	echo '        <url-pattern>$($*_URL)</url-pattern>' >>  web.xml;\
	echo '    </servlet-mapping>' >>  web.xml;\
	fi

clean:
	-rm $(JAVA_CLASS_SEARCH)

t:
	sudo /etc/init.d/tomcat6 restart

web.xml:web.start web.content web.end

web.start:
	@echo '<?xml version="1.0" encoding="ISO-8859-1"?>' > web.xml
	@echo '<web-app xmlns="http://java.sun.com/xml/ns/javaee"' >> web.xml
	@echo '  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"' >> web.xml
	@echo '  xsi:schemaLocation="http://java.sun.com/xml/ns/javaee' >> web.xml
	@echo '                      http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"'>> web.xml
	@echo '  version="3.0"'>> web.xml
	@echo '  metadata-complete="true">'>> web.xml

web.content:
	@echo "<display-name>${DISPLAY_NAME}</display-name>" >> web.xml
	@echo "<description>${DESCRPTION}</description>" >> web.xml

web.end:
	@echo "</web-app>" >> web.xml
