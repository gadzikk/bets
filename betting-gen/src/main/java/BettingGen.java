import file.FileProcessor;
import logic.Calculator;

public class BettingGen {

    public static void main(String[] args) throws Exception {
        var fp = new FileProcessor();
        var c = new Calculator();

        var events = fp.read();
        var eventsSize = events.size();
        var slotSize = events.stream().findFirst().get().getSlotOdds().size();


        var favourites = c.findFavourites(events);
        c.findSingleSurprise(events, favourites, slotSize);
        var chain = c.findChain(eventsSize, slotSize);
        var all = c.findAllOdds(events, chain);
        c.filterForDoubleDraw(all);
        c.filterForStartsWith2(all);
        c.filterForStartingChain(all, slotSize);


    }


}
