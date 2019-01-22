package com.validity.service;

import com.opencsv.CSVReader;
import info.debatty.java.stringsimilarity.*;
import java.util.Map;
import com.validity.model.User;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final int CONTACT_ID = 0;
    private static final int CONTACT_FNAME = 1;
    private static final int CONTACT_LNAME = 2;
    private static final int CONTACT_COMPANY = 3;
    private static final int CONTACT_EMAIL = 4;
    private static final int CONTACT_ADDRESS1 = 5;
    private static final int CONTACT_ADDRESS2 = 6;
    private static final int CONTACT_ZIP = 7;
    private static final int CONTACT_CITY = 8;
    private static final int CONTACT_STATELONG = 9;
    private static final int CONTACT_STATE = 10;
    private static final int CONTACT_PHONE = 11;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Constructor
     */
    public UserService() {}

    /**
     * function to load csv file to a list and clean the data
     * @return
     */
    public List<User> loadUsers() {
        logger.info("Loading the csv file");
        Levenshtein l = new Levenshtein();

        Resource resource = resourceLoader.getResource("classpath:normal.csv");
        List<User> users = new ArrayList<>();
        try{
            InputStream is = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVReader csvReader = new CSVReader(br);
            // ignore first row
            String[] nextRecord = csvReader.readNext();
            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                // remove leading 0 from address
                String address1 = nextRecord[CONTACT_ADDRESS1].replaceFirst("^0+(?!$)", "");
                // add lead "0" to the zip code
                String zip = nextRecord[CONTACT_ZIP].length() <5?"0"+nextRecord[CONTACT_ZIP]:nextRecord[CONTACT_ZIP];
                User user = new User(
                    Integer.parseInt(nextRecord[CONTACT_ID]),
                    nextRecord[CONTACT_FNAME],
                    nextRecord[CONTACT_LNAME],
                    nextRecord[CONTACT_COMPANY],
                    nextRecord[CONTACT_EMAIL],
                    address1,
                    nextRecord[CONTACT_ADDRESS2],
                    zip,
                    nextRecord[CONTACT_CITY],
                    nextRecord[CONTACT_STATELONG],
                    nextRecord[CONTACT_STATE],
                    nextRecord[CONTACT_PHONE]);
                users.add(user);
            }
            csvReader.close();
            br.close();

            return users;
        }catch(IOException e){
            logger.error("Can not read the file");
            e.printStackTrace();
        }
        // compute the fuzzy match
        return null;
    }

    /**
     * function to find potential duplicates
     * used java-string-similarity library (https://github.com/tdebatty/java-string-similarity)
     * @param users - list of all users
     * @return - list of potential duplicates
     */
    public List<User> computeFuzzyMatch(List<User> users){
        int objSize = users.size();
        // Jaro-Winkler computes the similarity between 2 short strings, like name
        JaroWinkler jw = new JaroWinkler();
        double MATCH_SCORE = 1.00;
        double FUZZY_MATCH_SCORE = 0.8;
        // Shingle (n-gram) based algorithms
        List<User> duplicates = new ArrayList<>();

        Cosine cosine = new Cosine(2);
        for (int i = 0; i <objSize; i++){
            String namePattern = users.get(i).getFName().toLowerCase() + " " + users.get(i).getLName().toLowerCase();
            String emailPattern = users.get(i).getEmail();
            String addressPattern = users.get(i).getAddress1().toLowerCase() + " " + users.get(i).getAddress2().toLowerCase().toLowerCase() + " " + users.get(i).getCity().toLowerCase().toLowerCase() + " " + users.get(i).getState().toLowerCase() + " " + users.get(i).getZip();
            Map<String, Integer> profile1 = cosine.getProfile(addressPattern);
            String phonePattern = users.get(i).getPhone();
            for (int j=i+1; j<objSize-1 ; j++){
                String nameSearch = users.get(j).getFName().toLowerCase() + " " +  users.get(j).getLName().toLowerCase();
                String emailSearch = users.get(j).getEmail();
                String addressSearch = users.get(j).getAddress1().toLowerCase() + " " + users.get(j).getAddress2().toLowerCase() + " " + users.get(j).getCity().toLowerCase() + " " + users.get(j).getState().toLowerCase() + " " + users.get(j).getZip().toLowerCase();
                Map<String, Integer> profile2 = cosine.getProfile(addressSearch);
                String phoneSearch = users.get(j).getPhone();
                //if email is match or name + address is similar or name + phone is similar, then it's a potential dup
                if((jw.similarity(emailPattern, emailSearch.toLowerCase()) == MATCH_SCORE
                    || (jw.similarity(namePattern, nameSearch.toLowerCase()) > FUZZY_MATCH_SCORE && jw.similarity(phonePattern, phoneSearch) == MATCH_SCORE))
                    || (jw.similarity(namePattern, nameSearch) > FUZZY_MATCH_SCORE && cosine.similarity(profile1, profile2) >FUZZY_MATCH_SCORE)){
                    duplicates.add(users.get(i));
                }
            }
        }

        return duplicates;
    }

}

