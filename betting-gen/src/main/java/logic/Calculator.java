package logic;

import model.Event;
import model.Result;
import model.SlotOdd;
import model.C;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Calculator {

    public List<Result> findFavourites(List<Event> events) {
        StringBuilder chain = new StringBuilder("");
        List<Result> res = new ArrayList<>();

        events.forEach(e -> {
            var min = e.getSlotOdds().stream().min(Comparator.comparingDouble(SlotOdd::getOdd)).get();
            res.add(new Result(e.getName(), min.getSlot(), min.getOdd()));
            chain.append(min.getSlot());
        });
        System.out.println("##### FAVOURITES #####");
        res.forEach(System.out::println);
        System.out.println("----");
        System.out.println(chain);
        System.out.println("##### FAVOURITES ##### \n");
        return res;
    }

    public Map<String, Double> findSingleSurprise(List<Event> events, List<Result> favs, int slotSize) {
        var onlySurprises = new ArrayList<Result>();
        var cumulativeSlotToOdd = new TreeMap<String, Double>();

        for (int idx = 0; idx < events.size(); idx++) {
            var event = events.get(idx);
            var favslot = favs.get(idx).getSlot();

            var possibilities = fetchPossibilitesForSurprise(favslot, slotSize);
            for (int p : possibilities) {
                var sldd = fetchSlotOddBy(event, p);
                onlySurprises.add(new Result(event.getName(), sldd.getSlot(), sldd.getOdd()));
                var res = new ArrayList<>(favs);
                res.set(idx, new Result(event.getName(), sldd.getSlot(), sldd.getOdd()));
//                System.out.println(res);
                var cumulativeOdd = res.stream()
                        .map(Result::getOdd)
                        .reduce(0.0, Double::sum);
                var cumulativeSlots = res.stream()
                        .map(Result::getSlot)
                        .map(Object::toString)
                        .collect(Collectors.joining());
//                System.out.println(cumulativeOdd);
//                System.out.println(cumulativeSlots);
                cumulativeSlotToOdd.put(cumulativeSlots, cumulativeOdd);
            }
        }
        System.out.println("##### SINGLE SURPRISES #####");
//        System.out.println(onlySurprises);
        Map<String, Double> sortedMapByValues = cumulativeSlotToOdd.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//        System.out.println(sortedMapByValues);
        sortedMapByValues.forEach((key, value) -> System.out.println(key + " " + value));
        System.out.println("##### SINGLE SURPRISES ##### \n");
        return sortedMapByValues;
    }

    public Map<String, Double> findAllOdds(List<Event> events, List<String> chains) {
        var chainToOdd = new TreeMap<String, Double>();
        for (String chain : chains) {
            var res = new ArrayList<Result>();
            for (int idx = 0; idx < chain.length(); idx++) {
                var slot = Integer.valueOf(String.valueOf(chain.charAt(idx)));
                var event = events.get(idx);
                var so = fetchSlotOddBy(event, slot);
                res.add(new Result(event.getName(), so.getSlot(), so.getOdd()));
            }
            var cumulativeOdd = res.stream()
                    .map(Result::getOdd)
                    .reduce(0.0, Double::sum);
            chainToOdd.put(chain, cumulativeOdd);
        }
        Map<String, Double> sortedChanToOddByValues = chainToOdd.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println("##### ALL ODDS #####");
        sortedChanToOddByValues.forEach((key, value) -> System.out.println(key + " " + value));
        System.out.println("##### ALL ODDS ##### \n");
        return sortedChanToOddByValues;
//        System.out.println(sortedChanToOddByValues);
    }

//    public List<model.Result> find2And3SurprisesForEvents(List<model.Result> favourites) {
//        // eventSize, replace by inddexes for reversed value
//        var res = new ArrayList<>(favourites);
//
//    }


    public List<Result> findAllSurprisesThreeSlot(List<Event> events, List<Result> favs) {
        var res = new ArrayList<Result>();

        for (int idx = 0; idx < events.size(); idx++) {
            var event = events.get(idx);
            var positionFav = favs.get(idx).getSlot();
            int surprise = Integer.MAX_VALUE;
            if (positionFav == 0) {
                var sldd = fetchSlotOddBy(event, 1);
                res.add(new Result(event.getName(), sldd.getSlot(), sldd.getOdd()));
                surprise = sldd.getSlot();
            } else if (positionFav == 1) {
                var sldd = fetchSlotOddBy(event, 2);
                res.add(new Result(event.getName(), sldd.getSlot(), sldd.getOdd()));
                surprise = sldd.getSlot();
                // write 2
            } else if (positionFav == 2) {
                var sldd = fetchSlotOddBy(event, 0);
                res.add(new Result(event.getName(), sldd.getSlot(), sldd.getOdd()));
                surprise = sldd.getSlot();
            }

//            if (surprise == 0) {
//                var sldd = fetchSlotOddBy(event, 1);
//                res.add(new model.Result(sldd.getSlot(), sldd.getOdd()));
//            } else if (surprise == 1) {
//                var sldd = fetchSlotOddBy(event, 2);
//                res.add(new model.Result(sldd.getSlot(), sldd.getOdd()));
//            } else if (surprise == 2) {
//                var sldd = fetchSlotOddBy(event, 0);
//                res.add(new model.Result(sldd.getSlot(), sldd.getOdd()));
//            }
        }
        System.out.println("##### ALL SURPRISES #####");
        System.out.println(res);
        System.out.println("##### ALL SURPRISES ##### \n");
        return res;
    }

    public SlotOdd fetchSlotOddBy(Event event, Integer slot) {
        return event.getSlotOdds().stream()
                .filter(so -> so.getSlot() == slot)
                .findFirst()
                .get();
    }


    public List<Integer> fetchPossibilitesForSurprise(int slot, int slotSize) {
        var lst = slotSize == 3 ? Stream.of(1, 0, 2).collect(Collectors.toList()) : Stream.of(1, 2).collect(Collectors.toList());
        return lst.stream().filter(el -> !el.equals(slot)).collect(Collectors.toList());
    }

    public List<String> findChain(int eventsSize, int slotSize) {
        if (eventsSize == 3 && slotSize == 3) {
            return C.THREE_3SLOT_EVENTS_CHAIN;
        } else if (eventsSize == 4 && slotSize == 3) {
            return C.FOUR_3SLOT_EVENTS_CHAIN;
        } else if (eventsSize == 5 && slotSize == 3) {
            return C.FIVE_3SLOT_EVENTS;
        } else if (eventsSize == 4 && slotSize == 2) {
            return C.FOUR_2SLOT_EVENTS;
        } else if (eventsSize == 5 && slotSize == 2) {
            return C.FIVE_2SLOT_EVENTS;
        } else if (eventsSize == 6 && slotSize == 2) {
            return C.SIX_2SLOT_EVENTS;
        } else {
            return List.of();
        }
    }

    public Map<String, Double> filterForDoubleDraw(Map<String, Double> all) {
        var doubleDraw = all.entrySet().stream()
                .filter(e -> isDoubleDrawPredicate(e.getKey())).collect(Collectors.toList());
        System.out.println("##### DOUBLE DRAW #####");
        doubleDraw.forEach((entry) -> System.out.println(entry.getKey() + " " + entry.getValue()));
        System.out.println("##### DOUBLE DRAW ##### \n");
        return null;
    }

    public Map<String, Double> filterForStartsWith2(Map<String, Double> all) {
        var doubleDraw = all.entrySet().stream()
                .filter(e -> e.getKey().startsWith("2")).collect(Collectors.toList());
        System.out.println("##### STARTS WITH 2 #####");
        doubleDraw.forEach((entry) -> System.out.println(entry.getKey() + " " + entry.getValue()));
        System.out.println("##### STARTS WITH 2 ##### \n");
        return null;
    }

    public Map<String, List<Map.Entry<String, Double>>> filterForStartingChain(Map<String, Double> all, int slotSize) {
        var startToChain = new TreeMap<String, List<Map.Entry<String, Double>>>();
        if (slotSize == 2) {
            var _11 = all.entrySet().stream().filter(e -> e.getKey().startsWith("11")).collect(Collectors.toList());
            var _12 = all.entrySet().stream().filter(e -> e.getKey().startsWith("12")).collect(Collectors.toList());
            var _21 = all.entrySet().stream().filter(e -> e.getKey().startsWith("21")).collect(Collectors.toList());
            var _22 = all.entrySet().stream().filter(e -> e.getKey().startsWith("22")).collect(Collectors.toList());
            startToChain.put("11", _11);
            startToChain.put("12", _12);
            startToChain.put("21", _21);
            startToChain.put("22", _22);
        } else {
            var _11 = all.entrySet().stream().filter(e -> e.getKey().startsWith("11")).collect(Collectors.toList());
            var _12 = all.entrySet().stream().filter(e -> e.getKey().startsWith("12")).collect(Collectors.toList());
            var _21 = all.entrySet().stream().filter(e -> e.getKey().startsWith("21")).collect(Collectors.toList());
            var _22 = all.entrySet().stream().filter(e -> e.getKey().startsWith("22")).collect(Collectors.toList());

            var _10 = all.entrySet().stream().filter(e -> e.getKey().startsWith("10")).collect(Collectors.toList());
            var _01 = all.entrySet().stream().filter(e -> e.getKey().startsWith("01")).collect(Collectors.toList());
            var _20 = all.entrySet().stream().filter(e -> e.getKey().startsWith("20")).collect(Collectors.toList());
            var _02 = all.entrySet().stream().filter(e -> e.getKey().startsWith("02")).collect(Collectors.toList());
            var _00 = all.entrySet().stream().filter(e -> e.getKey().startsWith("00")).collect(Collectors.toList());
            startToChain.put("11", _11);
            startToChain.put("12", _12);
            startToChain.put("21", _21);
            startToChain.put("22", _22);

            startToChain.put("10", _10);
            startToChain.put("01", _01);
            startToChain.put("20", _20);
            startToChain.put("02", _02);
            startToChain.put("00", _00);
        }
        System.out.println("##### STARTING CHAIN #####");
        startToChain.forEach((k, v) -> {
            System.out.println(k + ":");
            v.forEach(e -> System.out.println("   " + e.getKey() + " " + e.getValue()));
        });
        System.out.println("##### STARTING CHAIN ##### \n");
        return startToChain;
    }

    public boolean isDoubleDrawPredicate(String chain) {
        int count = 0;
        char[] chs = chain.toCharArray();
        for (char ch : chs) {
            if (ch == '0') {
                count++;
            }
        }
        return count == 2;
    }


    public boolean isConditionMet(String seq, char c, int occurrences) {
        int count = 0;
        char[] chs = seq.toCharArray();
        for (char ch : chs) {
            if (ch == c) {
                count++;
            }
        }
        return count <= occurrences;
    }
}
