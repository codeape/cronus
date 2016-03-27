#!/bin/bash

SCALA_VERSION='2.11.7'
SBT_VERSION='0.13.9'

sudo apt-get update

sudo apt-get -y dist-upgrade

# Install Java
sudo apt-get -y install openjdk-8-jdk
sudo echo 'export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64' > /tmp/jdk.sh
sudo echo 'export PATH=$PATH:$JAVA_HOME/bin' >> /tmp/jdk.sh
sudo mv /tmp/jdk.sh /etc/profile.d/

# Install scala
curl -L -o /tmp/scala-$SCALA_VERSION.tgz http://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz
(
    cd /opt/
    sudo tar xzvf /tmp/scala-$SCALA_VERSION.tgz
)
sudo echo 'export SCALA_HOME=/opt/scala-'$SCALA_VERSION > /tmp/scala.sh
sudo echo 'export PATH=$PATH:$SCALA_HOME/bin' >> /tmp/scala.sh
sudo mv /tmp/scala.sh /etc/profile.d/

# Install sbt
curl -L -o /tmp/sbt-$SBT_VERSION.tgz https://dl.bintray.com/sbt/native-packages/sbt/$SBT_VERSION/sbt-$SBT_VERSION.tgz
(
    cd /opt
    tar xzvf /tmp/sbt-$SBT_VERSION.tgz
    rm -v sbt/bin/sbt.bat
    rm -v sbt/conf/sbtconfig.txt
    mv -v sbt/bin/sbt sbt/bin/sbt.sh
    echo '#!/bin/bash' > sbt/bin/sbt
    echo 'SBT_OPTS="-Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled"' >> sbt/bin/sbt
    echo 'java $SBT_OPTS -jar `dirname $0`/sbt-launch.jar "$@"' >> sbt/bin/sbt
    chmod 755 sbt/bin/sbt
    mv sbt sbt-$SBT_VERSION
)
sudo echo 'export PATH=$PATH:/opt/sbt-'$SBT_VERSION/bin > /tmp/sbt.sh
sudo mv /tmp/sbt.sh /etc/profile.d/

# Fixing security issues
sudo /var/lib/dpkg/info/ca-certificates-java.postinst configure

#eof
