package dk.ek.gruppe2.chooseyourfate.dto;

public class CreateCharacterRequestDTO {

    private Integer accountId;
    private Integer raceDetailsId;
    private String name;

    public CreateCharacterRequestDTO() {
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getRaceDetailsId() {
        return raceDetailsId;
    }

    public void setRaceDetailsId(Integer raceDetailsId) {
        this.raceDetailsId = raceDetailsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
