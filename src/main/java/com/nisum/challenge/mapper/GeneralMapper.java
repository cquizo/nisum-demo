package com.nisum.challenge.mapper;

import com.nisum.challenge.dto.PhoneDTO;
import com.nisum.challenge.dto.UserDTO;
import com.nisum.challenge.model.Phone;
import com.nisum.challenge.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GeneralMapper {

    GeneralMapper INSTANCE = Mappers.getMapper(GeneralMapper.class);

    @Mapping(source = "updatedOn", target = "modifiedOn")
    UserDTO toUserDTO(User user);

    @Mapping(source = "modifiedOn", target = "updatedOn")
    User toUser(UserDTO userDTO);

    List<UserDTO> toUserDTOList(List<User> userList);

    List<User> toUserList(List<UserDTO> userDTOList);

    @Mapping(source = "cityCode", target = "citycode")
    @Mapping(source = "countryCode", target = "countrycode")
    PhoneDTO toPhoneDTO(Phone phone);

    @Mapping(source = "citycode", target = "cityCode")
    @Mapping(source = "countrycode", target = "countryCode")
    Phone toPhone(PhoneDTO phoneDTO);

    List<PhoneDTO> toPhoneDTOList(List<Phone> phoneList);

    List<Phone> toPhoneList(List<PhoneDTO> phoneDTOList);
}