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

##################################
# Specify the following variables
##################################

# requirements for versions:
#   - for BND: dash must come before a classifier, such as RC1 (e.g. 1.0-RC1 is valid while 1.0RC1 is invalid)
#RELEASE_VERSION=1.0-RC1
#DEV_VERSION=1.0-SNAPSHOT
#GIT_USER='Ondrej Mihalyi'
#GIT_EMAIL='ondrej.mihalyi@gmail.com'
#ORIGIN_REMOTE_REPO=origin # - the name of the upstream repository to push changes to. It should be the main repository, not a fork

# check that we have all variables

if echo XX"$RELEASE_VERSION"XX"$DEV_VERSION"XX"$GIT_USER"XX"$GIT_EMAIL"XX"$ORIGIN_REMOTE_REPO" | grep XXXX > /dev/null
  then
    echo "ERROR: Some of the required environment variables are undefined. Please define and export them before running this script." >&2
    exit
  fi

exit

# Specify derived variables

BRANCH=branch_$RELEASE_VERSION
TAG=$RELEASE_VERSION

# set git identity

git config user.name "$GIT_USER"
git config user.email "$GIT_EMAIL"

# delete release branch and tag

git checkout master
git reset --hard
git clean -f
git branch -D "$BRANCH"
git tag -d "$TAG"
# create and checkout release branch

git branch "$BRANCH"
git checkout "$BRANCH"

# prepare release

mvn --batch-mode -DreleaseVersion=$RELEASE_VERSION -DdevelopmentVersion=$DEV_VERSION -Dtag=$TAG release:clean release:prepare

# save release staging metadata

tar -cvzf staging.tar.gz `git status -s | grep '^??' | sed 's/^[?][?] //'`

# build the artifacts

# publish the release TAG

git push "$ORIGIN_REMOTE_REPO" "$TAG"

# revert git identity

git config --unset user.name
git config --unset user.email
