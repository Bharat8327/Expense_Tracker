package com.chenu.expensetracker.util;

import com.chenu.expensetracker.dto.UserDTO;
import com.chenu.expensetracker.entity.User;

public class UserMapper {
    public static UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setTotalMoney(user.getTotalMoney());
        dto.setRemainingMoney(user.getRemainingMoney());
        return dto;
    }
}
