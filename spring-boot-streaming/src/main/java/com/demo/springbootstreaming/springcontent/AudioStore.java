package com.demo.springbootstreaming.springcontent;

import org.springframework.content.commons.repository.Store;
import org.springframework.content.rest.StoreRestResource;

@StoreRestResource(path = "/audios")
public interface AudioStore extends Store<String> {
}
