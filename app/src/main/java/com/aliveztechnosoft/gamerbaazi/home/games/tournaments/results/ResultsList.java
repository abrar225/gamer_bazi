package com.aliveztechnosoft.gamerbaazi.home.games.tournaments.results;

public class ResultsList {

    private final String fullName, profilePic, amount;

    public ResultsList(String fullName, String profilePic, String amount) {
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.amount = amount;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAmount() {
        return amount;
    }
}
