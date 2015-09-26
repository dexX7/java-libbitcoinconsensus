#!/bin/bash
set -e

echo "Deploying JavaDoc to GitHub Pages.."

# Commit hash of the main repository
GIT_COMMIT=$(git rev-parse --short HEAD)

# Base dir of the project
BUILD_DIR=$TRAVIS_BUILD_DIR

# The destination of the documentation
DOCS_DIR=$BUILD_DIR/build/docs/javadoc

# Generate the JavaDoc
$BUILD_DIR/gradlew --quiet clean javadoc

# Setup a new repository locally and commit the documentation
cd $DOCS_DIR
git init
git config user.name "Travis CI"
git config user.email "ci@bitwatch.co"
git add .
git commit -m "Deploy Javadoc for commit: ${GIT_COMMIT}"

# Force push to the remote gh-pages branch
#
# Note: the GIT_REPO_URL should contain the remote destination, including
# OAuth access token, such as: https://1234567@github.com/user/repo.git
#
# The actual value is provided via Travis CI as secure environment variable:
# http://docs.travis-ci.com/user/encryption-keys/
# http://docs.travis-ci.com/user/environment-variables/#Defining-Variables-in-Repository-Settings
#
# Write access for the repository is required, and the minimum scope of
# the access token is "repo". For more information about API tokens see:
# https://help.github.com/articles/creating-an-access-token-for-command-line-use/
#
# To avoid leaking credentials, the push is silenced.
git push --force --quiet $GIT_REPO_URL master:gh-pages > /dev/null 2>&1
