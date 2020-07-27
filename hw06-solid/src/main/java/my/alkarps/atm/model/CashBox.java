package my.alkarps.atm.model;

import my.alkarps.atm.model.exception.*;
import my.alkarps.atm.model.memento.BackupState;
import my.alkarps.atm.model.memento.RestoreState;
import my.alkarps.atm.model.operation.AddBanknotes;
import my.alkarps.atm.model.operation.CurrentAmount;
import my.alkarps.atm.model.operation.Empty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author alkarps
 * create date 22.07.2020 14:35
 */
public class CashBox implements CurrentAmount, Empty, BackupState, RestoreState, AddBanknotes {
    private static final String DELIMITER = ";";
    private List<Cassette> cassettes;

    private CashBox(List<Cassette> cassettes) {
        updateCassettesWithSort(cassettes);
    }

    private void updateCassettesWithSort(List<Cassette> cassettes) {
        cassettes.sort(Cassette::compare);
        this.cassettes = cassettes;
    }

    @Override
    public long getCurrentAmount() {
        return cassettes.stream()
                .map(Cassette::getCurrentAmount)
                .reduce(Long::sum)
                .orElse(0L);
    }

    @Override
    public boolean isEmpty() {
        return cassettes.isEmpty() || cassettes.stream().allMatch(Cassette::isEmpty);
    }

    @Override
    public String backup() {
        return cassettes.stream()
                .map(Cassette::backup)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public void restore(String restoringState) {
        if (isNullOrEmpty(restoringState) || restoringState.isBlank()) {
            throw new CashBoxStateIsWrongException();
        }
        this.cassettes = Stream.of(restoringState.split(DELIMITER))
                .map(Cassette::restore)
                .sorted(Cassette::compare)
                .collect(Collectors.toList());
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void addBanknotes(long amount) {
        if (amount > 0) {
            List<Cassette> _cassettes = new ArrayList<>();
            for (Cassette cassette : cassettes) {
                long banknotes = amount / cassette.getDenomination().getAmount();
                _cassettes.add(cassette.addBanknotes(banknotes));
                amount = amount - (banknotes * cassette.getDenomination().getAmount());
            }
            if (amount != 0) {
                throw new UnknownDenominationException();
            }
            updateCassettesWithSort(_cassettes);
        } else {
            throw new InvalidAmountException();
        }
    }

    public static class Builder {
        private final List<Cassette> cassettes = new ArrayList<>();

        private Builder() {
        }

        public Builder addCassettes(Cassette cassette) {
            if (cassette == null || cassette.isEmpty()) {
                throw new CassetteIsEmptyException();
            }
            //TODO проверить на необходимость копирования
            this.cassettes.add(cassette);
            return this;
        }

        public CashBox build() {
            if (cassettes.isEmpty()) {
                throw new CashBoxIsEmptyException();
            }
            return new CashBox(cassettes);
        }
    }
}