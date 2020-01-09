package com.imuliar.decima.service.util;

import com.imuliar.decima.service.UpdateProcessor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

/**
 * <p>Produce {@link com.imuliar.decima.service.UpdateProcessor} instances</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
public abstract class UpdateProcessorFactory {

    @Lookup("defaultPlebeianProcessor")
    public abstract UpdateProcessor defaultPlebeianProcessor();

    @Lookup("defaultPatricianProcessor")
    public abstract UpdateProcessor defaultPatricianProcessor();

    @Lookup("bookSlotPatricianProcessor")
    public abstract UpdateProcessor bookSlotPatricianProcessor();

    @Lookup("findRandomSlotPlebeianProcessor")
    public abstract UpdateProcessor findRandomSlotPlebeianProcessor();

    @Lookup("sharePatricianSlotProcessor")
    public abstract UpdateProcessor sharePatricianSlotProcessor();

    @Lookup("cancelBookingProcessor")
    public abstract UpdateProcessor cancelBookingProcessor();

    @Lookup("inputForUserSearchPlebeianProcessor")
    public abstract UpdateProcessor inputForUserSearchPlebeianProcessor();

    @Lookup("inputForUserSearchPatricianProcessor")
    public abstract UpdateProcessor inputForUserSearchPatricianProcessor();

    @Lookup("searchUserBySlotPlebeianProcessor")
    public abstract UpdateProcessor searchUserBySlotPlebeianProcessor();

    @Lookup("searchUserBySlotPatricianProcessor")
    public abstract UpdateProcessor searchUserBySlotPatricianProcessor();

    @Lookup("showPlanProcessor")
    public abstract UpdateProcessor showPlanProcessor();

    @Lookup("setSharingPeriodProcessor")
    public abstract UpdateProcessor setSharingPeriodProcessor();

    @Lookup("toPatricianBeginningProcessor")
    public abstract UpdateProcessor toPatricianBeginningProcessor();

    @Lookup("toPlebeianBeginningProcessor")
    public abstract UpdateProcessor toPlebeianBeginningProcessor();

    @Lookup("yearBackProcessor")
    public abstract UpdateProcessor yearBackProcessor();

    @Lookup("yearForwardProcessor")
    public abstract UpdateProcessor yearForwardProcessor();

    @Lookup("monthBackProcessor")
    public abstract UpdateProcessor monthBackProcessor();

    @Lookup("monthForwardProcessor")
    public abstract UpdateProcessor monthForwardProcessor();

    @Lookup("pickStartDateProcessor")
    public abstract UpdateProcessor pickStartDateProcessor();

    @Lookup("pickEndDateProcessor")
    public abstract UpdateProcessor pickEndDateProcessor();

    @Lookup("saveVacantPeriodProcessor")
    public abstract UpdateProcessor saveVacantPeriodProcessor();

    @Lookup("listPeriodsPatricianProcessor")
    public abstract UpdateProcessor listPeriodsPatricianProcessor();

    @Lookup("manipulatePeriodPatricianProcessor")
    public abstract UpdateProcessor manipulatePeriodPatricianProcessor();

    @Lookup("cancelSharingPatricianProcessor")
    public abstract UpdateProcessor cancelSharingPatricianProcessor();

    @Lookup("doThePollPlebeianProcessor")
    public abstract UpdateProcessor doThePollPlebeianProcessor();

    @Lookup("yesPatricianProcessor")
    public abstract UpdateProcessor yesPatricianProcessor();

    @Lookup("noPatricianProcessor")
    public abstract UpdateProcessor noPatricianProcessor();
}
