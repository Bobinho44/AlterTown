package fr.bobinho.altertown.message;

import fr.bobinho.altertown.AlterTownCore;

import javax.annotation.Nonnull;

public enum AlterTownMessage {
    NOT_IN_TOWN,
    NOT_LEADER_OR_OFFICIAL_OF_TOWN,
    NOT_LEADER_OF_TOWN,
    ALREADY_IN_TOWN,
    NO_INVITATION_FROM_TOWN,
    JOIN_TOWN,
    DENY_INVITATION_TOWN,
    REMOVE_FROM_TOWN,
    PLAYER_ALREADY_IN_TOWN,
    PLAYER_ALREADY_HAVE_INVITATION,
    PLAYER_SEND_INVITATION,
    PLAYER_RECEIVE_INVITATION,
    PLAYER_JOIN_TOWN,
    PLAYER_DENY_INVITATION_TOWN,
    PLAYER_LEADER_OR_OFFICIAL_OF_TOWN,
    PLAYER_NOT_IN_TOWN,
    PLAYER_NOT_IN_SAME_TOWN,
    PLAYER_REMOVE_FROM_TOWN,
    PLAYER_NOT_CITIZEN_OF_TOWN,
    PLAYER_NOT_OFFICIAL_OF_TOWN,
    PLAYER_PROMOTE,
    PLAYER_DEMOTE,
    NAME_ALREADY_USE,
    NOT_ITEM_IN_HAND,
    NOT_NAME_TOWN,
    CREATE_TOWN,
    DELETE_TOWN,
    OFFICIAL_IS_FULL,
    ADMIN_NAME_TOWN,
    ADMIN_DESCRIPTION_TOWN,
    ADMIN_BIOME_TOWN,
    ADMIN_LEADER_TOWN,
    ADMIN_PROMOTE,
    ADMIN_DEMOTE,
    ADMIN_EMPTY_HAND,
    ADMIN_BUILDING_ALREADY_EXIST,
    ADMIN_NEW_BUILDING,
    ADMIN_BUILDING_NOT_EXIST,
    ADMIN_REMOVE_BUILDING,
    ADMIN_COMMAND_DOESNT_EXISTS,
    ADMIN_URL_TOWN,
    TOWN_FULL,
    NOT_CONNECTED,
    PLAYER_NOT_IN_SELECTED_TOWN,
    PLAYER_SET_LEADER_TOWN,
    PREVIOUS_PAGE,
    NEXT_PAGE,
    INFORMATION,
    LEADER_COLOR,
    OFFICIAL_COLOR;

   public String getRawText() {
       return AlterTownCore.getMessagesSettings().getConfiguration().getString(this.toString(), "N/D");
   }

}