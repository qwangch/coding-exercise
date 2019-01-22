package com.validity;

import com.validity.model.User;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UtilFunction {

    private static final Logger logger = LoggerFactory.getLogger(UtilFunction.class);
    /** function to remove duplicates from the full list
    * @param fullListist - full user
    * @param dupList - duplicates
    * @return - non-duplicates
    */
    public static List<User> removeDuplicates(List<User> fullListist, List<User> dupList) {
        Set<Integer> ids = dupList.stream()
            .map(User::getId)
            .collect(Collectors.toSet());
        return fullListist.stream()
            .filter(user -> !ids.contains(user.getId()))
            .collect(Collectors.toList());
    }
}
