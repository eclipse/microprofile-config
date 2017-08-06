#######################################################################
## Copyright (c) 2016-2017 Contributors to the Eclipse Foundation
##
## See the NOTICE file(s) distributed with this work for additional
## information regarding copyright ownership.
##
## Licensed under the Apache License, Version 2.0 (the "License");
## you may not use this file except in compliance with the License.
## You may obtain a copy of the License at
##
##     http://www.apache.org/licenses/LICENSE-2.0
##
## Unless required by applicable law or agreed to in writing, software
## distributed under the License is distributed on an "AS IS" BASIS,
## WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
## See the License for the specific language governing permissions and
## limitations under the License.
#######################################################################

### Deploy to maven central
##############################

#RELEASE_VERSION=1.0
TAG=$RELEASE_VERSION
TARGET_MAVEN_REPO="central::default::https://oss.sonatype.org/service/local/staging/deploy/maven2" # Maven central
#TARGET_MAVEN_REPO="local::default::file:///tmp/maven-repository" # For testing - deploys to a local repository in /tmp

# validates that variables are set
if echo XX"$RELEASE_VERSION"XX | grep XXXX > /dev/null
  then
    echo "ERROR: Some of the required environment variables are undefined. Please define and export them before running this script." >&2
    exit
  fi


# add credentials for repository to settings.xml - for Maven Central, you need to have an account at Sonatype and access to the org.eclipse.microprofile group. See https://issues.sonatype.org/browse/OSSRH-32787

# checkout release tag

git checkout "$TAG"
git reset --hard
git clean -f

# prepare artifacts
## - signing with GPG key
##   - Create GPG key - follow https://wiki.eclipse.org/GPG#Creating_your_GPG_keypair
##   - upload your public key to one of the key servers that Maven Central checks: http://pgp.mit.edu:11371/, http://keyserver.ubuntu.com:11371/, http://pool.sks-keyservers.net:11371/
##   - optionally have the key signed by other people (e.g. at Eclipse) or just create a proof that the signature is created by you, e.g. by adding it to KEYS file in the github repo
##   - specify GPG passphrase in settings.xml as:
##   <server>
##      <id>gpg.passphrase</id>
##      <passphrase>clear or encrypted text</passphrase>
##    </server>
##

mvn --batch-mode -DaltDeploymentRepository="$TARGET_MAVEN_REPO" --projects .,api,tck -Pgpg-sign clean deploy

# don't continue if the mvn command fails or aborted
if [[ x$? != x0 ]]
  then 
    echo ERROR, aborting
    exit
fi
