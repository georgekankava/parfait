Release Process
===============

To release parfait out to the wider community, you will need the following:

   * checked out the Parfait git repo locally
   * Maven
   * gpg
   * An account on [OSS Sonatype Repo](https://oss.sonatype.org/)
   
   
OSS Sonatype
------------

You need to have access to OSS Sonatype repo to perform some manual actions during release.

The best place to start is to [read this Overview guide](http://central.sonatype.org/pages/ossrh-guide.html).

It appears the account to be created _may_ need to be linked with access to the `com.custardsource` project when Paul Cowan original set this project up way back when.  You may need to either refer to the original issue [https://issues.sonatype.org/browse/OSSRH-576], however my recommendation is to create a new Issue and add myself (`psmith@aconex.com`) as a watcher.  

gpg
---

Part of the Maven release process uses `gpg` to digitally sign the releases using a signature.  Please refer to the OSSRH Overview guide above in the OSS Sonatype section as most of the links stem from there.

As outlined in the docs, to streamline the release process I recommend encoding your `gpg` password into ``~/.m2/settings.xml`:

    ...
    <profiles>
    ...
      <profile>
         <id>gpg</id>
         <properties>
            <gpg.executable>gpg2</gpg.executable>
            <gpg.passphrase>..................</gpg.passphrase>
          </properties>
      </profile>
    ...
    </profiles>
    ...
    <activeProfiles>
      <activeProfile>gpg</activeProfile>
    ...
    </activeProfiles>
    
   
Otherwise you will be asked for the passphrase for every single Parfait module (which is quite a few)....

Maven
-----

Along with the `gpg` key, you need to store your password for the OSS Sonatype account in the `~/.m2/settings.xml`:

    ...
    <server>
      <id>sonatype-nexus-snapshots</id>
      <username>psmith@aconex.com</username>
      <password>..................</password>
    </server>
    <server>
      <id>sonatype-nexus-staging</id>
      <username>psmith@aconex.com</username>
      <password>..................</password>
    </server>


When Maven wants to push the artifacts to OSS Sonatype, it'll use this block.

ACTUALLY doing the release
==========================

Once you have all the above components setup, the actual release process involves the following steps:

  1. Invoke Maven's release process, a neat one-liner is this:
  ```
  mvn release:prepare -DautoVersionSubmodules release:perform
  ```
  This publishes the artifacts to a _*temporary*_ holding area within OSS Sonatype.
  
  2. The next step is outlined well here: [http://central.sonatype.org/pages/releasing-the-deployment.html]


One the `Release` action is performed you & others in the OSS Sonatype group for this project will receive an email from Nexus indicating the promotion is complete.  Once you receive this, the new version should be referencable in any POM.
