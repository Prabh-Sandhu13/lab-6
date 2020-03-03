
Context
-------

The best way of handling usernames and passwords for an application
is to have that information in a configuration file.  There is a
Java class called "Properties" that you can then load from the
configuration file data.

When you deploy your software, you do not include the configuration
file in the compilation.  Instead, it is a file that the user fills
in on their computer (directly or through a configuration interface).

For simplicity right now, this sample data is using a Java file to
store the configuration information rather than using a true
configuration file.  Doing this will let you get going on the
database part of the lab without needing to understand how to get
your IDE to find a configuration file.

If you have time, find the way to use a true configuration file
with your IDE rather than the MyIdentity.java file provided here.

Update to do
------------

Modify the MyIdentity.java file to include your own CSID and your
own Banner ID.  The Banner ID currently in the file is a fake one.

