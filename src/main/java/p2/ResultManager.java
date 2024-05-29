package p2;

import p1.Result;

import java.util.ArrayList;
import java.util.List;

public class ResultManager {
    private List<Result> results;

    public ResultManager() {this.results = new ArrayList<>();}

    public void addResult(Result result) {results.add(result);}

    public void updateResult(Result oldResult,Result newResult){
        int index = results.indexOf(oldResult);
        if (index != -1) {
            results.set(index, newResult);
        }
    }

    public void deleteResult(Result result) {
        results.remove(result);
    }

    public List<Result> getResultsByEvent(int eventId) {
        List<Result> eventResults = new ArrayList<>();
        for (Result result : results) {
            if (result.getCompetitionEventId() == eventId) {
                eventResults.add(result);
            }
        }
        return eventResults;
    }

    public boolean verifyResult(Result result) {
        return true;
    }
}