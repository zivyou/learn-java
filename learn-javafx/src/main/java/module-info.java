module com.zivyou.learnjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.zivyou.learnjavafx to javafx.fxml;
    exports com.zivyou.learnjavafx;
}