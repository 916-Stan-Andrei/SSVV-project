package ssvv.example.repository;


import ssvv.example.domain.Tema;
import ssvv.example.validation.Validator;

public class TemaRepository extends AbstractCRUDRepository<String, Tema> {
    public TemaRepository(Validator<Tema> validator) {
        super(validator);
    }
}

