package by.dzmitry_lakisau.currencies_fetcher.model.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Root(name="DailyExRates")
public class DailyExchangeRates {

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    @ElementList(name="Currency", entry="Currency", inline=true)
    private List<Currency> currencies;

    @Attribute(name="Date", required = false)
    private String date;

    public List<Currency> getCurrencies() {return this.currencies;}

    public Date getDate() throws ParseException {
        calendar.setTime(simpleDateFormat.parse(date));
        return calendar.getTime();
    }
    public void setDate(String value) throws ParseException {
        date = value;
    }
}
