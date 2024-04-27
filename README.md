# Mochi
Web application for anonymously creating vaults for storing notes.
### Created with Java and Vaadin

## Core functionality

The basic functionality of the application is to create anonymized safes for storing notes. You can go to the safe
log in using the combination of the safe code and its PIN. After logging in, the application allows you to create notes that
are related only to each other by the name of the source object. Notes are stored in the form of Markdown files and the engine
The rendering is handled by the HtmlToMD parser written specifically for this application. Application database storing
all application objects are MongoDB database.

## Application data entities

- Mochi - object for storing core application configuration
- MochiLog - object for storing collection of application log data
- Note - object for storing user notes ( in raw format as .md for parsing)
- Vault - object for storing list of notes

## Security

The application does not store e-mail addresses, IP or MAC addresses of devices
that are connected to it. If you lose your pin or safe name, you can restore access to the application
using the recovery code.

## UI / UX
The application has two UI screens - the first is used to create and log in to the safe, the second is the editor
text along with a terminal field that allows you to perform an action on the application itself or on the content
of the currently open note.
Mochi contains sharing feature that allows users to create public URL links to pages with
their notes.

#### OpenVaultView - endpoint / or /sezame
View for logging to the vault or creating vaults:<br>
        - create - creates the vault<br>
        - help - shows component with help information<br>
        **or**<br>
        **insert vault code or shared URL to the note to open**
#### FocusView - endpoint /focus 
View for editing notes stored in vault or creating new ones.<br>
Use terminal documentation for checking commands

#### NoteViewerView - endpoint /note-viewer
View for showing shared notes by user.

#### Deployment
Download newest release from Release section and uzip that to desired
location. After that get your MongoDB connection string from selected instance.
After the mongodb setup run command:<br>
```bash
java -jar mochi.jar
```





