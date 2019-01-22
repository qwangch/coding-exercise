package com.validity.controller;

import com.google.gson.Gson;
import com.validity.UtilFunction;
import com.validity.model.User;
import com.validity.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Controller handle request and response
     * @param model - springframework.ui.ModelMap
     * @return - duplicates and non-duplicates to jsp page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String result(ModelMap model) {
        List<User> userList = userService.loadUsers();
        if(!userList.isEmpty()){
            List<User> duplicates = userService.computeFuzzyMatch(userList);
            // find the non-duplicates
            List<User> uniques = UtilFunction.removeDuplicates(userList, duplicates);
            //convert to a json object
            Gson gson = new Gson();
            // convert your list to json
            String jsonDuplicates = gson.toJson(duplicates);
            logger.info("Duplicates: " + jsonDuplicates);
            String jsonNonDuplicates = gson.toJson(uniques);
            logger.info("NonDuplicates: " + jsonNonDuplicates);
            model.put("duplicates", duplicates);
            model.put("uniques", uniques);
        }
        return "result";
    }

}
