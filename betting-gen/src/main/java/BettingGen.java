import file.FileProcessor;
import logic.Calculator;

public class BettingGen {

    public static void main(String[] args) throws Exception {
        var fp = new FileProcessor();
        var c = new Calculator();
        var events = fp.readSts();
//        var events = fp.readExhbs();
//        var events = fp.readTemplate();
        var eventsSize = events.size();
        var slotSize = events.stream().findFirst().get().getSlotOdds().size();


        var favourites = c.findFavourites(events);
        var chain = c.findChain(eventsSize, slotSize);
        var all = c.findAllOdds(events, chain);
        c.filterForStartingChain(all, slotSize);


    }


}
