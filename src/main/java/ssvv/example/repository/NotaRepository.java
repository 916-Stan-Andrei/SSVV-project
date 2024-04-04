package ssvv.example.repository;

import ssvv.example.domain.Nota;
import ssvv.example.domain.Pair;
import ssvv.example.validation.Validator;

public class NotaRepository extends AbstractCRUDRepository<Pair<String, String>, Nota> {
    public NotaRepository(Validator<Nota> validator) {
        super(validator);
    }
}
