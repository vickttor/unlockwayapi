package unlockway.unlockway_api.DTO;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class WeekRepetitionsDTO {
    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;
    private Boolean sunday;

    public WeekRepetitionsDTO(Boolean monday, Boolean tuesday,
                              Boolean wednesday, Boolean thursday,
                              Boolean friday, Boolean saturday,
                              Boolean sunday
    ) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }
}
