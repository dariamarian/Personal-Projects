module com.example.gamificationapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.logging.log4j;

    opens com.example.gamificationapp to javafx.fxml;
    opens com.example.gamificationapp.domain;
    opens com.example.gamificationapp.repository;
    opens com.example.gamificationapp.controllers;
    opens com.example.gamificationapp.service;
    opens com.example.gamificationapp.domain.validators;
    opens com.example.gamificationapp.exceptions;
    exports com.example.gamificationapp;
    exports com.example.gamificationapp.domain;
    exports com.example.gamificationapp.repository;
    exports com.example.gamificationapp.controllers;
    exports com.example.gamificationapp.service;
    exports com.example.gamificationapp.domain.validators;
    exports com.example.gamificationapp.exceptions;
}