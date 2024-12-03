# Use Amazon Corretto as the base image (supports ARM and AMD architectures)
FROM amazoncorretto:22.0.2-al2023-headless

# Set WildFly, MariaDB driver, and environment variables
ENV WILDFLY_VERSION=33.0.1.Final
ENV MARIADB_DRIVER_VERSION=3.4.1
ENV WILDFLY_SHA1=943ec801bf65bb42de27ef78c85d325180dcab0d
ENV JBOSS_HOME=/opt/jboss/wildfly
ENV HOME=/opt/jboss
ENV LAUNCH_JBOSS_IN_BACKGROUND=true

# Install necessary packages and create jboss user
RUN yum install shadow-utils tar gzip -y && yum -y update \
    && mkdir /opt/jboss && groupadd -r jboss -g 1000 \
    && useradd -u 1000 -r -g jboss -m -d /opt/jboss -s /sbin/nologin -c "JBoss user" jboss \
    && chmod 755 /opt/jboss && yum clean all && rm -rf /var/cache/yum

# Set working directory
WORKDIR /opt/jboss

# Switch to root for installation steps
USER root

# Download and install WildFly
RUN cd $HOME \
    && mkdir -p $JBOSS_HOME \
    && curl -L -O https://github.com/wildfly/wildfly/releases/download/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
    && echo "$WILDFLY_SHA1 wildfly-$WILDFLY_VERSION.tar.gz" | sha1sum -c - \
    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-$WILDFLY_VERSION/* $JBOSS_HOME \
    && rm wildfly-$WILDFLY_VERSION.tar.gz \
    && chown -R jboss:jboss ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

# Expose necessary ports
EXPOSE 8080 9990

# Add MariaDB connector
RUN curl -L https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/$MARIADB_DRIVER_VERSION/mariadb-java-client-$MARIADB_DRIVER_VERSION.jar \
    -o $JBOSS_HOME/standalone/lib/ext/mariadb-connector-java.jar

# Configure WildFly with customized settings
COPY --chmod=0644 standalone-full.xml $JBOSS_HOME/standalone/configuration/standalone.xml
COPY --chmod=0644 standalone-full.xml $JBOSS_HOME/standalone/configuration/standalone-full.xml

# Create admin user for WildFly management console
RUN $JBOSS_HOME/bin/add-user.sh admin admin --silent

# Add and execute a custom script for further configurations
COPY --chmod=0755 customization.sh $JBOSS_HOME/
RUN /bin/bash $JBOSS_HOME/customization.sh \
    && rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history $JBOSS_HOME/customization.sh \
    && chown -R jboss:jboss $JBOSS_HOME/standalone/deployments \
    && chmod -R ug+rwX $JBOSS_HOME/standalone/deployments

# Set the user to jboss for runtime
USER jboss

# Command to start WildFly
CMD ["$JBOSS_HOME/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]