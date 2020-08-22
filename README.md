# Bumpkis

Automatically bumps the version of an internal dependency in your Gradle project in Bitbucket when there's a new
release of that dependency.

This is how it works:
- When your project merges a pull request, the service is notified via a webhook, reads your dependencies from your
  Gradle build files (even Gradle submodules) and stores them in a database
- When a library does a new release, the service is notified via a webhook, checks the database if any repo has a
  dependency to this library and creates a pull request in your project with a change bumping the dependency version
  
## Requirements

- JDK 14 Preview or later
- Maven 3.6.3 or later
- Postgres 12.2 or later
- [Atlassian Plugin SDK](https://developer.atlassian.com/server/framework/atlassian-sdk/atlas-run-standalone/)

## How to install Atlassian SDK for running local Bitbucket

- Install with Homebrew, and start:
```
brew tap atlassian/tap
brew install atlassian/tap/atlassian-plugin-sdk
atlas-run-standalone --product bitbucket
```
- Make sure your local Bitbucket is running on http://localhost:7990/bitbucket (login as admin/admin)

## How to set up database

- Install with Homebrew, and start:
```
brew install postgres
pg_ctl -D /usr/local/var/postgres start
```
(Stop database when you're finished with: `pg_ctl -D /usr/local/var/postgres stop`)

- Create user and database with postgres tools:
```
createuser bumpkis -s
createdb bumpkis
```
- Create tables in the database with the Dropwizard migration command:
```
java --enable-preview -jar target/bumpkis-1.0.0-SNAPSHOT.jar db migrate config.yml
```

## How to start the Bumpkis application

- Run `mvn clean package` to build your application
- Start application with:
```
java --enable-preview -jar target/bumpkis-1.0.0-SNAPSHOT.jar server config.yml
```

## How to set up demo repos

- Create a repo named `foo` in you local Bitbucket dashboard, and a page with instructions will be shown
- Navigate to the demo project `demo/foo` in this repo and follow the instructions under the title
  **My code is ready to be pushed** on the Bitbucket page
- Create a repo named `bar` the same way as you did for `foo`
- Create a webhook in Bitbucket in each repo in the Repository Settings (cog icon) with name `bitbucket` and URL 
  `http://localhost:8080/bitbucket`. Make sure the events `Push` and `Merged` are checked for the webhook
 
## Running a demo

- Make sure you have a demo setup as described in **How to set up demo repos** above
- Make sure the database is set up and running as described in **How to set up database** above
- Start the Bumpkis app as described in **How to start the application** above
- Create and merge a pull request in the repo `foo` so that it registers in the Bumpkis database:
```
git checkout -b dummy
git commit -m "Empty" --allow-empty
git push origin dummy
```
- Bitbucket will respond with URL to create a pull request, and you can open it from Terminal (Macos):
```
open http://localhost:7990/bitbucket/projects/PROJECT_1/repos/foo/pull-requests?create&sourceBranch=refs/heads/dummy
```
- Create and merge the pull request in Bitbucket, which will be visible in the Bumper log
- Now create a release in the library repo `bar` that `foo` depends on:
```
git pull;git commit -m "Empty" --allow-empty;git push origin master;./gradlew release -Prelease.useAutomaticVersion=true
```
- How Bumper processes the events from Bitbucket will be visible in the Bumper log
- There should now exist a pull request in the [Bitbucket dashboard](http://localhost:7990/bitbucket/dashboard) for 
bumping the dependency `bar`, titled "[AUTO] Bump version to com.example.bar:bar:<version>"
