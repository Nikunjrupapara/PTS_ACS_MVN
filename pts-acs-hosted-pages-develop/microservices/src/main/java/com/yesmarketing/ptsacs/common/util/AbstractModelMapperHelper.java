package com.yesmarketing.ptsacs.common.util;

import com.yesmarketing.acsapi.admin.dto.CollectionDto;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractModelMapperHelper {

	protected final ModelMapper modelMapper;

	public AbstractModelMapperHelper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public ModelMapper getModelMapper() {
		return modelMapper;
	}

	public abstract void initialise();

	public <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> dtoClass) {
		return entityList
				   .stream()
				   .map(entity -> modelMapper.map(entity, dtoClass))
				   .collect(Collectors.toList());
	}

	public <D, M> M fromDto(D dto, Class<M> modelClass) {
		return modelMapper.map(dto, modelClass);
	}

	public <D, M> List<M> fromDto(List<D> dtoList, Class<M> modelClass) {
		return dtoList
				   .stream()
				   .map(dto -> modelMapper.map(dto, modelClass))
				   .collect(Collectors.toList());
	}

	public <M, D> D toDto(M model, Class<D> dtoClass) {
		return modelMapper.map(model, dtoClass);
	}

	public <M, D> List<D> toDto(List<M> modelList, Class<D> dtoClass) {
		return modelList
				   .stream()
				   .map(model -> modelMapper.map(model, dtoClass))
				   .collect(Collectors.toList());
	}

	public <D extends CollectionDto<T>, M, T> D toCollectionDto(List<M> models, Supplier<D> supplier, Class<T> dtoClass) {
		D collectionDto = supplier.get();
		collectionDto.setCollection(mapAll(models, dtoClass));
		return collectionDto;
	}

	public <D extends CollectionDto<T>, M, T> D toCollectionDto(String company, List<M> models, Supplier<D> supplier, Class<T> dtoClass) {
		D collectionDto = toCollectionDto(models, supplier, dtoClass);
		collectionDto.setCompany(company);
		return collectionDto;
	}
}
