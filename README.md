## Requirements

- JDK 21 (tested with OpenJDK 21)
- JavaFX SDK 21.0.2 (Download from [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/))

---

## How to Run (Manually)
- [⬇ Download LibraryManager.zip](https://github.com/ZakariaHoussemTaleb/LibraryManager/releases/download/v1.0/LibraryManager_installer.zip)

zip content :
LibraryManager/
 books.txt          # Books database
 users.txt          # Users database
 demo2.jar 

i tryed it on ubunto:

java --module-path /path/to/javafx-sdk-21.0.2/lib \
     --add-modules javafx.controls,javafx.fxml \
     -jar demo2.jar

default admin account = "admin|admin"


default reader account = "reader|reader"


An additional reader account is included to help test specific cases (e.g. the same book borrowed by two different users, attempting to borrow an already borrowed book, or trying to return a book that wasn't borrowed), and all such edge cases are properly handled by the application.


default reader account = "reader2|reader2"

Zakaria (M1 ISIL student)
