package org.guccigang.mini_google_docs.controller.DocumentControllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.guccigang.mini_google_docs.controller.UserUI.VisitorUIController;
import org.guccigang.mini_google_docs.model.UILocation;

import org.guccigang.mini_google_docs.model.GuiUtil;
import org.guccigang.mini_google_docs.model.DocumentDAO;
import org.guccigang.mini_google_docs.model.DocumentFile;

import java.io.IOException;

public class VisitorViewDocumentsController {
    @FXML
    private TextField searchBar;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<DocumentFile> documentFileTable;
    @FXML
    private TableColumn<DocumentFile, String> documentNameColumn;
    @FXML
    private TableColumn<DocumentFile, String> documentOwnerColumn;
    @FXML
    private TableColumn<DocumentFile, String> documentViewColumn;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public VisitorViewDocumentsController(){

    }

    /**
     *
     * @param event
     */
    public void handleSearch(ActionEvent event) {

    }
    /**
     * This function is called when the user clicks open document.
     */
    @FXML
    private void handleOpenDocument(){
        int selectedIndex = documentFileTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0){
            DocumentFile selectedDocument = documentFileTable.getItems().get(selectedIndex);
            try {
                VisitorTextEditorController controller = new VisitorTextEditorController(selectedDocument);
                GuiUtil.createWindow(UILocation.VISITOR_TEXT_EDITOR, "Text Editor", controller);
            } catch (Exception e) {
                e.printStackTrace();
                GuiUtil.createAlertWindow(Alert.AlertType.ERROR, "Please try again later.", "An error occurred.", "Error");
            }
        }else {
            //Nothing selected.
            GuiUtil.createAlertWindow(Alert.AlertType.WARNING, "Please select a document in the table.",
                    "No Document Selected", "No Selection");
        }
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize(){
        //Files the TableView with documents.
        fillTable();
        // Initialize the person table with the two columns.
        documentNameColumn.setCellValueFactory(cellData -> cellData.getValue().documentNameProperty());
        documentOwnerColumn.setCellValueFactory(cellData -> cellData.getValue().ownerProperty());
        documentViewColumn.setCellValueFactory(cellData -> cellData.getValue().counterProperty());
        /**Listens for selection changes and when the user clicks open document on while
         *while document is highlighted then that exact document should open.
         */
       // documentFileTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->{} ));
    }

    @FXML
    private void handleHome(ActionEvent event){
        try{
            VisitorUIController controller = new VisitorUIController();
            GuiUtil.changeScene(event, UILocation.VISITOR_UI,"Visitor",controller);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * This function acesses the data base and fills the table with all the documents of the data base.
     */
    private void fillTable(){
        try{
            documentFileTable.setItems(DocumentDAO.getAllDocumentFilesDataForVisitor());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
