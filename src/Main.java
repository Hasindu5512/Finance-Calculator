import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;

import java.text.DecimalFormat;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.Label;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    DecimalFormat decimalFormat = new DecimalFormat("####0.00");  /**---> Convert To decimal Formate <----**/
    TextField textFieldSD = new TextField();

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage primaryStage) throws Exception{
        //connecting driver
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        //connecting to database
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        //creating database
        MongoDatabase database = mongo.getDatabase("PP2CWDb");

        //creating collection
        //database.createCollection("CWCollection");

        //assign collection to document type
        MongoCollection<Document> collection = database.getCollection("CWCollection");


        Stage stgHome = new Stage();
        stgHome.setTitle("Finance Calculator");
        Label lblHomeTitle = new Label(" Finance Calculator");
        lblHomeTitle.setFont(new Font("Goudy Stout", 38));
        lblHomeTitle.setStyle("-fx-text-fill: aliceblue;-fx-background-color: #1B1464;-fx-pref-width: 1200;-fx-pref-height: 80;-fx-font-weight: bold");
        lblHomeTitle.setLayoutY(30);

        Button btnFince = new Button("Finance");
        btnFince.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        DropShadow dropShadow1= new DropShadow();
        dropShadow1.setRadius(8);
        dropShadow1.setOffsetX(5);                  /**----> Home button shadows <----**/
        dropShadow1.setOffsetY(5);
        dropShadow1.setColor(Color.web("#006666"));
        btnFince.setEffect(dropShadow1);
        btnFince.setOnMouseEntered(event -> {
            btnFince.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-background-color: #00cec9;-fx-font-weight: bold;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px");
        });
        btnFince.setOnMouseExited(event -> {
            btnFince.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        });
        btnFince.setOnAction(event -> {
            Stage stgFince = new Stage();
            stgFince.setTitle("Finance");

            GridPane gridPane = keypad();
            gridPane.setLayoutX(530);
            gridPane.setLayoutY(30);

            TabPane tabPaneFince = new TabPane();
            tabPaneFince.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);  /**----> To open same Window <----**/

//....Fv..............................................................................................
            Pane paneFV = new Pane();
            paneFV.setStyle("-fx-background-color: #CAD3C8");
            GridPane gridPane1 = keypad();  /**----> Keypad <----**/
            gridPane1.setLayoutX(530);
            gridPane1.setLayoutY(30);

            Label lblFV1 = new Label("Number of Periods");
            TextField txtFV1 = new TextField();
            txtFV1.setOnMouseClicked(event1 -> {
                textFieldSD = txtFV1;
            });
            Label lblFV2 = new Label("Start Principal");
            TextField txtFV2 = new TextField();
            txtFV2.setOnMouseClicked(event1 -> {
                textFieldSD = txtFV2;
            });
            Label lblFV3 = new Label("I/Y (Interest Rate - %)");
            TextField txtFV3 = new TextField();
            txtFV3.setOnMouseClicked(event1 -> {
                textFieldSD = txtFV3;
            });
            Label lblFV4 = new Label("PMT (Annuity Payment)");
            TextField txtFV4 = new TextField();
            txtFV4.setOnMouseClicked(event1 -> {
                textFieldSD = txtFV4;
            });
            Label lblFV5 = new Label("Year");
            TextField txtFV5 = new TextField();
            txtFV5.setOnMouseClicked(event1 -> {
                textFieldSD = txtFV5;
            });

            VBox vBoxFV1 = new VBox();
            vBoxFV1.setLayoutX(50);
            vBoxFV1.setLayoutY(50);
            vBoxFV1.setSpacing(22);
            vBoxFV1.getChildren().addAll(lblFV1,lblFV2,lblFV3,lblFV4,lblFV5);

            VBox vBoxFV2 = new VBox();
            vBoxFV2.setLayoutX(250);
            vBoxFV2.setLayoutY(50);
            vBoxFV2.setSpacing(8);
            vBoxFV2.getChildren().addAll(txtFV1,txtFV2,txtFV3,txtFV4,txtFV5);

            Button btnFVCal = new Button("Calculate");
            btnFVCal.setLayoutX(220);
            btnFVCal.setLayoutY(290);
            btnFVCal.setStyle("-fx-pref-width:200;-fx-pref-height:35;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
            btnFVCal.setOnAction(event1 -> {
                if(txtFV1.getText().isEmpty() && txtFV2.getText().isEmpty() && txtFV3.getText().isEmpty() && txtFV4.getText().isEmpty() && txtFV5.getText().isEmpty()){
                    Alert alertEmptySpace = new Alert(Alert.AlertType.ERROR,"Please..! Fill the all text field.",ButtonType.OK);
                    alertEmptySpace.show();
                }                           /**----> fixed empty text field error <----**/
                else {
                    try {
                        double numberOFPeriods = Double.parseDouble(txtFV1.getText());
                        double startPrincipal = Double.parseDouble(txtFV2.getText());
                        double iY = Double.parseDouble(txtFV3.getText());
                        double pmt = Double.parseDouble(txtFV4.getText());
                        double year = Double.parseDouble(txtFV5.getText());
                        double futureValue = pmt * ((Math.pow((1 + (iY / 100) / numberOFPeriods), numberOFPeriods * year) - 1) / ((iY / 100) / numberOFPeriods));

                        Document document1 = new Document("Name", "Finance FV")
                                .append("Result", decimalFormat.format(futureValue));
                        collection.insertOne(document1);

                        //...Minus value error correcting...................................
                        if (futureValue<0){
                            Alert alertMinusVError = new Alert(Alert.AlertType.ERROR, "Please check your input values", ButtonType.OK);
                            alertMinusVError.show();
                        } else {
                            Stage stage = new Stage();
                            stage.setTitle("Finance FV Output");
                            stage.initOwner(stgFince);  /**--> owner this stage <---*/
                            stage.initModality(Modality.WINDOW_MODAL);   /**----> TO close previous window <----*/
                            Label lbl = new Label(String.valueOf(decimalFormat.format(futureValue)));  /**---> decimal format <----**/
                            Button btn = new Button("OK");
                            btn.setOnAction(event2 -> {
                                txtFV1.clear();
                                txtFV2.clear();
                                txtFV3.clear();
                                txtFV4.clear();
                                txtFV5.clear();
                                stage.close();
                            });

                            VBox vBox = new VBox();
                            vBox.getChildren().addAll(lbl, btn);
                            vBox.setSpacing(15);
                            vBox.setLayoutX(575);
                            vBox.setLayoutY(40);

                            Pane paneFVCal = new VBox();
                            paneFVCal.getChildren().add(vBox);
                            stage.setScene(new Scene(paneFVCal, 300, 300));
                            stage.show();
                        }
                    }
                    catch (Exception error){
                        Alert alertStringError1 = new Alert(Alert.AlertType.ERROR,"Some is worng. Please, Enter correct values",ButtonType.OK);
                        txtFV1.clear();
                        txtFV2.clear();
                        txtFV3.clear();                        /**----> fixed string errors in text field <----*/
                        txtFV4.clear();
                        txtFV5.clear();
                        alertStringError1.show();
                    }
                }
            });

            Button btnFVBack =  new Button("Back");
            btnFVBack.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnFVBack.setLayoutX(575);
            btnFVBack.setLayoutY(293);
            btnFVBack.setOnAction(event1 -> {
                stgFince.close();
                stgHome.show();

            });

            paneFV.getChildren().addAll(vBoxFV1,vBoxFV2,btnFVCal,btnFVBack,gridPane1);

//....PMT......................................................................................
            Pane panePMT = new Pane();
            panePMT.setStyle("-fx-background-color: #CAD3C8");
            GridPane gridPane2 = keypad();  /**----> Keypad <----**/
            gridPane2.setLayoutX(530);
            gridPane2.setLayoutY(30);

            Label lblPMT1 = new Label("FV (Future Values)");
            TextField txtPMT1 = new TextField();
            txtPMT1.setOnMouseClicked(event1 -> {
                textFieldSD = txtPMT1;
            });
            Label lblPMT2 = new Label("Number of periods");
            TextField txtPMT2 = new TextField();
            txtPMT2.setOnMouseClicked(event1 -> {
                textFieldSD = txtPMT2;
            });
            Label lblPMT3 = new Label("Present Value");
            TextField txtPMT3 = new TextField();
            txtPMT3.setOnMouseClicked(event1 -> {
                textFieldSD = txtPMT3;
            });
            Label lblPMT4 = new Label("I/Y (Interest Rate - %)");
            TextField txtPMT4 = new TextField();
            txtPMT4.setOnMouseClicked(event1 -> {
                textFieldSD = txtPMT4;
            });
            Label lblPMT5 = new Label("Year");
            TextField txtPMT5 = new TextField();
            txtPMT5.setOnMouseClicked(event1 -> {
                textFieldSD = txtPMT5;
            });

            VBox vBoxPMT1 = new VBox();
            vBoxPMT1.setLayoutX(50);
            vBoxPMT1.setLayoutY(50);
            vBoxPMT1.setSpacing(22);
            vBoxPMT1.getChildren().addAll(lblPMT1,lblPMT2,lblPMT3,lblPMT4,lblPMT5);

            VBox vBoxPMT2 = new VBox();
            vBoxPMT2.setLayoutX(250);
            vBoxPMT2.setLayoutY(50);
            vBoxPMT2.setSpacing(8);
            vBoxPMT2.getChildren().addAll(txtPMT1,txtPMT2,txtPMT3,txtPMT4,txtPMT5);

            Button btnPMTCal = new Button("Calculate");
            btnPMTCal.setLayoutX(220);
            btnPMTCal.setLayoutY(290);
            btnPMTCal.setStyle("-fx-pref-width:200;-fx-pref-height:35;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
            btnPMTCal.setOnAction(event1 -> {
                if(txtFV1.getText().isEmpty() && txtFV2.getText().isEmpty() && txtFV3.getText().isEmpty() && txtFV4.getText().isEmpty() && txtFV5.getText().isEmpty()){
                    Alert alertEmptySpace = new Alert(Alert.AlertType.ERROR,"Please..! Fill the all text field.",ButtonType.OK);
                    alertEmptySpace.show();
                }                                   /**---> Fixed empty text feild error  <---**/
                else {
                    try{
                        double futureValue = Double.parseDouble(txtPMT1.getText());
                        double numberOfperiods = Double.parseDouble(txtPMT2.getText());
                        double presenrValue = Double.parseDouble(txtPMT3.getText());
                        double iY = Double.parseDouble(txtPMT4.getText());
                        double year = Double.parseDouble(txtPMT5.getText());

                        double pmt = futureValue/((Math.pow(1+(iY/numberOfperiods),(numberOfperiods*year))-1)/(iY/numberOfperiods));

                        Document document1 = new Document("Name", "Finance FV")
                                .append("Result", decimalFormat.format(pmt));
                        collection.insertOne(document1);

                        //...Minus value error..................
                        if (pmt<0){
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Please check your input values", ButtonType.OK);
                            alert.show();
                        } else {
                            Stage stagePMT = new Stage();
                            stagePMT.setTitle("PMT");
                            stagePMT.initOwner(stgFince);
                            stagePMT.initModality(Modality.WINDOW_MODAL);  /**----> TO close previous window <----**/
                            Label lblPMT = new Label(String.valueOf(decimalFormat.format(pmt)));
                            lblPMT.setStyle("-fx-font-weight: bold");
                            Button btnPMT = new Button("OK");
                            btnPMT.setOnAction(event2 -> {
                                txtPMT1.clear();
                                txtPMT2.clear();
                                txtPMT3.clear();                /**---> Clear Button <---*/
                                txtPMT4.clear();
                                txtPMT5.clear();
                                stagePMT.close();
                            });

                            VBox vBoxPMT = new VBox();
                            vBoxPMT.getChildren().addAll(lblPMT, btnPMT);
                            vBoxPMT.setSpacing(15);
                            vBoxPMT.setLayoutX(575);
                            vBoxPMT.setLayoutY(40);

                            Pane panePMTCal = new VBox();
                            panePMTCal.getChildren().add(vBoxPMT);
                            stagePMT.setScene(new Scene(panePMTCal, 300, 300));
                            stagePMT.show();
                        }
                    }
                    catch (Exception error){
                        Alert alertStringError = new Alert(Alert.AlertType.ERROR,"Some is worng. Please, Enter correct values",ButtonType.CANCEL);
                        txtPMT1.clear();
                        txtPMT2.clear();
                        txtPMT3.clear();                        /**----> fixed string errors in text field <----*/
                        txtPMT4.clear();
                        txtPMT5.clear();
                        alertStringError.show();
                    }
                }
            });

            Button btnPMTBack =  new Button("Back");
            btnPMTBack.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnPMTBack.setLayoutX(575);
            btnPMTBack.setLayoutY(293);
            btnPMTBack.setOnAction(event1 -> {
                stgFince.close();
                stgHome.show();
            });

            panePMT.getChildren().addAll(vBoxPMT1,vBoxPMT2,btnPMTCal,btnPMTBack,gridPane2);

            //....I/Y...............................................
            Pane paneIY = new Pane();
            paneIY.setStyle("-fx-background-color: #CAD3C8");
            GridPane gridPane3 = keypad();  /**---> Keypad <---**/
            gridPane3.setLayoutX(530);
            gridPane3.setLayoutY(30);

            Label lblIY1 = new Label("FV (Future Values)");
            TextField txtIY1 = new TextField();
            txtIY1.setOnMouseClicked(event1 -> {
                textFieldSD = txtIY1;
            });

            Label lblIY2 = new Label("Number of Periods");
            TextField txtIY2 = new TextField();
            txtIY2.setOnMouseClicked(event1 -> {
                textFieldSD = txtIY2;
            });

            Label lblIY3 = new Label("Start Principal");
            TextField txtIY3 = new TextField();
            txtIY3.setOnMouseClicked(event1 -> {
                textFieldSD = txtIY3;
            });

            Label lblIY4 = new Label("PMT (Annuity Payment)");
            TextField txtIY4 = new TextField();
            txtIY4.setOnMouseClicked(event1 -> {
                textFieldSD = txtIY4;
            });

            VBox vBoxIY1 = new VBox();
            vBoxIY1.setLayoutX(50);
            vBoxIY1.setLayoutY(50);
            vBoxIY1.setSpacing(22);
            vBoxIY1.getChildren().addAll(lblIY1,lblIY2,lblIY3,lblIY4);

            VBox vBoxIY2 = new VBox();
            vBoxIY2.setLayoutX(250);
            vBoxIY2.setLayoutY(50);
            vBoxIY2.setSpacing(8);
            vBoxIY2.getChildren().addAll(txtIY1,txtIY2,txtIY3,txtIY4);

            Button btnIYCal = new Button("Calculate");
            btnIYCal.setLayoutX(220);
            btnIYCal.setLayoutY(290);
            btnIYCal.setStyle("-fx-pref-width:200;-fx-pref-height:35;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
            btnIYCal.setOnAction(event1 -> {
                if(txtFV1.getText().isEmpty() && txtFV2.getText().isEmpty() && txtFV3.getText().isEmpty() && txtFV4.getText().isEmpty() && txtFV5.getText().isEmpty()){
                    Alert alertEmptySpace = new Alert(Alert.AlertType.ERROR,"Please..! Fill the all text field.",ButtonType.OK);
                    alertEmptySpace.show();
                }                               /**---> Fixed emply text feild error <---*/
                else {
                    try{
                        double futureValue = Double.parseDouble(txtIY1.getText());
                        double numberOfperiods = Double.parseDouble(txtIY2.getText());
                        double startPrincipal = Double.parseDouble(txtIY3.getText());
                        double pmt = Double.parseDouble(txtIY4.getText());

                        double iY = ((Math.pow((futureValue / startPrincipal), (1 / 12 * numberOfperiods))) - 1) * 12;

                        Document document1 = new Document("Name", "Finance I/Y")
                                .append("Result", decimalFormat.format(iY));
                        collection.insertOne(document1);

                        //...Minus value error..........
                        if (iY<0){
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Please check your input values", ButtonType.OK);
                            alert.show();
                        } else {
                            Stage stage = new Stage();
                            stage.setTitle("I/Y");
                            stage.initOwner(stgFince);
                            stage.initModality(Modality.WINDOW_MODAL); /**----> close previous window <----**/
                            Label lblIY = new Label(String.valueOf(decimalFormat.format(iY)));  /**----> Convert to decimal <----**/
                            lblIY.setStyle("-fx-font-weight: bold");
                            Button btn = new Button("Ok");
                            btn.setOnAction(event2 -> {
                                txtIY1.clear();
                                txtIY2.clear();
                                txtIY3.clear();
                                txtIY4.clear();
                                stage.close();
                            });
                            VBox vBoxIYCal = new VBox();
                            vBoxIYCal.setLayoutX(100);
                            vBoxIYCal.setLayoutY(15);
                            vBoxIYCal.setSpacing(18);
                            vBoxIYCal.getChildren().addAll(lblIY, btn);

                            Pane pane = new Pane();
                            pane.getChildren().add(vBoxIYCal);
                            stage.setScene(new Scene(pane, 300, 180));
                            stage.show();
                        }
                    }
                    catch (Exception error){
                        Alert alertStringError = new Alert(Alert.AlertType.ERROR,"Some is worng. Please, Enter correct values",ButtonType.OK);
                        txtIY1.clear();
                        txtIY2.clear();
                        txtIY3.clear();                       /**----> fixed string errors in text field <----*/
                        txtIY4.clear();
                        alertStringError.show();
                    }
                }
            });

            Button btnIYBack =  new Button("Back");
            btnIYBack.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnIYBack.setLayoutX(575);
            btnIYBack.setLayoutY(293);
            btnIYBack.setOnAction(event1 -> {
                stgFince.close();
                stgHome.show();
            });

            paneIY.getChildren().addAll(vBoxIY1,vBoxIY2,btnIYCal,btnIYBack,gridPane3);

            //....Number of periods......................................................
            Pane paneN = new Pane();
            paneN.setStyle("-fx-background-color: #CAD3C8");
            GridPane gridPane4 = keypad();  /**----> Keypad <----**/
            gridPane4.setLayoutX(530);
            gridPane4.setLayoutY(30);
            Label lblN1 = new Label("FV (Future Values)");
            TextField txtN1 = new TextField();
            txtN1.setOnMouseClicked(event1 -> {
                textFieldSD = txtN1;
            });

            Label lblN2 = new Label("Start Principal");
            TextField txtN2 = new TextField();
            txtN2.setOnMouseClicked(event1 -> {
                textFieldSD = txtN2;
            });

            Label lblN3 = new Label("I/Y (Interest Rate - %)");
            TextField txtN3 = new TextField();
            txtN3.setOnMouseClicked(event1 -> {
                textFieldSD = txtN3;
            });

            Label lblN4 = new Label("PMT (Annuity Payment)");
            TextField txtN4 = new TextField();
            txtN4.setOnMouseClicked(event1 -> {
                textFieldSD = txtN4;
            });

            VBox vBoxN1 = new VBox();
            vBoxN1.setLayoutX(50);
            vBoxN1.setLayoutY(50);
            vBoxN1.setSpacing(22);
            vBoxN1.getChildren().addAll(lblN1,lblN2,lblN3,lblN4);

            VBox vBoxN2 = new VBox();
            vBoxN2.setLayoutX(250);
            vBoxN2.setLayoutY(50);
            vBoxN2.setSpacing(8);
            vBoxN2.getChildren().addAll(txtN1,txtN2,txtN3,txtN4);

            Button btnNCal = new Button("Calculate");
            btnNCal.setLayoutX(220);
            btnNCal.setLayoutY(290);
            btnNCal.setStyle("-fx-pref-width:200;-fx-pref-height:35;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
            btnNCal.setOnAction(event1 -> {
                if(txtN1.getText().isEmpty() && txtN2.getText().isEmpty() && txtN3.getText().isEmpty() && txtN4.getText().isEmpty()){
                    Alert alertEmptySpace = new Alert(Alert.AlertType.ERROR,"Please..! Fill the all text field.",ButtonType.OK);
                    alertEmptySpace.show();
                }                                /**---> Fixed emply text feild error <---*/
                else {
                    try {
                        double futureValue = Double.parseDouble(txtN1.getText());
                        double numberOfperiods = Double.parseDouble(txtN2.getText());
                        double startPrincipal = Double.parseDouble(txtN3.getText());
                        double pmt = Double.parseDouble(txtN4.getText());

//                        double n = ;



//                        //...Minus value error..................
//                        if (n<0){
//                            Alert alert = new Alert(Alert.AlertType.ERROR, "Please check your input values", ButtonType.OK);
//                            alert.show();
//                        } else {
//                            Alert alertMinusVError = new Alert(Alert.AlertType.INFORMATION, decimalFormat.format(n), ButtonType.OK);
//                            alertMinusVError.show();
//                        }
                    }
                    catch (Exception error){
                        Alert alertStringError = new Alert(Alert.AlertType.ERROR,"Some is worng. Please, Enter correct values",ButtonType.CANCEL);
                        txtN1.clear();
                        txtN2.clear();
                        txtN3.clear();                               /**----> fixed string errors in text field <----*/
                        txtN4.clear();
                        alertStringError.show();
                    }
                }
            });

            Button btnNBack =  new Button("Back");
            btnNBack.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnNBack.setLayoutX(575);
            btnNBack.setLayoutY(293);
            btnNBack.setOnAction(event1 -> {
                stgFince.close();
                stgHome.show();
            });

            paneN.getChildren().addAll(vBoxN1,vBoxN2,btnNCal,btnNBack,gridPane4);

            //....Start Principal........................................
            Pane paneSP = new Pane();
            paneSP.setStyle("-fx-background-color: #CAD3C8");
            GridPane gridPane5 = keypad();  /**----> Keypad <----**/
            gridPane5.setLayoutX(530);
            gridPane5.setLayoutY(30);
            Label lblSP1 = new Label("FV (Future Values)");
            TextField txtSP1 = new TextField();
            txtSP1.setOnMouseClicked(event1 -> {
                textFieldSD = txtSP1;
            });

            Label lblSP2 = new Label("Number of Periods");
            TextField txtSP2 = new TextField();
            txtSP2.setOnMouseClicked(event1 -> {
                textFieldSD = txtSP2;
            });

            Label lblSP3 = new Label("I/Y (Interest Rate - %)");
            TextField txtSP3 = new TextField();
            txtSP3.setOnMouseClicked(event1 -> {
                textFieldSD = txtSP3;
            });

            Label lblSP4 = new Label("PMT (Annuity Payment)");
            TextField txtSP4 = new TextField();
            txtSP4.setOnMouseClicked(event1 -> {
                textFieldSD = txtSP4;
            });

            VBox vBoxSP1 = new VBox();
            vBoxSP1.setLayoutX(50);
            vBoxSP1.setLayoutY(50);
            vBoxSP1.setSpacing(22);
            vBoxSP1.getChildren().addAll(lblSP1,lblSP2,lblSP3,lblSP4);

            VBox vBoxSP2 = new VBox();
            vBoxSP2.setLayoutX(250);
            vBoxSP2.setLayoutY(50);
            vBoxSP2.setSpacing(8);
            vBoxSP2.getChildren().addAll(txtSP1,txtSP2,txtSP3,txtSP4);

            Button btnSPCal = new Button("Calculate");
            btnSPCal.setLayoutX(220);
            btnSPCal.setLayoutY(290);
            btnSPCal.setStyle("-fx-pref-width:200;-fx-pref-height:35;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
            btnSPCal.setOnAction(event1 -> {
                if(txtSP1.getText().isEmpty() && txtSP2.getText().isEmpty() && txtSP3.getText().isEmpty() && txtSP4.getText().isEmpty()){
                    Alert alertEmptySpace = new Alert(Alert.AlertType.ERROR,"Please..! Fill the all text field.",ButtonType.OK);
                    alertEmptySpace.show();
                }                                   /**---> Fixed emply text feild error <---*/
                else {
                    try {
                        double futureValue = Double.parseDouble(txtSP1.getText());
                        double numberOfperiods = Double.parseDouble(txtSP2.getText());
                        double iY = Double.parseDouble(txtSP3.getText());
                        double pmt = Double.parseDouble(txtSP4.getText());

//                        double sp =

//                        //...Minus value error..................
//                        if (sp<0){
//                            Alert alert = new Alert(Alert.AlertType.ERROR, "Please check your input values", ButtonType.OK);
//                            alert.show();
//                        } else {
//                            Alert alertMinusVError = new Alert(Alert.AlertType.INFORMATION, decimalFormat.format(sp), ButtonType.OK);
//                            alertMinusVError.show();
//                        }
                    }
                    catch (Exception error){
                        Alert alertStringError = new Alert(Alert.AlertType.ERROR,"Some is worng. Please, Enter correct values",ButtonType.CANCEL);
                        txtSP1.clear();
                        txtSP2.clear();
                        txtSP3.clear();                         /**----> fixed string errors in text field <----*/
                        txtSP4.clear();
                        alertStringError.show();
                    }
                }
            });

            Button btnSPBack =  new Button("Back");
            btnSPBack.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnSPBack.setLayoutX(575);
            btnSPBack.setLayoutY(293);
            btnSPBack.setOnAction(event1 -> {
                stgFince.close();
                stgHome.show();
            });

            paneSP.getChildren().addAll(vBoxSP1,vBoxSP2,btnSPCal,btnSPBack,gridPane5);
            //..........................................
            Tab tabFV = new Tab("FV",paneFV);
            Tab tabPMT = new Tab("PMI",panePMT);
            Tab tabIY = new Tab("I/Y",paneIY);
            Tab tabN = new Tab("N",paneN);
            Tab tabSP = new Tab("Start Principal",paneSP);

            tabPaneFince.getTabs().addAll(tabFV,tabPMT,tabIY,tabN,tabSP);

            stgFince.setScene(new Scene(tabPaneFince, 800, 400));
            stgFince.setResizable(false);
            stgHome.close();
            stgFince.show();
        });

//.......Loan..........................................................................
        Button btnLoan = new Button("Loan");
        btnLoan.setEffect(dropShadow1);
        btnLoan.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        btnLoan.setOnMouseEntered(event -> {
            btnLoan.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-background-color: #00cec9;-fx-font-weight: bold;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px");
        });
        btnLoan.setOnMouseExited(event -> {
            btnLoan.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        });
        btnLoan.setOnAction(event -> {
            Stage stgLoan = new Stage();
            stgLoan.setTitle("Loan");

            TabPane tabPaneLoan = new TabPane();
            tabPaneLoan.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);  /**----> To opne same Window <----**/

            //....Total Price....................................................
            Pane paneTP = new Pane();
            paneTP.setStyle("-fx-background-color: #c7ecee");
            GridPane gridPane5 = keypad();  /**---> Keypad <---**/
            gridPane5.setLayoutX(530);
            gridPane5.setLayoutY(30);

            Label lblTP1 = new Label("Auto Price");
            TextField txtTP1 = new TextField();
            txtTP1.setOnMouseClicked(event1 -> {
                textFieldSD = txtTP1;
            });

            Label lblTP2 = new Label("Year");
            TextField txtTP2 = new TextField();
            txtTP2.setOnMouseClicked(event1 -> {
                textFieldSD = txtTP2;
            });

            Label lblTP3 = new Label("I/Y (Interest Rate - %)");
            TextField txtTP3 = new TextField();
            txtTP3.setOnMouseClicked(event1 -> {
                textFieldSD = txtTP3;
            });

            Label lblTP4 = new Label("Down Payment");
            TextField txtTP4 = new TextField();
            txtTP4.setOnMouseClicked(event1 -> {
                textFieldSD = txtTP4;
            });

            VBox vBoxTP1 = new VBox();
            vBoxTP1.setLayoutX(50);
            vBoxTP1.setLayoutY(50);
            vBoxTP1.setSpacing(22);
            vBoxTP1.getChildren().addAll(lblTP1,lblTP2,lblTP3,lblTP4);

            VBox vBoxTP2 = new VBox();
            vBoxTP2.setLayoutX(250);
            vBoxTP2.setLayoutY(50);
            vBoxTP2.setSpacing(8);
            vBoxTP2.getChildren().addAll(txtTP1,txtTP2,txtTP3,txtTP4);

            Button btnTPCal = new Button("Calculate");
            btnTPCal.setLayoutX(220);
            btnTPCal.setLayoutY(290);
            btnTPCal.setStyle("-fx-pref-width:200;-fx-pref-height:35;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
            btnTPCal.setOnAction(event1 -> {
                if(txtTP1.getText().isEmpty() && txtTP2.getText().isEmpty() && txtTP3.getText().isEmpty() && txtTP4.getText().isEmpty()){
                    Alert alertEmptySpace = new Alert(Alert.AlertType.ERROR,"Please..! Fill the all text field.",ButtonType.OK);
                    alertEmptySpace.show();
                }                                  /**---> Fixed empty text field error <---*/
                else {
                    try {
                        double autoPrice = Double.parseDouble(txtTP1.getText());
                        double year = Double.parseDouble(txtTP2.getText());
                        double iY = Double.parseDouble(txtTP3.getText());
                        double downPayment = Double.parseDouble(txtTP4.getText());

                        double totalPrice = (autoPrice * iY/100) + (autoPrice - downPayment);

                        Document document1 = new Document("Name", "Loan")
                                .append("Result", decimalFormat.format(totalPrice));
                        collection.insertOne(document1);

                        //...Minus value error..................
                        if (totalPrice<0){
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Please check your input values", ButtonType.OK);
                            alert.show();
                        } else {
                            Stage stage = new Stage();
                            stage.setTitle("Total Price");
                            stage.initOwner(stgLoan);
                            stage.initModality(Modality.WINDOW_MODAL); /**----> close previous window <----**/
                            Label lblIY = new Label(String.valueOf(decimalFormat.format(iY)));  /**----> Convert to decimal <----**/
                            lblIY.setStyle("-fx-font-weight: bold");
                            Button btn = new Button("Ok");
                            btn.setOnAction(event2 -> {
                                txtTP1.clear();
                                txtTP2.clear();
                                txtTP3.clear();
                                txtTP4.clear();
                                stage.close();
                            });
                            VBox vBoxTPCal = new VBox();
                            vBoxTPCal.setLayoutX(100);
                            vBoxTPCal.setLayoutY(15);
                            vBoxTPCal.setSpacing(18);
                            vBoxTPCal.getChildren().addAll(lblIY, btn);

                            Pane pane = new Pane();
                            pane.getChildren().add(vBoxTPCal);
                            stage.setScene(new Scene(pane, 300, 180));
                            stage.show();
                        }
                    }
                    catch (Exception error){
                        Alert alertStringError = new Alert(Alert.AlertType.ERROR,"Some is worng. Please, Enter correct values",ButtonType.CANCEL);
                        txtTP1.clear();
                        txtTP2.clear();
                        txtTP3.clear();                     /**----> fixed string errors in text field <----*/
                        txtTP4.clear();
                        alertStringError.show();
                    }
                }
            });

            Button btnTPBack = new Button("Back");
            btnTPBack.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnTPBack.setLayoutX(575);
            btnTPBack.setLayoutY(293);
            btnTPBack.setOnAction(event1 -> {
                stgLoan.close();
                stgHome.show();
            });

            paneTP.getChildren().addAll(vBoxTP1,vBoxTP2,btnTPCal,btnTPBack,gridPane5);

            //....Monthly Payment.................................................
            Pane paneMP = new Pane();
            paneMP.setStyle("-fx-background-color: #c7ecee");
            GridPane gridPane6 = keypad();  /**----> Keypad <----**/
            gridPane6.setLayoutX(530);
            gridPane6.setLayoutY(30);

            Label lblMP1 = new Label("Loan Amount");
            TextField txtMP1 = new TextField();
            txtMP1.setOnMouseClicked(event1 -> {
                textFieldSD = txtMP1;
            });

            Label lblMP2 = new Label("Year");
            TextField txtMP2 = new TextField();
            txtMP2.setOnMouseClicked(event1 -> {
                textFieldSD = txtMP2;
            });

            Label lblMP3 = new Label("I/Y (Interest Rate - %)");
            TextField txtMP3 = new TextField();
            txtMP3.setOnMouseClicked(event1 -> {
                textFieldSD = txtMP3;
            });

            Label lblMP4 = new Label("Down Payment");
            TextField txtMP4 = new TextField();
            txtMP4.setOnMouseClicked(event1 -> {
                textFieldSD = txtMP4;
            });

            VBox vBoxMP1 = new VBox();
            vBoxMP1.setLayoutX(50);
            vBoxMP1.setLayoutY(50);
            vBoxMP1.setSpacing(22);
            vBoxMP1.getChildren().addAll(lblMP1,lblMP2,lblMP3,lblMP4);

            VBox vBoxMP2 = new VBox();
            vBoxMP2.setLayoutX(250);
            vBoxMP2.setLayoutY(50);
            vBoxMP2.setSpacing(8);
            vBoxMP2.getChildren().addAll(txtMP1,txtMP2,txtMP3,txtMP4);

            Button btnMPCal = new Button("Calculate");
            btnMPCal.setLayoutX(220);
            btnMPCal.setLayoutY(290);
            btnMPCal.setStyle("-fx-pref-width:200;-fx-pref-height:35;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
            btnMPCal.setOnAction(event1 -> {
                try{
                    double loanAmount = Double.parseDouble(txtMP1.getText());
                    double year = Double.parseDouble(txtMP2.getText());
                    double interestRate = Double.parseDouble(txtMP3.getText());
                    double downPayment = Double.parseDouble(txtMP4.getText());

                    double monthlPayment = loanAmount*interestRate/(1-(1/Math.pow((1+interestRate),year)));

                    Document document1 = new Document("Name", "Loan MP")
                            .append("Result", decimalFormat.format(monthlPayment));
                    collection.insertOne(document1);

                    //...Minus value error..........
                    if (monthlPayment<0){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Please check your input values", ButtonType.OK);
                        alert.show();
                    } else {
                        Stage stageMP = new Stage();
                        stageMP.setTitle("Monthly Payment");
                        stageMP.initOwner(stgLoan);
                        stageMP.initModality(Modality.WINDOW_MODAL); /**----> close previous window <----**/
                        Label lblMP = new Label(String.valueOf(decimalFormat.format(monthlPayment)));
                        lblMP.setStyle("-fx-font-weight: bold");
                        Button button = new Button("Ok");
                        button.setOnAction(event2 -> {
                            txtMP1.clear();
                            txtMP2.clear();
                            txtMP3.clear();
                            txtMP4.clear();
                            stageMP.close();
                        });
                        VBox vBoxMPCal = new VBox();
                        vBoxMPCal.setLayoutX(100);
                        vBoxMPCal.setLayoutY(15);
                        vBoxMPCal.setSpacing(18);
                        vBoxMPCal.getChildren().addAll(lblMP,button);

                        Pane pane = new Pane();
                        pane.getChildren().add(vBoxMPCal);
                        stageMP.setScene(new Scene(pane, 300, 180));
                        stageMP.show();
                    }
                }
                catch (Exception error){
                    Alert alertStringError = new Alert(Alert.AlertType.ERROR,"Some is worng. Please, Enter correct values",ButtonType.CANCEL);
                    txtMP1.clear();
                    txtMP2.clear();
                    txtMP3.clear();                         /**----> fixed string errors in text field <----*/
                    txtMP4.clear();
                    alertStringError.show();
                }
            });

            Button btnMPback = new Button("Back");
            btnMPback.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnMPback.setLayoutX(575);
            btnMPback.setLayoutY(318);
            btnMPback.setOnAction(event1 -> {
                stgLoan.close();
                stgHome.show();

            });

            Button btnMPBack = new Button("Back");
            btnMPBack.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnMPBack.setLayoutX(575);
            btnMPBack.setLayoutY(293);
            btnMPBack.setOnAction(event1 -> {
                stgLoan.close();
                stgHome.show();
            });

            paneMP.getChildren().addAll(vBoxMP1,vBoxMP2,btnMPCal,btnMPBack,gridPane6);
            //.....................................................
            Tab tabTP = new Tab("Total Price",paneTP);
            Tab tabMP = new Tab("Monthly Payment",paneMP);

            tabPaneLoan.getTabs().addAll(tabTP,tabMP);

            stgLoan.setScene(new Scene(tabPaneLoan, 800, 400));
            stgLoan.setResizable(false);
            stgHome.close();
            stgLoan.show();
        });

//.....Mortgage...............................................................
        Button btnMotgg = new Button("Mortgage");
        btnMotgg.setEffect(dropShadow1);
        btnMotgg.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        btnMotgg.setOnMouseEntered(event -> {
            btnMotgg.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-background-color: #00cec9;-fx-font-weight: bold;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px");
        });
        btnMotgg.setOnMouseExited(event -> {
            btnMotgg.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        });

        btnMotgg.setOnAction(event -> {
            Stage stgMtgg = new Stage();
            stgMtgg.setTitle("Mortgage");

            Pane paneMtgg = new Pane();
            paneMtgg.setStyle("-fx-background-color: #9AECDB");

            GridPane gridPane = keypad(); /**----> Keypad <----**/
            gridPane.setLayoutX(530);
            gridPane.setLayoutY(30);

            Label lblMtgg1 = new Label("Home Price");
            TextField txtMtgg1 = new TextField();
            txtMtgg1.setOnMouseClicked(event1 -> {
                textFieldSD = txtMtgg1;
            });

            Label lblMtgg2 = new Label("Down Payment");
            TextField txtMtgg2 = new TextField();
            txtMtgg2.setOnMouseClicked(event1 -> {
                textFieldSD = txtMtgg2;
            });

            Label lblMtgg3 = new Label("Year");
            TextField txtMtgg3 = new TextField("");
            txtMtgg3.setOnMouseClicked(event1 -> {
                textFieldSD = txtMtgg3;
            });

            Label lblMtgg4 = new Label("I/Y (Interest Rate - %)");
            TextField txtMtgg4 = new TextField();
            txtMtgg4.setOnMouseClicked(event1 -> {
                textFieldSD = txtMtgg4;
            });

            VBox vBoxMtgg1 = new VBox();
            vBoxMtgg1.setLayoutX(50);
            vBoxMtgg1.setLayoutY(50);
            vBoxMtgg1.setSpacing(22);
            vBoxMtgg1.getChildren().addAll(lblMtgg1,lblMtgg2,lblMtgg3,lblMtgg4);

            VBox vBoxMtgg2 = new VBox();
            vBoxMtgg2.setLayoutX(250);
            vBoxMtgg2.setLayoutY(50);
            vBoxMtgg2.setSpacing(8);
            vBoxMtgg2.getChildren().addAll(txtMtgg1,txtMtgg2,txtMtgg3,txtMtgg4);

            Button btnMtggCal = new Button("Calculate");
            btnMtggCal.setLayoutX(220);
            btnMtggCal.setLayoutY(315);
            btnMtggCal.setStyle("-fx-pref-width:200;-fx-pref-height:35;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
            btnMtggCal.setOnAction(event1 -> {
                if(txtMtgg1.getText().isEmpty() && txtMtgg2.getText().isEmpty() && txtMtgg3.getText().isEmpty() && txtMtgg4.getText().isEmpty()){
                    Alert alertEmptySpace = new Alert(Alert.AlertType.ERROR,"Please..! Fill the all text field.",ButtonType.OK);
                    alertEmptySpace.show();
                }                                /**---> Fixed emply text feild error <---*/
                else {
                    try{
                        double homePrice = Double.parseDouble(txtMtgg1.getText());
                        double downPayment = Double.parseDouble(txtMtgg2.getText());
                        double year = Double.parseDouble(txtMtgg3.getText());
                        double iY = Double.parseDouble(txtMtgg4.getText());
                        homePrice -= downPayment;
                        double mortgage = ((Math.pow((1 + (iY / 100) / 12), (12 * year))) * (homePrice * (iY / 100) / 12)) / ((Math.pow((1 + (iY / 100) / 12), (12 * year))) - 1);

                        Document document1 = new Document("Name", "Mortgage")
                                .append("Result", decimalFormat.format(mortgage));
                        collection.insertOne(document1);

                        //...Minus value error..........
                        if (mortgage<0){
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Please check your input values", ButtonType.OK);
                            alert.show();
                        } else {
                            Stage stage = new Stage();
                            stage.setTitle("Results");
                            stage.initOwner(stgMtgg);
                            stage.initModality(Modality.WINDOW_MODAL); /**----> close previous window <----**/
                            Label lbl = new Label(String.valueOf(decimalFormat.format(mortgage)));
                            lbl.setStyle("-fx-font-weight: bold");
                            Button button = new Button("Ok");
                            button.setOnAction(event2 -> {
                                txtMtgg1.clear();
                                txtMtgg2.clear();
                                txtMtgg3.clear();
                                txtMtgg4.clear();
                                stage.close();
                            });
                            VBox vBoxMtggCal = new VBox();
                            vBoxMtggCal.setLayoutX(100);
                            vBoxMtggCal.setLayoutY(15);
                            vBoxMtggCal.setSpacing(18);
                            vBoxMtggCal.getChildren().addAll(lbl, button);

                            Pane pane = new Pane();
                            pane.getChildren().add(vBoxMtggCal);
                            stage.setScene(new Scene(pane, 300, 180));
                            stage.show();
                        }

                    }
                    catch (Exception error){
                        Alert alertStringError = new Alert(Alert.AlertType.ERROR,"Some is worng. Please, Enter correct values",ButtonType.CANCEL);
                        txtMtgg1.clear();
                        txtMtgg2.clear();
                        txtMtgg3.clear();
                        txtMtgg4.clear();
                        alertStringError.show();
                    }
                }
            });

            Button btnMtggback = new Button("Back");
            btnMtggback.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnMtggback.setLayoutX(575);
            btnMtggback.setLayoutY(318);
            btnMtggback.setOnAction(event1 -> {
                stgMtgg.close();
                stgHome.show();
            });

            paneMtgg.getChildren().addAll(vBoxMtgg1,vBoxMtgg2,btnMtggCal,btnMtggback, gridPane);

            stgMtgg.setScene(new Scene(paneMtgg, 800, 400));
            stgMtgg.setResizable(false);
            stgHome.close();
            stgMtgg.show();

        });

//.....History...........................................................
        Button btnHistory = new Button("History");
        btnHistory.setEffect(dropShadow1);
        btnHistory.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        btnHistory.setOnMouseEntered(event -> {
            btnHistory.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-background-color: #00cec9;-fx-font-weight: bold;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px");
        });
        btnHistory.setOnMouseExited(event -> {
            btnHistory.setStyle("-fx-pref-width: 115;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #006064;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        });

        btnHistory.setOnAction(event -> {
            Stage stgHis = new Stage();
            stgHis.setTitle("History");

            VBox vBoxHistory = new VBox();
            vBoxHistory.setLayoutX(20);
            vBoxHistory.setLayoutY(20);

            FindIterable<Document> iterDoc = collection.find();

            for (Document record : iterDoc) {
                String name = (String) record.get("Name");
                String result = (String) record.get("Result");

                Label label1 = new Label("Type :- " + name + " --- " + "Result :- " + result + "\n");
                label1.setStyle("-fx-font-weight: bold");

                vBoxHistory.getChildren().add(label1);
            }

            Button btnHisBack = new Button("Back");
            btnHisBack.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnHisBack.setLayoutX(590);
            btnHisBack.setLayoutY(315);
            btnHisBack.setOnAction(event1 -> {
                stgHis.close();
                stgHome.show();
            });

            vBoxHistory.getChildren().add(btnHisBack);

            BorderPane paneHis = new BorderPane();
            paneHis.setCenter(vBoxHistory);

            stgHis.setScene(new Scene(paneHis, 800, 400));
            stgHis.setResizable(false);
            stgHome.close();
            stgHis.show();
        });

//.....Help......................................................................
        Button btnHelp = new Button("Help");
        btnHelp.setLayoutX(670);
        btnHelp.setLayoutY(347);
        btnHelp.setEffect(dropShadow1);
        btnHelp.setStyle("-fx-pref-width: 100;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #d63031;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        btnHelp.setOnMouseEntered(event -> {
            btnHelp.setStyle("-fx-pref-width: 100;-fx-pref-height: 30;-fx-background-color: #EE5A24;-fx-font-weight: bold;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px");
        });
        btnHelp.setOnMouseExited(event -> {
            btnHelp.setStyle("-fx-pref-width: 100;-fx-pref-height: 30;-fx-font-weight: bold;-fx-background-color: #d63031;-fx-text-fill: #FFFFFF;-fx-background-radius: 20px;-fx-border-color: aliceblue;-fx-border-radius: 20;-fx-border-width: 2");
        });
        btnHelp.setOnAction(event -> {
            Stage stgHelp = new Stage();
            stgHelp.setTitle("Help");

            Label lblHelp1 = new Label("Home : These are the Calculators");
            lblHelp1.setStyle("-fx-font-weight: bold;");
            Image imgHelp1 = new Image("home.JPG",600,400,true,true);
            ImageView imgView1 = new ImageView(imgHelp1);
            imgView1.setLayoutX(60);

            Label lblHelp2 = new Label("Finance : This is the \"Finance\" Calculator.");
            lblHelp2.setStyle("-fx-font-weight: bold;");
            Image imgHelp2 = new Image("finance.JPG",600,400,true,true);
            ImageView imgView2 = new ImageView(imgHelp2);

            Label lblHelp3 = new Label("Loan : This is the \"Loan\" Calculator.");
            lblHelp3.setStyle("-fx-font-weight: bold;");
            Image imgHelp3 = new Image("loan.JPG",600,400,true,true);
            ImageView imgView3 = new ImageView(imgHelp3);

            Label lblHelp4 = new Label("Mortgage : This is the \"Finance\" Calculator.");
            lblHelp4.setStyle("-fx-font-weight: bold;");
            Image imgHelp4 = new Image("mortgage.JPG",600,400,true,true);
            ImageView imgView4 = new ImageView(imgHelp4);

            VBox vBoxHelp = new VBox();
            vBoxHelp.setPrefSize(800,400);
            vBoxHelp.setSpacing(25);
            vBoxHelp.getChildren().addAll(lblHelp1,imgView1,lblHelp2,imgView2,lblHelp3,imgView3,lblHelp4,imgView4);

            Button btnHelpBack = new Button("Back");
            btnHelpBack.setStyle("-fx-pref-width: 100;-fx-pref-height: 35;-fx-font-weight: bold;-fx-font-size: 18;-fx-background-color: #d35400;-fx-text-fill: aliceblue;-fx-background-radius: 20;-fx-border-color: #c0392b;-fx-border-radius: 20;-fx-border-width: 2");
            btnHelpBack.setLayoutX(670);
            btnHelpBack.setLayoutY(8);
            btnHelpBack.setOnAction(event1 -> {
                stgHelp.close();
                stgHome.show();
            });

            ScrollPane scrollPaneHelp = new ScrollPane();
            scrollPaneHelp.setContent(vBoxHelp);

            Pane  paneHelp = new Pane();
            paneHelp.getChildren().addAll(scrollPaneHelp,btnHelpBack);

            stgHelp.setScene(new Scene(paneHelp, 800, 400));
            stgHelp.setResizable(false);
            stgHelp.show();
            stgHome.close();
        });
//..............................................................................
        VBox vBoxFinceCal = new VBox();
        vBoxFinceCal.setSpacing(25);
        vBoxFinceCal.setLayoutX(350);
        vBoxFinceCal.setLayoutY(140);
        vBoxFinceCal.getChildren().addAll(btnFince,btnLoan,btnMotgg,btnHistory);

        Pane paneHome = new Pane();
        paneHome.setStyle("-fx-background-color:#6FA8DC");
        paneHome.getChildren().addAll(vBoxFinceCal,lblHomeTitle,btnHelp);

        stgHome.setScene(new Scene(paneHome,800,400));
        stgHome.setResizable(false);
        stgHome.close();
        stgHome.show();
    }

    public GridPane keypad(){
        Button num1 = new Button("1");
        num1.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num1.setOnAction(event -> {
            textFieldSD.appendText("1");
        });
        Button num2 = new Button("2");
        num2.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num2.setOnAction(event -> {
            textFieldSD.appendText("2");
        });
        Button num3 = new Button("3");
        num3.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num3.setOnAction(event -> {
            textFieldSD.appendText("3");
        });
        Button num4 = new Button("4");
        num4.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num4.setOnAction(event -> {
            textFieldSD.appendText("4");
        });
        Button num5 = new Button("5");
        num5.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num5.setOnAction(event -> {
            textFieldSD.appendText("5");
        });
        Button num6 = new Button("6");
        num6.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num6.setOnAction(event -> {
            textFieldSD.appendText("6");
        });
        Button num7 = new Button("7");
        num7.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num7.setOnAction(event -> {
            textFieldSD.appendText("7");
        });
        Button num8 = new Button("8");
        num8.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num8.setOnAction(event -> {
            textFieldSD.appendText("8");
        });
        Button num9 = new Button("9");
        num9.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num9.setOnAction(event -> {
            textFieldSD.appendText("9");
        });
        Button num0 = new Button("0");
        num0.setStyle("-fx-pref-width: 95; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        num0.setOnAction(event -> {
            textFieldSD.appendText("0");
        });
        Button numDot = new Button(".");
        numDot.setStyle("-fx-pref-width: 40; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        numDot.setOnAction(event -> {
            textFieldSD.appendText(".");
        });
        Button numClear = new Button("Clear");
        numClear.setStyle("-fx-pref-width: 142; -fx-pref-height: 40;-fx-background-color: #2980b9;-fx-font-weight: bold;-fx-border-color: black;-fx-border-radius: 10;-fx-background-radius: 10;-fx-font-size: 20");
        numClear.setOnAction(event -> {
            textFieldSD.clear();
        });

        GridPane gridPaneKeyPad = new GridPane();
        gridPaneKeyPad.setHgap(10);
        gridPaneKeyPad.setVgap(4);

        gridPaneKeyPad.add(num1,0,0,1,1);
        gridPaneKeyPad.add(num2,1,0,1,1);
        gridPaneKeyPad.add(num3,2,0,1,1);
        gridPaneKeyPad.add(num4,0,1,1,1);
        gridPaneKeyPad.add(num5,1,1,1,1);
        gridPaneKeyPad.add(num6,2,1,1,1);
        gridPaneKeyPad.add(num7,0,2,1,1);
        gridPaneKeyPad.add(num8,1,2,1,1);
        gridPaneKeyPad.add(num9,2,2,1,1);
        gridPaneKeyPad.add(num0,0,3,2,1);
        gridPaneKeyPad.add(numDot,2,3,1,1);
        gridPaneKeyPad.add(numClear,0,4,3,1);

        return gridPaneKeyPad;
    }
}
