package de.fraunhofer.iem.opcuascanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultReporter {
    private static final String CSV_DELIMITER = ",";
    private static final String CSV_LINE_BREAK = "\n";
    private static final String DEFAULT_FILENAME = "OPCUAScannerResults";
    private static final String DEFAULT_FILE_EXTENSION= ".csv";
    private static final String UNKNOWN = "unknown";

    private static final Logger logger = LoggerFactory.getLogger(ResultReporter.class);

    /**
     * Report the results to a csv file so that only given privileges are visible as "true"
     * or unknown field as "unknown".
     * @param results The results which will be written to the file
     */
    static void reportToFile(HashMap<String, AccessPrivileges> results){
        String csvOutput = buildCsvOutput(results);

        File outputFile = new File(DEFAULT_FILENAME + DEFAULT_FILE_EXTENSION);

        try(FileOutputStream output = new FileOutputStream(outputFile)){
            output.write(csvOutput.getBytes());
        }
        catch (FileNotFoundException e){
            logger.info("Could not find file.");
        } catch (IOException e) {
            logger.info("Error while writing results: {}", e.getMessage());
        }

    }

    private static String buildCsvOutput(HashMap<String,AccessPrivileges> results) {
        StringBuilder outputBuilder = new StringBuilder();

        //Make the headers for the table
        outputBuilder.append("Server" + CSV_DELIMITER);
        for(Authentication auth : Authentication.values()) {
            for (Privilege priv : Privilege.values()) {
                outputBuilder.append(auth.toString()+"_"+priv.toString());
            }
        }

        for (Map.Entry<String, AccessPrivileges> resultForServer : results.entrySet()){
            String server = resultForServer.getKey();
            AccessPrivileges privForServer = resultForServer.getValue();

            outputBuilder.append(server + CSV_DELIMITER);

            for(Authentication auth : Authentication.values()){
                for (Privilege priv : Privilege.values()){
                    if (privForServer.getWasTested(priv, auth)){
                        if (privForServer.isPrivilegePerAuthentication(priv, auth)){
                            outputBuilder.append("true");
                        }
                    } else{
                        outputBuilder.append(UNKNOWN);
                    }
                    outputBuilder.append(CSV_DELIMITER);

                }
            }
            outputBuilder.append(CSV_LINE_BREAK);
        }
        return outputBuilder.toString();
    }
}
