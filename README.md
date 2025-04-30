## Requirements

- JDK 21 (tested with OpenJDK 21)
- JavaFX SDK 21.0.2 (Download from [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/))

---

## How to Run (Manually)
- [⬇ Download LibraryManager.zip](https://github.com/ZakariaHoussemTaleb/LibraryManager/releases/download/v1.0/LibraryManager.zip)

zip content :
LibraryManager/
 books.txt          # Books database
 users.txt          # Users database
 demo2.jar 

i tryed it on ubunto:

java --module-path /path/to/javafx-sdk-21.0.2/lib \
     --add-modules javafx.controls,javafx.fxml \
     -jar app/demo2.jar

Zakaria (M1 ISIL student)
