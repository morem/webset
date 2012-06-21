include Config

SRC_DIR = ./src \
          ./src2

DISPLAY_NAME = miao
DESCRPTION = My_Project

JAVA_SRC = $(foreach var, $(SRC_DIR),$(wildcard $(var)/*.java))
JAVA_CLASS = $(patsubst %.java,%.class,$(JAVA_SRC))
WEN_INFO_CONTENT=$(notdir $(patsubst %.java,%.info,$(JAVA_SRC)))
CONTENT = $(JAVA_CLASS)

WEB_ROOT=/opt/apache-tomcat-7.0.27/webapps
WEB_NAME=a
WEB_PATH=$(WEB_ROOT)/$(WEB_NAME)

all:start $(CONTENT) info end

start:
	@echo start at $(shell date)
end:
	@echo end at $(shell date)

install:
	-rm -rf $(WEB_PATH)
	mkdir $(WEB_PATH)
	mkdir $(WEB_PATH)/WEB-INF
	mkdir $(WEB_PATH)/WEB-INF/classes
	cp web.xml $(WEB_PATH)/WEB-INF/
	cp -t $(WEB_PATH)/WEB-INF/classes $(JAVA_CLASS)

server:
	$(CATALINA_HOME)/bin/shutdown.sh
	$(CATALINA_HOME)/bin/startup.sh


%.class:%.java
	javac $<


info:web.start  $(WEN_INFO_CONTENT)  web.end

%.info:
	@echo '    <servlet>' >>  web.xml
	@echo '        <servlet-name>$($*_NAME)</servlet-name>' >>  web.xml
	@echo '        <servlet-class>$*</servlet-class>' >>  web.xml
	@echo '    </servlet>' >>  web.xml
	@echo '    <servlet-mapping>' >>  web.xml
	@echo '        <servlet-name>$($*_NAME)</servlet-name>' >>  web.xml
	@echo '        <url-pattern>$($*_URL)</url-pattern>' >>  web.xml
	@echo '    </servlet-mapping>' >>  web.xml

clean:
	rm $(CONTENT)
	-rm web.xml -rf

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
