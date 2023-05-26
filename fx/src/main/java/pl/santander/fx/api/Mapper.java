package pl.santander.fx.api;

public interface Mapper<ModelT, DtoT> {

    DtoT mapToDto(ModelT model);

}
